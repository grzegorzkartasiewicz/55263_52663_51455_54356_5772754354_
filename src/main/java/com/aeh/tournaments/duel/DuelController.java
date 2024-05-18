package com.aeh.tournaments.duel;

import com.aeh.tournaments.tournament.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = DuelController.INTERFACE_DUEL)
@RequiredArgsConstructor
 class DuelController {

    public static final String INTERFACE_DUEL = "/duels";
    private final DuelService duelService;
    private final TournamentService tournamentService;

    @GetMapping
    List<Duel> getAllDuels() {
        return duelService.getAllDuels();
    }

    @GetMapping("/{duelId}")
    ResponseEntity<DuelDTO> getDuelById(@PathVariable Long duelId) {
        DuelDTO duelDTO = duelService.getDuelById(duelId);
        if (duelDTO != null) {
            return ResponseEntity.ok(duelDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    ResponseEntity<DuelDTO> addDuel(@RequestBody DuelDTO duel) {
        DuelDTO newDuel = duelService.save(duel);
        return new ResponseEntity<>(newDuel, HttpStatus.CREATED);
    }

    @PutMapping("/{duelId}")
    ResponseEntity<DuelDTO> updateDuel(@PathVariable Long duelId, @RequestBody DuelDTO updatedDuel) {
        return ResponseEntity.ok(duelService.updateDuel(duelId, updatedDuel));
    }

    @DeleteMapping("/{duelId}")
     ResponseEntity<Void> deleteDuel(@PathVariable Long duelId) {
        duelService.deleteById(duelId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/tournement/{tournamentId}")
    ResponseEntity<Set<DuelDTO>> getDuelsByTournamentId(@PathVariable long tournamentId) {
        List<DuelDTO> duels = duelService.getDuelsByTournamentId(tournamentId);
        return ResponseEntity.ok((Set<DuelDTO>) duels);
    }
}