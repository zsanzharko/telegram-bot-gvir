package kz.service.game;

import kz.exception.game.GameStartException;
import kz.pojo.Player;

import java.util.List;
import java.util.UUID;

public interface GameService {

  UUID startSession(List<Player> players) throws GameStartException;

  void stopSession(UUID session);
}
