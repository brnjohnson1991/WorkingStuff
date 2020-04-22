package com.sg.mastermind.controller;

import com.sg.mastermind.dao.GameEmptyException;
import com.sg.mastermind.dao.GameNotFoundException;
import com.sg.mastermind.service.MastermindService;
import com.sg.mastermind.entity.Game;
import com.sg.mastermind.entity.Round;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author BRNJO
 */
@RestController
@RequestMapping("/api/mastermind")
public class MastermindController {

    final int GUESS_SIZE = 4;

    @Autowired
    MastermindService service;

    @PostMapping("/begin")
    public ResponseEntity<Integer> startGame() {
        return ResponseEntity.ok(service.startNewGame().getId());
    }

    @PostMapping("/guess/{gameId}")
    public ResponseEntity<Round> makeGuess(@PathVariable int gameId, @RequestBody ArrayList<Character> guess) throws GameNotFoundException, BadRequestException {
        if (guess.size() != GUESS_SIZE) {
            throw new BadRequestException("Malformed request, please submit a list of " + GUESS_SIZE + " digits.");
        } 
        return ResponseEntity.ok(service.makeGuess(guess, gameId));
    }

    @GetMapping("/game")
    public ResponseEntity<List<Game>> displayGames() throws GameNotFoundException {
        return ResponseEntity.ok(service.getAllGamesForDisplay());
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<Game> displayOneGame(@PathVariable int gameId) throws GameNotFoundException {
        return ResponseEntity.ok(service.getGameByIdForDisplay(gameId));
    }

    @GetMapping("/rounds/{gameId}")
    public ResponseEntity<List<Round>> displayRounds(@PathVariable int gameId) throws GameEmptyException {
        return ResponseEntity.ok(service.getRoundsByGameId(gameId));
    }
    
    @GetMapping("/test")
    public ResponseEntity<List<Character>> displaySampleList() {
        ArrayList<Character> solution = new ArrayList<>(Arrays.asList('1', '2', '3', '4'));
        return ResponseEntity.ok(solution);
    }
}
