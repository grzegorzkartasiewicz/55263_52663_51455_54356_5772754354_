package com.aeh.tournaments.competitors;


import com.aeh.tournaments.competitors.model.Duel;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class DuelTest {

    @Test
    void entityTest() {

        Duel duel = new Duel();
        duel.setDuelId(1L);
        duel.setParticipant1("John Smith");
        duel.setParticipant2("Mark Zuckerberg");
        duel.setWinner("");
        duel.setCategory("Boys under 7's");

        assertThat(duel).isInstanceOf(Duel.class);
        assertThat(duel.getDuelId()).isEqualTo(1L);
        assertThat(duel.getParticipant1()).isEqualTo("John Smith");
        assertThat(duel.getParticipant2()).isEqualTo("Mark Zuckerberg");
        assertThat(duel.getWinner()).isEqualTo("");
        assertThat(duel.getCategory()).isEqualTo("Boys under 7's");

    }




    }

