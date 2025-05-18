package com.arjun.order_service.bookstore.orders.domain;

import com.arjun.order_service.bookstore.orders.domain.models.CreateOrderRequest;
import com.arjun.order_service.bookstore.orders.domain.models.CreateOrderResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private static final List<String> DELIVERY_ALLOWED_COUNTRIES = List.of("INDIA","USA","GERMANY","UK");

    private final OrderRepository orderRepository;


    OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }


    public CreateOrderResponse createOrder(String userName, CreateOrderRequest request){
       OrderEntity newOrder = OrderMapper.convertToEntity(request);
       newOrder.setUserName(userName);
       OrderEntity savedOrder = this.orderRepository.save(newOrder);
       log.info("Created Order with orderNumber={}",savedOrder.getOrderNumber());
       return new CreateOrderResponse(savedOrder.getOrderNumber());
    }




}
