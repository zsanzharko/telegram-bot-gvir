package kz.service.gamecard;

import kz.pojo.GameCard;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class GameCardService {
  private static GameCardService service;
  private final GameCardManager cardManager;

  private GameCardService() throws SQLException {
    cardManager = new GameCardManager();
  }

  private GameCardService(GameCardManager cardManager) {
    this.cardManager = cardManager;
  }

  public List<GameCard> getCards() {
    return cardManager.getCards();
  }

  public static GameCardService getInstance() throws SQLException {
    if (service == null) {
      service = new GameCardService();
      log.info("Created Game Card Service.");
    }
    return service;
  }

  public static GameCardService getInstance(GameCardManager cardManager) {
    if (service == null) {
      service = new GameCardService(cardManager);
      log.info("Created Game Card Service.");
    }
    return service;
  }
}
