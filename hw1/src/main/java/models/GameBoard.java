package models;

import com.google.gson.Gson;

public class GameBoard {

  private Player p1;

  private Player p2;

  private boolean gameStarted;

  private int turn;

  private char[][] boardState;

  private int winner;

  private boolean isDraw;

  /**
   * Constructs a new game board.
   * @param p1 Player 1 object.
   * @param p2 Player 2 object.
   * @param gameStarted True if the game has started, false otherwise.
   * @param turn The player whose turn it is.
   * @param boardState A 2D array representing the board.
   * @param winner The number of the winning player.
   * @param isDraw True if the game results in a draw.
   */
  public GameBoard(Player p1, Player p2, boolean gameStarted, int turn, char[][] boardState,
      int winner, boolean isDraw) {
    super();
    this.p1 = p1;
    this.p2 = p2;
    this.gameStarted = gameStarted;
    this.turn = turn;
    this.boardState = boardState;
    this.winner = winner;
    this.isDraw = isDraw;
  }

  /**
   * Starts a fresh game.
   * @param p1Type Player 1's type.
   */
  public GameBoard(char p1Type) {
    super();
    this.p1 = new Player(type, 1);
    this.gameStarted = false;
    this.turn = 1;
    this.boardState = new char[3][3];
    this.winner = 0;
    this.isDraw = false;
  }

  /**
   * Gets the first player.
   * @return the p1
   */
  public Player getP1() {
    return p1;
  }

  /**
   * Sets the first player.
   */
  public void setP1(Player p1) {
    this.p1 = p1;
  }

  /**
   * Gets the second player.
   * @return the p2
   */
  public Player getP2() {
    return p2;
  }

  /**
   * Sets the second player.
   */
  public void setP2(Player p2) {
    this.p2 = p2;
  }

  /**
   * Checks if the game has started.
   * @return the gameStarted
   */
  public boolean isGameStarted() {
    return gameStarted;
  }
  
  /**
   * Changes whether or not the game is started.
   * @param gameStarted the gameStarted to set
   */
  public void setGameStarted(boolean gameStarted) {
    this.gameStarted = gameStarted;
  }
  
  /**
   * Gets the player ID for whose turn it currently is.
   * @return the turn
   */
  public int getTurn() {
    return turn;
  }
  
  /**
   * Sets the player ID for whose turn it currently is.
   * @param turn the turn to set
   */
  public void setTurn(int turn) {
    this.turn = turn;
  }
  
  /**
   * Gets the board as a 2D array.
   * @return the boardState
   */
  public char[][] getBoardState() {
    return boardState;
  }
  
  /**
   * Sets the 2D array board.
   * @param boardState the boardState to set
   */
  public void setBoardState(char[][] boardState) {
    this.boardState = boardState;
  }
  
  /**
   * Returns the winning player number.
   * @return the winner
   */
  public int getWinner() {
    return winner;
  }
  
  /**
   * Sets the winning player number.
   * @param winner the winner to set
   */
  public void setWinner(int winner) {
    this.winner = winner;
  }
  
  /**
   * Returns true if there is a draw.
   * @return the isDraw
   */
  public boolean isDraw() {
    return isDraw;
  }
  
  /**
   * Sets whether or not there is a draw.
   * @param isDraw the isDraw to set
   */
  public void setDraw(boolean isDraw) {
    this.isDraw = isDraw;
  }
  
  /**
   * Lets a second player join the game, and starts the game.
   * @throws Exception A simple exception thrown if the game has already started and has not ended.
   */
  public void joinGame() throws Exception {
    if (gameStarted && (winner != 0 || isDraw)) {
      throw new Exception("game already started");
    }
    p2 = new Player(p1.getType() == 'X' ? 'O' : 'X', 2);
    gameStarted = true;
  }
  
  /**
   * Plays a turn for the given player in the (x, y) coordinate specified.
   * @param player The player to play the turn for.
   * @param x The x-position.
   * @param y The y-position.
   * @throws Exception A simple exception if the move cannot be made: either
   *         the position is out of bounds, the position is already filled,
   *         or it's not the player's turn.
   */
  public void playTurn(Player player, int x, int y) throws Exception {
    final char type = player.getType();
    if (x < 0 || y < 0 || x > 2 || y > 2) {
      throw new Exception("playTurn: invalid position");
    } else if (boardState[x][y] != '\0') {
      throw new Exception("playTurn: position already filled");
    } else if (player.getId() != turn) {
      throw new Exception("playTurn: not your turn");
    }
    boardState[x][y] = type;
    updateWinner();
  }
  
  /**
   * Updates the Gameboard with the winner and whether or not there is a draw.
   */
  private void updateWinner() {
    boolean p1Wins = false;
    boolean p2Wins = false;
    boolean containsEmpty = false;
    
    int countP1 = 0;
    int countP2 = 0;
    char move = '\0';
    
    // Check horizontally
    for (int i = 0; i < 3; i++) {
      countP1 = 0;
      countP2 = 0;
      for (int j = 0; j < 3; j++) {
        move = boardState[i][j];
        countP1 += move == p1.getType() ? 1 : 0;
        countP2 += move == p2.getType() ? 1 : 0;
        containsEmpty = containsEmpty || move == '\0';
      }
      if (countP1 == 3) {
        p1Wins = true;
      } else if (countP2 == 3) {
        p2Wins = true;
      }
    }
    
    // Check vertically
    for (int i = 0; i < 3; i++) {
      countP1 = 0;
      countP2 = 0;
      for (int j = 0; j < 3; j++) {
        move = boardState[j][i];
        countP1 += move == p1.getType() ? 1 : 0;
        countP2 += move == p2.getType() ? 1 : 0;
      }
      if (countP1 == 3) {
        p1Wins = true;
      } else if (countP2 == 3) {
        p2Wins = true;
      }
    }
    
    // Check diagonally from top-left
    countP1 = 0;
    countP2 = 0;
    for (int i = 0; i < 3; i++) {
      move = boardState[i][i];
      countP1 += move == p1.getType() ? 1 : 0;
      countP2 += move == p2.getType() ? 1 : 0;
    }
    if (countP1 == 3) {
      p1Wins = true;
    } else if (countP2 == 3) {
      p2Wins = true;
    }
    
    // Check diagonally from top-right
    countP1 = 0;
    countP2 = 0;
    for (int i = 0; i < 3; i++) {
      move = boardState[i][2 - i];
      countP1 += move == p1.getType() ? 1 : 0;
      countP2 += move == p2.getType() ? 1 : 0;
    }
    if (countP1 == 3) {
      p1Wins = true;
    } else if (countP2 == 3) {
      p2Wins = true;
    }
    
    if ((p1Wins && p2Wins) || (!p1Wins && !p2Wins && !containsEmpty)) {
      // Draw situation
      setDraw(true);
    } else if (p1Wins) {
      // P1 wins
      setWinner(1);
    } else if (p2Wins) {
      // P2 wins
      setWinner(2);
    } else {
      // Increment the turn
      setTurn(turn == 1 ? 2 : 1);
    }
  }
  
  /**
   * Convert this Board into a JSON string.
   * @return A JSON Board string.
   */
  public String toJson() {
    return new Gson().toJson(this);
  }
  
}
