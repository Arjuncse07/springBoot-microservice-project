package com.arjun.order_service.bookstore.orders.domain;

import com.arjun.order_service.bookstore.orders.domain.models.CreateOrderRequest;
import com.arjun.order_service.bookstore.orders.domain.models.CreateOrderResponse;
import com.arjun.order_service.bookstore.orders.domain.models.OrderDTO;
import com.arjun.order_service.bookstore.orders.domain.models.OrderItem;
import com.arjun.order_service.bookstore.orders.domain.models.OrderSummary;
import jakarta.transaction.Transactional;
import org.hibernate.query.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private static final List<String> DELIVERY_ALLOWED_COUNTRIES = List.of("INDIA","USA","GERMANY","UK");

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;


    OrderService(OrderRepository orderRepository, OrderValidator orderValidator){
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

   /* ORDER CREATION */
    public CreateOrderResponse createOrder(String userName, CreateOrderRequest request){
        orderValidator.validate(request);
       OrderEntity newOrder = OrderMapper.convertToEntity(request);
       newOrder.setUserName(userName);
       OrderEntity savedOrder = this.orderRepository.save(newOrder);
       log.info("Created Order with orderNumber={}",savedOrder.getOrderNumber());
       return new CreateOrderResponse(savedOrder.getOrderNumber());
    }


   public List<OrderSummary> findOrders(String userName){
        return orderRepository.findByUserName(userName);
    }

    public Optional<OrderDTO> findUserOrder(String userName, String orderNumber){
        return orderRepository
                .findByUserNameAndOrderNumber(userName,orderNumber)
                .map(OrderMapper::convertToDTO);
    }

}
