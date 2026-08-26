package com.arjun.library_service.bookstore.library.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "organizations")
public class OrganizationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String slug;

    @Column(name = "loan_period_days", nullable = false)
    private int loanPeriodDays = 14;

    @Column(name = "max_renewals", nullable = false)
    private int maxRenewals = 2;

    @Column(name = "fine_per_day", nullable = false)
    private BigDecimal finePerDay = new BigDecimal("5.00");

    @Enumerated(EnumType.STRING)
    @Column(name = "gate_policy", nullable = false, length = 20)
    private GatePolicy gatePolicy = GatePolicy.WARNING;

    @Column(name = "seat_allocation_enabled", nullable = false)
    private boolean seatAllocationEnabled = false;

    @Column(name = "logo_url", length = 255)
    private String logoUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public OrganizationEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getLoanPeriodDays() {
        return loanPeriodDays;
    }

    public void setLoanPeriodDays(int loanPeriodDays) {
        this.loanPeriodDays = loanPeriodDays;
    }

    public int getMaxRenewals() {
        return maxRenewals;
    }

    public void setMaxRenewals(int maxRenewals) {
        this.maxRenewals = maxRenewals;
    }

    public BigDecimal getFinePerDay() {
        return finePerDay;
    }

    public void setFinePerDay(BigDecimal finePerDay) {
        this.finePerDay = finePerDay;
    }

    public GatePolicy getGatePolicy() {
        return gatePolicy;
    }

    public void setGatePolicy(GatePolicy gatePolicy) {
        this.gatePolicy = gatePolicy;
    }

    public boolean isSeatAllocationEnabled() {
        return seatAllocationEnabled;
    }

    public void setSeatAllocationEnabled(boolean seatAllocationEnabled) {
        this.seatAllocationEnabled = seatAllocationEnabled;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
