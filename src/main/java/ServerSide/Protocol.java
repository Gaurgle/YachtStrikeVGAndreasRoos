package ServerSide;

public class Protocol {
    private Player player1;
    private Player player2;
    private Player currentPlayer;

    public Protocol(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        currentPlayer = player1;
    }
}
