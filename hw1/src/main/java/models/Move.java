package models;

public class Move {

  private Player player;

  private int moveX;

  private int moveY;

  /**
   * Constructs a move object.
   * @param player The player associated with the move.
   * @param moveX The horizontal position of the move.
   * @param moveY The vertical position of the move.
   */
  public Move(Player player, int moveX, int moveY) {
    super();
    this.player = player;
    this.moveX = moveX;
    this.moveY = moveY;
  }

  /**
   * Gets the player who made this move.
   * @return A Player object.
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Sets the player who made this move.
   * @param player A Player object.
   */
  public void setPlayer(Player player) {
    this.player = player;
  }

  /**
   * Gets the x-coordinate of this move.
   * @return The x-coordinate as an integer.
   */
  public int getMoveX() {
    return moveX;
  }

  /**
   * Sets the x-coordinate of this move.
   * @param moveX The x-coordinate as an integer.
   */
  public void setMoveX(int moveX) {
    this.moveX = moveX;
  }

  /**
   * Gets the y-coordinate of this move.
   * @return The y-coordinate as an integer.
   */
  public int getMoveY() {
    return moveY;
  }

  /**
   * Sets the y-coordinate of this move.
   * @param moveY The y-coordinate as an integer.
   */
  public void setMoveY(int moveY) {
    this.moveY = moveY;
  }

}
