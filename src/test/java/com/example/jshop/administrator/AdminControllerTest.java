package com.example.jshop.administrator;

import com.example.jshop.error_handlers.exceptions.*;
import com.example.jshop.warehouse_and_products.domain.category.CategoryDto;
import com.example.jshop.warehouse_and_products.domain.category.CategoryWithProductsDto;
import com.example.jshop.warehouse_and_products.domain.product.ProductDto;
import com.example.jshop.warehouse_and_products.domain.product.ProductDtoAllInfo;
import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringJUnitWebConfig
@WebMvcTest(controllers = AdminController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminConfig adminConfig;

    @MockBean
    private AdminService adminService;

    @Nested
    @DisplayName("test addNewCategory /v1/j-shop/admin/category")
    class TestAddNewCategory {
        @Test
        void testAddNewCategoryAccessDeniedException() throws Exception {

            CategoryDto categoryDto = new CategoryDto("Cars");

            when(adminConfig.getAdminKey()).thenReturn("1");
            when(adminConfig.getAdminToken()).thenReturn("2");

            Mockito.doNothing().when(adminService).addNewCategory(any(CategoryDto.class));
            Gson gson = new Gson();
            String jsonContent = gson.toJson(categoryDto);

            //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .post("/v1/j-shop/admin/category")
                            .param("key", "1")
                            .param("token", "3")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(jsonContent))

                    .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                    .andExpect(result -> assertEquals("Access denied", result.getResponse().getContentAsString()));

            verify(adminService, never()).addNewCategory(any(CategoryDto.class));
        }

        @Test
        void testAddNewCategoryInvalidArgumentException() throws Exception {

            CategoryDto empty = new CategoryDto("");

            when(adminConfig.getAdminKey()).thenReturn("1");
            when(adminConfig.getAdminToken()).thenReturn("2");
            Mockito.doThrow(InvalidArgumentException.class).when(adminService).addNewCategory(any(CategoryDto.class));

            Gson gson = new Gson();
            String jsonContent = gson.toJson(empty);

            //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .post("/v1/j-shop/admin/category")
                            .param("key", "1")
                            .param("token", "2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(jsonContent))

                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result -> assertEquals("Provide proper name", result.getResponse().getContentAsString()));

            verify(adminService, times(1)).addNewCategory(any(CategoryDto.class));
        }

        @Test
        void testAddNewCategoryCategoryExistsException() throws Exception {

            CategoryDto categoryDto = new CategoryDto("Cars");

            when(adminConfig.getAdminKey()).thenReturn("1");
            when(adminConfig.getAdminToken()).thenReturn("2");
            Mockito.doThrow(CategoryExistsException.class).when(adminService).addNewCategory(any(CategoryDto.class));

            Gson gson = new Gson();
            String jsonContent = gson.toJson(categoryDto);

            //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .post("/v1/j-shop/admin/category")
                            .param("key", "1")
                            .param("token", "2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(jsonContent))

                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result -> assertEquals("Category already exists", result.getResponse().getContentAsString()));

            verify(adminService, times(1)).addNewCategory(any(CategoryDto.class));
        }

        @Test
        void testAddNewCategoryPositive() throws Exception {
            CategoryDto categoryDto = new CategoryDto("Cars");

            when(adminConfig.getAdminKey()).thenReturn("1");
            when(adminConfig.getAdminToken()).thenReturn("2");
            Mockito.doCallRealMethod().when(adminService).addNewCategory(categoryDto);

            Gson gson = new Gson();
            String jsonContent = gson.toJson(categoryDto);

            //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .post("/v1/j-shop/admin/category")
                            .param("key", "1")
                            .param("token", "2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(jsonContent))

                    .andExpect(MockMvcResultMatchers.status().isOk());

            verify(adminService, times(1)).addNewCategory(any(CategoryDto.class));
        }
    }

    @Nested
    @DisplayName("test removeCategory /v1/j-shop/admin/category")
    class TestRemoveCategory {
        @Test
        void testRemoveCategoryAccessDeniedException() throws Exception {

            CategoryDto categoryDto = new CategoryDto("Cars");

            when(adminConfig.getAdminKey()).thenReturn("1");
            when(adminConfig.getAdminToken()).thenReturn("2");

            Mockito.doNothing().when(adminService).removeCategory(any(CategoryDto.class));
            Gson gson = new Gson();
            String jsonContent = gson.toJson(categoryDto);

            //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .delete("/v1/j-shop/admin/category")
                            .param("key", "1")
                            .param("token", "3")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(jsonContent))

                    .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                    .andExpect(result -> assertEquals("Access denied", result.getResponse().getContentAsString()));

            verify(adminService, never()).removeCategory(any(CategoryDto.class));
        }
    }

    @Test
    void testRemoveCategoryCategoryNotFoundException() throws Exception {

        CategoryDto categoryDto = new CategoryDto("Cars");

        when(adminConfig.getAdminKey()).thenReturn("1");
        when(adminConfig.getAdminToken()).thenReturn("2");

        Mockito.doThrow(CategoryNotFoundException.class).when(adminService).removeCategory(any(CategoryDto.class));
        Gson gson = new Gson();
        String jsonContent = gson.toJson(categoryDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/v1/j-shop/admin/category")
                        .param("key", "1")
                        .param("token", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))

                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertEquals("Category not found", result.getResponse().getContentAsString()));

        verify(adminService, times(1)).removeCategory(any(CategoryDto.class));
    }

    @Test
    void testRemoveCategoryCategoryException() throws Exception {

        CategoryDto categoryDto = new CategoryDto("Unknown");

        when(adminConfig.getAdminKey()).thenReturn("1");
        when(adminConfig.getAdminToken()).thenReturn("2");

        Mockito.doThrow(CategoryException.class).when(adminService).removeCategory(any(CategoryDto.class));
        Gson gson = new Gson();
        String jsonContent = gson.toJson(categoryDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/v1/j-shop/admin/category")
                        .param("key", "1")
                        .param("token", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))

                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertEquals("Deleting category \"Unknown\" denied", result.getResponse().getContentAsString()));

        verify(adminService, times(1)).removeCategory(any(CategoryDto.class));
    }

    @Test
    void testRemoveCategoryPositive() throws Exception {

        CategoryDto categoryDto = new CategoryDto("Unknown");

        when(adminConfig.getAdminKey()).thenReturn("1");
        when(adminConfig.getAdminToken()).thenReturn("2");

        Mockito.doNothing().when(adminService).removeCategory(any(CategoryDto.class));
        Gson gson = new Gson();
        String jsonContent = gson.toJson(categoryDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/v1/j-shop/admin/category")
                        .param("key", "1")
                        .param("token", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))

                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(adminService, times(1)).removeCategory(any(CategoryDto.class));
    }

    @Nested
    @DisplayName("test removeCategory /v1/j-shop/admin/category")
    class TestShowAllCategoriesAndProducts {
        @Test
        void testShowAllCategoriesAndProductsAccessDeniedException() throws Exception {

            List<CategoryWithProductsDto> listEmpty = List.of();
            when(adminConfig.getAdminKey()).thenReturn("1");
            when(adminConfig.getAdminToken()).thenReturn("2");

            when(adminService.showAllCategories()).thenReturn(listEmpty);
            Gson gson = new Gson();
            String jsonContent = gson.toJson(listEmpty);

            //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .get("/v1/j-shop/admin/category")
                            .param("key", "1")
                            .param("token", "3")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(jsonContent))

                    .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                    .andExpect(result -> assertEquals("Access denied", result.getResponse().getContentAsString()));

            verify(adminService, never()).showAllCategories();
        }

        @Test
        void testShowAllCategoriesAndProductsPositiveEmptyList() throws Exception {

            List<CategoryWithProductsDto> listEmpty = List.of();

            when(adminConfig.getAdminKey()).thenReturn("1");
            when(adminConfig.getAdminToken()).thenReturn("2");

            when(adminService.showAllCategories()).thenReturn(listEmpty);
            Gson gson = new Gson();
            String jsonContent = gson.toJson(listEmpty);

            //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .get("/v1/j-shop/admin/category")
                            .param("key", "1")
                            .param("token", "2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(jsonContent))

                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));

            verify(adminService, times(1)).showAllCategories();
        }

        @Test
        void testShowAllCategoriesAndProductsPositiveNonEmptyList() throws Exception {

            List<CategoryWithProductsDto> list = List.of(new CategoryWithProductsDto(1L, "Music",
                    List.of(new ProductDtoAllInfo(2L, "Music", "Album1", "CD", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN)))));


            when(adminConfig.getAdminKey()).thenReturn("1");
            when(adminConfig.getAdminToken()).thenReturn("2");

            when(adminService.showAllCategories()).thenReturn(list);
            Gson gson = new Gson();
            String jsonContent = gson.toJson(list);

            //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .get("/v1/j-shop/admin/category")
                            .param("key", "1")
                            .param("token", "2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(jsonContent))
                    // CategoryWithProductsDto
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].categoryId", Matchers.is(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].categoryName", Matchers.is("Music")))
                    // ProductDtoAllInfo
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].listOfProducts", Matchers.hasSize(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].listOfProducts[0].productId", Matchers.is(2)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].listOfProducts[0].productName", Matchers.is("Album1")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].listOfProducts[0].description", Matchers.is("CD")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].listOfProducts[0].price", Matchers.is(25.12)));

            verify(adminService, times(1)).showAllCategories();
        }
    }

    @Nested
    @DisplayName("test showCategories /v1/j-shop/admin/category/name")
    class TestShowCategories {
        @Test
        void testShowCategoriesAccessDeniedException() throws Exception {

            CategoryWithProductsDto productsInCategory = new CategoryWithProductsDto(1L, "Music",
                    List.of(new ProductDtoAllInfo(2L, "Music", "Album1", "CD", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN))));

            when(adminConfig.getAdminKey()).thenReturn("1");
            when(adminConfig.getAdminToken()).thenReturn("2");

            when(adminService.searchForProductsInCategory(any(String.class))).thenReturn(productsInCategory);

            //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .get("/v1/j-shop/admin/category/name")
                            .param("key", "1")
                            .param("token", "3")
                            .param("categoryName", "Music")
                            .contentType(MediaType.APPLICATION_JSON))

                    .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                    .andExpect(result -> assertEquals("Access denied", result.getResponse().getContentAsString()));

            verify(adminService, never()).searchForProductsInCategory(anyString());
        }

        @Test
        void testShowCategoriesCategoryNotFoundException() throws Exception {

            when(adminConfig.getAdminKey()).thenReturn("1");
            when(adminConfig.getAdminToken()).thenReturn("2");

            when(adminService.searchForProductsInCategory("Music")).thenThrow(CategoryNotFoundException.class);

            //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .get("/v1/j-shop/admin/category/name")
                            .param("key", "1")
                            .param("token", "2")
                            .param("categoryName", "Music")
                            .contentType(MediaType.APPLICATION_JSON))

                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result -> assertEquals("Category not found", result.getResponse().getContentAsString()));

            verify(adminService, times(1)).searchForProductsInCategory(anyString());
        }

        @Test
        void testShowCategoriesPositiveEmptyCategory() throws Exception {

            CategoryWithProductsDto emptyCategory = new CategoryWithProductsDto(1L, "Music",
                    List.of());

            when(adminConfig.getAdminKey()).thenReturn("1");
            when(adminConfig.getAdminToken()).thenReturn("2");

            when(adminService.searchForProductsInCategory("Music")).thenReturn(emptyCategory);

            //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .get("/v1/j-shop/admin/category/name")
                            .param("key", "1")
                            .param("token", "2")
                            .param("categoryName", "Music")
                            .contentType(MediaType.APPLICATION_JSON))

                    // CategoryWithProductsDto
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryId", Matchers.is(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryName", Matchers.is("Music")))
                    // ProductDtoAllInfo
                    .andExpect(MockMvcResultMatchers.jsonPath("$.listOfProducts", Matchers.hasSize(0)));

            verify(adminService, times(1)).searchForProductsInCategory(anyString());
        }

        @Test
        void testShowCategoriesPositiveNonEmptyCategory() throws Exception {

            CategoryWithProductsDto productsInCategory = new CategoryWithProductsDto(1L, "Music",
                    List.of(new ProductDtoAllInfo(2L, "Music", "Album1", "CD", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN))));

            when(adminConfig.getAdminKey()).thenReturn("1");
            when(adminConfig.getAdminToken()).thenReturn("2");

            when(adminService.searchForProductsInCategory("Music")).thenReturn(productsInCategory);

            //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .get("/v1/j-shop/admin/category/name")
                            .param("key", "1")
                            .param("token", "2")
                            .param("categoryName", "Music")
                            .contentType(MediaType.APPLICATION_JSON))

                    // CategoryWithProductsDto
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryId", Matchers.is(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.categoryName", Matchers.is("Music")))
                    // ProductDtoAllInfo
                    .andExpect(MockMvcResultMatchers.jsonPath("$.listOfProducts", Matchers.hasSize(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.listOfProducts[0].productId", Matchers.is(2)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.listOfProducts[0].productName", Matchers.is("Album1")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.listOfProducts[0].description", Matchers.is("CD")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.listOfProducts[0].price", Matchers.is(25.12)));

            verify(adminService, times(1)).searchForProductsInCategory(anyString());
        }
    }

    @Nested
    @DisplayName("test addProduct /v1/j-shop/admin/product")
    class TestAddProduct {
        @Test
        void testShowCategoriesAccessDeniedException() throws Exception {

            ProductDto productDto = new ProductDto("Album1", "CD", "Music", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN));
            ProductDtoAllInfo productDtoAllInfo = new ProductDtoAllInfo(2L, "Music", "Album1", "CD", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN));

            when(adminConfig.getAdminKey()).thenReturn("1");
            when(adminConfig.getAdminToken()).thenReturn("2");

            when(adminService.addProduct(any(ProductDto.class))).thenReturn(productDtoAllInfo);

            Gson gson = new Gson();
            String jsonContent = gson.toJson(productDto);

            //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .post("/v1/j-shop/admin/product")
                            .param("key", "1")
                            .param("token", "3")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(jsonContent))

                    .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                    .andExpect(result -> assertEquals("Access denied", result.getResponse().getContentAsString()));

            verify(adminService, never()).addProduct(any(ProductDto.class));
        }

        @Test
        void testShowCategoriesInvalidArgumentException() throws Exception {

            ProductDto productDto = new ProductDto("Album1", "CD", "Music", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN));
            ProductDtoAllInfo productDtoAllInfo = new ProductDtoAllInfo(2L, "Music", "Album1", "CD", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN));

            when(adminConfig.getAdminKey()).thenReturn("1");
            when(adminConfig.getAdminToken()).thenReturn("2");

            when(adminService.addProduct(any(ProductDto.class))).thenThrow(InvalidArgumentException.class);

            Gson gson = new Gson();
            String jsonContent = gson.toJson(productDto);

            //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .post("/v1/j-shop/admin/product")
                            .param("key", "1")
                            .param("token", "2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(jsonContent))

                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result -> assertEquals("Provide proper name", result.getResponse().getContentAsString()));

            verify(adminService, times(1)).addProduct(any(ProductDto.class));
        }

        @Test
        void testShowCategoriesCategoryExistsException() throws Exception {

            ProductDto productDto = new ProductDto("Album1", "CD", "unknown", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN));
            ProductDtoAllInfo productDtoAllInfo = new ProductDtoAllInfo(2L, "Music", "Album1", "CD", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN));

            when(adminConfig.getAdminKey()).thenReturn("1");
            when(adminConfig.getAdminToken()).thenReturn("2");

            when(adminService.addProduct(any(ProductDto.class))).thenThrow(CategoryExistsException.class);

            Gson gson = new Gson();
            String jsonContent = gson.toJson(productDto);

            //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .post("/v1/j-shop/admin/product")
                            .param("key", "1")
                            .param("token", "2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(jsonContent))

                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result -> assertEquals("Category already exists", result.getResponse().getContentAsString()));

            verify(adminService, times(1)).addProduct(any(ProductDto.class));
        }

        @Test
        void testShowCategoriesInvalidPriceException() throws Exception {

            ProductDto productDto = new ProductDto("Album1", "CD", "unknown", new BigDecimal(-25.12).setScale(2, RoundingMode.HALF_EVEN));
            ProductDtoAllInfo productDtoAllInfo = new ProductDtoAllInfo(2L, "Music", "Album1", "CD", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN));

            when(adminConfig.getAdminKey()).thenReturn("1");
            when(adminConfig.getAdminToken()).thenReturn("2");

            when(adminService.addProduct(any(ProductDto.class))).thenThrow(InvalidPriceException.class);

            Gson gson = new Gson();
            String jsonContent = gson.toJson(productDto);

            //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .post("/v1/j-shop/admin/product")
                            .param("key", "1")
                            .param("token", "2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(jsonContent))

                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result -> assertEquals("Price is incorrect", result.getResponse().getContentAsString()));

            verify(adminService, times(1)).addProduct(any(ProductDto.class));
        }

        @Test
        void testShowCategoriesPositive() throws Exception {

            ProductDto productDto = new ProductDto("Album1", "CD", "unknown", new BigDecimal(-25.12).setScale(2, RoundingMode.HALF_EVEN));
            ProductDtoAllInfo productDtoAllInfo = new ProductDtoAllInfo(2L, "Music", "Album1", "CD", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN));

            when(adminConfig.getAdminKey()).thenReturn("1");
            when(adminConfig.getAdminToken()).thenReturn("2");

            when(adminService.addProduct(any(ProductDto.class))).thenReturn(productDtoAllInfo);

            Gson gson = new Gson();
            String jsonContent = gson.toJson(productDto);

            //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .post("/v1/j-shop/admin/product")
                            .param("key", "1")
                            .param("token", "2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(jsonContent))

                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.productId", Matchers.is(2)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.category", Matchers.is("Music")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.productName", Matchers.is("Album1")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("CD")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.price", Matchers.is(25.12)));

            verify(adminService, times(1)).addProduct(any(ProductDto.class));
        }
    }

    @Nested
    @DisplayName("test updateProduct /v1/j-shop/admin/product")
    class TestUpdateProduct {
        @Test
        void testUpdateProductAccessDeniedException() throws Exception {

            ProductDto productDto = new ProductDto("Album1", "CD", "Music", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN));
            ProductDtoAllInfo productDtoAllInfo = new ProductDtoAllInfo(2L, "Music", "Album1", "CD", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN));

            when(adminConfig.getAdminKey()).thenReturn("1");
            when(adminConfig.getAdminToken()).thenReturn("2");

            when(adminService.updateProduct(anyLong(), any(ProductDto.class))).thenReturn(productDtoAllInfo);

            Gson gson = new Gson();
            String jsonContent = gson.toJson(productDto);

            //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .put("/v1/j-shop/admin/product")
                            .param("key", "1")
                            .param("token", "3")
                            .param("productId", "2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(jsonContent))

                    .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                    .andExpect(result -> assertEquals("Access denied", result.getResponse().getContentAsString()));

            verify(adminService, never()).updateProduct(anyLong(), any(ProductDto.class));
        }

        @Test
        void testUpdateProductCategoryNotFoundException() throws Exception {

            ProductDto productDto = new ProductDto("Album1", "CD", "Music", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN));
            ProductDtoAllInfo productDtoAllInfo = new ProductDtoAllInfo(2L, "Music", "Album1", "CD", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN));

            when(adminConfig.getAdminKey()).thenReturn("1");
            when(adminConfig.getAdminToken()).thenReturn("2");

            when(adminService.updateProduct(anyLong(), any(ProductDto.class))).thenThrow(ProductNotFoundException.class);

            Gson gson = new Gson();
            String jsonContent = gson.toJson(productDto);

            //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .put("/v1/j-shop/admin/product")
                            .param("key", "1")
                            .param("token", "2")
                            .param("productId", "2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(jsonContent))

                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result -> assertEquals("Product with given Id not found", result.getResponse().getContentAsString()));

            verify(adminService, times(1)).updateProduct(anyLong(), any(ProductDto.class));
        }

        @Test
        void testUpdateProductInvalidArgumentException() throws Exception {

            ProductDto productDto = new ProductDto("Album1", "CD", "Music", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN));
            ProductDtoAllInfo productDtoAllInfo = new ProductDtoAllInfo(2L, "Music", "Album1", "CD", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN));

            when(adminConfig.getAdminKey()).thenReturn("1");
            when(adminConfig.getAdminToken()).thenReturn("2");

            when(adminService.updateProduct(anyLong(), any(ProductDto.class))).thenThrow(InvalidArgumentException.class);

            Gson gson = new Gson();
            String jsonContent = gson.toJson(productDto);

            //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .put("/v1/j-shop/admin/product")
                            .param("key", "1")
                            .param("token", "2")
                            .param("productId", "2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(jsonContent))

                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result -> assertEquals("Provide proper name", result.getResponse().getContentAsString()));

            verify(adminService, times(1)).updateProduct(anyLong(), any(ProductDto.class));
        }

        @Test
        void testUpdateProductCategoryExistsException() throws Exception {

            ProductDto productDto = new ProductDto("Album1", "CD", "Music", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN));
            ProductDtoAllInfo productDtoAllInfo = new ProductDtoAllInfo(2L, "Music", "Album1", "CD", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN));

            when(adminConfig.getAdminKey()).thenReturn("1");
            when(adminConfig.getAdminToken()).thenReturn("2");

            when(adminService.updateProduct(anyLong(), any(ProductDto.class))).thenThrow(CategoryExistsException.class);

            Gson gson = new Gson();
            String jsonContent = gson.toJson(productDto);

            //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .put("/v1/j-shop/admin/product")
                            .param("key", "1")
                            .param("token", "2")
                            .param("productId", "2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(jsonContent))

                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result -> assertEquals("Category already exists", result.getResponse().getContentAsString()));

            verify(adminService, times(1)).updateProduct(anyLong(), any(ProductDto.class));
        }

        @Test
        void testUpdateProductInvalidPriceException() throws Exception {

            ProductDto productDto = new ProductDto("Album1", "CD", "Music", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN));

            when(adminConfig.getAdminKey()).thenReturn("1");
            when(adminConfig.getAdminToken()).thenReturn("2");

            when(adminService.updateProduct(anyLong(), any(ProductDto.class))).thenThrow(InvalidPriceException.class);

            Gson gson = new Gson();
            String jsonContent = gson.toJson(productDto);

            //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .put("/v1/j-shop/admin/product")
                            .param("key", "1")
                            .param("token", "2")
                            .param("productId", "2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(jsonContent))

                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result -> assertEquals("Price is incorrect", result.getResponse().getContentAsString()));

            verify(adminService, times(1)).updateProduct(anyLong(), any(ProductDto.class));
        }

        @Test
        void testUpdateProductPositive() throws Exception {

            ProductDto productDto = new ProductDto("Album1", "CD", "Music", new BigDecimal(25.12).setScale(2, RoundingMode.HALF_EVEN));
            ProductDtoAllInfo productDtoAllInfo = new ProductDtoAllInfo(2L, "Movie", "Album2", "CD", new BigDecimal(44.12).setScale(2, RoundingMode.HALF_EVEN));

            when(adminConfig.getAdminKey()).thenReturn("1");
            when(adminConfig.getAdminToken()).thenReturn("2");

            when(adminService.updateProduct(anyLong(), any(ProductDto.class))).thenReturn(productDtoAllInfo);

            Gson gson = new Gson();
            String jsonContent = gson.toJson(productDto);

            //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .put("/v1/j-shop/admin/product")
                            .param("key", "1")
                            .param("token", "2")
                            .param("productId", "2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(jsonContent))

                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.productId", Matchers.is(2)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.category", Matchers.is("Movie")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.productName", Matchers.is("Album2")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("CD")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.price", Matchers.is(44.12)));

            verify(adminService, times(1)).updateProduct(anyLong(), any(ProductDto.class));
        }
    }


    @Test
    void removeProduct() {
    }

    @Test
    void showAllProducts() {
    }

    @Test
    void addProductToWarehouse() {
    }

    @Test
    void removeProductFromWarehouse() {
    }

    @Test
    void displayAllItemsInWareHouse() {
    }

    @Test
    void displayAllOrders() {
    }
}
