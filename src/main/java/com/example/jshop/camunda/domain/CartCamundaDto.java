package com.example.jshop.camunda.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartCamundaDto {

    Long cartId;
    String processInstanceId;
}
