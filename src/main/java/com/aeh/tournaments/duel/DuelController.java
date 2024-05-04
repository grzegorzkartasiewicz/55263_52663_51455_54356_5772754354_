package com.aeh.tournaments.duel;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


import java.util.List;

@RestController
@RequestMapping(value = DuelController.INTERFACE_DUEL)
 class DuelController {

    public static final String INTERFACE_DUEL = "/duels";
    private final DuelService duelService;

    DuelController(DuelService duelService) {
        this.duelService = duelService;
    }


    @GetMapping
    List<Duel> getAllDuels() {
        return duelService.getAllDuels();
    }

    @GetMapping("/{duelId}")
    ResponseEntity<Duel> getDuelById(@PathVariable Long duelId) {
        Duel duel = duelService.getDuelById(duelId);
        if (duel != null) {
            return ResponseEntity.ok(duel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/category/{category}")
    List<Duel> getDuelsByCategory(@PathVariable String category) {
        return duelService.findByCategory(category);
    }

    @PostMapping
    ResponseEntity<Duel> addDuel(@RequestBody Duel duel) {
        Duel newDuel = duelService.save(duel);
        return new ResponseEntity<>(newDuel, HttpStatus.CREATED);
    }

    @PutMapping("/{duelId}")
    ResponseEntity<Duel> updateDuel(@PathVariable Long duelId, @RequestBody Duel updatedDuel) {
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

    @DeleteMapping("/{duelId}")
     ResponseEntity<Void> deleteDuel(@PathVariable Long duelId) {
        duelService.deleteById(duelId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}