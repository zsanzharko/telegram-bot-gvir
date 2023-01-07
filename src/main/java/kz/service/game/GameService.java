package kz.service.game;

import kz.pojo.Player;

import java.util.UUID;

public interface GameService {

  UUID startSession(Player player1, Player player2);

  void stopSession(UUID session);
}
