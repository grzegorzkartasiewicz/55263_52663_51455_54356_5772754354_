package com.aeh.tournaments.tournament;

import com.aeh.tournaments.competitors.CompetitorDTO;
import com.aeh.tournaments.duel.DuelDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class TournamentReadDTO {
    private long id;
    private Set<DuelDTO> duels;
    private int numberOfCompetitors;
    private CompetitorDTO winner;

    static TournamentReadDTO toDto(Tournament tournament) {
        TournamentReadDTO tournamentReadDTO = new TournamentReadDTO();
        tournamentReadDTO.setId(tournament.getId());
        tournamentReadDTO.setDuels(tournament.getDuels().stream().map(DuelDTO::toDto).collect(Collectors.toSet()));
        tournamentReadDTO.setWinner(CompetitorDTO.toDto(tournament.getWinner()));
        return tournamentReadDTO;
    }
}
