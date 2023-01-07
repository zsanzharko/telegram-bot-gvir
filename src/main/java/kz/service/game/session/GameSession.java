package kz.service.game.session;

import kz.pojo.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameSession {
  private final Player player1;
  private final Player player2;
  private GameRoundState roundState;

  public GameSession(Player player1, Player player2) {
    this.player1 = player1;
    this.player2 = player2;
    this.roundState = GameRoundState.NONE;
  }


}
