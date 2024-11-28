package com.mathias.reserve.domain.entities;

import com.mathias.reserve.domain.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@Table(name = "jtoken")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JToken extends BaseEntity {
    @Column(unique = true)
    public String token;

    public boolean revoked;

    public boolean expired;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public Person person;
}
