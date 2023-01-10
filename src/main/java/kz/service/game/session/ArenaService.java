package kz.service.game.session;

import kz.pojo.GameCard;
import kz.pojo.Player;

import java.util.List;
import java.util.Map;

public interface ArenaService {

  GameCard addCard(Player player, Integer row, GameCard card);
  boolean removeCard(Player player, Integer row, GameCard card);

  Map<Player, Map<String, String>> getStatistics();

  Map<Integer, List<GameCard>> getArena(Player player);

  List<GameCard> getArena(Player player, int row);
}
