package com.arjun.order_service.bookstore.orders.web.controllers;

import com.arjun.order_service.bookstore.orders.domain.OrderService;
import com.arjun.order_service.bookstore.orders.domain.SecurityService;
import com.arjun.order_service.bookstore.orders.domain.models.CreateOrderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.BDDMockito.given;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;



@WebMvcTest(OrderController.class)
public class OrderControllerUnitTests {

    @MockBean
    private OrderService orderService;

    @MockBean
    private SecurityService securityService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        BDDMockito.given(securityService.getLoginUserName())
              .willReturn("arjun");
    }

    /*@ParameterizedTest(name = "[{index}]--{0}")
    @MethodSource("createOrderRequestProvider")
    void shouldReturnBadRequestWhenOrderPayloadIsInvalid(CreateOrderRequest request){


    }*/

}
