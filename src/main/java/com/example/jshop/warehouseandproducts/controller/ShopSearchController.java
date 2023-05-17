package com.example.jshop.warehouseandproducts.controller;

import java.math.BigDecimal;
import java.util.List;
import com.example.jshop.errorhandlers.exceptions.LimitException;
import com.example.jshop.errorhandlers.exceptions.ProductNotFoundException;
import com.example.jshop.warehouseandproducts.domain.warehouse.Warehouse;
import com.example.jshop.warehouseandproducts.domain.warehouse.WarehouseDto;
import com.example.jshop.warehouseandproducts.mapper.WarehouseMapper;
import com.example.jshop.warehouseandproducts.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/j-shop/search")
public class ShopSearchController {

    private final WarehouseService warehouseService;
    private final WarehouseMapper warehouseMapper;

    @Autowired
    public ShopSearchController(WarehouseService warehouseService, WarehouseMapper warehouseMapper) {
        this.warehouseService = warehouseService;
        this.warehouseMapper = warehouseMapper;
    }

    @GetMapping("select")
    ResponseEntity<List<WarehouseDto>> showSelectedProducts(@RequestParam(required = false) String categoryName,
        @RequestParam(required = false) String productName, @RequestParam(required = false) BigDecimal productPrice,
        @RequestParam Integer limit) throws LimitException, ProductNotFoundException {
        List<Warehouse> warehouse = warehouseService.findProductsInWarehouseWithSelection(categoryName, productName,
            productPrice, limit);
        return ResponseEntity.ok(warehouseMapper.mapToWarehouseDtoList(warehouse));
    }

    @GetMapping(value = "{productId}")
    public WarehouseDto findProductByID(@PathVariable Long productId) throws ProductNotFoundException {
        Warehouse warehouse = warehouseService.findWarehouseByProductId(productId);
        if (warehouse == null) {
            throw new ProductNotFoundException();
        }
        return warehouseMapper.mapToWarehouseDto(warehouse);
    }
}

