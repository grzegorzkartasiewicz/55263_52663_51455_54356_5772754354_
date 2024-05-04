package com.aeh.tournaments.tournament;

import com.aeh.tournaments.competitors.Competitor;
import com.aeh.tournaments.competitors.CompetitorDTO;
import lombok.Getter;

import java.util.Set;

@Getter
public class TournamentDTO {
    private long id;
    private Set<CompetitorDTO> competitors;
}
