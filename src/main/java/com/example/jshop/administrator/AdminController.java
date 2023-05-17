package com.example.jshop.administrator;

import java.util.List;
import com.example.jshop.cartsandorders.domain.order.OrderDtoToCustomer;
import com.example.jshop.errorhandlers.exceptions.CategoryException;
import com.example.jshop.errorhandlers.exceptions.CategoryExistsException;
import com.example.jshop.errorhandlers.exceptions.CategoryNotFoundException;
import com.example.jshop.errorhandlers.exceptions.InvalidCategoryNameException;
import com.example.jshop.errorhandlers.exceptions.InvalidOrderStatusException;
import com.example.jshop.errorhandlers.exceptions.InvalidPriceException;
import com.example.jshop.errorhandlers.exceptions.InvalidQuantityException;
import com.example.jshop.errorhandlers.exceptions.OrderNotFoundException;
import com.example.jshop.errorhandlers.exceptions.ProductNotFoundException;
import com.example.jshop.warehouseandproducts.domain.category.CategoryDto;
import com.example.jshop.warehouseandproducts.domain.category.CategoryWithProductsDto;
import com.example.jshop.warehouseandproducts.domain.product.ProductDto;
import com.example.jshop.warehouseandproducts.domain.product.ProductDtoAllInfo;
import com.example.jshop.warehouseandproducts.domain.warehouse.WarehouseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("v1/j-shop/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("add-category")
    ResponseEntity<Void> addNewCategory(@RequestBody CategoryDto categoryDto)
        throws InvalidCategoryNameException, CategoryExistsException {
        adminService.addNewCategory(categoryDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("remove-category")
    ResponseEntity<Void> removeCategory(@RequestBody CategoryDto categoryDto)
        throws CategoryNotFoundException, CategoryException {
        adminService.removeCategory(categoryDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("show-category")
    ResponseEntity<List<CategoryWithProductsDto>> showAllCategoriesAndProducts() {
        return ResponseEntity.ok(adminService.showAllCategoriesWithProducts());
    }

    @GetMapping("show-category/name")
    ResponseEntity<CategoryWithProductsDto> showCategoriesByName(@RequestParam String categoryName)
        throws CategoryNotFoundException {
        return ResponseEntity.ok(adminService.showCategoryByNameWithProducts(categoryName));
    }

    @PostMapping("add-product")
    ResponseEntity<ProductDtoAllInfo> addProduct(@RequestBody ProductDto productDto)
        throws InvalidCategoryNameException, CategoryExistsException, InvalidPriceException {
        return ResponseEntity.ok(adminService.addNewProduct(productDto));
    }

    @PutMapping("update-product")
    ResponseEntity<ProductDtoAllInfo> updateProduct(@RequestParam Long productId, @RequestBody ProductDto productDto)
        throws ProductNotFoundException, InvalidCategoryNameException, CategoryExistsException, InvalidPriceException {
        return ResponseEntity.ok(adminService.updateProduct(productId, productDto));
    }

    @DeleteMapping("remove-product")
    ResponseEntity<Void> removeProduct(@RequestParam Long productId) throws ProductNotFoundException {
        adminService.deleteProductById(productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("show-product")
    ResponseEntity<List<ProductDtoAllInfo>> showAllProducts() {
        return ResponseEntity.ok(adminService.showAllProducts());
    }

    @PostMapping("add-to-warehouse")
    ResponseEntity<WarehouseDto> addProductToWarehouse(@RequestParam Long productId,
        @RequestParam Integer productQuantity)
        throws ProductNotFoundException, InvalidQuantityException, CategoryNotFoundException {
        return ResponseEntity.ok(adminService.addOrUpdateProductInWarehouse(productId, productQuantity));
    }

    @DeleteMapping("remove-from-warehouse")
    ResponseEntity<Void> removeProductFromWarehouse(@RequestParam Long productId) throws ProductNotFoundException {
        adminService.deleteProductFromWarehouse(productId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("show-products-in-warehouse")
    ResponseEntity<List<WarehouseDto>> displayAllProductsInWarehouse() {
        return ResponseEntity.ok(adminService.displayAllProductsInWarehouse());
    }

    @GetMapping("show-order")
    ResponseEntity<List<OrderDtoToCustomer>> displayAllOrders(@RequestParam(required = false) String order_status)
        throws InvalidOrderStatusException, OrderNotFoundException {
        return ResponseEntity.ok(adminService.displayOrders(order_status));
    }
}
