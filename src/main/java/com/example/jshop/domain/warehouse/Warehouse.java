package com.example.jshop.domain.warehouse;

import com.example.jshop.domain.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "warehouse")
public class Warehouse {

    @Id
    @GeneratedValue
    private Long warehouseID;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "products_product_id")
    private Product product;

    @Column(name = "quantity")
    private Integer productQuantity;

    public Warehouse(Product product, Integer productQuantity) {
        this.product = product;
        this.productQuantity = productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }
}
