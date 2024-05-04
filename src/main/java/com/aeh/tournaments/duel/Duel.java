package com.aeh.tournaments.duel;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Duel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long duelId;
    private String participant1;
    private String participant2;
    private String winner;
    private String category;


}