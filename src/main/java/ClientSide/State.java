package ClientSide;

public interface State {

    void handleInput(Client client, String input);
    void executeStateAction(Client client);


}
