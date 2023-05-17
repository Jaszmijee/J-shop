package com.example.jshop.warehouseandproducts.service;

import java.math.BigDecimal;
import java.util.List;
import com.example.jshop.cartsandorders.domain.order.Order;
import com.example.jshop.errorhandlers.exceptions.LimitException;
import com.example.jshop.errorhandlers.exceptions.ProductNotFoundException;
import com.example.jshop.warehouseandproducts.domain.warehouse.Warehouse;
import com.example.jshop.warehouseandproducts.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public List<Warehouse> findAllProductsInWarehouse() {
        return warehouseRepository.findAll();
    }

    public Warehouse findWarehouseByProductId(Long itemId) {
        return warehouseRepository.findWarehouseByProduct_ProductID(itemId).orElse(null);
    }

    public Warehouse save(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    public void deleteProductFromWarehouseByProductId(Long productId) {
        warehouseRepository.deleteByProduct_ProductID(productId);
    }

    public List<Warehouse> findProductsInWarehouseWithSelection(String categoryName, String productName,
        BigDecimal productPrice, Integer limit) throws LimitException, ProductNotFoundException {
        if (limit > 100 || limit < 1) {
            throw new LimitException();
        }
        List<Warehouse> listOfProducts = warehouseRepository.findWarehouseByProduct_CategoryOrProduct_ProductNameOAndProduct_Price(
            categoryName, productName, productPrice, limit);
        if (listOfProducts.isEmpty()) {
            throw new ProductNotFoundException();
        }
        return listOfProducts;
    }

    public void sentForShipment(Order createdOrder) {
        String shipment = createdOrder.getListOfProducts() + "\ntotal price " + createdOrder.getCalculatedPrice() + "\n"
            + createdOrder.getLoggedCustomer().getFirstName() + " " + createdOrder.getLoggedCustomer().getLastName()
            + ", " + createdOrder.getLoggedCustomer().getAddress();
        System.out.println("prepare and send shipment: " + shipment);
    }
}
