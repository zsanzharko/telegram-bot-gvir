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
import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceImplTest {
  private static GameServiceImpl gameService;

  @BeforeAll
  public static void initDatasource() throws IOException, SQLException, ClassNotFoundException {
    DatabaseConnector.getInstance(new DataSourceConfiguration().getDatasourceConfig());
    Properties properties = new Properties();
    properties.put("game.players.size", 2);
    gameService = GameServiceImpl.getInstance(properties);
  }

  @Test
  void start() {
    List<Player> players = new ArrayList<>();
    List<GameCard> cards = new ArrayList<>();
    cards.add(new GameCard("Card", "Desc", 10));
    players.add(new Player("Player1", cards));
    players.add(new Player("Player2", cards));
    UUID session = gameService.startSession(players);
    assertNotNull(session);
    GameSession gameSession = gameService.getGameBySession(session);
    assertEquals(2, gameSession.getPlayers().size());
    assertEquals(GameRoundState.NONE, gameSession.getRoundState());
  }

  @Test
  void stop() {
    List<Player> players = new ArrayList<>();
    List<GameCard> cards = new ArrayList<>();
    cards.add(new GameCard("Card", "Desc", 10));
    players.add(new Player("Player1", cards));
    players.add(new Player("Player2", cards));
    UUID session = gameService.startSession(players);
    assertNotNull(session);
    gameService.stopSession(session);
    assertNull(gameService.getGameBySession(session));
    players.forEach(player -> assertEquals(PlayerState.NONE, player.getState()));
  }
}