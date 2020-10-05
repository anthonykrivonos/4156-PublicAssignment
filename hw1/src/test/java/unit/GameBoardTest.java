package unit;

import models.GameBoard;
import models.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GameBoardTest {

  @Test
  public void testGameBoardConstructor() {
    final Player p1 = new Player('X', 1);
    final Player p2 = new Player('O', 1);
    final boolean gameStarted = false;
    final int turn = 0;
    final char[][] boardState = new char[3][3];
    final int winner = 0;
    final boolean isDraw = false;
    final GameBoard board = new GameBoard(p1, p2, gameStarted, turn, boardState, winner, isDraw);

    Assertions.assertEquals(p1, board.getP1());
    Assertions.assertEquals(p2, board.getP2());
    Assertions.assertEquals(gameStarted, board.isGameStarted());
    Assertions.assertEquals(turn, board.getTurn());
    Assertions.assertArrayEquals(boardState, board.getBoardState());
    Assertions.assertEquals(winner, board.getWinner());
    Assertions.assertEquals(isDraw, board.isDraw());
  }

  @Test
  public void testGameBoardP1Constructor() {
    final char p1Type = 'X';
    final GameBoard board = new GameBoard(p1Type);

    Assertions.assertEquals(p1Type, board.getP1().getType());
    Assertions.assertEquals(null, board.getP2());
    Assertions.assertEquals(false, board.isGameStarted());
    Assertions.assertEquals(1, board.getTurn());
    Assertions.assertArrayEquals(new char[3][3], board.getBoardState());
    Assertions.assertEquals(0, board.getWinner());
    Assertions.assertEquals(false, board.isDraw());
  }

  @Test
  public void testIsGameOver() {
    final GameBoard board = new GameBoard('X');

    // winner == 0 && !isDraw
    Assertions.assertEquals(false, board.isGameOver());

    // winner != 0 && !isDraw
    board.setWinner(1);
    Assertions.assertEquals(true, board.isGameOver());

    // winner == 0 && isDraw
    board.setWinner(0);
    board.setDraw(true);
    Assertions.assertEquals(true, board.isGameOver());
  }

  @Test
  public void testSetP1() {
    final GameBoard board = new GameBoard('X');

    final Player newP1 = new Player('X', 1);
    board.setP1(newP1);

    Assertions.assertEquals(newP1.getId(), board.getP1().getId());
    Assertions.assertEquals(newP1.getType(), board.getP1().getType());
  }

  @Test
  public void testSetP2() {
    final GameBoard board = new GameBoard('X');

    final Player newP2 = new Player('O', 2);
    board.setP2(newP2);

    Assertions.assertEquals(newP2.getId(), board.getP2().getId());
    Assertions.assertEquals(newP2.getType(), board.getP2().getType());
  }

  @Test
  public void testSetGameStarted() {
    final GameBoard board = new GameBoard('X');

    final boolean gameStarted = true;
    board.setGameStarted(gameStarted);

    Assertions.assertEquals(gameStarted, board.isGameStarted());
  }

  @Test
  public void testSetTurn() {
    final GameBoard board = new GameBoard('X');

    final int turn = 1;
    board.setTurn(turn);

    Assertions.assertEquals(turn, board.getTurn());
  }

  @Test
  public void testSetBoardState() {
    final GameBoard board = new GameBoard('X');

    final char[][] boardState = new char[3][3];
    boardState[1][1] = 'O';
    board.setBoardState(boardState);

    Assertions.assertEquals('O', board.getBoardState()[1][1]);
  }

  @Test
  public void testSetWinner() {
    final GameBoard board = new GameBoard('X');

    final int winner = 1;
    board.setWinner(winner);

    Assertions.assertEquals(winner, board.getWinner());
  }

  @Test
  public void testSetDraw() {
    final GameBoard board = new GameBoard('X');

    final boolean isDraw = true;
    board.setDraw(isDraw);

    Assertions.assertEquals(isDraw, board.isDraw());
  }

  @Test
  public void testJoinGameSuccess() {
    final GameBoard board = new GameBoard('X');
    board.setGameStarted(true);

    try {
      board.joinGame();
    } catch (Exception e) {
      Assertions.fail(e);
    }

    Assertions.assertEquals('O', board.getP2().getType());
    Assertions.assertEquals(true, board.isGameStarted());
  }

  @Test
  public void testJoinGameFailure_WinnerNotZero() {
    final GameBoard board = new GameBoard('X');
    board.setGameStarted(true);
    board.setWinner(1);

    try {
      board.joinGame();
    } catch (Exception e) {
      Assertions.assertEquals("Game already started", e.getMessage());
    }
  }

  @Test
  public void testJoinGameFailure_Draw() {
    final GameBoard board = new GameBoard('X');
    board.setGameStarted(true);
    board.setDraw(true);

    try {
      board.joinGame();
    } catch (Exception e) {
      Assertions.assertEquals("Game already started", e.getMessage());
    }
  }

  @Test
  public void testJoinGameFailure_WinnerNotZeroAndDraw() {
    final GameBoard board = new GameBoard('X');
    board.setGameStarted(true);
    board.setWinner(1);
    board.setDraw(true);

    try {
      board.joinGame();
    } catch (Exception e) {
      Assertions.assertEquals("Game already started", e.getMessage());
    }
  }

  @Test
  public void testPlayTurnFailure_InvalidPosition_XLessThanZero() {
    final GameBoard board = new GameBoard('X');
    try {
      board.joinGame();
    } catch (Exception e) {
      Assertions.fail(e);
    }

    try {
      board.playTurn(board.getP1(), -1, 0);
    } catch (Exception e) {
      Assertions.assertEquals("Invalid position", e.getMessage());
    }
  }

  @Test
  public void testPlayTurnFailure_InvalidPosition_XGreaterThanTwo() {
    final GameBoard board = new GameBoard('X');
    try {
      board.joinGame();
    } catch (Exception e) {
      Assertions.fail(e);
    }

    try {
      board.playTurn(board.getP1(), 3, 0);
    } catch (Exception e) {
      Assertions.assertEquals("Invalid position", e.getMessage());
    }
  }

  @Test
  public void testPlayTurnFailure_InvalidPosition_YLessThanZero() {
    final GameBoard board = new GameBoard('X');
    try {
      board.joinGame();
    } catch (Exception e) {
      Assertions.fail(e);
    }

    try {
      board.playTurn(board.getP1(), 0, -1);
    } catch (Exception e) {
      Assertions.assertEquals("Invalid position", e.getMessage());
    }
  }

  @Test
  public void testPlayTurnFailure_InvalidPosition_YGreaterThanTwo() {
    final GameBoard board = new GameBoard('X');
    try {
      board.joinGame();
    } catch (Exception e) {
      Assertions.fail(e);
    }

    try {
      board.playTurn(board.getP1(), 0, 3);
    } catch (Exception e) {
      Assertions.assertEquals("Invalid position", e.getMessage());
    }
  }

  @Test
  public void testPlayTurnFailure_PositionAlreadyFilled() {
    final GameBoard board = new GameBoard('X');
    try {
      board.joinGame();
    } catch (Exception e) {
      Assertions.fail(e);
    }

    try {
      board.playTurn(board.getP1(), 1, 1);
      board.playTurn(board.getP2(), 1, 1);
    } catch (Exception e) {
      Assertions.assertEquals("Position already filled", e.getMessage());
    }
  }

  @Test
  public void testPlayTurnFailure_NotYourTurn() {
    final GameBoard board = new GameBoard('X');
    try {
      board.joinGame();
    } catch (Exception e) {
      Assertions.fail(e);
    }

    try {
      board.playTurn(board.getP2(), 1, 1);
    } catch (Exception e) {
      Assertions.assertEquals("Not your turn", e.getMessage());
    }
  }

  @Test
  public void testPlayTurnSuccess_NextTurn() {
    final GameBoard board = new GameBoard('X');
    try {
      board.joinGame();
    } catch (Exception e) {
      Assertions.fail(e);
    }

    try {
      board.playTurn(board.getP1(), 1, 1);
    } catch (Exception e) {
      Assertions.fail();
    }

    Assertions.assertEquals(2, board.getTurn());
  }

  @Test
  public void testPlayTurnSuccess_P1Wins_Horizontal() {
    final GameBoard board = new GameBoard('X');
    try {
      board.joinGame();
    } catch (Exception e) {
      Assertions.fail(e);
    }

    try {
      board.playTurn(board.getP1(), 0, 0);
      board.playTurn(board.getP2(), 1, 0);
      board.playTurn(board.getP1(), 0, 1);
      board.playTurn(board.getP2(), 2, 0);
      board.playTurn(board.getP1(), 0, 2);
    } catch (Exception e) {
      Assertions.fail();
    }

    Assertions.assertEquals(1, board.getWinner());
  }

  @Test
  public void testPlayTurnSuccess_P1Wins_Vertical() {
    final GameBoard board = new GameBoard('X');
    try {
      board.joinGame();
    } catch (Exception e) {
      Assertions.fail(e);
    }

    try {
      board.playTurn(board.getP1(), 0, 0);
      board.playTurn(board.getP2(), 0, 1);
      board.playTurn(board.getP1(), 1, 0);
      board.playTurn(board.getP2(), 0, 2);
      board.playTurn(board.getP1(), 2, 0);
    } catch (Exception e) {
      Assertions.fail();
    }

    Assertions.assertEquals(1, board.getWinner());
  }

  @Test
  public void testPlayTurnSuccess_P1Wins_DiagRight() {
    final GameBoard board = new GameBoard('X');
    try {
      board.joinGame();
    } catch (Exception e) {
      Assertions.fail(e);
    }

    try {
      board.playTurn(board.getP1(), 0, 0);
      board.playTurn(board.getP2(), 0, 1);
      board.playTurn(board.getP1(), 1, 1);
      board.playTurn(board.getP2(), 0, 2);
      board.playTurn(board.getP1(), 2, 2);
    } catch (Exception e) {
      Assertions.fail();
    }

    Assertions.assertEquals(1, board.getWinner());
  }

  @Test
  public void testPlayTurnSuccess_P1Wins_DiagLeft() {
    final GameBoard board = new GameBoard('X');
    try {
      board.joinGame();
    } catch (Exception e) {
      Assertions.fail(e);
    }

    try {
      board.playTurn(board.getP1(), 0, 2);
      board.playTurn(board.getP2(), 0, 0);
      board.playTurn(board.getP1(), 1, 1);
      board.playTurn(board.getP2(), 0, 1);
      board.playTurn(board.getP1(), 2, 0);
    } catch (Exception e) {
      Assertions.fail();
    }

    Assertions.assertEquals(1, board.getWinner());
  }

  @Test
  public void testPlayTurnSuccess_P2Wins_Horizontal() {
    final GameBoard board = new GameBoard('X');
    try {
      board.joinGame();
    } catch (Exception e) {
      Assertions.fail(e);
    }

    try {
      board.playTurn(board.getP1(), 1, 1);
      board.playTurn(board.getP2(), 0, 0);
      board.playTurn(board.getP1(), 1, 0);
      board.playTurn(board.getP2(), 0, 1);
      board.playTurn(board.getP1(), 2, 2);
      board.playTurn(board.getP2(), 0, 2);
    } catch (Exception e) {
      Assertions.fail();
    }

    Assertions.assertEquals(2, board.getWinner());
  }

  @Test
  public void testPlayTurnSuccess_P2Wins_Vertical() {
    final GameBoard board = new GameBoard('X');
    try {
      board.joinGame();
    } catch (Exception e) {
      Assertions.fail(e);
    }

    try {
      board.playTurn(board.getP1(), 1, 1);
      board.playTurn(board.getP2(), 0, 0);
      board.playTurn(board.getP1(), 0, 1);
      board.playTurn(board.getP2(), 1, 0);
      board.playTurn(board.getP1(), 0, 2);
      board.playTurn(board.getP2(), 2, 0);
    } catch (Exception e) {
      Assertions.fail();
    }

    Assertions.assertEquals(2, board.getWinner());
  }

  @Test
  public void testPlayTurnSuccess_P2Wins_DiagRight() {
    final GameBoard board = new GameBoard('X');
    try {
      board.joinGame();
    } catch (Exception e) {
      Assertions.fail(e);
    }

    try {
      board.playTurn(board.getP1(), 1, 2);
      board.playTurn(board.getP2(), 0, 0);
      board.playTurn(board.getP1(), 0, 1);
      board.playTurn(board.getP2(), 1, 1);
      board.playTurn(board.getP1(), 0, 2);
      board.playTurn(board.getP2(), 2, 2);
    } catch (Exception e) {
      Assertions.fail();
    }

    Assertions.assertEquals(2, board.getWinner());
  }

  @Test
  public void testPlayTurnSuccess_P2Wins_DiagLeft() {
    final GameBoard board = new GameBoard('X');
    try {
      board.joinGame();
    } catch (Exception e) {
      Assertions.fail(e);
    }

    try {
      board.playTurn(board.getP1(), 1, 2);
      board.playTurn(board.getP2(), 0, 2);
      board.playTurn(board.getP1(), 0, 0);
      board.playTurn(board.getP2(), 1, 1);
      board.playTurn(board.getP1(), 0, 1);
      board.playTurn(board.getP2(), 2, 0);
    } catch (Exception e) {
      Assertions.fail();
    }

    Assertions.assertEquals(2, board.getWinner());
  }

  @Test
  public void testPlayTurnSuccess_Draw_NoOneWins() {
    final GameBoard board = new GameBoard('O');
    try {
      board.joinGame();
    } catch (Exception e) {
      Assertions.fail(e);
    }

    try {
      board.playTurn(board.getP1(), 1, 1);
      board.playTurn(board.getP2(), 0, 0);
      board.playTurn(board.getP1(), 0, 1);
      board.playTurn(board.getP2(), 2, 1);
      board.playTurn(board.getP1(), 1, 0);
      board.playTurn(board.getP2(), 1, 2);
      board.playTurn(board.getP1(), 0, 2);
      board.playTurn(board.getP2(), 2, 0);
      board.playTurn(board.getP1(), 2, 2);
    } catch (Exception e) {
      Assertions.fail();
    }

    Assertions.assertEquals(0, board.getWinner());
    Assertions.assertEquals(true, board.isDraw());
  }

  @Test
  public void testPlayTurnSuccess_Draw_BothWin() {
    final GameBoard board = new GameBoard('O');
    try {
      board.joinGame();
    } catch (Exception e) {
      Assertions.fail(e);
    }

    try {
      board.playTurn(board.getP1(), 1, 1);
      board.playTurn(board.getP2(), 0, 0);
      board.playTurn(board.getP1(), 0, 1);
      board.playTurn(board.getP2(), 2, 1);
      board.playTurn(board.getP1(), 1, 0);
      board.playTurn(board.getP2(), 1, 2);
      board.playTurn(board.getP1(), 0, 2);
      board.playTurn(board.getP2(), 2, 0);
      board.playTurn(board.getP1(), 2, 2);
    } catch (Exception e) {
      Assertions.fail();
    }

    Assertions.assertEquals(0, board.getWinner());
    Assertions.assertEquals(true, board.isDraw());
  }

  @Test
  public void testToJson() {
    final GameBoard board = new GameBoard('X');

    Assertions.assertNotEquals("", board.toJson());
  }

}
