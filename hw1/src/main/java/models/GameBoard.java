package models;

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
   * @param turn The number of turns taken.
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

}
