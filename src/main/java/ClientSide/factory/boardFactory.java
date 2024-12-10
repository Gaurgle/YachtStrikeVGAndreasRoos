package ClientSide.factory;

import java.util.List;

public class boardFactory {
    private final int[][] board;
    int numBoard;
    private final String[] harbours = {"MONACO", "MALIBU", "NICE", "CANNES", "MARBELLA", "MIAMI BEACH", "ST TROPEZ", "ABU DHABI"};
    private final shipFactory shipFactory;
    private final List<Ship> listOfShips;


    public boardFactory(int numBoard, shipFactory shipFactory) {
        this.board = new int[numBoard][numBoard]; // does not create board yet.
        this.numBoard = numBoard;
        this.shipFactory = shipFactory;
        this.listOfShips = shipFactory.getShips();

    }

    // user to be prompted for harbour. Initializes board and ship creation.
    // use Sam's board generator here?
    public String createBord(String harbour) {

        shipFactory.createShip(harbour);

        if (!isValidHarbour(harbour)) {
            return "invalid harbour";
        }

        shipFactory.createShip("JP");
        shipFactory.createShip("BEZ");
        shipFactory.createShip("MUSK");
        shipFactory.createShip("TRUMP");
        shipFactory.createShip("WARREN");
        numBoard++;

        return listOfShips.size() + " ships created";

    }

    public boolean isValidHarbour(String harbour) {
        for (String h : harbours) {
            if (h.equalsIgnoreCase(harbour)) {
                return true;
            }
        }
        return false;
    }
}