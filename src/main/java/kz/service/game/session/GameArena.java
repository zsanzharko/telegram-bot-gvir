package kz.service.game.session;

import kz.pojo.GameCard;
import kz.pojo.Player;
import kz.service.game.statistic.GameStatisticsState;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;
import java.util.*;

/**
 * Control arena on game. Need for Game Session
 *
 * @author Sanzhar
 * @see GameSession
 */
@Getter
@Immutable
public class GameArena extends Arena {
  private final List<Player> players;
  private Map<Player, Map<Integer, List<GameCard>>> arena;
  private final Properties arenaRuleProperties;

  public GameArena(List<Player> players, Properties arenaRuleProperties) {
    super();
    this.arenaRuleProperties = arenaRuleProperties;
    this.players = players;
    initArena();
  }

  public void initArena() {
    this.arena = new HashMap<>(players.size());
    for (Player player : players) {
      Map<Integer, List<GameCard>> playerArena = new HashMap<>();
      int initNumberOfRows = Integer.parseInt(arenaRuleProperties.getProperty("session.arena.rows"));
      for (int row = 0; row < initNumberOfRows; row++) {
        playerArena.put(row, new LinkedList<>());
      }
      this.arena.put(player, playerArena);
    }
  }

  @Override
  protected GameCard addCard(Player player, Integer row, GameCard card) {
    if (invalidRow(row)) return null;
    Map<Integer, List<GameCard>> playerArena = arena.get(player);
    playerArena.computeIfAbsent(row, k -> new LinkedList<>());
    playerArena.get(row).add(card);
    return card;
  }

  @Override
  protected boolean removeCard(Player player, Integer row, GameCard card) {
    if (invalidRow(row)) return false;
    Map<Integer, List<GameCard>> playerArena = arena.get(player);
    return playerArena.get(row).remove(card);
  }

  @Override
  public Map<Player, Map<GameStatisticsState, String>> getStatistics() {
    Map<Player, Map<GameStatisticsState, String>> statistics = new HashMap<>(players.size());
    players.forEach(player -> {
      Map<GameStatisticsState, String> playerStatistics = new HashMap<>();
      Map<Integer, List<GameCard>> playerArena = arena.get(player);
      int maxPower = 0;
      for (List<GameCard> cards : playerArena.values()) {
        Optional<Integer> cardPowerSum = cards.stream()
                .map(GameCard::getPower)
                .max(Integer::sum);
        if (cardPowerSum.isPresent()) {
          maxPower += cardPowerSum.get();
        }
      }
      playerStatistics.put(GameStatisticsState.PLAYER_POWER, Integer.toString(maxPower));
      statistics.put(player, playerStatistics);
    });
    return statistics;
  }

  @Override
  public Map<Integer, List<GameCard>> getArena(Player player) {
    return arena.get(player);
  }

  @Override
  public List<GameCard> getArena(Player player, int row) {
    if (row >=0 && row < Integer.parseInt(arenaRuleProperties.getProperty("session.arena.rows"))) {
      Map<Integer, List<GameCard>> playerArena = arena.get(player);
      return playerArena.get(row);
    }
    return null;
  }



  private boolean invalidRow(Integer row) {
    return row < 0 || row >= Integer.parseInt(arenaRuleProperties.getProperty("session.arena.rows"));
  }
}
