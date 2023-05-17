package com.example.jshop.cartsandorders.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.example.jshop.cartsandorders.domain.cart.Cart;
import com.example.jshop.cartsandorders.domain.cart.CartDto;
import com.example.jshop.cartsandorders.domain.cart.CartItemsDto;
import com.example.jshop.cartsandorders.domain.cart.CartStatus;
import com.example.jshop.cartsandorders.domain.cart.Item;
import com.example.jshop.cartsandorders.domain.order.Order;
import com.example.jshop.cartsandorders.domain.order.OrderIdDTO;
import com.example.jshop.cartsandorders.domain.order.OrderStatus;
import com.example.jshop.cartsandorders.mapper.CartMapper;
import com.example.jshop.cartsandorders.mapper.ItemMapper;
import com.example.jshop.cartsandorders.mapper.OrderMapper;
import com.example.jshop.cartsandorders.repository.CartRepository;
import com.example.jshop.customer.domain.Address;
import com.example.jshop.customer.domain.LoggedCustomer;
import com.example.jshop.customer.domain.UnauthenticatedCustomerDto;
import com.example.jshop.customer.service.CustomerService;
import com.example.jshop.email.service.EmailContentCreator;
import com.example.jshop.email.service.SimpleEmailService;
import com.example.jshop.errorhandlers.exceptions.CartNotFoundException;
import com.example.jshop.errorhandlers.exceptions.InvalidCustomerDataException;
import com.example.jshop.errorhandlers.exceptions.InvalidDataException;
import com.example.jshop.errorhandlers.exceptions.InvalidQuantityException;
import com.example.jshop.errorhandlers.exceptions.NotEnoughItemsException;
import com.example.jshop.errorhandlers.exceptions.OrderNotFoundException;
import com.example.jshop.errorhandlers.exceptions.PaymentErrorException;
import com.example.jshop.errorhandlers.exceptions.ProductNotFoundException;
import com.example.jshop.errorhandlers.exceptions.UserNotFoundException;
import com.example.jshop.warehouseandproducts.domain.product.Product;
import com.example.jshop.warehouseandproducts.domain.warehouse.Warehouse;
import com.example.jshop.warehouseandproducts.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final TaskService taskService;
    private final RuntimeService runtimeService;
    private final ProcessEngine processEngine;
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final WarehouseService warehouseService;
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final CustomerService customerService;
    private final SimpleEmailService emailService;
    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final EmailContentCreator emailCreator;

    private Cart findCartById(Long cartId) throws CartNotFoundException {
        return cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);
    }

    private BigDecimal calculatePriceWithDiscount(Long cartId, Long discount) throws CartNotFoundException {
        Cart cart = findCartById(cartId);
        return cart.getCalculatedPrice().subtract(
            (BigDecimal.valueOf(discount).divide(BigDecimal.valueOf(100L))).multiply(cart.getCalculatedPrice()));
    }

    public Cart createCart() {
        Cart newCart = new Cart();
        newCart.setCartStatus(CartStatus.EMPTY);
        newCart.setCreated(LocalDate.now());
        newCart.setCalculatedPrice(BigDecimal.ZERO);
        cartRepository.save(newCart);
        return newCart;
    }

    public void setUpProcessInstance(Long cartId, String processInstanceId) throws CartNotFoundException {
        Cart cart = findCartById(cartId);
        cart.setCamundaProcessId(processInstanceId);
        cartRepository.save(cart);

        Map<String, Object> variables = new HashMap<>();
        variables.put("cartValue", cart.getCalculatedPrice());

        DmnDecisionTableResult discountResult = processEngine.getDecisionService()
            .evaluateDecisionTableByKey("discount", variables);
        Long discount = discountResult.getSingleResult().getEntry("discountPercent");

        cart.setDiscount(discount);
        cart.setFinalPrice(calculatePriceWithDiscount(cartId, discount));
        cartRepository.save(cart);
    }

    private void updateProductInWarehouse(Warehouse warehouse, Integer productQuantity) {
        warehouse.setProductQuantity(warehouse.getProductQuantity() - productQuantity);
        warehouseService.save(warehouse);
    }

    private BigDecimal calculateCurrentCartValue(Cart cart) {
        if (cart.getCartStatus() == CartStatus.PROCESSING) {
            return new BigDecimal(String.valueOf(cart.getListOfItems().stream()
                .map(i -> i.getProduct().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, (sum, item) -> sum = sum.add(item))));
        } else {
            return BigDecimal.ZERO;
        }
    }

    private void validateCartForProcessing(Cart cart) throws CartNotFoundException {
        if (cart.getCartStatus() == CartStatus.FINALIZED) {
            throw new CartNotFoundException();
        }
    }

    private Warehouse validateProductInWarehouse(Long productId, int quantity)
        throws ProductNotFoundException, NotEnoughItemsException {
        Warehouse warehouse = warehouseService.findWarehouseByProductId(productId);
        if (warehouse == null || warehouse.getProductQuantity() == 0) {
            throw new ProductNotFoundException();
        } else if (warehouse.getProductQuantity() < quantity) {
            throw new NotEnoughItemsException();
        } else {
            return warehouse;
        }
    }

    public CartDto addToCart(Long cartId, CartItemsDto cartItemsDto)
        throws InvalidQuantityException, CartNotFoundException, NotEnoughItemsException, ProductNotFoundException {
        validateQuantityOfPurchasedProduct(cartItemsDto.getQuantity());
        Cart cartToUpdate = findCartById(cartId);
        validateCartForProcessing(cartToUpdate);
        validateProductInWarehouse(cartItemsDto.getProductId(), cartItemsDto.getQuantity());

        Task task = taskService.createTaskQuery()
            .processInstanceBusinessKey(String.valueOf(cartId))
            .singleResult();

        Map<String, Object> variables = new HashMap<>();
        variables.put("activity", "addToCart");
        variables.put("productId", cartItemsDto.getProductId());
        variables.put("quantity", cartItemsDto.getQuantity());
        variables.put("cartValue", cartToUpdate.getCalculatedPrice());

        String executionId = task.getExecutionId();
        runtimeService.setVariables(executionId, variables);

        taskService.complete(task.getId());
        return cartMapper.mapCartToCartDto(cartToUpdate);
    }

    public void addToCartCamunda(Long cartId, CartItemsDto cartItemsDto) throws CartNotFoundException {
        Cart cartToUpdate = findCartById(cartId);
        Warehouse warehouse = warehouseService.findWarehouseByProductId(cartItemsDto.getProductId());
        Product product = warehouse.getProduct();
        Item item;
        List<Item> items = cartToUpdate.getListOfItems().stream()
            .filter(i -> i.getProduct().getProductID().equals(product.getProductID()))
            .toList();
        if (items.size() > 0) {
            item = items.get(0);
            item.setQuantity(item.getQuantity() + cartItemsDto.getQuantity());
        } else {
            item = Item.builder()
                .product(product)
                .quantity(cartItemsDto.getQuantity())
                .cart(cartToUpdate)
                .build();
            cartToUpdate.getListOfItems().add(item);
        }
        itemService.save(item);
        cartToUpdate.setCartStatus(CartStatus.PROCESSING);
        cartToUpdate.setCalculatedPrice(calculateCurrentCartValue(cartToUpdate));
        updateProductInWarehouse(warehouse, cartItemsDto.getQuantity());
        cartRepository.save(cartToUpdate);
        itemService.save(item);

        Map<String, Object> variables = new HashMap<>();
        variables.put("cartValue", cartToUpdate.getCalculatedPrice());

        DmnDecisionTableResult discountResult = processEngine.getDecisionService()
            .evaluateDecisionTableByKey("discount", variables);
        Long discount = discountResult.getSingleResult().getEntry("discountPercent");

        cartToUpdate.setDiscount(discount);
        cartToUpdate.setFinalPrice(calculatePriceWithDiscount(cartId, discount));
        cartRepository.save(cartToUpdate);
    }

    private void validateQuantityOfPurchasedProduct(int quantity) throws InvalidQuantityException {
        if (quantity <= 0) {
            throw new InvalidQuantityException();
        }
    }

    public Cart showCart(Long cartId) throws CartNotFoundException {
        return cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);
    }

    public CartDto removeFromCart(Long cartId, CartItemsDto cartItemsDto)
        throws InvalidQuantityException, CartNotFoundException, ProductNotFoundException {
        validateQuantityOfPurchasedProduct(cartItemsDto.getQuantity());
        Cart cartToUpdate = findCartById(cartId);
        validateCartForProcessing(cartToUpdate);
        if (cartToUpdate.getCartStatus() == CartStatus.EMPTY) {
            throw new CartNotFoundException();
        }
        List<Item> items = cartToUpdate.getListOfItems().stream()
            .filter(i -> i.getProduct().getProductID().equals(cartItemsDto.getProductId()))
            .toList();
        if (items.size() == 0) {
            throw new ProductNotFoundException();
        }

        Task task = taskService.createTaskQuery()
            .processInstanceBusinessKey(String.valueOf(cartId))
            .singleResult();

        Map<String, Object> variables = new HashMap<>();
        variables.put("activity", "removeFromCart");
        variables.put("productId", cartItemsDto.getProductId());
        variables.put("quantity", cartItemsDto.getQuantity());

        String executionId = task.getExecutionId();
        runtimeService.setVariables(executionId, variables);

        taskService.complete(task.getId());
        return cartMapper.mapCartToCartDto(cartToUpdate);
    }

    public void removeFromCartCamunda(Long cartId, CartItemsDto cartItemsDto) throws CartNotFoundException {
        Cart cartToUpdate = findCartById(cartId);
        validateCartForProcessing(cartToUpdate);
        if (cartToUpdate.getCartStatus() == CartStatus.EMPTY) {
            throw new CartNotFoundException();
        }
        Item item;
        item = cartToUpdate.getListOfItems().stream()
            .filter(i -> i.getProduct().getProductID().equals(cartItemsDto.getProductId()))
            .toList().get(0);

        Warehouse warehouse = warehouseService.findWarehouseByProductId(cartItemsDto.getProductId());
        if (item.getQuantity() <= cartItemsDto.getQuantity()) {
            updateProductInWarehouse(warehouse, -(item.getQuantity()));
            cartToUpdate.getListOfItems().remove(item);
            itemService.delete(item);
        } else {
            updateProductInWarehouse(warehouse, -(cartItemsDto.getQuantity()));
            item.setQuantity(item.getQuantity() - cartItemsDto.getQuantity());
            itemService.save(item);
            cartToUpdate.getListOfItems().set(cartToUpdate.getListOfItems().indexOf(item), item);
        }
        if (cartToUpdate.getListOfItems().isEmpty()) {
            cartToUpdate.setCartStatus(CartStatus.EMPTY);
        }
        cartToUpdate.setCalculatedPrice(calculateCurrentCartValue(cartToUpdate));
        cartRepository.save(cartToUpdate);

        Map<String, Object> variables = new HashMap<>();
        variables.put("cartValue", cartToUpdate.getCalculatedPrice());

        DmnDecisionTableResult discountResult = processEngine.getDecisionService()
            .evaluateDecisionTableByKey("discount", variables);
        Long discount = discountResult.getSingleResult().getEntry("discountPercent");

        cartToUpdate.setDiscount(discount);
        cartToUpdate.setFinalPrice(calculatePriceWithDiscount(cartId, discount));
        cartRepository.save(cartToUpdate);
    }

    public void cancelCart(Long cartId) throws CartNotFoundException {
        Cart cart = findCartById(cartId);
        validateCartForProcessing(cart);
        if (!(cart.getListOfItems().isEmpty())) {
            for (Item items : cart.getListOfItems()) {
                Warehouse warehouse = warehouseService.findWarehouseByProductId(items.getProduct().getProductID());
                warehouse.setProductQuantity(warehouse.getProductQuantity() + items.getQuantity());
                warehouseService.save(warehouse);
                itemService.delete(items);
            }
        }
        cartRepository.deleteById(cartId);
    }

    public void decideToFinalize(Long cartId, String authenticated) throws CartNotFoundException, InvalidDataException {
        Cart cart = findCartById(cartId);
        if (cart.getCartStatus() != CartStatus.PROCESSING) {
            throw new CartNotFoundException();
        }
        String answer = authenticated.toLowerCase();
        boolean isAuthenticated = switch (answer) {
            case "y", "yes" -> true;
            case "n", "no" -> false;
            default -> throw new InvalidDataException();
        };

        Task task = taskService.createTaskQuery()
            .processInstanceBusinessKey(String.valueOf(cartId))
            .singleResult();

        Map<String, Object> variables = new HashMap<>();
        variables.put("activity", "finalizeCart");
        variables.put("authenticated", isAuthenticated);

        String executionId = task.getExecutionId();
        runtimeService.setVariables(executionId, variables);

        taskService.complete(task.getId());
    }

    private Order createNewOrder(Long orderId, Long cartId, String userName)
        throws CartNotFoundException, UserNotFoundException, InvalidCustomerDataException, OrderNotFoundException {
        Cart cart = findCartById(cartId);
        LoggedCustomer loggedCustomer = customerService.verifyLogin1(userName);
        String listOfItems = cart.getListOfItems().stream()
            .map(itemMapper::mapToItemDto)
            .map(result -> "product: " + result.getProductName() + ", quantity: " + result.getProductQuantity())
            .collect(Collectors.joining("\n"));
        cart.setCalculatedPrice(calculateCurrentCartValue(cart));
        Order newOrder = orderService.findOrderById(orderId);
        newOrder.setLoggedCustomer(loggedCustomer);
        newOrder.setCart(cart);
        newOrder.setCreated(LocalDate.now());
        newOrder.setOrderStatus(OrderStatus.UNPAID);
        newOrder.setListOfProducts(listOfItems);
        newOrder.setCalculatedPrice(cart.getFinalPrice());
        newOrder.setPaymentDue(LocalDate.now().plusDays(14));
        newOrder.setCamundaProcessId(cart.getCamundaProcessId());
        orderService.save(newOrder);
      /*  Order createdOrder = Order.builder()
                .loggedCustomer(loggedCustomer)
                .cart(cart)
                .created(LocalDate.now())
                .orderStatus(OrderStatus.UNPAID)
                .listOfProducts(listOfItems)
                .calculatedPrice(cart.getFinalPrice())
                .paymentDue(LocalDate.now().plusDays(14))
                .camundaProcessId(cart.getCamundaProcessId())
                .build();
*/
        loggedCustomer.getListOfOrders().add(newOrder);
        customerService.updateCustomer(loggedCustomer);
        orderService.save(newOrder);
        return newOrder;
    }

    public OrderIdDTO finalizeCart(Long cartId, String userName)
        throws CartNotFoundException, UserNotFoundException, InvalidCustomerDataException {
        Cart cart = findCartById(cartId);
        validateCartForProcessing(cart);
        Order order = new Order();
        orderService.save(order);

        Task task = taskService.createTaskQuery()
            .processInstanceBusinessKey(String.valueOf(cartId))
            .singleResult();

        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", userName);
        variables.put("orderId", order.getOrderID());

        String executionId = task.getExecutionId();
        runtimeService.setVariables(executionId, variables);

        taskService.complete(task.getId());

        return orderMapper.mapToOrderId(order);
    }

    public void finalizeCartAuthenticatedCamunda(Long orderId, Long cartId, String userName)
        throws CartNotFoundException, UserNotFoundException, InvalidCustomerDataException, OrderNotFoundException {
        Cart cart = findCartById(cartId);
        Order order = createNewOrder(orderId, cartId, userName);
        emailService.send(emailCreator.createContent(order));
        cart.setCartStatus(CartStatus.FINALIZED);
        cartRepository.save(cart);

        Map<String, Object> variables = new HashMap<>();
        variables.put("orderId", order.getOrderID());
        runtimeService.setVariables(order.getCamundaProcessId(), variables);
    }

    private Order validateOrderToPay(Long orderId, String username) throws OrderNotFoundException {
        Order order = orderService.findByIdAndUserName(orderId, username);
        if (order.getOrderStatus() == OrderStatus.PAID) {
            throw new OrderNotFoundException();
        } else {
            return order;
        }
    }

    //TODO transaction with Bank
    private boolean orderIsPaid(Order order) {
        return true;
    }

    public void payForOrder(Long orderId, String userName) throws OrderNotFoundException, PaymentErrorException {
        Order orderToPay = validateOrderToPay(orderId, userName);
        boolean isPaid = orderIsPaid(orderToPay);
        if (!(isPaid)) {
            throw new PaymentErrorException();
        }

        Task task = taskService.createTaskQuery()
            .processInstanceBusinessKey(String.valueOf(orderToPay.getCart().getCartID()))
            .singleResult();

        Map<String, Object> variables = new HashMap<>();
        variables.put("activity", "payForOrder");

        String executionId = task.getExecutionId();
        runtimeService.setVariables(executionId, variables);

        taskService.complete(task.getId());
    }

    public void payForOrderCamunda(Long orderId) throws OrderNotFoundException, PaymentErrorException {
        Order order = orderService.findOrderById(orderId);
        boolean isPaid = orderIsPaid(order);
        if (!(isPaid)) {
            throw new PaymentErrorException();
        } else {
            order.setOrderStatus(OrderStatus.PAID);
            orderService.save(order);
            emailService.send(emailCreator.createContent(order));
            Cart cart = order.getCart();
            order.setCart(null);
            order.setPaid(LocalDate.now());
            order.setPaymentDue(null);
            orderService.save(order);
            cartRepository.deleteById(cart.getCartID());
            warehouseService.sentForShipment(order);
        }
    }

    public void payForCartUnauthenticatedCustomer(Long cartId, UnauthenticatedCustomerDto unauthenticatedCustomerDto)
        throws InvalidCustomerDataException, CartNotFoundException {
        checkCustomerDataValidity(unauthenticatedCustomerDto);
        Cart cart = findCartById(cartId);
        validateCartForProcessing(cart);

        Map<String, Object> variables = new HashMap<>();
        variables.put("firstName", unauthenticatedCustomerDto.getFirstName());
        variables.put("lastName", unauthenticatedCustomerDto.getLastName());
        variables.put("email", unauthenticatedCustomerDto.getEmail());
        variables.put("street", unauthenticatedCustomerDto.getStreet());
        variables.put("houseNo", unauthenticatedCustomerDto.getHouseNo());
        variables.put("flatNo", unauthenticatedCustomerDto.getFlatNo());
        variables.put("zipCode", unauthenticatedCustomerDto.getZipCode());
        variables.put("city", unauthenticatedCustomerDto.getCity());
        variables.put("country", unauthenticatedCustomerDto.getCountry());

        Task task = taskService.createTaskQuery()
            .processInstanceBusinessKey(String.valueOf(cartId))
            .singleResult();

        String executionId = task.getExecutionId();
        runtimeService.setVariables(executionId, variables);

        taskService.complete(task.getId());
    }

    public void payForCartUnauthenticatedCustomerCamunda(Long cartId,
        UnauthenticatedCustomerDto unauthenticatedCustomerDto) throws CartNotFoundException, PaymentErrorException {
        Cart cart = findCartById(cartId);
        cart.setCalculatedPrice(calculateCurrentCartValue(cart));
        cartRepository.save(cart);
        String listOfItems = cart.getListOfItems().stream()
            .map(itemMapper::mapToItemDto)
            .map(result -> "\nproduct: " + result.getProductName() + ", quantity: " + result.getProductQuantity())
            .collect(Collectors.joining(" "));
        Order createdOrder = new Order(new LoggedCustomer(null, null, unauthenticatedCustomerDto.getFirstName(),
            unauthenticatedCustomerDto.getLastName(),
            unauthenticatedCustomerDto.getEmail(),
            new Address(unauthenticatedCustomerDto.getStreet(), unauthenticatedCustomerDto.getHouseNo(),
                unauthenticatedCustomerDto.getFlatNo(), unauthenticatedCustomerDto.getZipCode(),
                unauthenticatedCustomerDto.getCity(), unauthenticatedCustomerDto.getCountry())),
            LocalDate.now(), OrderStatus.UNPAID, listOfItems, cart.getFinalPrice(), cart,
            cart.getCamundaProcessId());
        customerService.updateCustomer(createdOrder.getLoggedCustomer());
        orderService.save(createdOrder);
        boolean isPaid = orderIsPaid(createdOrder);
        if (!(isPaid)) {
            throw new PaymentErrorException();
        } else {
            warehouseService.sentForShipment(createdOrder);
            Cart cartToPay = createdOrder.getCart();
            emailService.send(emailCreator.createContent(createdOrder));
            createdOrder.setCart(null);
            createdOrder.setOrderStatus(OrderStatus.PAID);
            createdOrder.setPaid(LocalDate.now());
            orderService.save(createdOrder);

            long customerId = createdOrder.getLoggedCustomer().getCustomerID();
            createdOrder.setLoggedCustomer(null);
            orderService.save(createdOrder);
            customerService.deleteUnauthenticatedCustomer(customerId);
            cartRepository.deleteById(cartToPay.getCartID());
        }
    }

    private void checkCustomerDataValidity(UnauthenticatedCustomerDto unauthenticatedCustomerDto)
        throws InvalidCustomerDataException {
        if ((unauthenticatedCustomerDto.getFirstName() == null || unauthenticatedCustomerDto.getFirstName().isEmpty())
            || (unauthenticatedCustomerDto.getLastName() == null || unauthenticatedCustomerDto.getLastName()
            .isEmpty())
            || (unauthenticatedCustomerDto.getStreet() == null || unauthenticatedCustomerDto.getStreet().isEmpty())
            || (unauthenticatedCustomerDto.getHouseNo() == null || unauthenticatedCustomerDto.getHouseNo()
            .isEmpty())
            || (unauthenticatedCustomerDto.getCity() == null || unauthenticatedCustomerDto.getCity().isEmpty())
            || (unauthenticatedCustomerDto.getZipCode() == null || unauthenticatedCustomerDto.getZipCode().isEmpty()
            || !unauthenticatedCustomerDto.getZipCode().matches("^[0-9]{2}[-]?[0-9]{3}$"))
            || (unauthenticatedCustomerDto.getEmail() == null || unauthenticatedCustomerDto.getEmail().isEmpty()
            || !unauthenticatedCustomerDto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$"))) {
            throw new InvalidCustomerDataException();
        }
    }

    public void cancelOrder(Long orderId) throws OrderNotFoundException {
        Order orderToCancel = orderService.findOrderById(orderId);
        if (orderToCancel.getOrderStatus() == OrderStatus.PAID) {
            throw new OrderNotFoundException();
        }
        for (Item items : orderToCancel.getCart().getListOfItems()) {
            Warehouse warehouse = warehouseService.findWarehouseByProductId(items.getProduct().getProductID());
            warehouse.setProductQuantity(warehouse.getProductQuantity() + items.getQuantity());
            warehouseService.save(warehouse);
            items.setCart(null);
        }
        orderToCancel.getCart().getListOfItems().clear();
        orderService.save(orderToCancel);
        emailService.send(emailCreator.createContent(orderToCancel));
        LoggedCustomer _Logged_Customer = orderToCancel.getLoggedCustomer();
        _Logged_Customer.getListOfOrders().remove((orderToCancel));
        customerService.updateCustomer(_Logged_Customer);
        orderToCancel.setLoggedCustomer(null);
        Long cartId = orderToCancel.getCart().getCartID();
        orderToCancel.setCart(null);
        cartRepository.deleteById(cartId);
        orderService.deleteOrder(orderToCancel);
    }

    public void cancelOrderLogged(Long orderId, String userName)
        throws UserNotFoundException, OrderNotFoundException, InvalidCustomerDataException {
        customerService.verifyLogin1(userName);
        orderService.findByIdAndUserName(orderId, userName);

        Order order = orderService.findByIdAndUserName(orderId, userName);

        Task task = taskService.createTaskQuery()
            .processInstanceBusinessKey(String.valueOf(order.getCart().getCartID()))
            .singleResult();

        Map<String, Object> variables = new HashMap<>();
        variables.put("activity", "cancelOrder");

        String executionId = task.getExecutionId();
        runtimeService.setVariables(executionId, variables);

        taskService.complete(task.getId());
    }

    public void deleteByCartStatus(CartStatus cartStatus, String processId) {
        cartRepository.deleteByCartStatusAndAndCamundaProcessId(cartStatus, processId);
    }

    public void removeNotFinalizedCartsCamunda(String processId) {
        log.info("deleting empty carts " + LocalDate.now());
        try {
            deleteByCartStatus(CartStatus.EMPTY, processId);
            log.info("empty carts deleted " + LocalDate.now());
        } catch (Exception e) {
            log.error("empty cars were not removed ", e);
        }

        log.info("deleting carts with status\"PROCESSING\" " + LocalDate.now());
        try {
            List<Cart> listOfCarts = cartRepository.selectByProcessingTime(processId);
            if (listOfCarts.size() > 0) {
                for (Cart cart : listOfCarts) {
                    cancelCart(cart.getCartID());
                    log.info("carts with status\"PROCESSING\" deleted " + LocalDate.now());
                }
            } else {
                log.info("no carts with status\"PROCESSING\" present " + LocalDate.now());
            }
        } catch (Exception e) {
            log.error("\"PROCESSING\" carts were not removed", e);
        }
    }

    public void remindAboutPaymentCamunda(String processId) {
        List<Order> unpaidOrders = orderService.findCloseUnpaidOrders(processId);
        for (Order order : unpaidOrders) {
            emailService.send(emailCreator.createContentReminder(order));
        }
    }
}
