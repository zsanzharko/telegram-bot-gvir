package kz.service.gamecard;

import kz.pojo.GameCard;
import kz.utils.DatabaseConnector;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class GameCardManager {
  private List<GameCard> cards;

  protected GameCardManager() throws SQLException {
    log.info("Creating Game Card Manager....");
    initCards();
  }

  protected GameCardManager(List<GameCard> cards) {
    log.info("Creating Game Card Manager....");
    this.cards = cards;
  }

  private void initCards() throws SQLException {
    DatabaseConnector connector = DatabaseConnector.getService();
    cards = new LinkedList<>();
    Statement statement = connector.getConnection().createStatement();
    ResultSet sqlResultCards = statement.executeQuery("select * from game.cards");
    while (sqlResultCards.next()) {
      cards.add(new GameCard(
              sqlResultCards.getString("title"),
              sqlResultCards.getString("description"),
              sqlResultCards.getInt("power")
      ));
    }
    statement.close();
  }

  public List<GameCard> getCards() {
    return new LinkedList<>(cards);
  }

  public Optional<GameCard> getCardByTitle(String title) {
    return cards.stream()
            .filter(gameCard -> gameCard.getTitle().equals(title))
            .findFirst();
  }
}
