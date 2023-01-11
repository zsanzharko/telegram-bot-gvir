package kz.service.game.statistic;

import kz.pojo.Player;

import java.util.Map;

public interface SendGameStatistics {
  void send(Map<Player, Map<GameStatisticsState, String>> statistic);
}
