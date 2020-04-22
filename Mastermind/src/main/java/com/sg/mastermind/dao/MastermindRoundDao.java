package com.sg.mastermind.dao;

import com.sg.mastermind.entity.Round;
import java.util.List;

/**
 *
 * @author BRNJO
 */
public interface MastermindRoundDao {
    public Round createRound(Round round);
    public List<Round> getRoundsByGameId(int id) throws GameEmptyException;
    public void clearStorage();
}
