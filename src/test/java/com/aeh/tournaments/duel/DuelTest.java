package com.aeh.tournaments.duel;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DuelTest {

    @Test
    void entityTest() {

        Duel duel = new Duel();
        duel.setId(1L);
        duel.setParticipant1(1L);
        duel.setParticipant2(2L);
        duel.setWinner("");

        assertThat(duel).isInstanceOf(Duel.class);
        assertThat(duel.getId()).isEqualTo(1L);
        assertThat(duel.getParticipant1()).isEqualTo(1L);
        assertThat(duel.getParticipant2()).isEqualTo(2L);
        assertThat(duel.getWinner()).isEmpty();

    }
}