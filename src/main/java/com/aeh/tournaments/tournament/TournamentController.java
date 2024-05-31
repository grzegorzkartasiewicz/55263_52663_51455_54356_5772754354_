package com.aeh.tournaments.tournament;

import com.aeh.tournaments.competitors.CompetitorDTO;
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
    ResponseEntity<CreateTournamentResponse> createTournament(@RequestBody CreateTournamentRequest createTournamentRequest) {
        return ResponseEntity.ok(tournamentService.createTournament(createTournamentRequest));
    }

    @CrossOrigin
    @PutMapping("/{tournamentId}/competitors/{competitorId}")
    ResponseEntity<CompetitorDTO> addCompetitor(@PathVariable long tournamentId, @PathVariable long competitorId) {
        return ResponseEntity.ok(tournamentService.addCompetitor(tournamentId, competitorId));
    }

    @CrossOrigin
    @PutMapping("/{tournamentId}")
    ResponseEntity<TournamentRoundResponse> newRound(@PathVariable long tournamentId, @RequestParam int round) {
        return ResponseEntity.ok(tournamentService.newRound(tournamentId, round));
    }

    @CrossOrigin
    @PutMapping("/losing/{tournamentId}")
    ResponseEntity<TournamentRoundResponse> createLosingBracket(@PathVariable long tournamentId) {
        return ResponseEntity.ok(tournamentService.createLosingBranches(tournamentId));
    }

    @CrossOrigin
    @GetMapping("/podium/{tournamentId}")
    ResponseEntity<TournamentPodiumDTO> getPodium(@PathVariable long tournamentId) {
        return ResponseEntity.ok(tournamentService.getPodium(tournamentId));
    }

    public record TournamentPodiumDTO (CompetitorDTO winner, CompetitorDTO secondPlace, CompetitorDTO thirdPlaceLeft, CompetitorDTO thirdPlaceRight) {
    }

    public record CreateTournamentRequest(String name,
                                          String data,
                                          int numberOfCompetitors,
                                          String category,
                                          String email) {
    }

    public record CreateTournamentResponse(long tournamentId,
                                           String name,
                                           String data,
                                           int numberOfCompetitors,
                                           String category,
                                           String email) {
    }
}
