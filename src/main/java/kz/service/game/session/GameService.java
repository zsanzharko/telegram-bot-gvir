package kz.service.game.session;

import kz.pojo.Player;

import java.util.List;
import java.util.UUID;

public interface GameService {

  UUID startSession(List<Player> players) throws Exception;

  void stopSession(UUID session);
}
