package com.example.jshop.warehouseandproducts.service;

import java.util.List;
import java.util.regex.Pattern;
import com.example.jshop.errorhandlers.exceptions.CategoryException;
import com.example.jshop.errorhandlers.exceptions.CategoryExistsException;
import com.example.jshop.errorhandlers.exceptions.CategoryNotFoundException;
import com.example.jshop.errorhandlers.exceptions.InvalidCategoryNameException;
import com.example.jshop.warehouseandproducts.domain.category.Category;
import com.example.jshop.warehouseandproducts.domain.product.Product;
import com.example.jshop.warehouseandproducts.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductService productService;

    public Category findByName(String name) {
        return categoryRepository.findByNameEqualsIgnoreCase(name);
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public Category addCategory(Category category) throws InvalidCategoryNameException, CategoryExistsException {
        if ((category.getName() == null)
            || (category.getName().isEmpty())
            || (category.getName().trim().isEmpty())
            || (category.getName().length() < 3)
            || (Pattern.compile("\\W").matcher(category.getName()).find())) {
            throw new InvalidCategoryNameException();
        }
        if (findByName(category.getName()) != null) {
            throw new CategoryExistsException();
        }
        return categoryRepository.save(category);
    }

    public void deleteCategory(String name) throws CategoryNotFoundException, CategoryException {
        if (name.equalsIgnoreCase("unknown")) {
            throw new CategoryException();
        }
        Category category = categoryRepository.findByNameEqualsIgnoreCase(name);
        if (category == null) {
            throw new CategoryNotFoundException();
        }
        if (category.getListOfProducts().size() > 0) {
            Category category1 = categoryRepository.findByNameEqualsIgnoreCase("unknown");
            if (category1 == null) {
                category1 = new Category("Unknown");
                categoryRepository.save(category1);
            }
            for (Product product : category.getListOfProducts()) {
                product.setCategory(category1);
                productService.saveProduct(product);
                category1.getListOfProducts().add(product);
            }
            category.getListOfProducts().clear();
            categoryRepository.save(category);
            categoryRepository.save(category1);
        }
        categoryRepository.deleteByNameEqualsIgnoreCase(name);
    }

    public List<Category> showAllCategories() {
        return categoryRepository.findAll();
    }
}
