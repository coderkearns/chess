package ui;

import client.ServerFacade;

import java.util.List;

public interface UI {

    public void print(String msg);

    public void printError(String msg);

    public void printGameList(List<ServerFacade.GameResponse> games);

    public String getCommand(String currentState);
}
