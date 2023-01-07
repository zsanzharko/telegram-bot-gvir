package kz.exception.game;

public class GameStartException extends GameException {
  public GameStartException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public GameStartException(String message) {
    super(message);
  }
}
