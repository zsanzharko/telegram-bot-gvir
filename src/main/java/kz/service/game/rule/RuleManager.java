package kz.service.game.rule;

import kz.pojo.Player;

public class RuleManager {

  public static boolean prepareToGameSession(Player player) {
    return new PreparationPlayerGameRule(player).rule();
  }
}
