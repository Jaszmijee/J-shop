package com.example.jshop.service;

import com.example.jshop.domain.order.ORDER_STATUS;
import com.example.jshop.domain.customer.Address;
import com.example.jshop.domain.order.Order;
import com.example.jshop.domain.cart.*;
import com.example.jshop.domain.customer.Customer_Logged;
import com.example.jshop.domain.customer.LoggedCustomerDto;
import com.example.jshop.domain.customer.UnlogedCustomerDto;
import com.example.jshop.domain.mail.Mail;
import com.example.jshop.domain.order.OrderDtoToCustomer;
import com.example.jshop.domain.product.Product;
import com.example.jshop.domain.warehouse.Warehouse;
import com.example.jshop.exception.*;
import com.example.jshop.mapper.CartMapper;
import com.example.jshop.mapper.ItemMapper;
import com.example.jshop.mapper.OrderMapper;
import com.example.jshop.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    WarehouseService warehouseService;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    ItemService itemService;

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    CustomerService customerService;

    @Autowired
    SimpleEmailService emailService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderMapper orderMapper;

    private Cart findCartById(Long cartId) throws CartNotFoundException {
        return cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);
    }

    public Cart createCart() {
        Cart newCart = new Cart();
        newCart.setCartStatus(CartStatus.EMPTY);
        newCart.setCreated(LocalDate.now());
        cartRepository.save(newCart);
        return newCart;
    }

    private void findIfQuantityInWarehouseIsEnough(Warehouse warehouse, int quantity) throws ItemNotAvailableException, NotEnoughItemsException {
        if (warehouse.getProductQuantity() == 0) {
            throw new ItemNotAvailableException();
        }
        if (warehouse.getProductQuantity() < quantity) {
            throw new NotEnoughItemsException();
        }
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
        } else return BigDecimal.ZERO;
    }

    private void validateCartForProcessing(Cart cart) throws CartNotFoundException {
        if (cart.getCartStatus() == CartStatus.FINALIZED) {
            throw new CartNotFoundException();
        }
    }

    private Warehouse validateProductInWarehouse(Long productId) throws ProductNotFoundException {
        Warehouse warehouse = warehouseService.findItemByID(productId);
        if (warehouse == null) {
            throw new ProductNotFoundException();
        } else return warehouse;
    }

    public CartDto addToCart(Long cartId, CartItemsDto cartItemsDto) throws CartNotFoundException, ItemNotAvailableException, NotEnoughItemsException, ProductNotFoundException {
        Cart cartToUpdate = findCartById(cartId);
        validateCartForProcessing(cartToUpdate);
        Warehouse warehouse = validateProductInWarehouse(cartItemsDto.getProductId());
        findIfQuantityInWarehouseIsEnough(warehouse, cartItemsDto.getQuantity());
        Product product = warehouse.getProduct();
        Item item;
        List<Item> items = cartToUpdate.getListOfItems().stream().filter(i -> i.getProduct().getProductID().equals(product.getProductID()))
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
        return cartMapper.mapCartToCartDto(cartToUpdate);
    }

    public Cart showCart(Long cartId) throws CartNotFoundException {
        return cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);
    }

    public CartDto removeFromCart(Long cartId, CartItemsDto cartItemsDto) throws CartNotFoundException, ProductNotFoundException {
        Cart cartToUpdate = findCartById(cartId);
        validateCartForProcessing(cartToUpdate);
        Item item;
        List<Item> items = cartToUpdate.getListOfItems().stream().filter(i -> i.getProduct().getProductID().equals(cartItemsDto.getProductId()))
                .toList();
        if (items.size() == 0) {
            throw new ProductNotFoundException();
        }
        Warehouse warehouse = warehouseService.findItemByID(cartItemsDto.getProductId());
        if (items.size() > 0) {
            item = items.get(0);
            if (item.getQuantity() <= cartItemsDto.getQuantity()) {
                updateProductInWarehouse(warehouse, -(cartItemsDto.getQuantity()));
                cartToUpdate.getListOfItems().remove(item);
                itemService.delete(item);
            } else {
                updateProductInWarehouse(warehouse, -(cartItemsDto.getQuantity()));
                item.setQuantity(item.getQuantity() - cartItemsDto.getQuantity());
                itemService.save(item);
                cartToUpdate.getListOfItems().set(cartToUpdate.getListOfItems().indexOf(item), item);
            }
        }
        if (cartToUpdate.getListOfItems().isEmpty()) {
            cartToUpdate.setCartStatus(CartStatus.EMPTY);
        }
        cartToUpdate.setCalculatedPrice(calculateCurrentCartValue(cartToUpdate));
        cartRepository.save(cartToUpdate);
        return cartMapper.mapCartToCartDto(cartToUpdate);
    }

    public void cancelCart(Long cartId) throws CartNotFoundException {
        Cart cart = findCartById(cartId);
        validateCartForProcessing(cart);
        if (!(cart.getListOfItems().isEmpty())) {
            for (Item items : cart.getListOfItems()) {
                Warehouse warehouse = warehouseService.findItemByID(items.getProduct().getProductID());
                warehouse.setProductQuantity(warehouse.getProductQuantity() + items.getQuantity());
                warehouseService.save(warehouse);
                itemService.delete(items);
            }
        }
        cartRepository.deleteById(cartId);
    }

    private Order createNewOrder(Long cartId, LoggedCustomerDto loggedCustomerDto) throws CartNotFoundException, UserNotFoundException, AccessDeniedException {
        Cart cart = findCartById(cartId);
        Customer_Logged customer = customerService.verifyLogin(loggedCustomerDto.getUsername(), loggedCustomerDto.getPassword());

        String listOfItems = cart.getListOfItems().stream()
                .map(item -> itemMapper.mapToItemDto(item))
                .map(result -> "\nproduct: " + result.getProductName() + ", quantity: " + result.getProductQuantity() + ", total price: " + result.getCalculatedPrice())
                .collect(Collectors.joining("\n"));
        Order createdOrder = new Order(customer, cart, LocalDate.now(), ORDER_STATUS.UNPAID, listOfItems, cart.getCalculatedPrice());
        customer.getListOfOrders().add(createdOrder);
        customerService.saveCustomer(customer);
        orderService.save(createdOrder);
        return createdOrder;
    }

    private Mail createContent(Order order) {

        String subject = "Your order: " + order.getOrderID();
        String message = "Your order: " + order.getOrderID() + ", created on: " + order.getCreated() +
                " " + order.getListOfProducts() + " " +
                "\n total sum: " + order.getCalculatedPrice();
        if (order.getOrder_status() == ORDER_STATUS.UNPAID) {
            message += "\n Your payment is due on: " + order.getCreated().plusDays(14);
        } else if (order.getCart().getListOfItems().isEmpty()) {
            message = "Your order: " + order.getOrderID() + " is cancelled";
        } else {
            message += "\n Your order is paid and ready for shipment,";
        }

        message += """
                Thank you for your purchase
                Your J-Shop""".indent(1);

        return new Mail(
                order.getCustomer().getEmail(),
                subject,
                message,
                "admin@j-shop.com"
        );
    }

    public OrderDtoToCustomer finalizeCart(Long cartId, LoggedCustomerDto loggedCustomerDto) throws CartNotFoundException, UserNotFoundException, AccessDeniedException {
        customerService.verifyLogin(loggedCustomerDto.getUsername(), loggedCustomerDto.getPassword());
        Order order = createNewOrder(cartId, loggedCustomerDto);
        emailService.send(createContent(order));
        Cart cart = findCartById(cartId);
        validateCartForProcessing(cart);
        cart.setCartStatus(CartStatus.FINALIZED);
        cartRepository.save(cart);
        return orderMapper.mapToOrderDtoToCustomer(order);
    }

    private Order validateOrderToPay(Long orderId, String username) throws OrderNotFoundException {
        Order order = orderService.findByIdAndUserName(orderId, username);
        if (order.getOrder_status() != ORDER_STATUS.UNPAID) {
            throw new OrderNotFoundException();
        } else return order;
    }

    //TODO transaction with Bank
    private boolean orderIsPaid(Order order) {
        return true;
    }

    public OrderDtoToCustomer payForCart(Long orderId, LoggedCustomerDto loggedCustomerDto) throws UserNotFoundException, AccessDeniedException, OrderNotFoundException, PaymentErrorException {
        customerService.verifyLogin(loggedCustomerDto.getUsername(), loggedCustomerDto.getPassword());
        Order order = validateOrderToPay(orderId, loggedCustomerDto.getUsername());
        boolean isPaid = orderIsPaid(order);
        if (!(isPaid)) {
            throw new PaymentErrorException();
        } else {
            Cart cart = order.getCart();
            order.setCart(null);
            order.setOrder_status(ORDER_STATUS.PAID);
            order.setPaid(LocalDate.now());
            orderService.save(order);
            emailService.send(createContent(order));
            cartRepository.deleteById(cart.getCartID());
            warehouseService.sentForShipment(order);
        }
        return orderMapper.mapToOrderDtoToCustomer(order);
    }


    public OrderDtoToCustomer payForCartUnlogged(Long cartId, UnlogedCustomerDto unlogedCustomerDto) throws CartNotFoundException, PaymentErrorException {
        Cart cart = findCartById(cartId);
        String listOfItems = cart.getListOfItems().stream()
                .map(item -> itemMapper.mapToItemDto(item))
                .map(result -> "product: " + result.getProductName() + ", quantity: " + result.getProductQuantity() + ", total price: " + result.getCalculatedPrice())
                .collect(Collectors.joining("\n"));
        Order createdOrder = new Order(new Customer_Logged(null, null, unlogedCustomerDto.getFirstName(), unlogedCustomerDto.getLastName(),
                unlogedCustomerDto.getEmail(), new Address(unlogedCustomerDto.getStreet(), unlogedCustomerDto.getHouseNo(), unlogedCustomerDto.getFlatNo(), unlogedCustomerDto.getZipCode(), unlogedCustomerDto.getCity(), unlogedCustomerDto.getCountry())),
                cart, LocalDate.now(), ORDER_STATUS.UNPAID, listOfItems, cart.getCalculatedPrice());

        customerService.saveCustomer(createdOrder.getCustomer());
        orderService.save(createdOrder);
        boolean isPaid = orderIsPaid(createdOrder);
        if (!(isPaid)) {
            throw new PaymentErrorException();
        } else {
            Cart cartToPay = createdOrder.getCart();
            createdOrder.setCart(null);
            createdOrder.setOrder_status(ORDER_STATUS.PAID);
            createdOrder.setPaid(LocalDate.now());
            orderService.save(createdOrder);
            emailService.send(createContent(createdOrder));
            //TODO sent for shipment
            warehouseService.sentForShipment(createdOrder);
            long customerId = createdOrder.getCustomer().getCustomerID();
            createdOrder.setCustomer(null);
            orderService.save(createdOrder);
            customerService.deleteUnloggedCustomer(customerId);
            cartRepository.deleteById(cartToPay.getCartID());
        }
        return orderMapper.mapToOrderDtoToCustomer(createdOrder);
    }

    public void cancelOrder(Long orderId) throws OrderNotFoundException {
        Order orderToCancel = orderService.findOrderById(orderId);
        if (orderToCancel.getOrder_status() == ORDER_STATUS.PAID) {
            throw new OrderNotFoundException();
        } else {
            if (!(orderToCancel.getCart().getListOfItems().isEmpty())) {
                for (Item items : orderToCancel.getCart().getListOfItems()) {
                    Warehouse warehouse = warehouseService.findItemByID(items.getProduct().getProductID());
                    warehouse.setProductQuantity(warehouse.getProductQuantity() + items.getQuantity());
                    warehouseService.save(warehouse);
                    itemService.delete(items);
                }
                emailService.send(createContent(orderToCancel));
                Customer_Logged customer_logged = orderToCancel.getCustomer();
                customer_logged.getListOfOrders().remove((orderToCancel));
                customerService.saveCustomer(customer_logged);
                orderToCancel.setCustomer(null);
                Long cartId = orderToCancel.getCart().getCartID();
                orderToCancel.setCart(null);
                cartRepository.deleteById(cartId);
                orderService.deleteOrder(orderToCancel);
            }
        }
    }

    public void cancelOrderLogged(Long orderId, LoggedCustomerDto loggedCustomerDto) throws UserNotFoundException, AccessDeniedException, OrderNotFoundException {
        customerService.verifyLogin(loggedCustomerDto.getUsername(), loggedCustomerDto.getPassword());
        cancelOrder(orderId);
    }

    public void deleteByCartStatus(CartStatus cartStatus) {
        cartRepository.deleteByCartStatus(cartStatus);
    }

    public void deleteByProcessingTime() throws CartNotFoundException {
        List<Cart> listOfCarts = cartRepository.selectByProcessingTime();
        for (Cart cart : listOfCarts) {
            cancelCart(cart.getCartID());
        }
    }
}






