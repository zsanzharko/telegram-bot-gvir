package kz.service.game.session;

import kz.pojo.GameCard;
import kz.pojo.Player;
import kz.pojo.PlayerState;
import kz.service.game.statistic.GameStatisticsState;
import kz.service.game.statistic.SendGameStatistics;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Provide full game control on session with players.
 *
 * @author Sanzhar
 * @see GameServiceImpl
 */
@Slf4j
@Getter
public class GameSession implements ArenaService {
  @Setter
  private GameRoundState roundState;
  private Map<Player, List<GameCard>> playersWithCard;
  private GameArena gameArena;
  private final Properties gameRuleProperties;

  public GameSession(List<Player> players, Properties gameRuleProperties) throws Exception {
    this.gameRuleProperties = gameRuleProperties;
    this.playersWithCard = initCardFromCardDeckPlayers(players);
    if (this.playersWithCard == null) {
      throw new Exception("Can init cards on players");
    }
    this.gameArena = new GameArena(players, gameRuleProperties);
    this.roundState = GameRoundState.NONE;
  }

  /**
   * Put cards from players card deck. Have limit size to put on card play.
   * This method will be balance cards on players, that a game need to be normal.
   *
   * @param players for get cards from card deck
   * @return playable cards on current session game
   */
  private Map<Player, List<GameCard>> initCardFromCardDeckPlayers(List<Player> players) {
    int maxCardOnSession = Integer.parseInt(gameRuleProperties
            .getProperty("game.cards.init.size"));
    if (!correctCardSizeForGame(maxCardOnSession, players)) return null;
    Random random = new Random();
    boolean cardsIsBalanced = false;
    Map<Player, List<GameCard>> preparationCardsPlayers = new HashMap<>(players.size());
    while (!cardsIsBalanced) {
      preparationCardsPlayers.clear();
      for (Player player : players) {
        preparationCardsPlayers.put(player, new LinkedList<>());
        for (int cardIndex = 0; cardIndex < maxCardOnSession; cardIndex++) {
          preparationCardsPlayers.get(player).add(player
                  .getCardDeck()
                  .get(random.nextInt(maxCardOnSession)));
        }
      }
      List<Integer> sumPowersOnPlayers = new ArrayList<>(players.size());
      preparationCardsPlayers.forEach((player, cards) -> sumPowersOnPlayers.add(cards.stream()
              .map(GameCard::getPower)
              .max(Integer::sum)
              .orElse(-1)));
      // Example
      // player 1     player 2
      //  52            51 == true  3
      //  55            52
      //  52            55
      //  60            50 == false
      //  | a
      // [52, 50, 50, 53]
      //      | b
      // [52, 50, 50, 53]
      int a;
      int b;
      int result;
      for (int i = 0; i < sumPowersOnPlayers.size(); i++) {
        for (int j = 1; j < sumPowersOnPlayers.size(); j++) {
          a = sumPowersOnPlayers.get(i);
          b = sumPowersOnPlayers.get(j);
          result = Math.abs(a - b);
          if (!(result >= 0 && result <= 3)) {
            cardsIsBalanced = false;
            break;
          }
          cardsIsBalanced = true;
        }
        if (!cardsIsBalanced) break;
      }
    }
    return preparationCardsPlayers;
  }

  private boolean correctCardSizeForGame(int maxCardOnSession, List<Player> players) {
    for (Player player : players) {
      if (!(player.getCardDeck().size() >= maxCardOnSession)) {
        return false;
      }
    }
    return true;
  }

  public Set<Player> getPlayers() {
    return new HashSet<>(playersWithCard.keySet());
  }

  void stopSession() {
    if (roundState == GameRoundState.NONE) {
      playersWithCard.keySet().forEach(p -> p.setState(PlayerState.NONE));
      playersWithCard = null;
      gameArena = null;
    }
  }

  @Override
  public GameCard addCard(Player player, Integer row, GameCard card) {
    if (notExist(player) || invalidCard(player, card)) {
      return null;
    }
    return gameArena.addCard(player, row, card);
  }

  @Override
  public boolean removeCard(Player player, Integer row, GameCard card) {
    if (notExist(player) || invalidCard(player, card)) {
      return false;
    }
    return gameArena.removeCard(player, row, card);
  }

  /**
   * Get information about a current on game arena.
   * This method send statistics to implemented services
   * like server side or mobile app
   *
   * @param sender send statistics to implemented services
   */
  public void sendStatistics(SendGameStatistics sender) {
    Map<Player, Map<GameStatisticsState, String>> playersStatistics = getStatistics();

    sender.send(playersStatistics);
  }

  @Override
  public Map<Player, Map<GameStatisticsState, String>> getStatistics() {
    return gameArena.getStatistics();
  }

  @Override
  public Map<Integer, List<GameCard>> getArenaFromPlayer(Player player) {
    if (notExist(player)) {
      return null;
    }
    return gameArena.getArena(player);
  }

  @Override
  public List<GameCard> getArenaFromPlayer(Player player, int row) {
    if (notExist(player)) {
      return null;
    }
    return gameArena.getArena(player, row);
  }

  private boolean notExist(Player player) {
    return !gameArena.getPlayers().contains(player)
            || !gameArena.getArena().containsKey(player)
            || player.getState() != PlayerState.IN_GAME;
  }

  private boolean invalidCard(Player player, GameCard card) {
    return !playersWithCard.containsKey(player)
            || !playersWithCard.get(player).contains(card);
  }
}
