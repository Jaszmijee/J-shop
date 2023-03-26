package com.example.jshop.warehouseandproducts.mapper;

import com.example.jshop.warehouseandproducts.domain.warehouse.Warehouse;
import com.example.jshop.warehouseandproducts.domain.warehouse.WarehouseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WarehouseMapper {

    public List<WarehouseDto> mapToWarehouseDtoList(List<Warehouse> items) {
        return items.stream().map(this::mapToWarehouseDto).collect(Collectors.toList());
    }

    public WarehouseDto mapToWarehouseDto(Warehouse warehouse) {
        return new WarehouseDto(
                warehouse.getWarehouseID(),
                warehouse.getProduct().getProductID(),
                warehouse.getProduct().getProductName(),
                warehouse.getProduct().getCategory().getName(),
                warehouse.getProduct().getPrice(),
                warehouse.getProductQuantity());
      }


}