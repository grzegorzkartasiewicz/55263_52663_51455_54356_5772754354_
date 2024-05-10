package com.aeh.tournaments.tournament;


import com.aeh.tournaments.competitors.Competitor;
import io.restassured.RestAssured;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatusCode;

import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TournamentControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TournamentRepository tournamentRepository;


    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }
    @Test
    void getTournamentByIdShouldReturnTournamentIfPresentTest() {
        Tournament tournament = new Tournament();
        tournament.setId(1L);
        tournament.setDuels(Collections.emptySet());
        tournament.setWinner(null);
        tournament.setNumberOfCompetitors(2);
        tournamentRepository.save(tournament);

        RestAssured.get("/tournaments/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("numberOfCompetitors", equalTo(2));
    }

    @Test
    void getTournamentByIdShouldReturnBadRequestIfTournamentNotPresentTest() {
        Tournament tournament = new Tournament();
        tournament.setId(1L);
        tournament.setDuels(Collections.emptySet());
        tournament.setWinner(null);
        tournament.setNumberOfCompetitors(2);
        tournamentRepository.save(tournament);

        RestAssured.get("/tournaments/2")
                .then()
                .statusCode(500);
    }
}
