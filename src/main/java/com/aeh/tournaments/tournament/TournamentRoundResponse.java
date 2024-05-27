package com.aeh.tournaments.tournament;

import com.aeh.tournaments.competitors.CompetitorDTO;
import com.aeh.tournaments.duel.DuelDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class TournamentRoundResponse {
    private long id;
    private Set<DuelDTO> duels;
    private int numberOfCompetitors;
    private CompetitorDTO winner;

    static TournamentRoundResponse toDtoInRound(Tournament tournament, int round) {
        TournamentRoundResponse tournamentRoundResponse = new TournamentRoundResponse();
        tournamentRoundResponse.setId(tournament.getId());
        tournamentRoundResponse.setDuels(tournament.getDuels().stream()
                .filter(duel -> duel.getRound() == round)
                .map(DuelDTO::toDto).collect(Collectors.toSet()));
        tournamentRoundResponse.setWinner(CompetitorDTO.toDto(tournament.getWinner()));
        tournamentRoundResponse.setNumberOfCompetitors(tournament.getNumberOfCompetitors());
        return tournamentRoundResponse;
    }
}
