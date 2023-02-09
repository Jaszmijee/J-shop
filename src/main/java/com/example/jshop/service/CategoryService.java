package com.example.jshop.service;

import com.example.jshop.domain.category.Category;
import com.example.jshop.domain.product.Product;
import com.example.jshop.exception.CategoryException;
import com.example.jshop.exception.CategoryExistsException;
import com.example.jshop.exception.CategoryNotFoundException;
import com.example.jshop.exception.InvalidArgumentException;
import com.example.jshop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductService productService;

    public Category findByName(String name) {
        return categoryRepository.findByNameEqualsIgnoreCase(name);
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public Category searchForProductsInCategory(String name) throws CategoryNotFoundException {
        Category category1 = findByName(name);
        if(category1 == null){
            throw new CategoryNotFoundException();
        }
        return category1;
    }

    public Category addCategory(Category category) throws InvalidArgumentException, CategoryExistsException {
        if ((category.getName() == null)
                || (category.getName().isEmpty())
                || (category.getName().trim().isEmpty())
                || (category.getName().length() < 3)
                || (Pattern.compile("\\W").matcher(category.getName()).find())) {
            throw new InvalidArgumentException();
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

    public List<Category> showAllCategories(){
        return categoryRepository.findAll();
    }


}

/*
    @Autowired
    ProductService productService;


    public Category findById(Long categoryId) {
        return categoryRepository.findByCategoryID(categoryId);
    }

  }
*/
   /* public void deleteCategory(String name) throws CategoryNotFoundException {
        Category category = categoryRepository.findByNameEqualsIgnoreCase(name);
        for (Product product : category.getListOfProducts()) {
            product.setCategory(null);
            productService.saveProduct(product);
        }
        categoryRepository.deleteByNameEqualsIgnoreCase(name);
    }

    public Category updateCategory(Category category) {
        return categoryRepository.save(category);
    }

    //  addProductToCategory
}
*/