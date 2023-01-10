package kz.service.game.session;

import kz.pojo.GameCard;
import kz.pojo.Player;
import kz.pojo.PlayerState;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Provide full game control on session with players.
 *
 * @author Sanzhar
 * @see kz.service.game.GameServiceImpl
 */
@Slf4j
@Getter
public class GameSession implements GameSessionService {
  @Setter
  private GameRoundState roundState;
  private Map<Player, List<GameCard>> playersWithCard;
  private GameArena gameArena;

  public GameSession(List<Player> players, Properties gameRuleProperties) {
    this.playersWithCard = setCardFromCardDeckPlayers(players);
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
  private Map<Player, List<GameCard>> setCardFromCardDeckPlayers(List<Player> players) {
    // FIXME: 1/8/2023 Add rule to get cards from card deck.
    Map<Player, List<GameCard>> playerListMap = new HashMap<>(players.size());
    players.forEach(player -> playerListMap.put(player, player.getCardDeck()));
    return playerListMap;
  }


  public Set<Player> getPlayers() {
    return new HashSet<>(playersWithCard.keySet());
  }

  /**
   * Session will stop on session side. Players will get set None.
   * And show full information about game sessions.
   */
  @Override
  public void stopSession() {
    roundState = GameRoundState.NONE;
    Map<Player, Map<String, String>> statistics = gameArena.getStatistics();
    playersWithCard.keySet().forEach(p -> p.setState(PlayerState.NONE));
    playersWithCard = null;
    gameArena = null;
    log.info(statistics.toString());
    // TODO: 1/8/2023 Finish game for to players and notify
    //  with him from take instance object
  }
}
