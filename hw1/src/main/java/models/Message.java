package models;

public class Message {

  private boolean moveValidity;

  private int code;

  private String message;

  /**
   * Constructs a new game message.
   * @param moveValidity True if the move is valid, false otherwise.
   * @param code The move's code.
   * @param message A string message associated with the move.
   */
  public Message(boolean moveValidity, int code, String message) {
    super();
    this.moveValidity = moveValidity;
    this.code = code;
    this.message = message;
  }

}
