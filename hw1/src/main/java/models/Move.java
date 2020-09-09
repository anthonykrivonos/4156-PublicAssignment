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

}
