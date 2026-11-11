package com.home.expenses_control.model;

import com.home.expenses_control.enums.Status;
import com.home.expenses_control.enums.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private Type type;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column
    private String note;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "last_updated_at")
    private  LocalDateTime lastUpdatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private Status status;

    @PrePersist
    private void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.status = Status.ACTIVE;
    }

    // Used when creating new entries
    public Transaction(Type type, BigDecimal amount, LocalDate date, Category category, String note) {
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.note = note;
    }

    public void update(Type newType, BigDecimal newAmount, LocalDate newDate, Category newCategory, String newNote) {
        this.type = newType;
        this.amount = newAmount;
        this.date = newDate;
        this.category = newCategory;
        this.note = newNote;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void deactivate(){
        LocalDateTime now = LocalDateTime.now();
        this.status = Status.INACTIVE;
        this.lastUpdatedAt = now;
        this.deletedAt = now;
    }

    public void activate(){
        this.status = Status.ACTIVE;
        this.lastUpdatedAt = LocalDateTime.now();
        this.deletedAt = null;
    }
}
