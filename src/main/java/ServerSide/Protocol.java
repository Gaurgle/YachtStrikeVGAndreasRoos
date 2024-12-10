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
        player2.sendToClient("test");

        String preset1 = player1.receieveFromClient();

        System.out.println(preset1.split(":")[0]);

        String preset2 = player2.receieveFromClient();

        System.out.println(preset2.split(":")[0]);

        boolean hit = true;
        while(hit){
            currentPlayer.sendToClient("GET_SHOT");
            String shots = currentPlayer.receieveFromClient();
            System.out.println();

        }

    }
}
