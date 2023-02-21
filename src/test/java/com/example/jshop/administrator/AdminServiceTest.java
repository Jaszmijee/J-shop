package com.example.jshop.administrator;

import com.example.jshop.carts_and_orders.mapper.OrderMapper;
import com.example.jshop.carts_and_orders.service.OrderService;
import com.example.jshop.error_handlers.exceptions.*;
import com.example.jshop.warehouse_and_products.domain.category.Category;
import com.example.jshop.warehouse_and_products.domain.category.CategoryDto;
import com.example.jshop.warehouse_and_products.domain.category.CategoryWithProductsDto;
import com.example.jshop.warehouse_and_products.domain.product.Product;
import com.example.jshop.warehouse_and_products.domain.product.ProductDto;
import com.example.jshop.warehouse_and_products.domain.product.ProductDtoAllInfo;
import com.example.jshop.warehouse_and_products.mapper.CategoryMapper;
import com.example.jshop.warehouse_and_products.mapper.ProductMapper;
import com.example.jshop.warehouse_and_products.mapper.WarehouseMapper;
import com.example.jshop.warehouse_and_products.service.CategoryService;
import com.example.jshop.warehouse_and_products.service.ProductService;
import com.example.jshop.warehouse_and_products.service.WarehouseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;
    @Mock
    private CategoryService categoryService;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private ProductService productService;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private WarehouseService warehouseService;
    @Mock
    private WarehouseMapper warehouseMapper;
    @Mock
    private OrderService orderService;
    @Mock
    private OrderMapper orderMapper;

    @Nested
    @DisplayName("test addNewCategory")
    class TestAddNewCategory {

        @Test
        void testAddNewCategoryInvalidCategoryNameException() throws InvalidCategoryNameException, CategoryExistsException {
            //Given
            CategoryDto categoryDto = new CategoryDto("%%");
            Category category = new Category("%%");

            when(categoryMapper.mapToCategory(categoryDto)).thenReturn(category);
            when(categoryService.addCategory(category)).thenThrow(InvalidCategoryNameException.class);
            assertThrows(InvalidCategoryNameException.class, () -> adminService.addNewCategory(categoryDto));
            verify(categoryMapper, times(1)).mapToCategory(categoryDto);
            verify(categoryService, times(1)).addCategory(any(Category.class));
        }

        @Test
        void testAddNewCategoryInvalidCategoryExistsException() throws InvalidCategoryNameException, CategoryExistsException {
            //Given
            CategoryDto categoryDto = new CategoryDto("%%");
            Category category = new Category("%%");

            when(categoryMapper.mapToCategory(categoryDto)).thenReturn(category);
            when(categoryService.addCategory(category)).thenThrow(CategoryExistsException.class);

            assertThrows(CategoryExistsException.class, () -> adminService.addNewCategory(categoryDto));
            verify(categoryMapper, times(1)).mapToCategory(categoryDto);
            verify(categoryService, times(1)).addCategory(any(Category.class));
        }

        @Test
        void testAddNewCategoryPositive() throws InvalidCategoryNameException, CategoryExistsException {
            //Given
            CategoryDto categoryDto = new CategoryDto("Car");
            Category category = new Category("Car");
            when(categoryMapper.mapToCategory(categoryDto)).thenReturn(category);
            when(categoryService.addCategory(any(Category.class))).thenReturn(category);

            //When
            adminService.addNewCategory(categoryDto);

            //Then
            verify(categoryService, times(1)).addCategory(category);
            verify(categoryMapper, times(1)).mapToCategory(categoryDto);
        }
    }

    @Nested
    @DisplayName("test removeCategory")
    class TestRemoveCategory {
        @Test
        void testRemoveCategoryCategoryNotFoundException() throws CategoryException, CategoryNotFoundException {
            CategoryDto categoryDto = new CategoryDto("Car");
            Mockito.doThrow(CategoryNotFoundException.class).when(categoryService).deleteCategory(categoryDto.getName());
            assertThrows(CategoryNotFoundException.class, () -> adminService.removeCategory(categoryDto));
            verify(categoryService, times(1)).deleteCategory(anyString());
        }

        @Test
        void testRemoveCategoryCategoryException() throws CategoryException, CategoryNotFoundException {
            CategoryDto categoryDto = new CategoryDto("unknown");
            Mockito.doThrow(CategoryException.class).when(categoryService).deleteCategory(categoryDto.getName());
            assertThrows(CategoryException.class, () -> adminService.removeCategory(categoryDto));
            verify(categoryService, times(1)).deleteCategory(anyString());
        }

        @Test
        void testRemoveCategoryPositive() throws CategoryException, CategoryNotFoundException {
            CategoryDto categoryDto = new CategoryDto("Car");
            Mockito.doNothing().when(categoryService).deleteCategory(categoryDto.getName());
            //When
            adminService.removeCategory(categoryDto);
            verify(categoryService, times(1)).deleteCategory(anyString());
        }
    }

    @Nested
    @DisplayName("test showAllCategoriesWithProducts")
    class TestShowAllCategoriesWithProducts {
        @Test
        void testShowAllCategoriesWithProducts() {
            List<Category> categoryList = new ArrayList<>();
            categoryList.add(new Category(1L, "Car", List.of()));
            categoryList.add(new Category(4L, "Music", List.of(new Product(2L, "Album1", "CD", new Category("Music"), new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN)))));

            List<CategoryWithProductsDto> categoryWithProductsDtos = new ArrayList<>();
            categoryWithProductsDtos.add(new CategoryWithProductsDto(1L, "Car", List.of()));
            categoryWithProductsDtos.add(new CategoryWithProductsDto(4L, "Music",
                    List.of(new ProductDtoAllInfo(2L, "Music", "Album1", "CD", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN)))));

            when(categoryService.showAllCategories()).thenReturn(categoryList);
            when(categoryMapper.mapToCategoryDtoListAllInfo(categoryList)).thenReturn(categoryWithProductsDtos);

            //When & Then
            List<CategoryWithProductsDto> list = adminService.showAllCategoriesWithProducts();
            assertEquals(2, list.size());
            assertEquals(1, list.get(0).getCategoryId());
            assertEquals(1, list.get(1).getListOfProducts().size());
            verify(categoryService, times(1)).showAllCategories();
            verify(categoryMapper, times(1)).mapToCategoryDtoListAllInfo(anyList());
        }
    }

    @Nested
    @DisplayName("test showCategoryByNameWithProducts")
    class TestShowCategoryByNameWithProducts {

        @Test
        void testShowCategoryByNameWithProductsCategoryNotFoundException() throws CategoryNotFoundException {
            List<Category> categoryList = new ArrayList<>();
            categoryList.add(new Category(1L, "Car", List.of()));
            categoryList.add(new Category(4L, "Music", List.of(new Product(2L, "Album1", "CD", new Category("Music"), new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN)))));

            List<CategoryWithProductsDto> categoryWithProductsDtos = new ArrayList<>();
            categoryWithProductsDtos.add(new CategoryWithProductsDto(1L, "Car", List.of()));
            categoryWithProductsDtos.add(new CategoryWithProductsDto(4L, "Music",
                    List.of(new ProductDtoAllInfo(2L, "Music", "Album1", "CD", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN)))));

            when(categoryService.searchForProductsInCategory(anyString())).thenThrow(CategoryNotFoundException.class);

            assertThrows(CategoryNotFoundException.class, () -> adminService.showCategoryByNameWithProducts(anyString()));
            verify(categoryService, times(1)).searchForProductsInCategory(anyString());
            verify(categoryMapper, times(0)).mapToCategoryDtoListAllInfo(anyList());
        }

        @Test
        void testShowCategoryByNameWithProductsPositive() throws CategoryNotFoundException {
            List<Category> categoryList = new ArrayList<>();
            categoryList.add(new Category(1L, "Car", List.of()));
            categoryList.add(new Category(4L, "Music", List.of(new Product(2L, "Album1", "CD", new Category("Music"), new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN)))));

            List<CategoryWithProductsDto> categoryWithProductsDtos = new ArrayList<>();
            categoryWithProductsDtos.add(new CategoryWithProductsDto(1L, "Car", List.of()));
            categoryWithProductsDtos.add(new CategoryWithProductsDto(4L, "Music",
                    List.of(new ProductDtoAllInfo(2L, "Music", "Album1", "CD", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN)))));

            when(categoryService.searchForProductsInCategory("Car")).thenReturn(categoryList.get(0));
            when(categoryMapper.mapToCategoryDtoAllInfo(categoryList.get(0))).thenReturn(categoryWithProductsDtos.get(0));

            //When
            CategoryWithProductsDto category = adminService.showCategoryByNameWithProducts("Car");
            assertEquals(1L, category.getCategoryId());
            assertEquals(0, category.getListOfProducts().size());
            verify(categoryService, times(1)).searchForProductsInCategory(anyString());
            verify(categoryMapper, times(1)).mapToCategoryDtoAllInfo(any(Category.class));
        }
    }

    @Nested
    @DisplayName("test addNewProduct")
    class TestAddNewProduct {
        @Test
        void testAddNewProductInvalidPriceException() {
            ProductDto productDto = new ProductDto("testProduct", "testDescription", "testCategory", new BigDecimal(-25.12).setScale(2, RoundingMode.HALF_EVEN));
            assertThrows(InvalidPriceException.class, () -> adminService.addNewProduct(productDto));
        }

        @Test
        void testAddNewProductInvalidCategoryNameException() throws InvalidPriceException, InvalidCategoryNameException, CategoryExistsException {
            ProductDto productDto = new ProductDto("testProduct", "testDescription", "%%%", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN));
            Product product = new Product("testProduct", "testDescription", new Category("%%%"), new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN));
            ProductDtoAllInfo productDtoAllInfo = new ProductDtoAllInfo(4L, "Unknown", "testProduct", "testDescription", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN));
            Category category = new Category("Unknown");
            category.getListOfProducts().add(product);

