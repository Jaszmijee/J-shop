package com.example.jshop.warehouseandproducts.domain.category;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import com.example.jshop.warehouseandproducts.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "categories")
public class Category {

    @Id
    @GeneratedValue
    @Column(unique = true)
    private Long categoryID;

    @Column(name = "category", unique = true)
    private String name;


    @OneToMany(targetEntity = Product.class,
        mappedBy = "category",
        fetch = FetchType.LAZY)
    private List<Product> listOfProducts = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }
}

