package com.aeh.tournaments.duel;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.Collections;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DuelControllerIT {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Autowired
    private DuelService duelService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllDuelsShouldDownloadAllDuels() {
        DuelService duelService = mock(DuelService.class);
        when(duelService.getAllDuels()).thenReturn(Collections.emptyList());

        given()
                .contentType(io.restassured.http.ContentType.JSON)
                .when()
                .get("/duels")
                .then()
                .statusCode(200)
                .contentType(io.restassured.http.ContentType.JSON)
                .body("", hasSize(0));
    }

    @Test
    void getDuelByIdIfDuelExists() {
        DuelDTO duelDTO = new DuelDTO();
        duelDTO.setId(1L);
        duelDTO.setParticipant1(1L);
        duelDTO.setParticipant2(2L);
        duelDTO.setWinner(1L);

        duelService.save(duelDTO);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/duels/1")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(1))
                .body("participant1", equalTo(1))
                .body("participant2", equalTo(2))
                .body("winner", equalTo(1));
    }

    @Test
    void getDuelByIdIfNotExists() {
        DuelService duelService = mock(DuelService.class);
        when(duelService.getDuelById(100L)).thenReturn(null);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/duels/100")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void addDuelIfNotExists() throws Exception {
        DuelDTO duelDTO = new DuelDTO();
        duelDTO.setId(1L);
        duelDTO.setParticipant1(1L);
        duelDTO.setParticipant2(2L);
        duelDTO.setWinner(1L);

        DuelService duelService = mock(DuelService.class);
        when(duelService.save(duelDTO)).thenReturn(duelDTO);

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(duelDTO))
                .when()
                .post("/duels")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", equalTo(1))
                .body("participant1", equalTo(1))
                .body("participant2", equalTo(2))
                .body("winner", equalTo(1));
    }

    @Test
    void updateDuelShouldAllowedToChangeData() throws Exception {
        DuelDTO duelDTO = new DuelDTO();
        duelDTO.setId(1L);
        duelDTO.setParticipant1(1L);
        duelDTO.setParticipant2(2L);
        duelDTO.setWinner(1L);
        DuelService duelService = mock(DuelService.class);
        when(duelService.updateDuel(1L, duelDTO)).thenReturn(duelDTO);

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(duelDTO))
                .when()
                .put("/duels/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("participant1", equalTo(1))
                .body("participant2", equalTo(2))
                .body("winner", equalTo(1));
    }

    @Test
    void deleteDuelIfNotNeeded() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/duels/1")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
