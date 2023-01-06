package kz;

import kz.bot.GvinBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
  public static void main(String[] args) throws IOException, TelegramApiException {
    InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("telegram_bot_conf.properties");
    Properties properties = new Properties();
    properties.load(inputStream);

    String botUsername = properties.getProperty("gvin.username");
    String botToken = properties.getProperty("gvin.token");

    TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
    telegramBotsApi.registerBot(new GvinBot(botUsername, botToken));
  }
}