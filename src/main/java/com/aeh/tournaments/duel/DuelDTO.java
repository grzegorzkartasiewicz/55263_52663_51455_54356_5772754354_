package com.aeh.tournaments.duel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DuelDTO {

    private long id;
    private Long participant1;
    private Long participant2;
    private Long winner;
    private int position;
    private int round;
    private Branch branch;

    public Duel toEntity() {
        Duel duel = new Duel();
        duel.setId(id);
        duel.setParticipant1(participant1);
        duel.setParticipant2(participant2);
        duel.setWinner(winner);
        duel.setPosition(position);
        duel.setRound(round);
        duel.setBranch(branch);
        return duel;
    }

     public static DuelDTO toDto(Duel duel) {
        DuelDTO duelDTO = new DuelDTO();
        duelDTO.setId(duel.getId());
        duelDTO.setParticipant1(duel.getParticipant1());
        duelDTO.setParticipant2(duel.getParticipant2());
        duelDTO.setWinner(duel.getWinner());
        duelDTO.setPosition(duel.getPosition());
        duelDTO.setRound(duel.getRound());
        duelDTO.setBranch(duel.getBranch());
        return duelDTO;
    }
}
