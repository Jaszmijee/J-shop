package com.example.jshop.cartsandorders.domain.cart;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "carts")
public class Cart {

    @Id
    @GeneratedValue
    private Long cartID;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CartStatus cartStatus;

    @Column(name = "items_cart")
    @OneToMany(targetEntity = Item.class,
        mappedBy = "cart",
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER)
    private List<Item> listOfItems = new ArrayList<>();

    @Column(name = "total")
    private BigDecimal calculatedPrice;

    @Column(name = "created")
    private LocalDate created;

    @Column(name = "camunda_process_Id")
    private String camundaProcessId;

    @Column(name = "discount")
    private Long discount;

    @Column(name = "final_price")
    private BigDecimal finalPrice;

    public Cart(CartStatus cartStatus, List<Item> listOfItems, BigDecimal calculatedPrice, LocalDate created) {
        this.cartStatus = cartStatus;
        this.listOfItems = listOfItems;
        this.calculatedPrice = calculatedPrice;
        this.created = created;
    }

    public void setCartStatus(CartStatus cartStatus) {
        this.cartStatus = cartStatus;
    }

    public void setCalculatedPrice(BigDecimal calculatedPrice) {
        this.calculatedPrice = calculatedPrice;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public void setCamundaProcessId(String camundaProcessId) {
        this.camundaProcessId = camundaProcessId;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }
}
