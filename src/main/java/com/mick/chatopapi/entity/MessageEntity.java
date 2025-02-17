package com.mick.chatopapi.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="messages")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 2000)
    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "rental_id", referencedColumnName = "id", nullable = false)
    private RentalEntity rental;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private LocalDateTime updated_at;

    public MessageEntity() {
    }

    public MessageEntity(RentalEntity rental, UserEntity user, String message ) {
        this.message = message;
        this.user = user;
        this.rental = rental;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }
}
