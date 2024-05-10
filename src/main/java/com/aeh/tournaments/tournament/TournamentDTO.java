package com.aeh.tournaments.tournament;

import com.aeh.tournaments.competitors.CompetitorDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class TournamentDTO {
    private Set<CompetitorDTO> competitors;
    private int round;
}
