package com.example.jshop.controller;

import com.example.jshop.domain.warehouse.Warehouse;
import com.example.jshop.domain.warehouse.WarehouseDto;
import com.example.jshop.exception.CategoryNotFoundException;
import com.example.jshop.exception.LimitException;
import com.example.jshop.exception.ProductNotFoundException;
import com.example.jshop.mapper.CategoryMapper;
import com.example.jshop.mapper.WarehouseMapper;
import com.example.jshop.service.CategoryService;
import com.example.jshop.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("search")
public class ShopSearchController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    WarehouseService warehouseService;
    @Autowired
    WarehouseMapper warehouseMapper;

    @GetMapping("warehouse")
    ResponseEntity<List<WarehouseDto>> showProductsInCategory(@RequestParam String categoryName, int limit) throws CategoryNotFoundException {
        List<Warehouse> warehouse = warehouseService.findProductsByCategory(categoryName, limit);
        return ResponseEntity.ok(warehouseMapper.mapToWarehouseDtoList(warehouse));
    }

    @GetMapping("warehouse/select")
    ResponseEntity<List<WarehouseDto>> showSelectedProducts(@RequestParam(required = false) String categoryName, @RequestParam(required = false) String productName, @RequestParam(required = false) BigDecimal productPrice, Integer limit) throws CategoryNotFoundException, LimitException {
        List<Warehouse> warehouse = warehouseService.findProductsWithSelection(categoryName, productName, productPrice, limit);
        return ResponseEntity.ok(warehouseMapper.mapToWarehouseDtoList(warehouse));
    }

    @GetMapping("warehouse/selectById")
    public WarehouseDto findItemByID(@RequestParam Long itemId) {
        return warehouseMapper.mapToWarehouseDto(warehouseService.findItemByID(itemId));
    }
}
