package com.sg.mastermind.dao;

import com.sg.mastermind.entity.Game;
import java.util.List;

/**
 *
 * @author BRNJO
 */
public interface MastermindGameDao {
    public Game createGame (Game game);
    public Game getGameById(int id) throws GameNotFoundException;
    public List<Game> getAllGames();
    public boolean endGame(Game game) throws GameNotFoundException;
    public void clearStorage();
}
