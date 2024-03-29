package com.example.jshop.warehouseandproducts.repository;

import java.util.List;
import com.example.jshop.warehouseandproducts.domain.category.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CategoryRepository extends CrudRepository<Category, Long> {

    @Override
    List<Category> findAll();

    Category findByNameEqualsIgnoreCase(String name);

    @Override
    Category save(Category category);

    void deleteByNameEqualsIgnoreCase(String name);
}
