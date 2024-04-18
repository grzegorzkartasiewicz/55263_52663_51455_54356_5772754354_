package com.aeh.tournaments.competitors.model;


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

    private Long duelId;
    private String participant1;
    private String participant2;
    private String winner;
    private String category;


}