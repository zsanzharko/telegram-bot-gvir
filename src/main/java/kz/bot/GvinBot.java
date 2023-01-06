package kz.bot;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class GvinBot extends TelegramLongPollingBot {
  private final String botUsername;
  private final String botToken;

  public GvinBot(String botUsername, String botToken) {
    this.botUsername = botUsername;
    this.botToken = botToken;
  }

  @Override
  public String getBotUsername() {
    return botUsername;
  }

  @Override
  public String getBotToken() {
    return botToken;
  }

  @Override
  public void onUpdateReceived(Update update) {

  }
}
