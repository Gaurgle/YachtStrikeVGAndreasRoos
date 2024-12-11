package ClientSide.factory;

import java.util.List;
import java.util.Scanner;

public class boardFactory {
    /*
    private final int[][] board;
    int numBoard;
    private final String[] harbours = {"MONACO", "MALIBU", "NICE", "CANNES", "MARBELLA", "MIAMI BEACH", "ST TROPEZ", "ABU DHABI"};
    private final ShipFactory shipFactory;
    private final List<Ship> listOfShips;


    public boardFactory(int numBoard, ShipFactory shipFactory) {
        this.board = new int[numBoard][numBoard]; // does not create grid yet.
        this.numBoard = numBoard;
        this.shipFactory = shipFactory;
        this.listOfShips = shipFactory.getShips();
    }

    // test input for creating board & ships
    public void userInput(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("choose ship:");
        for (String harbour : harbours) {
            System.out.println(harbour +" ");
        }

        System.out.println();
        String userHarbour = scanner.nextLine();

        if (isValidHarbour(userHarbour)) {
            System.out.println("Creating ships for harbour "+userHarbour);
            shipFactory.createShip("JP");
            shipFactory.createShip("BEZ");
            shipFactory.createShip("MUSK");
            shipFactory.createShip("TRUMP");
            shipFactory.createShip("WARREN");
            System.out.println("Ships created");

        } else {
            System.out.println("Invalid harbour.");
        }

        System.out.println("List of created ships:");
        for (Ship ship : listOfShips) {
            System.out.println(ship);
        }
    }

    public boolean isValidHarbour(String harbour) {
        for (String h : harbours) {
            if (h.equalsIgnoreCase(harbour)) {
                return true;
            }
        }
        return false;
    }

    // test
    public static void main(String[] args) {
        ShipFactory shipFactory = new ShipFactory();
        boardFactory boardFactory = new boardFactory(1,shipFactory);

        boardFactory.userInput();
    }*/
}