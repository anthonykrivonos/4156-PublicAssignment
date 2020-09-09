package models;

public class Player {

  private char type;

  private int id;

  /**
   * Constructs a new player.
   * @param type The character the player is using ('X' or 'O').
   * @param id The unique identifier of the player.
   */
  public Player(char type, int id) {
    super();
    this.type = type;
    this.id = id;
  }

}
