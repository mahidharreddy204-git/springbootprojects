package com.example.order_service.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class OrderRequest {

    @NotEmpty (message = "order should contain atleast one item")
    @Valid 
    private List<OrderItemRequest> items;

}
