package com.aeh.tournaments.tournament;


import com.aeh.tournaments.competitors.CompetitorDTO;
import com.aeh.tournaments.competitors.CompetitorService;
import com.aeh.tournaments.duel.DuelService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

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
                .statusCode(HttpStatus.SC_OK)
                .body("numberOfCompetitors", equalTo(2));
    }

    @Test
    void getTournamentByIdShouldReturnBadRequestIfTournamentNotPresentTest() {
        Tournament tournament = new Tournament();
        tournament.setId(Long.MAX_VALUE);
        tournament.setDuels(Collections.emptySet());
        tournament.setWinner(null);
        tournament.setNumberOfCompetitors(2);
        tournamentRepository.save(tournament);

        RestAssured.get("/tournaments/{tournamentId}", Long.MAX_VALUE)
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    void createTournamentWithEvenCompetitorsShouldReturnTournamentWithFirstRoundTest() {
        Set<CompetitorDTO> competitorDTOS = new HashSet<>();
        for (int i=0; i<10; i++) {
            CompetitorDTO competitorDTO = new CompetitorDTO();
            competitorDTO.setId(i+1);
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
                .statusCode(HttpStatus.SC_OK)
                .body("numberOfCompetitors", equalTo(10))
                .body("duels", hasSize(5));
    }

    @Test
    void createTournamentWithNotEvenCompetitorsShouldReturnTournamentWithFirstRoundTest() {
        Set<CompetitorDTO> competitorDTOS = new HashSet<>();
        for (int i=0; i<11; i++) {
            CompetitorDTO competitorDTO = new CompetitorDTO();
            competitorDTO.setId(i+1);
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
                .statusCode(HttpStatus.SC_OK)
                .body("numberOfCompetitors", equalTo(11))
                .body("duels", hasSize(6));
    }

    @Test
    void createTournamentWithoutCompetitorsShouldReturnTournamentWithFirstRoundTest() {
        Set<CompetitorDTO> competitorDTOS = new HashSet<>();
        TournamentDTO tournamentDTO = new TournamentDTO(competitorDTOS);

        RestAssured.with().body(tournamentDTO).contentType(ContentType.JSON).when().post("/tournaments")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void newRoundWithEvenCompetitorsShouldReturnTournamentWithNewRoundTest() {
        Set<CompetitorDTO> competitorDTOS = new HashSet<>();
        for (int i=0; i<10; i++) {
            CompetitorDTO competitorDTO = new CompetitorDTO();
            competitorDTO.setId(i+1);
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

        int tournamentId = RestAssured.with().body(tournamentDTO).contentType(ContentType.JSON).when().post("/tournaments")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("numberOfCompetitors", equalTo(10))
                .body("duels", hasSize(5))
                .extract().body().jsonPath().getInt("id");

        Tournament tournament = tournamentRepository.findById((long) tournamentId).orElseThrow();
        tournament.getDuels().forEach(duel -> duelService.updateWinner(duel.getId(), duel.getParticipant1()));

        RestAssured.with().param("round", 2).when().put("/tournaments/{tournamentId}", tournamentId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(tournamentId))
                .body("numberOfCompetitors", equalTo(5))
                .body("duels", hasSize(8));
    }

    @Test
    void newRoundWithNotEvenCompetitorsShouldReturnTournamentWithNewRoundTest() {
        Set<CompetitorDTO> competitorDTOS = new HashSet<>();
        for (int i=0; i<11; i++) {
            CompetitorDTO competitorDTO = new CompetitorDTO();
            competitorDTO.setId(i+1);
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

        int tournamentId = RestAssured.with().body(tournamentDTO).contentType(ContentType.JSON).when().post("/tournaments")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("numberOfCompetitors", equalTo(11))
                .body("duels", hasSize(6))
                .extract().body().jsonPath().getInt("id");

        Tournament tournament = tournamentRepository.findById((long) tournamentId).orElseThrow();
        tournament.getDuels().forEach(duel -> duelService.updateWinner(duel.getId(), duel.getParticipant1()));

        RestAssured.with().param("round", 2).when().put("/tournaments/{tournamentId}", tournamentId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(tournamentId))
                .body("numberOfCompetitors", equalTo(6))
                .body("duels", hasSize(10));
    }

    @Test
    void e2eTournamentTest() {
        Set<CompetitorDTO> competitorDTOS = new HashSet<>();
        for (int i=0; i<10; i++) {
            CompetitorDTO competitorDTO = new CompetitorDTO();
            competitorDTO.setId(i+1);
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

        int tournamentId = RestAssured.with().body(tournamentDTO).contentType(ContentType.JSON).when().post("/tournaments")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("numberOfCompetitors", equalTo(10))
                .body("duels", hasSize(5))
                .extract().body().jsonPath().getInt("id");

        Tournament tournament = tournamentRepository.findById((long) tournamentId).orElseThrow();
        tournament.getDuels().forEach(duel -> duelService.updateWinner(duel.getId(), duel.getParticipant1()));

        TournamentReadDTO tournamentReadDTO = RestAssured.with().param("round", 2).when().put("/tournaments/{tournamentId}", tournamentId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(tournamentId))
                .body("numberOfCompetitors", equalTo(5))
                .body("duels", hasSize(3))
                .extract().body().as(TournamentReadDTO.class);

        tournamentReadDTO.getDuels().forEach(duel -> duelService.updateWinner(duel.getId(), duel.getParticipant1()));

        tournamentReadDTO = RestAssured.with().param("round", 3).when().put("/tournaments/{tournamentId}", tournamentId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(tournamentId))
                .body("numberOfCompetitors", equalTo(3))
                .body("duels", hasSize(2))
                .extract().body().as(TournamentReadDTO.class);

        tournamentReadDTO.getDuels().forEach(duel -> duelService.updateWinner(duel.getId(), duel.getParticipant1()));

        tournamentReadDTO = RestAssured.with().param("round", 4).when().put("/tournaments/{tournamentId}", tournamentId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(tournamentId))
                .body("numberOfCompetitors", equalTo(2))
                .body("duels", hasSize(1))
                .extract().body().as(TournamentReadDTO.class); // result of this duel is first and second place

        tournamentReadDTO.getDuels().forEach(duel -> duelService.updateWinner(duel.getId(), duel.getParticipant1()));

        tournamentReadDTO = RestAssured.put("/tournaments/losing/{tournamentId}", tournamentId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(tournamentId))
                .body("numberOfCompetitors", equalTo(7))
                .body("duels", hasSize(4))
                .extract().body().as(TournamentReadDTO.class);

        tournamentReadDTO.getDuels().forEach(duel -> duelService.updateWinner(duel.getId(), duel.getParticipant1()));

        tournamentReadDTO = RestAssured.with().param("round", 6).when().put("/tournaments/{tournamentId}", tournamentId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(tournamentId))
                .body("numberOfCompetitors", equalTo(4))
                .body("duels", hasSize(2)) // result of these duels are going to be two third places
                .extract().body().as(TournamentReadDTO.class);

        tournamentReadDTO.getDuels().forEach(duel -> duelService.updateWinner(duel.getId(), duel.getParticipant1()));

        //TODO add check for podium calling podium endpoint
    }
}
