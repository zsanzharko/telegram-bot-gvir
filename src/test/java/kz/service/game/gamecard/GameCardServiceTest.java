package kz.service.game.gamecard;

import kz.utils.DataSourceConfiguration;
import kz.utils.DatabaseConnector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GameCardServiceTest {
  @BeforeAll
  public static void initDatasource() throws IOException, SQLException, ClassNotFoundException {
    DatabaseConnector.getInstance(new DataSourceConfiguration().getDatasourceConfig());
  }

  @Test
  void createInstance() throws SQLException {
    GameCardService service = GameCardService.getInstance();

    assertNotNull(service);
  }

  @Test
  void createInstanceWithCustomManager() {
    GameCardManager cardManager = new GameCardManager(List.of());
    GameCardService service = GameCardService.getInstance(cardManager);

    assertNotNull(service);
    assertNotEquals(0, service.getCards().size());
  }

  @Test
  void assertionInitCache() throws SQLException {
    GameCardService service = GameCardService.getInstance();

    assertNotNull(service);
    assertNotEquals(0, service.getCards().size());
  }
}