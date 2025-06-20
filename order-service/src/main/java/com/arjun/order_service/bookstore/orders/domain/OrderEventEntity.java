package com.arjun.order_service.bookstore.orders.domain;

import com.arjun.order_service.bookstore.orders.domain.models.OrderEventType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="order_events")
public class OrderEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "order_event_id_generator")
    private Long id;
    @Column(nullable = false)
    private String orderNumber;
    @Column(nullable = false,unique = true)
    private String eventId;

    @Enumerated(EnumType.STRING)
    private OrderEventType eventType;
    @Column(nullable = false)
    private String payload;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public OrderEventType getEventType() {
        return eventType;
    }

    public void setEventType(OrderEventType eventType) {
        this.eventType = eventType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
