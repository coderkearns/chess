package service;

import dataaccess.GameDAO;

public class GameService {
    final GameDAO gameDAO;

    public GameService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public void clear() {
        gameDAO.clear();
    }
}
