package kz.service.game;

import kz.pojo.GameCard;
import kz.pojo.Player;
import kz.pojo.PlayerState;
import kz.service.game.session.GameRoundState;
import kz.service.game.session.GameSession;
import kz.utils.DataSourceConfiguration;
import kz.utils.DatabaseConnector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceImplTest {

  @BeforeAll
  public static void initDatasource() throws IOException, SQLException, ClassNotFoundException {
    DatabaseConnector.getInstance(new DataSourceConfiguration().getDatasourceConfig());
  }

  @Test
  void start() throws SQLException {
    List<GameCard> cards = new ArrayList<>();
    cards.add(new GameCard("Card", "Desc", 10));

    Player player1 = new Player("Player1", cards);
    Player player2 = new Player("Player2", cards);
    GameServiceImpl gameService = GameServiceImpl.getInstance();

    UUID session = gameService.startSession(player1, player2);

    assertNotNull(session);
    GameSession gameSession = gameService.getGameBySession(session);

    assertEquals(player1, gameSession.getPlayer1());
    assertEquals(player2, gameSession.getPlayer2());
    assertEquals(GameRoundState.NONE, gameSession.getRoundState());
  }

  @Test
  void stop() throws SQLException {
    List<GameCard> cards = new ArrayList<>();
    cards.add(new GameCard("Card", "Desc", 10));
    Player player1 = new Player("Player1", cards);
    Player player2 = new Player("Player2", cards);

    GameServiceImpl gameService = GameServiceImpl.getInstance();

    UUID session = gameService.startSession(player1, player2);

    assertNotNull(session);

    gameService.stopSession(session);

    assertNull(gameService.getGameBySession(session));
    assertEquals(PlayerState.NONE, player1.getState());
    assertEquals(PlayerState.NONE, player2.getState());
  }
}