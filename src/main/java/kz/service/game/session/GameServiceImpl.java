package kz.service.game.session;

import kz.exception.game.GameException;
import kz.exception.game.GameStartException;
import kz.pojo.Player;
import kz.pojo.PlayerState;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.*;

import static kz.service.game.rule.RuleManager.prepareToGameSession;

@Slf4j
public class GameServiceImpl implements GameService {
  private static GameServiceImpl service;
  private final Map<UUID, GameSession> uuidGameSessionMap;
  private final Properties gameProperties;

  private GameServiceImpl(Properties gameProperties) {
    this.uuidGameSessionMap = new HashMap<>();
    this.gameProperties = gameProperties;
  }

  @Override
  public UUID startSession(List<Player> players) {
    if (players == null) {
      throw new GameStartException("Players can not be null");
    } else if (!Objects.equals(gameProperties.get("session.players"), String.valueOf(players.size()))) {
      // TODO: 1/25/2023 need to check how to except method
      throw new GameStartException("Players is not be compatibility size player on game",
              new GameException("Have problem with connect service... Check rule connect player." +
                      " Also check waiting room"));
    }
    log.debug("Players size: " + players.size());
    final UUID session = UUID.randomUUID();
    // check preparation for start playing game
    log.debug("Game properties");
    log.debug(gameProperties.toString());
    for (Player player : players) {
      if (!(prepareToGameSession(player))) {
        log.debug(String.format("Can't prepared player %s to this game", player));
        // FIXME: 1/9/2023 Take interface and send information about can't start game
        //  cause player is not prepared. and remove exception if problem on player side.
        throw new GameStartException("Can't start cause preparation is been successfully.");
      }
    }
    // set players on state in game
    players.forEach(player -> player.setState(PlayerState.IN_GAME));
    log.debug("Adding players to game session");
    GameSession gameSession;
    try {
      gameSession = new GameSession(players, gameProperties);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    uuidGameSessionMap.put(session, gameSession);
    return session;
  }

  @Override
  public void stopSession(@NotNull UUID uuidSession) {
    GameSession gameSession = uuidGameSessionMap.get(uuidSession);
    if (gameSession != null) {
      gameSession.stopSession();
      uuidGameSessionMap.remove(uuidSession);
    }
  }

  public GameSession getGameBySession(@NotNull UUID session) {
    return uuidGameSessionMap.get(session);
  }

  public static GameServiceImpl getInstance(@NotNull Properties gameProperties) {
    if (service == null) {
      service = new GameServiceImpl(gameProperties);
    }
    return service;
  }
}
