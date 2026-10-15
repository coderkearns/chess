package ui;

public interface UI {

    public void print(String msg);

    public void printError(String msg);

    public String getCommand(String currentState);
}
