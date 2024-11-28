package com.mathias.reserve.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
@Builder
public class ConfirmationToken extends BaseEntity {

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryDate;


    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private Person persons;


    public ConfirmationToken(Person person){
        this.token = UUID.randomUUID().toString();
        this.expiryDate = LocalDateTime.now().plusDays(1);
        this.persons = person;
    }
}
