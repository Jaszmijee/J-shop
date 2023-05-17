package com.example.jshop.warehouseandproducts.domain.warehouse;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import com.example.jshop.warehouseandproducts.domain.product.Product;
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

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
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
