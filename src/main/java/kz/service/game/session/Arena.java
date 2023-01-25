package kz.service.game.session;

import kz.pojo.GameCard;
import kz.pojo.Player;
import kz.service.game.statistic.GameStatisticsState;

import java.util.List;
import java.util.Map;

public abstract class Arena {

  abstract protected GameCard addCard(Player player, Integer row, GameCard card);
  abstract protected boolean removeCard(Player player, Integer row, GameCard card);

  abstract protected Map<Player, Map<GameStatisticsState, String>> getStatistics();

  abstract Map<Integer, List<GameCard>> getArena(Player player);

  abstract List<GameCard> getArena(Player player, int row);
}