/*            when(productMapper.mapToProduct(any(ProductDto.class))).thenReturn(product);
            when(categoryService.save(any(Category.class))).thenReturn(category);
            when(productService.saveProduct(product)).thenReturn(new Product("testProduct", "testDescription", category, new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN)));*/
            when(productMapper.mapToProductDtoAllInfo(any(Product.class))).thenReturn(productDtoAllInfo);

            assertEquals("Unknown", adminService.addNewProduct(productDto).getCategory());
        }
    }

/*         public ProductDtoAllInfo addNewProduct(ProductDto productDto) throws
 InvalidCategoryNameException, CategoryExistsException {
        validatePrice(productDto.getPrice());
        Product product = productMapper.mapToProduct(productDto);
        Category category = setUpCategoryNameForNewProduct(product);
        categoryService.save(category);
        Product savedProduct = productService.saveProduct(product);
        return productMapper.mapToProductDtoAllInfo(savedProduct);
    }*/

    @Test
    void updateProduct() {
    }

    @Test
    void deleteProductById() {
    }

    @Test
    void showAllProducts() {
    }

    @Test
    void addOrUpdateProductInWarehouse() {
    }

    @Test
    void deleteProductFromWarehouse() {
    }

    @Test
    void dispalyAllProductsInWarehouse() {
    }

    @Test
    void displayOrders() {
    }
}
