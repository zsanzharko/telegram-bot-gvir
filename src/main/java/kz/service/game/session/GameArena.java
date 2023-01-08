package kz.service.game.session;

import kz.pojo.GameCard;
import kz.pojo.Player;

import java.util.*;

public class GameArena implements ArenaService {
  private final List<Player> players;
  private Map<Player, Map<Integer, List<GameCard>>> arena;

  public GameArena(List<Player> players) {
    this.players = players;

    initArena();
  }

  public void initArena() {
    this.arena = new HashMap<>(players.size());
    for (Player player : players) {
      this.arena.put(player, new HashMap<>());
    }
  }

  @Override
  public void addCard(Player player, Integer row, GameCard card) {
    // TODO: 1/8/2023 add filter rules
    Map<Integer, List<GameCard>> playerArena = arena.get(player);
    playerArena.computeIfAbsent(row, k -> new LinkedList<>());
    playerArena.get(row).add(card);
  }

  @Override
  public void removeCard(Player player, Integer row, GameCard card) {
    // TODO: 1/8/2023 add filter rules
    Map<Integer, List<GameCard>> playerArena = arena.get(player);
    playerArena.get(row).remove(card);
  }

  @Override
  public Map<Player, Map<String, String>> getStatistics() {
    Map<Player, Map<String, String>> statistics = new HashMap<>(players.size());
    players.forEach(player -> {
      Map<String, String> playerStatistics = new HashMap<>();
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
      playerStatistics.put("Max Value", Integer.toString(maxPower));
      statistics.put(player, playerStatistics);
    });

    return statistics;
  }

}
