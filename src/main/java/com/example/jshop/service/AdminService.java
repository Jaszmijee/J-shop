package com.example.jshop.service;

import com.example.jshop.domain.category.Category;
import com.example.jshop.domain.category.CategoryDto;
import com.example.jshop.domain.category.CategoryWithProductsDto;
import com.example.jshop.domain.order.Order;
import com.example.jshop.domain.order.OrderDtoToCustomer;
import com.example.jshop.domain.product.Product;
import com.example.jshop.domain.product.ProductDto;
import com.example.jshop.domain.product.ProductDtoAllInfo;
import com.example.jshop.domain.warehouse.Warehouse;
import com.example.jshop.domain.warehouse.WarehouseDto;
import com.example.jshop.exception.*;
import com.example.jshop.mapper.CategoryMapper;
import com.example.jshop.mapper.OrderMapper;
import com.example.jshop.mapper.ProductMapper;
import com.example.jshop.mapper.WarehouseMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    ProductService productService;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderMapper orderMapper;


    public void addNewCategory(CategoryDto categoryDto) throws InvalidArgumentException, CategoryExistsException {
        Category category = categoryMapper.mapToCategory(categoryDto);
        categoryService.addCategory(category);
    }

    public void removeCategory(CategoryDto categoryDto) throws CategoryNotFoundException, CategoryException {
        categoryService.deleteCategory(categoryDto.getName());
    }

    public List<CategoryWithProductsDto> showAllCategories() {
        List<Category> listCategories = categoryService.showAllCategories();
        return categoryMapper.mapToCategoryDtoListAllInfo(listCategories);
    }

    public CategoryWithProductsDto searchForProductsInCategory(String categoryName) throws CategoryNotFoundException {
        Category category = categoryService.searchForProductsInCategory(categoryName);
        return categoryMapper.mapToCategoryDtoAllInfo(category);
    }

    public ProductDtoAllInfo addProduct(ProductDto productDto) throws InvalidArgumentException, CategoryExistsException, SQLException, InvalidPriceException {
        if (productDto.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceException();
        }
        Product product = productMapper.mapToProduct(productDto);
        Category category = categoryService.findByName(product.getCategory().getName());
        if (category != null) {
            category.getListOfProducts().add(product);
            product.setCategory(category);
        } else {
            category = categoryService.findByName("unknown");
            if (category == null) {
                category = categoryService.addCategory(new Category("Unknown"));
            }
            product.setCategory(category);
            category.getListOfProducts().add(product);
        }
        categoryService.save(category);
        Product savedProduct = productService.saveProduct(product);
        return productMapper.mapToProductDtoAllInfo(savedProduct);
    }

    public ProductDtoAllInfo updateProduct(Long productId, ProductDto productDto) throws ProductNotFoundException, InvalidArgumentException, CategoryExistsException, InvalidPriceException {
        if (productDto.getPrice().compareTo(BigDecimal.ZERO) < 0){
            throw new InvalidPriceException();
        }
        Product productToUpdate = productService.findProductById(productId);
        Category categoryToUpdate = categoryService.findByName(productDto.getCategoryName());
        if (categoryToUpdate == null) {
            categoryToUpdate = categoryService.findByName("unknown");
            if (categoryToUpdate == null) {
                categoryToUpdate = categoryService.addCategory(new Category("Unknown"));
            }
        }
        productToUpdate.setCategory(categoryToUpdate);
        categoryToUpdate.getListOfProducts().add(productToUpdate);
        categoryService.save(categoryToUpdate);
        productToUpdate.setPrice(productDto.getPrice());
        productService.saveProduct(productToUpdate);
        return productMapper.mapToProductDtoAllInfo(productToUpdate);
    }


    public void deleteProductById(Long productId) throws ProductNotFoundException {
        Product productToRemove = productService.findProductById(productId);
        Category category = productToRemove.getCategory();
        category.getListOfProducts().remove(productToRemove);
        categoryService.save(category);
        productService.deleteById(productId);
    }

    public List<ProductDtoAllInfo> showAllProducts() {
        List<Product> productList = productService.findAllProducts();
        return productMapper.mapToProductDtoList(productList);
    }

    public WarehouseDto addOrUpdateProductInWarehouse(Long productId, Integer productQuantity) throws InvalidQuantityException, ProductNotFoundException, CategoryNotFoundException {
        if (productQuantity < 0 || productQuantity > Integer.MAX_VALUE) {
            throw new InvalidQuantityException();
        }
        Product product = productService.findProductById(productId);
        if (product.getCategory().getName().equalsIgnoreCase("Unknown")) {
            throw new CategoryNotFoundException();
        }
        Warehouse warehouse = warehouseService.findItemByID(product.getProductID());
        if (warehouse == null) {
            warehouse = new Warehouse(product, productQuantity);
        } else {
            warehouse.setProductQuantity(warehouse.getProductQuantity() + productQuantity);
        }
        warehouseService.save(warehouse);
        return warehouseMapper.mapToWarehouseDto(warehouse);
    }

    public void deleteProductFromWarehouse(Long productId) throws ProductNotFoundException {
        if (warehouseService.findItemByID(productId) == null){
            throw new ProductNotFoundException();
        };
        warehouseService.deleteById(productId);
    }

    public List<WarehouseDto> displayAllItemsInWarehouse() {
        List<Warehouse> listOfAllItems = warehouseService.findAll();
        return warehouseMapper.mapToWarehouseDtoList(listOfAllItems);
    }


    public List<OrderDtoToCustomer> displayOrders(String order_status) {
       List<Order> listOfOrders = orderService.findOrders(order_status);
        return orderMapper.mapToOrderDtoToCustomerList(listOfOrders);

    }
}

