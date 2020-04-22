package com.sg.mastermind.dao;

import com.sg.mastermind.entity.Game;
import java.util.ArrayList;
import java.util.Arrays;
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
public class MastermindGameDaoMemImplTest {

    
    @Autowired
    MastermindGameDao gameDao;

    public MastermindGameDaoMemImplTest() {
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        gameDao.clearStorage();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCreateGetGame() throws Exception {
        Game game = new Game();
        ArrayList<Character> solution = new ArrayList<>(Arrays.asList('1', '2', '3', '4'));

        game.setSolution(solution);
        Game test = gameDao.createGame(game);


        assertEquals(game, test);

        Game test2 = gameDao.getGameById(test.getId());


        assertEquals(game, test2);
    }

    @Test (expected = GameNotFoundException.class)
    public void testGetGameByIdGameDoesNotExist() throws Exception {
        gameDao.getGameById(1);
    }
    
    @Test
    public void testGetAllGames() {
        Game game = new Game();
        ArrayList<Character> solution = new ArrayList<>(Arrays.asList('1', '2', '3', '4'));

        game.setSolution(solution);
        game.setId(1);
        gameDao.createGame(game);

        Game game2 = new Game();
        ArrayList<Character> solution2 = new ArrayList<>(Arrays.asList('4', '3', '2', '1'));

        game2.setSolution(solution2);
        game2.setId(2);
        gameDao.createGame(game2);

        List<Game> gameList = gameDao.getAllGames();

        assertEquals(2, gameList.size());
        assertTrue(gameList.contains(game));
        assertTrue(gameList.contains(game2));
    }

    /**
     * Test of clearStorage method, of class MastermindGameDaoMemImpl.
     */
    @Test
    public void testClearStorage() {
        Game game = new Game();
        game.setComplete(true);
        ArrayList<Character> solution = new ArrayList<>(Arrays.asList('1', '2', '3', '4'));

        game.setSolution(solution);
        gameDao.createGame(game);

        gameDao.clearStorage();
        assertEquals(0, gameDao.getAllGames().size());
    }

    @Test
    public void testEndGame() throws Exception {
        Game game = new Game();
        game.setComplete(true);
        ArrayList<Character> solution = new ArrayList<>(Arrays.asList('1', '2', '3', '4'));

        game.setSolution(solution);
        gameDao.createGame(game);

        assertTrue(gameDao.endGame(game));

        game = gameDao.getGameById(game.getId());
        assertTrue(game.isComplete());
    }
    
    @Test
    public void testEndGameGameDoesNotExist() throws Exception { 
        assertFalse(gameDao.endGame(new Game()));
    }
}
