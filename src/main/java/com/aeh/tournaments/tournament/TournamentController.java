package com.aeh.tournaments.tournament;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = TournamentController.INTERFACE_TOURNAMENTS)
class TournamentController {

    public static final String INTERFACE_TOURNAMENTS = "/tournaments";

    private final TournamentService tournamentService;

    TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping("/{tournamentId}")
    ResponseEntity<TournamentReadDTO> getTournamentById(@PathVariable(name = "tournamentId") long tournamentId) {
        return ResponseEntity.ok(tournamentService.getTournamentById(tournamentId).orElseThrow());
    }

    @PostMapping
    ResponseEntity<TournamentReadDTO> createTournament(@RequestBody TournamentDTO tournament) {
        if (tournament.getCompetitors().size() < 4 || tournament.getCompetitors().size() > 32) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(tournamentService.createTournament(tournament));
    }

    @PutMapping("/{tournamentId}")
    ResponseEntity<TournamentReadDTO> newRound(@PathVariable long tournamentId, @RequestParam int round) {
        return ResponseEntity.ok(tournamentService.newRound(tournamentId, round));
    }

    @PutMapping("/losing/{tournamentId}")
    ResponseEntity<TournamentReadDTO> createLosingBracket(@PathVariable long tournamentId) {
        return ResponseEntity.ok(tournamentService.createLosingBranches(tournamentId));
    }

//    @GetMapping("/podium/{tournamentId}")
//    ResponseEntity<TournamentPodiumDTO> getPodium(@PathVariable long tournamentId) {
//        return ResponseEntity.ok(tournamentService.getPodium(tournamentId));
//    }
}
