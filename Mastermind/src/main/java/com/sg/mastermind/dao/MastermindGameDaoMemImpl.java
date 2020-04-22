package com.sg.mastermind.dao;

import com.sg.mastermind.entity.Game;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

/**
 *
 * @author BRNJO
 */

@Repository
@Profile("memory")
public class MastermindGameDaoMemImpl implements MastermindGameDao {
    ArrayList<Game> games;

    public MastermindGameDaoMemImpl() {
        games = new ArrayList<>();
    }
    
    @Override
    public Game createGame (Game game) {
        int newId = games.stream()
                .mapToInt(g -> g.getId())
                .max().orElse(0) + 1;
        
        game.setId(newId);
        games.add(game);
        return game;
    }
    
    @Override
    public boolean endGame(Game game) throws GameNotFoundException {
        boolean changesMade = false;
        
        game.setComplete(true);
       
        for(int i = 0; i < games.size(); i++) {
            if(games.get(i).getId() == game.getId()) {
                games.remove(i);
                games.add(i, game);
                changesMade = true;
            }
        }
        
        return changesMade;
    }
    
    @Override
    public Game getGameById(int id) throws GameNotFoundException {
        Game game = games.stream()
                .filter(g -> g.getId() == id)
                .findFirst().orElse(null);
        
        if(game == null) {
            throw new GameNotFoundException("Game not found!");
        }
        
        return game;
    }
    
    @Override
    public List<Game> getAllGames() {
        return games;
    }
   
    // Needed for testing
    @Override
    public void clearStorage() {
        games.clear();
    }
}