package kz.service.game;

import kz.exception.game.GameException;
import kz.exception.game.GameStartException;
import kz.pojo.Player;
import kz.pojo.PlayerState;
import kz.service.game.gamecard.GameCardService;
import kz.service.game.session.GameSession;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.*;

import static kz.service.game.rule.RuleManager.prepareToGameSession;

@Slf4j
public class GameServiceImpl implements GameService {
  private static GameServiceImpl service;
  private final GameCardService cardService;
  private final Map<UUID, GameSession> gameSessions;
  private final Properties gameProperties;

  private GameServiceImpl(Properties gameProperties) throws SQLException {
    this.cardService = GameCardService.getInstance();
    this.gameSessions = new HashMap<>();
    this.gameProperties = gameProperties;
  }

  @Override
  public UUID startSession(List<Player> players) {
    if (players == null) {
      throw new GameStartException("Players can not be null");
    } else if (!Objects.equals(gameProperties.get("session.players"), String.valueOf(players.size()))) {
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
    gameSessions.put(session, new GameSession(players, gameProperties));
    return session;
  }

  @Override
  public void stopSession(UUID session) {
    gameSessions.get(session).stopSession();
    gameSessions.remove(session);
  }

  public GameSession getGameBySession(UUID session) {
    // TODO: 1/7/2023 If session is null throw game exception
    return gameSessions.get(session);
  }

  public static GameServiceImpl getInstance(Properties gameProperties) throws SQLException {
    if (service == null) {
      service = new GameServiceImpl(gameProperties);
    }
    return service;
  }
}
