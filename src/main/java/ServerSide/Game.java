package ServerSide;

public class Game extends Thread {
    private Player player1;
    private Player player2;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1.setOpponent(player2);
        this.player2.setOpponent(player1);
    }

    public void run() {
        try {
            Protocol protocol = new Protocol(player1, player2);
        } catch (Exception e) {
            System.out.println("Exception in run method.");
        } finally {
            player1.closeConnection();
            player2.closeConnection();
        }
    }
}


