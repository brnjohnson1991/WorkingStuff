package com.sg.mastermind.service;

import com.sg.mastermind.dao.GameNotFoundException;
import com.sg.mastermind.service.MastermindService;
import com.sg.mastermind.entity.Game;
import com.sg.mastermind.entity.Round;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author BRNJO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MastermindServiceTest {

    @Autowired
    MastermindService instance;

    public MastermindServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        instance.clearStorage();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of startNewGame method, of class MastermindService.
     */
    @Test
    public void testStartNewGame() throws GameNotFoundException {
        Game game = instance.startNewGame();

        HashSet<Character> hs = new HashSet<>();
        for (char i : game.getSolution()) {
            hs.add(i);
        }

        // Is the solution 4 unique characters?
        assertEquals(4, hs.size());

        instance.startNewGame();
        instance.startNewGame();

        // Did all three games get added?
        assertEquals(3, instance.getAllGamesForDisplay().size());
    }

    
        /**
     * Test of makeGuess method, of class MastermindService.
     */
    @Test(expected = GameNotFoundException.class)
    public void testMakeGuessNoGameException() throws Exception {
        ArrayList<Character> guess = new ArrayList<>(Arrays.asList('1','2','3','4'));
        Round r = instance.makeGuess(guess, 1);
    }

    
    /**
     * Test of makeGuess method, of class MastermindService.
     */
    @Test
    public void testMakeGuessAllMatch() throws Exception {
        Game game = instance.startNewGame();
        List<Character> guess = game.getSolution();

        Round r = instance.makeGuess(guess, game.getId());

        assertEquals(4, r.getExactMatches());
        assertEquals(0, r.getPartialMatches());
        assertTrue(instance.getGameByIdForDisplay(game.getId()).isComplete());
    }

    /**
     * Test of makeGuess method, of class MastermindService.
     */
    @Test
    public void testMakeGuessNoneMatch() throws Exception {
        Game game = instance.startNewGame();
        ArrayList<Character> guess = new ArrayList(Arrays.asList('A', 'B', 'C', 'D'));

        Round r = instance.makeGuess(guess, game.getId());

        assertEquals(0, r.getExactMatches());
        assertEquals(0, r.getPartialMatches());
    }

    /**
     * Test of makeGuess method, of class MastermindService.
     */
    @Test
    public void testMakeGuessSomeExactMatch() throws Exception {
        Game game = instance.startNewGame();
        ArrayList<Character> guess = new ArrayList(game.getSolution());
        guess.remove(0);
        guess.add(0, Character.MIN_VALUE);
        guess.remove(1);
        guess.add(1, Character.MAX_VALUE);
        Round r = instance.makeGuess(guess, game.getId());

        assertEquals(2, r.getExactMatches());
        assertEquals(0, r.getPartialMatches());
    }

    /**
     * Test of makeGuess method, of class MastermindService.
     */
    @Test
    public void testMakeGuessAllPartialMatch() throws Exception {
        Game game = instance.startNewGame();
        ArrayList<Character> guess = new ArrayList(game.getSolution());


        for (int i = 0; i < guess.size() / 2; i++) {
            char temp = guess.get(0);
            guess.remove(i);
            guess.add(i, guess.get(guess.size() - i - 1));
            guess.remove(guess.size() - i - 1);
            guess.add(guess.size() - i - 1, temp);
        }

        Round r = instance.makeGuess(guess, game.getId());

        assertEquals(0, r.getExactMatches());
        assertEquals(4, r.getPartialMatches());
    }

    /**
     * Test of makeGuess method, of class MastermindService.
     */
    @Test
    public void testMakeGuessSomePartialMatch() throws Exception {
        Game game = instance.startNewGame();
        ArrayList<Character> guess = new ArrayList(game.getSolution());

        // Just reverse the array
        // Note: this will break if we ever use non-unique guess choices
        for (int i = 0; i < guess.size() / 2; i++) {
            char temp = guess.get(0);
            guess.remove(i);
            guess.add(i, guess.get(guess.size() - i - 1));
            guess.remove(guess.size() - i - 1);
            guess.add(temp);

        }
        guess.remove(3);
        guess.add(3, Character.MIN_VALUE);
        guess.remove(2);
        guess.add(2, Character.MAX_VALUE);
        Round r = instance.makeGuess(guess, game.getId());

        assertEquals(0, r.getExactMatches());
        assertEquals(2, r.getPartialMatches());
    }

    /**
     * Test of makeGuess method, of class MastermindService.
     */
    @Test
    public void testMakeGuessSomePartialSomeExactMatch() throws Exception {
        Game game = instance.startNewGame();
        ArrayList<Character> guess = new ArrayList(game.getSolution());

        char temp = guess.get(0);
        guess.remove(0);
        guess.add(0, guess.get(guess.size() - 1));
        guess.remove(guess.size() - 1);
        guess.add(temp);

        Round r = instance.makeGuess(guess, game.getId());

        assertEquals(2, r.getExactMatches());
        assertEquals(2, r.getPartialMatches());
    }

    /**
     * Test of getAllGamesForDisplay method, of class MastermindService.
     */
    @Test
    public void testGetAllGamesForDisplay() throws Exception {
        Game game1 = instance.startNewGame();
        Game game2 = instance.startNewGame();
        Game game3 = instance.startNewGame();

        ArrayList<Character> guess = new ArrayList(game2.getSolution());

        instance.makeGuess(guess, game2.getId());

        List<Game> gameList = instance.getAllGamesForDisplay();

        // Game 1 should be blank
        Game testGame = instance.getGameByIdForDisplay(game1.getId());
        HashSet<Character> hs = new HashSet<>();

        for (char c : testGame.getSolution()) {
            hs.add(c);
        }

        assertEquals(0, hs.size());

        // Game 2 should be populated
        testGame = instance.getGameByIdForDisplay(game2.getId());
        hs = new HashSet<>();

        for (char c : testGame.getSolution()) {
            hs.add(c);
        }

        assertEquals(4, hs.size());

        // Game 3 should be blank
        testGame = instance.getGameByIdForDisplay(game3.getId());
        hs = new HashSet<>();

        for (char c : testGame.getSolution()) {
            hs.add(c);
        }

        assertEquals(0, hs.size());
    }

    /**
     * Test of getGameByIdForDisplay method, of class MastermindService.
     */
    @Test
    public void testGetGameByIdForDisplayGuessCorrect() throws Exception {
        Game game1 = instance.startNewGame();
        Game game2 = instance.startNewGame();
        Game game3 = instance.startNewGame();

        List<Character> guess = game2.getSolution();

        instance.makeGuess(guess, game2.getId());

        Game testGame = instance.getGameByIdForDisplay(game2.getId());
        HashSet<Character> hs = new HashSet<>();

        // Check if we got the solution
        for (char c : testGame.getSolution()) {
            hs.add(c);
        }

        assertEquals(4, hs.size());

        // Check an unsolved one for placeholder array
        testGame = instance.getGameByIdForDisplay(game3.getId());
        hs = new HashSet<>();

        // Check if we got the solution
        for (char c : testGame.getSolution()) {
            hs.add(c);
        }

        assertEquals(0, hs.size());
    }

    /**
     * Test of getGameByIdForDisplay method, of class MastermindService.
     */
    @Test(expected = GameNotFoundException.class)
    public void testGetGameByIdForDisplayNoGameExists() throws Exception {
        instance.getGameByIdForDisplay(1);
    }
    
    
    /**
     * Test of getRoundsByGameId method, of class MastermindService.
     */
    @Test
    public void testGetRoundsByGameId() {
        // Passthrough from dao, no test needed
    }

}
