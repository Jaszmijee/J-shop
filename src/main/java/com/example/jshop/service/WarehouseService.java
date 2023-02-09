package com.example.jshop.service;

import com.example.jshop.exception.CategoryNotFoundException;
import com.example.jshop.exception.ItemNotFoundEXception;
import com.example.jshop.domain.warehouse.Warehouse;
import com.example.jshop.exception.ProductNotFoundException;
import com.example.jshop.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WarehouseService {

    @Autowired
    WarehouseRepository warehouseRepository;

    public List<Warehouse> findAll() {
        return warehouseRepository.findAll();
    }

    public Warehouse findItemByID(Long itemId)  {
        return warehouseRepository.findWarehouseByProductId(itemId).orElse(null);
    }

    public List<Warehouse> findProductsByCategory(String category, Integer limit) throws CategoryNotFoundException {
        List<Warehouse> list = warehouseRepository.findWarehouseByProduct_Category_Name(category, limit);
        if (list.isEmpty()) {
            throw new CategoryNotFoundException();
        }
        return list;
    }

    public Warehouse save(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    public void deleteById(Long productId) {
        warehouseRepository.deleteByProduct_ProductID(productId);
    }

    public List<Warehouse> findProductsWithSelection(String categoryName, String productName, BigDecimal productPrice, Integer limit) {
      return warehouseRepository.findWarehouseByProduct_CategoryOrProduct_ProductNameOAndProduct_Price(categoryName,productName,productPrice,limit);
    }
}
