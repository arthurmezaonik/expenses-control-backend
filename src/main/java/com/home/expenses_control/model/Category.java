package com.home.expenses_control.model;

import com.home.expenses_control.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@Getter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private Status status;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = Status.ACTIVE;
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void update(String newName, String newDescription) {
        this.name = newName;
        this.description = newDescription;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        LocalDateTime time = LocalDateTime.now();

        this.status = Status.INACTIVE;
        this.lastUpdatedAt = time;
        this.deletedAt = time;
    }

    public void activate() {
        this.status = Status.ACTIVE;
        this.lastUpdatedAt = LocalDateTime.now();
        this.deletedAt = null;
    }
}
