package com.sg.mastermind.dao;

import com.sg.mastermind.entity.Round;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

/**
 *
 * @author BRNJO
 */
@Repository
@Profile("memory")
public class MastermindRoundDaoMemImpl implements MastermindRoundDao {
    ArrayList<Round> rounds;

    public MastermindRoundDaoMemImpl() {
        rounds = new ArrayList<>();
    }
    
    @Override
    public Round createRound(Round round) {
        int newId = rounds.stream()
                .mapToInt(g -> g.getId())
                .max().orElse(0) + 1;

        round.setId(newId);
        round.setTimestamp(LocalDateTime.now());
        rounds.add(round);
        return round;
    }
    
    @Override
    public List<Round> getRoundsByGameId(int id) throws GameEmptyException {
        List<Round> roundList = rounds.stream()
                .filter(r -> r.getGameId() == id)
                .collect(Collectors.toList());
        
        if(roundList.isEmpty()) {
            throw new GameEmptyException("No rounds found for this game ID");
        }
        
        return roundList;
    }
    
    @Override
    // Needed for testing, seriously don't delete
    public void clearStorage() {
        rounds.clear();
    }
}
