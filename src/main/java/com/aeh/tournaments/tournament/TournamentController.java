package com.aeh.tournaments.tournament;

import com.aeh.tournaments.competitors.CompetitorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    ResponseEntity<TournamentReadDTO> countLosingBracket(@PathVariable long tournamentId) {
        return ResponseEntity.ok(tournamentService.createLosingBranches(tournamentId));    }

    @GetMapping("/{tournamentId}/podium")
    public List<CompetitorDTO> getPodium(@PathVariable long tournamentId) {
        return tournamentService.getPodium(tournamentId);
    }
}
