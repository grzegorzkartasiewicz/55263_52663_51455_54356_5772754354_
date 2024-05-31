package com.aeh.tournaments.tournament;

import com.aeh.tournaments.competitors.Competitor;
import com.aeh.tournaments.duel.Duel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="TOURNAMENT")
@Getter
@Setter
class Tournament {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "tournament_id")
    private Set<Duel> duels;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "tournament_id")
    private Set<Competitor> competitors = new HashSet<>();
    private int numberOfCompetitors;
    @ManyToOne
    private Competitor winner;
    private String tournamentName;
    private String tournamentData;
    private String category;
    private String email;
}
