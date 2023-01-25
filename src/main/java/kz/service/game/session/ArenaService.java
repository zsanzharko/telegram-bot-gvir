package kz.service.game.session;

import kz.pojo.GameCard;
import kz.pojo.Player;
import kz.service.game.statistic.GameStatisticsState;

import java.util.List;
import java.util.Map;

public interface ArenaService {

  GameCard addCard(Player player, Integer row, GameCard card);
  boolean removeCard(Player player, Integer row, GameCard card);

  Map<Player, Map<GameStatisticsState, String>> getStatistics();

  Map<Integer, List<GameCard>> getArenaFromPlayer(Player player);

  List<GameCard> getArenaFromPlayer(Player player, int row);
}
