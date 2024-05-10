package com.aeh.tournaments.tournament;


import com.aeh.tournaments.competitors.Competitor;
import com.aeh.tournaments.competitors.CompetitorDTO;
import com.aeh.tournaments.competitors.CompetitorService;
import com.aeh.tournaments.duel.DuelService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatusCode;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TournamentControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private CompetitorService competitorService;
    @Autowired
    private DuelService duelService;


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

    @Test
    void createTournamentWithEvenCompetitorsShouldReturnTournamentWithFirstRoundTest() {
        Set<CompetitorDTO> competitorDTOS = new HashSet<>();
        for (int i=0; i<10; i++) {
            CompetitorDTO competitorDTO = new CompetitorDTO();
            competitorDTO.setId(i);
            competitorDTO.setAge(18);
            competitorDTO.setGender("Male");
            competitorDTO.setCompetition("Kumite");
            competitorDTO.setAdvancement(1);
            competitorDTO.setName("John");
            competitorDTO.setSurname("Smith");
            competitorDTO.setClub("Dragon " + i);
            competitorService.save(competitorDTO);
            competitorDTOS.add(competitorDTO);
        }
        TournamentDTO tournamentDTO = new TournamentDTO(competitorDTOS);

        RestAssured.with().body(tournamentDTO).contentType(ContentType.JSON).when().post("/tournaments")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("numberOfCompetitors", equalTo(10))
                .body("duels", hasSize(5));
    }

    @Test
    void createTournamentWithNotEvenCompetitorsShouldReturnTournamentWithFirstRoundTest() {
        Set<CompetitorDTO> competitorDTOS = new HashSet<>();
        for (int i=0; i<11; i++) {
            CompetitorDTO competitorDTO = new CompetitorDTO();
            competitorDTO.setId(i);
            competitorDTO.setAge(18);
            competitorDTO.setGender("Male");
            competitorDTO.setCompetition("Kumite");
            competitorDTO.setAdvancement(1);
            competitorDTO.setName("John");
            competitorDTO.setSurname("Smith");
            competitorDTO.setClub("Dragon " + i);
            competitorService.save(competitorDTO);
            competitorDTOS.add(competitorDTO);
        }
        TournamentDTO tournamentDTO = new TournamentDTO(competitorDTOS);

        RestAssured.with().body(tournamentDTO).contentType(ContentType.JSON).when().post("/tournaments")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("numberOfCompetitors", equalTo(11))
                .body("duels", hasSize(6));
    }

    @Test
    void createTournamentWithoutCompetitorsShouldReturnTournamentWithFirstRoundTest() {
        Set<CompetitorDTO> competitorDTOS = new HashSet<>();
        TournamentDTO tournamentDTO = new TournamentDTO(competitorDTOS);

        RestAssured.with().body(tournamentDTO).contentType(ContentType.JSON).when().post("/tournaments")
                .then()
                .statusCode(400);
    }

    @Test
    void newRoundWithEvenCompetitorsShouldReturnTournamentWithNewRoundTest() {
        Set<CompetitorDTO> competitorDTOS = new HashSet<>();
        for (int i=0; i<10; i++) {
            CompetitorDTO competitorDTO = new CompetitorDTO();
            competitorDTO.setId(i);
            competitorDTO.setAge(18);
            competitorDTO.setGender("Male");
            competitorDTO.setCompetition("Kumite");
            competitorDTO.setAdvancement(1);
            competitorDTO.setName("John");
            competitorDTO.setSurname("Smith");
            competitorDTO.setClub("Dragon " + i);
            competitorService.save(competitorDTO);
            competitorDTOS.add(competitorDTO);
        }
        TournamentDTO tournamentDTO = new TournamentDTO(competitorDTOS);

        RestAssured.with().body(tournamentDTO).contentType(ContentType.JSON).when().post("/tournaments")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("numberOfCompetitors", equalTo(10))
                .body("duels", hasSize(5));

        Tournament referenceById = tournamentRepository.findById(1L).get();
        referenceById.getDuels().forEach(duel -> duelService.updateWinner(duel.getId(), duel.getParticipant1()));

        RestAssured.with().param("round", 2).when().put("/tournaments/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("numberOfCompetitors", equalTo(10))
                .body("duels", hasSize(5));
    }

    @Test
    void newRoundWithNotEvenCompetitorsShouldReturnTournamentWithNewRoundTest() {
        Set<CompetitorDTO> competitorDTOS = new HashSet<>();
        for (int i=0; i<11; i++) {
            CompetitorDTO competitorDTO = new CompetitorDTO();
            competitorDTO.setId(i);
            competitorDTO.setAge(18);
            competitorDTO.setGender("Male");
            competitorDTO.setCompetition("Kumite");
            competitorDTO.setAdvancement(1);
            competitorDTO.setName("John");
            competitorDTO.setSurname("Smith");
            competitorDTO.setClub("Dragon " + i);
            competitorService.save(competitorDTO);
            competitorDTOS.add(competitorDTO);
        }
        TournamentDTO tournamentDTO = new TournamentDTO(competitorDTOS);

        RestAssured.with().body(tournamentDTO).contentType(ContentType.JSON).when().post("/tournaments")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("numberOfCompetitors", equalTo(11))
                .body("duels", hasSize(6));
    }
}
