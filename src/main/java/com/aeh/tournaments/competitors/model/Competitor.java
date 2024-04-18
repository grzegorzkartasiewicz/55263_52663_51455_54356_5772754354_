package com.aeh.tournaments.competitors.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="Competitor")
@Getter
@Setter
public class Competitor {
    @Id
    @GeneratedValue()
    private int id;
    @NotNull
    private String name;
    @NotNull
    private String surname;
    @Min(4)
    @Max(100)
    private int age;
    private String gender;
    private String competition;
    @NotNull
    private String club;
    private int advancement;
    private int weight;
}
