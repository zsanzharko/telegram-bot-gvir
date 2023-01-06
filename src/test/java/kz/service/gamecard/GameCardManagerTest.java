package kz.service.gamecard;

import kz.pojo.GameCard;
import kz.utils.DataSourceConfiguration;
import kz.utils.DatabaseConnector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GameCardManagerTest {

  @BeforeAll
  public static void initDatasource() throws IOException, SQLException, ClassNotFoundException {
    DatabaseConnector.getInstance(new DataSourceConfiguration().getDatasourceConfig());
  }

  @Test
  void createObject() throws SQLException {
    GameCardManager cardManager = new GameCardManager();
    assertNotNull(cardManager);
  }

  @Test
  void getCardByTitle() {
    List<GameCard> testCards = List.of(
            new GameCard("test-1", "", 0),
            new GameCard("test-2", "", 0),
            new GameCard("test-3", "", 0),
            new GameCard("test-4", "", 0)
    );
    GameCardManager cardManager = new GameCardManager(testCards);

    Optional<GameCard> card = cardManager.getCardByTitle("test-3");

    assertTrue(card.isPresent());
    assertEquals("test-3", card.get().getTitle());

    card = cardManager.getCardByTitle("test-5");

    assertFalse(card.isPresent());
  }

  @Test
  void getCards() throws SQLException {
    GameCardManager cardManager = new GameCardManager();
    assertNotNull(cardManager);
    assertNotNull(cardManager.getCards());
    assertNotEquals(0, cardManager.getCards().size());
  }
}