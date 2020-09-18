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

  /**
   * 'X' or 'O' for the player.
   * @return The character the player uses in the game.
   */
  public char getType() {
    return type;
  }

  /**
   * Sets 'X' or 'O' for the player.
   * @param type The character the player uses in the game.
   */
  public void setType(char type) {
    this.type = type;
  }

  /**
   * Gets the player's ID (1 or 2).
   * @return The numerical Player ID.
   */
  public int getId() {
    return id;
  }

  /**
   * Sets the player's ID.
   * @param id The numerical Player ID.
   */
  public void setId(int id) {
    this.id = id;
  }

}
