package com.example.jshop.controller;

import com.example.jshop.admin.config.AdminConfig;
import com.example.jshop.domain.category.CategoryWithProductsDto;
import com.example.jshop.domain.order.OrderDtoToCustomer;
import com.example.jshop.domain.product.ProductDto;
import com.example.jshop.domain.product.ProductDtoAllInfo;
import com.example.jshop.domain.warehouse.WarehouseDto;
import com.example.jshop.exception.AccessDeniedException;
import com.example.jshop.domain.category.CategoryDto;
import com.example.jshop.exception.*;
import com.example.jshop.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/j-shop/admin")
public class AdminController {

    @Autowired
    private final AdminConfig adminConfig;

    @Autowired
    private final AdminService adminService;

    private void verifyAdmin(@RequestParam String key, @RequestParam String token) throws AccessDeniedException {
        if (!(adminConfig.getAdminKey().equals(key) && adminConfig.getAdminToken().equals(token))) {
            throw new AccessDeniedException();
        }
    }

    @PostMapping("category")
    ResponseEntity<Void> addNewCategory(@RequestParam String key, @RequestParam String token, @RequestBody CategoryDto categoryDto) throws AccessDeniedException, InvalidArgumentException, CategoryExistsException {
        verifyAdmin(key, token);
        adminService.addNewCategory(categoryDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("category")
    ResponseEntity<Void> removeCategory(@RequestParam String key, @RequestParam String token, @RequestBody CategoryDto categoryDto) throws AccessDeniedException, CategoryNotFoundException, CategoryException {
        verifyAdmin(key, token);
        adminService.removeCategory(categoryDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("category")
    ResponseEntity<List<CategoryWithProductsDto>> showAllCategoriesAndProducts(@RequestParam String key, @RequestParam String token) throws AccessDeniedException {
        verifyAdmin(key, token);
        return ResponseEntity.ok(adminService.showAllCategories());
    }

    @GetMapping("category/name")
    ResponseEntity<CategoryWithProductsDto> showCategories(@RequestParam String key, @RequestParam String token, @RequestParam String categoryName) throws CategoryNotFoundException, AccessDeniedException {
        verifyAdmin(key, token);
        return ResponseEntity.ok(adminService.searchForProductsInCategory(categoryName));
    }

    @PostMapping("product")
    ResponseEntity<ProductDtoAllInfo> addProduct(@RequestParam String key, @RequestParam String token, @RequestBody ProductDto
            productDto) throws AccessDeniedException, InvalidArgumentException, CategoryExistsException, SQLException, InvalidPriceException {
        verifyAdmin(key, token);
        return ResponseEntity.ok(adminService.addProduct(productDto));
    }

    @PutMapping("product")
    ResponseEntity<ProductDtoAllInfo> updateProduct(@RequestParam String key, @RequestParam String token, @RequestParam Long productId, @RequestBody ProductDto
            productDto) throws AccessDeniedException, CategoryNotFoundException, ProductNotFoundException, InvalidArgumentException, CategoryExistsException, InvalidPriceException {
        verifyAdmin(key, token);
        return ResponseEntity.ok(adminService.updateProduct(productId, productDto));
    }

    @DeleteMapping("product")
    ResponseEntity<Void> removeProduct(@RequestParam String key, @RequestParam String token, @RequestParam Long productId) throws AccessDeniedException, ItemNotFoundEXception, ProductNotFoundException {
    verifyAdmin(key, token);
    adminService.deleteProductById(productId);
    return ResponseEntity.ok().build();
    }

    @GetMapping("product")
    ResponseEntity<List<ProductDtoAllInfo>> showAllProducts(@RequestParam String key, @RequestParam String token) throws AccessDeniedException {
        verifyAdmin(key, token);
        return ResponseEntity.ok(adminService.showAllProducts());
    }

    @PostMapping("warehouse")
    ResponseEntity<WarehouseDto> addProductToWarehouse(@RequestParam String key, @RequestParam String token, @RequestParam Long productId, @RequestParam Integer productQuantity) throws
            AccessDeniedException, ProductNotFoundException, InvalidQuantityException, CategoryNotFoundException {
        verifyAdmin(key, token);
        return ResponseEntity.ok(adminService.addOrUpdateProductInWarehouse(productId, productQuantity));
    }

    @DeleteMapping("warehouse")
    ResponseEntity<Void> removeProductFromWarehouse(@RequestParam String key, @RequestParam String token, @RequestParam Long productId) throws
            AccessDeniedException, ProductNotFoundException {
        verifyAdmin(key, token);
        adminService.deleteProductFromWarehouse(productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("warehouse")
    ResponseEntity<List<WarehouseDto>> displayAllItemsInWareHouse(@RequestParam String key, @RequestParam String token) throws
            AccessDeniedException {
        verifyAdmin(key, token);
        return ResponseEntity.ok(adminService.displayAllItemsInWarehouse());
    }

    @GetMapping("orders")
    ResponseEntity<List<OrderDtoToCustomer>> displayAllOrders(@RequestParam String key, @RequestParam String token, @RequestParam(required = false) String order_status) throws
            AccessDeniedException {
        verifyAdmin(key, token);
        return ResponseEntity.ok(adminService.displayOrders(order_status));
    }
}
