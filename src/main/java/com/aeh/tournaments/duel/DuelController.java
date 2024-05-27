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
    ResponseEntity<DuelDTO> getDuelById(@PathVariable long duelId) {
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

    @PatchMapping("/{duelId}/winner/{competitorId}")
    ResponseEntity<DuelDTO> updateDuel(@PathVariable long duelId, @PathVariable long competitorId) {
        return ResponseEntity.ok(duelService.updateDuel(duelId, competitorId));
    }

    @DeleteMapping("/{duelId}")
     ResponseEntity<Void> deleteDuel(@PathVariable long duelId) {
        duelService.deleteById(duelId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/tournament/{tournamentId}")
    ResponseEntity<Set<DuelDTO>> getDuelsByTournamentId(@PathVariable long tournamentId) {
        Set<DuelDTO> duels = tournamentService.getDuels(tournamentId);
        return ResponseEntity.ok(duels);
    }
}