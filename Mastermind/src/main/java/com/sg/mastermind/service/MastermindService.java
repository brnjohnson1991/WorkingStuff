package com.sg.mastermind.service;

import com.sg.mastermind.dao.GameEmptyException;
import com.sg.mastermind.dao.GameNotFoundException;
import com.sg.mastermind.dao.MastermindGameDao;
import com.sg.mastermind.dao.MastermindRoundDao;
import com.sg.mastermind.entity.Game;
import com.sg.mastermind.entity.Round;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author BRNJO
 */
@Component
public class MastermindService {

    final int GAME_SIZE = 4;
    @Autowired
    MastermindGameDao gameDao;

    @Autowired
    MastermindRoundDao roundDao;

    public Game startNewGame() {
        Game game = new Game();
        ArrayList<Character> a
                = new ArrayList<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));


        Collections.shuffle(a);
        ArrayList<Character> solution = new ArrayList<>(Arrays.asList(a.get(0),
                a.get(1),
                a.get(2),
                a.get(3)));

        game.setSolution(solution);
        return gameDao.createGame(game);
    }

    public Round makeGuess(List<Character> guess, int gameId) throws GameNotFoundException {
        Round round = new Round();
        Game game = gameDao.getGameById(gameId);
        List<Character> solution = game.getSolution();
        HashSet<Character> hs = new HashSet<>();
        int e = 0, p = 0;

        for (int i = 0; i < solution.size(); i++) {
            if (guess.get(i).equals(solution.get(i))) {
                e++;
            }
            hs.add(guess.get(i));
            hs.add(solution.get(i));
        }   


        p = (solution.size() * 2) - hs.size() - e;

        round.setExactMatches(e);
        round.setPartialMatches(p);
        round.setGameId(gameId);
        round.setGuess(guess);
        round = roundDao.createRound(round);

        if (e == GAME_SIZE) {
            gameDao.endGame(game);
        }
        return round;
    }

    public List<Game> getAllGamesForDisplay() throws GameNotFoundException {
        List<Game> gameList = gameDao.getAllGames();
        ArrayList<Character> placeholder = new ArrayList<>();

        gameList.stream()
                .forEach(g -> {
                    if (!g.isComplete()) {
                        g.setSolution(placeholder);
                    }
                });

        gameList.sort(new SortById());
        
        if(gameList.isEmpty()) {
            throw new GameNotFoundException("No games found, please start a game.");
        }
        
        return gameList;
    }

    public Game getGameByIdForDisplay(int id) throws GameNotFoundException {
        Game game = gameDao.getGameById(id);
        ArrayList<Character> placeholder = new ArrayList<>();
        
        if (!game.isComplete()) {
            game.setSolution(placeholder);
        }

        return game;
    }

    public List<Round> getRoundsByGameId(int id) throws GameEmptyException {
        return roundDao.getRoundsByGameId(id);
    }

    public void clearStorage() {
        gameDao.clearStorage();
        roundDao.clearStorage();
    }

    private class SortById implements Comparator<Game> {

        @Override
        public int compare(Game a, Game b) {
            return a.getId() - b.getId();
        }
    }
}
