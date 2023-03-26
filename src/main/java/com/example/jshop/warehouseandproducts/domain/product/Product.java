package com.example.jshop.warehouseandproducts.domain.product;

import com.example.jshop.warehouseandproducts.domain.category.Category;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "products")
//@Table (uniqueConstraints = @UniqueConstraint(columnNames = {"product_name", "product_description", "price"}))
public class Product {

    @Id
    @GeneratedValue
    @Column(name = "product_id", unique = true)
    private Long productID;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "categories_categoryID")
    private Category category;

    @Column(name = "price", precision = 19, scale = 2, columnDefinition="DECIMAL(19,2)")
    private BigDecimal price;

    public Product(String productName, String description, Category category, BigDecimal price) {
        this.productName = productName;
        this.description = description;
        this.category = category;
        this.price = price;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
