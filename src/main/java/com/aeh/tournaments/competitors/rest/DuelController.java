package com.aeh.tournaments.competitors.rest;

import com.aeh.tournaments.competitors.DuelService;
import com.aeh.tournaments.competitors.model.Duel;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


import java.util.List;

@RestController
@RequestMapping(value = DuelController.INTERFACE_DUEL)

public class DuelController {

    public static final String INTERFACE_DUEL = "/competitors";
    private final DuelService duelService;

    public DuelController(DuelService duelService) {
        this.duelService = duelService;
    }


    // Pobierz pojedynki z bazy danych
    @GetMapping
    public List<Duel> getAllDuels() {
        return duelService.getAllDuels();
    }

    // Pobieranie konkretnego pojedynku po wartosci ID, jesli nie ma to wysyla no found
    @GetMapping("/{duelId}")
    public ResponseEntity<Duel> getDuelById(@PathVariable Long duelId) {
        Duel duel = duelService.getDuelById(duelId);
        if (duel != null) {
            return ResponseEntity.ok(duel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Pobieranie pojedynków po kategorii np.: ""Boys under 7's"
    @GetMapping("/category/{category}")
    public List<Duel> getDuelsByCategory(@PathVariable String category) {
        return duelService.findByCategory(category);
    }

    // Dodawanie nowego pojedynku do bazy danych.
    @PostMapping
    public ResponseEntity<Duel> addDuel(@RequestBody Duel duel) {
        Duel newDuel = duelService.save(duel);
        return new ResponseEntity<>(newDuel, HttpStatus.CREATED);
    }

    // Zmiana danych w istniejacym pojedynku
    @PutMapping("/{duelId}")
    public ResponseEntity<Duel> updateDuel(@PathVariable Long duelId, @RequestBody Duel updatedDuel) {
        Duel existingDuel = duelService.findById(duelId).orElse(null);
        if (existingDuel != null) {
            existingDuel.setParticipant1(updatedDuel.getParticipant1());
            existingDuel.setParticipant2(updatedDuel.getParticipant2());
            existingDuel.setWinner(updatedDuel.getWinner());
            existingDuel.setCategory(updatedDuel.getCategory());
            Duel savedDuel = duelService.save(existingDuel);
            return new ResponseEntity<>(savedDuel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Usunięcie pojedynku
    @DeleteMapping("/{duelId}")
    public ResponseEntity<Void> deleteDuel(@PathVariable Long duelId) {
        duelService.deleteById(duelId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}