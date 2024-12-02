package com.mathias.reserve.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Terminal extends BaseEntity{
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false, name = "state_id")
    private State state;

    @OneToMany(mappedBy = "departureTerminal", cascade = CascadeType.ALL)
    private List<Ticket> departureTickets;

    @OneToMany(mappedBy = "arrivalTerminal", cascade = CascadeType.ALL)
    private List<Ticket> arrivalTickets;
}
