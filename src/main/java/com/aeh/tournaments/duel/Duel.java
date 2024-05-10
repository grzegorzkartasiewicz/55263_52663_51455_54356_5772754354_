package com.aeh.tournaments.duel;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Duel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Long participant1;
    private Long participant2;
    private Long winner;
    private int position;
    private int round;
    @Enumerated(value = EnumType.STRING)
    private Branch branch;

}