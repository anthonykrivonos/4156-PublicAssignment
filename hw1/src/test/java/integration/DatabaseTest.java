package integration;

import controllers.Database;
import models.GameBoard;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseTest {
  
  @Test
  @Order(1)
  public void testConstructorSuccess() {
    try {
      new Database();
    } catch (Exception e) {
      Assertions.fail("Constructing Database should not fail");
    }

    System.out.println("[Order 1] Tested Database constructor");
  }

  @Test
  @Order(2)
  public void testSetThenGet_StartedGame() {
    GameBoard gameboard;
    GameBoard gameboardResult;
    Database db;
    
    try {
      gameboard = new GameBoard('X');
    } catch (Exception e) {
      Assertions.fail("Constructing GameBoard should not fail");
      return;
    }
    
    try {
      db = new Database();
    } catch (Exception e) {
      Assertions.fail("Constructing Database should not fail");
      return;
    }
    
    try {
      db.set(gameboard);
    } catch (Exception e) {
      Assertions.fail("Setting into Database should not fail");
      return;
    }
    
    try {
      gameboardResult = db.get();
    } catch (Exception e) {
      Assertions.fail("Getting from Database should not fail");
      return;
    }

    Assertions.assertEquals(gameboard.getP1().getId(), gameboardResult.getP1().getId());
    Assertions.assertEquals(gameboard.getP1().getType(), gameboardResult.getP1().getType());
    Assertions.assertEquals(null, gameboardResult.getP2());
    Assertions.assertEquals(gameboard.isGameStarted(), gameboardResult.isGameStarted());
    Assertions.assertEquals(gameboard.getTurn(), gameboardResult.getTurn());
    Assertions.assertArrayEquals(gameboard.getBoardState(), gameboardResult.getBoardState());
    Assertions.assertEquals(gameboard.getWinner(), gameboardResult.getWinner());
    Assertions.assertEquals(gameboard.isDraw(), gameboardResult.isDraw());

    System.out.println("[Order 2] Tested Database set, then get (Started Game)");
  }

  @Test
  @Order(3)
  public void testSetThenGet_JoinedGame() {
    GameBoard gameboard;
    GameBoard gameboardResult;
    Database db;
    
    try {
      gameboard = new GameBoard('X');
      gameboard.joinGame();
      gameboard.playTurn(gameboard.getP1(), 1, 1);
    } catch (Exception e) {
      Assertions.fail("Constructing GameBoard should not fail");
      return;
    }
    
    try {
      db = new Database();
    } catch (Exception e) {
      Assertions.fail("Constructing Database should not fail");
      return;
    }
    
    try {
      db.set(gameboard);
    } catch (Exception e) {
      Assertions.fail("Setting into Database should not fail");
      return;
    }
    
    try {
      gameboardResult = db.get();
    } catch (Exception e) {
      Assertions.fail("Getting from Database should not fail");
      return;
    }

    Assertions.assertEquals(gameboard.getP1().getId(), gameboardResult.getP1().getId());
    Assertions.assertEquals(gameboard.getP1().getType(), gameboardResult.getP1().getType());
    Assertions.assertEquals(gameboard.getP2().getId(), gameboardResult.getP2().getId());
    Assertions.assertEquals(gameboard.getP2().getType(), gameboardResult.getP2().getType());
    Assertions.assertEquals(gameboard.isGameStarted(), gameboardResult.isGameStarted());
    Assertions.assertEquals(gameboard.getTurn(), gameboardResult.getTurn());
    Assertions.assertArrayEquals(gameboard.getBoardState(), gameboardResult.getBoardState());
    Assertions.assertEquals(gameboard.getWinner(), gameboardResult.getWinner());
    Assertions.assertEquals(gameboard.isDraw(), gameboardResult.isDraw());

    System.out.println("[Order 3] Tested Database set, then get (Joined Game)");
  }

  @Test
  @Order(4)
  public void testSetThenUnsetThenGet() {
    GameBoard gameboard;
    GameBoard gameboardResult;
    Database db;
    
    try {
      gameboard = new GameBoard('X');
      gameboard.setGameStarted(false);
      gameboard.setDraw(true);
    } catch (Exception e) {
      Assertions.fail("Constructing GameBoard should not fail");
      return;
    }
    
    try {
      db = new Database();
    } catch (Exception e) {
      Assertions.fail("Constructing Database should not fail");
      return;
    }
    
    try {
      db.set(gameboard);
    } catch (Exception e) {
      Assertions.fail("Setting into Database should not fail");
      return;
    }
    
    try {
      db.unset();
    } catch (Exception e) {
      Assertions.fail("Unsetting from Database should not fail");
      return;
    }
    
    try {
      gameboardResult = db.get();
    } catch (Exception e) {
      Assertions.fail("Getting from Database should not fail");
      return;
    }

    Assertions.assertEquals(null, gameboardResult);

    System.out.println("[Order 4] Tested Database set, then unset, then get");
  }
  
}
