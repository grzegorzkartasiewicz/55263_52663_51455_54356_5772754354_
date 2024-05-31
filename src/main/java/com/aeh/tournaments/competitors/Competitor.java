package com.aeh.tournaments.competitors;


import jakarta.persistence.*;
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
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String surname;
    @Min(4)
    @Max(100)
    private int age;
    private String category;
    @NotNull
    private String club;
    private boolean skippedLast;
}
