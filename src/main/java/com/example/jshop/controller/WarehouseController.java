package com.example.jshop.controller;

import com.example.jshop.mapper.WarehouseMapper;
import com.example.jshop.service.WarehouseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "v1/j-shop/warehouse")
public class WarehouseController {

    WarehouseService wareHouseService;
    WarehouseMapper warehouseMapper;





}
