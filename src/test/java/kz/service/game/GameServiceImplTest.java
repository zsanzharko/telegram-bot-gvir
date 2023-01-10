package kz.service.game;

import kz.exception.game.GameStartException;
import kz.pojo.GameCard;
import kz.pojo.Player;
import kz.pojo.PlayerState;
import kz.service.game.session.GameRoundState;
import kz.service.game.session.GameSession;
import kz.utils.DataSourceConfiguration;
import kz.utils.DatabaseConnector;
import kz.utils.FileUtils;
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
    Properties properties = FileUtils.getPropertiesFromFile("game-rule.properties");
    gameService = GameServiceImpl.getInstance(properties);
  }

  @Test
  void startWithEmptyCards() {
    List<Player> players = new ArrayList<>();
    List<GameCard> cards = new ArrayList<>();
    // add two players
    players.add(new Player("Player1", cards));
    players.add(new Player("Player2", cards));
    // get game session if session will start
    assertThrows(GameStartException.class, () -> gameService.startSession(players));
  }

  @Test
  void startWithOnePlayer() {
    List<Player> players = new ArrayList<>();
    List<GameCard> cards = new ArrayList<>();
    cards.add(new GameCard("Card", "Desc", 10));
    cards.add(new GameCard("Card", "Desc", 10));
    cards.add(new GameCard("Card", "Desc", 10));
    cards.add(new GameCard("Card", "Desc", 10));
    cards.add(new GameCard("Card", "Desc", 10));
    cards.add(new GameCard("Card", "Desc", 10));
    // add two players
    players.add(new Player("Player1", cards));
    // get game session if session will start
    assertThrows(GameStartException.class, () -> gameService.startSession(players));
  }

  @Test
  void startWithNull() {
    assertThrows(GameStartException.class, () ->
            gameService.startSession(null));
  }

  @Test
  void start() {
    List<Player> players = new ArrayList<>();
    List<GameCard> cards = new ArrayList<>();
    // add cards
    cards.add(new GameCard("Card", "Desc", 10));
    // add two players
    players.add(new Player("Player1", cards));
    players.add(new Player("Player2", cards));
    // get game session if session will start
    UUID session = gameService.startSession(players);
    assertNotNull(session);
    // get GameSession by session
    GameSession gameSession = gameService.getGameBySession(session);
    assertEquals(2, gameSession.getPlayers().size());
    assertEquals(GameRoundState.NONE, gameSession.getRoundState());
  }

  @Test
  void startGameAndGetGameSessionObjectBySession() {
    List<Player> players = new ArrayList<>();
    List<GameCard> cards = new ArrayList<>();
    // add cards
    cards.add(new GameCard("Card", "Desc", 10));
    // add two players
    players.add(new Player("Player1", cards));
    players.add(new Player("Player2", cards));
    // get game session if session will start
    UUID session = gameService.startSession(players);
    assertNotNull(session);
    // get GameSession by session
    assertNotNull(gameService.getGameBySession(session));
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