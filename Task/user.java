package com.task.www.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID userId;

    private String fullName;
    private String mobNum;
    private String panNum;
    private UUID managerId; // No foreign key constraint
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isActive = true;
}
