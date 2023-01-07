package kz.service.game;

import kz.exception.game.GameStartException;
import kz.pojo.Player;
import kz.pojo.PlayerState;
import kz.service.game.gamecard.GameCardService;
import kz.service.game.session.GameSession;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameServiceImpl implements GameService {
  private static GameServiceImpl service;
  private final GameCardService cardService;
  private final Map<UUID, GameSession> gameSessions;

  private GameServiceImpl() throws SQLException {
    this.cardService = GameCardService.getInstance();
    this.gameSessions = new HashMap<>();
  }

  @Override
  public UUID startSession(Player player1, Player player2) {
    final UUID session = UUID.randomUUID();
    if (player1 == null || player2 == null) {
      throw new GameStartException("Can't start cause players is empty");
    } else if (player1.getCardDeck().isEmpty() || player1.getCardDeck().isEmpty()) {
      throw new GameStartException("Can't start cause cards is empty");
    }
    // set state on GAME
    player1.setState(PlayerState.IN_GAME);
    player2.setState(PlayerState.IN_GAME);

    gameSessions.put(session, new GameSession(player1, player2));
    return session;
  }

  @Override
  public void stopSession(UUID session) {
    GameSession gameSession = gameSessions.get(session);
    gameSession.getPlayer1().setState(PlayerState.NONE);
    gameSession.getPlayer2().setState(PlayerState.NONE);
    gameSessions.remove(session);
  }

  public GameSession getGameBySession(UUID session) {
    // TODO: 1/7/2023 If session is null throw game exception
    return gameSessions.get(session);
  }

  public static GameServiceImpl getInstance() throws SQLException {
    if (service == null) {
      service = new GameServiceImpl();
    }
    return service;
  }
}
