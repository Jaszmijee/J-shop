package com.example.jshop.domain.category;

import com.example.jshop.domain.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "categories")
public class Category {

    @Id
    @GeneratedValue
    @Column(unique = true)
    Long categoryID;

    @Column(name = "category", unique = true)
    String name;


    @OneToMany(targetEntity = Product.class,
            mappedBy = "category",
            fetch = FetchType.LAZY)
    List<Product> listOfProducts = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }
}

