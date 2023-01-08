package kz.service.game.session;

import kz.pojo.GameCard;
import kz.pojo.Player;

import java.util.Map;

public interface ArenaService {

  void addCard(Player player, Integer row, GameCard card);
  void removeCard(Player player, Integer row, GameCard card);

  Map<Player, Map<String, String>> getStatistics();
}
