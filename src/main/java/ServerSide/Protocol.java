package ServerSide;

public class Protocol {
    private Player player1;
    private Player player2;
    private Player currentPlayer;

    public Protocol(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        currentPlayer = player1;

        player1.sendToClient("ALLOW_SELECT_PRESET");

        String preset1 = player1.receieveFromClient();
        System.out.println(preset1.split(":")[0]);

        player2.sendToClient("ALLOW_SELECT_PRESET");
        String preset2 = player2.receieveFromClient();

        System.out.println(preset2.split(":")[0]);

        boolean gameActive = true;
        while (gameActive) {
            boolean hit = true;
            while (hit) {
                currentPlayer.sendToClient("ALLOW_SHOT");
                String shot = currentPlayer.receieveFromClient();

                System.out.println("shot at: " + shot);

                currentPlayer.getOpponent().sendToClient("CHECK_SHOT:" + shot);

                hit = Boolean.parseBoolean(currentPlayer.getOpponent().receieveFromClient());
                currentPlayer.sendToClient("SEND_HIT_STATUS:" + hit);
                gameActive = Boolean.parseBoolean(currentPlayer.getOpponent().receieveFromClient());
                if (!gameActive){
                    currentPlayer.sendToClient("GAME_FINISHED:Boom! You win!");
                    currentPlayer.getOpponent().sendToClient("GAME_FINISHED:You lost.");
                    break;
                }






            }
            currentPlayer = currentPlayer.getOpponent();
        }
        player1.closeConnection();
        player2.closeConnection();
        System.out.println("Game finished");
    }
}
