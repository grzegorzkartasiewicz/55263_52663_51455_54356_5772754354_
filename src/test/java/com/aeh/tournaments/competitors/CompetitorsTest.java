package com.aeh.tournaments.competitors;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CompetitorsTest {

    @Test
    void entityTest() {
        Competitor competitor = new Competitor();
        competitor.setId(1L);
        competitor.setName("John");
        competitor.setSurname("Smith");
        competitor.setAge(10);
        competitor.setGender("M");
        competitor.setCompetition("Kumite");
        competitor.setClub("Dragons");
        competitor.setAdvancement(1);
        competitor.setWeight(40);

        assertThat(competitor).isInstanceOf(Competitor.class);
        assertThat(competitor.getId()).isEqualTo(1);
        assertThat(competitor.getName()).isEqualTo("John");
        assertThat(competitor.getSurname()).isEqualTo("Smith");
        assertThat(competitor.getAge()).isEqualTo(10);
        assertThat(competitor.getGender()).isEqualTo("M");
        assertThat(competitor.getCompetition()).isEqualTo("Kumite");
        assertThat(competitor.getClub()).isEqualTo("Dragons");
        assertThat(competitor.getAdvancement()).isEqualTo(1);
        assertThat(competitor.getWeight()).isEqualTo(40);
    }
}
