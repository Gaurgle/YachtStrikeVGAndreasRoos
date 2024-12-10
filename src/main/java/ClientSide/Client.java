package ClientSide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Client {

    private final int[][] field = new int[10][10];
    PrintWriter out;

    private ArrayList<String> letters;
    private final ArrayList<String> shots = new ArrayList<>();

    public Client() {

        int portNumber = 23456;

        try (Socket socket = new Socket("localhost", portNumber);

             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out = new PrintWriter(socket.getOutputStream(), true);

            String input;
            while (true) {
                if (in.ready()) {
                    input = in.readLine();
                    determaineAction(input);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client();
    }

    private void determaineAction(String input) {

        if (input.equals("ALLOW_SELECT_PRESET")) {
            int i = 1;
            clear();
            createField();
            preset(i);
            printField();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String answer;
            try {

                while (true) {
                    System.out.println("Do you want to use this preset? (Y/N)");
                    answer = reader.readLine().toUpperCase();

                    if (answer.equals("Y")) {
                        System.out.println("Preset selected. Wait for other player.");

                        out.println("PRESET_SELECTED:" + i);

                        break;
                    } else {
                        clear();
                        createField();
                        preset(i++);
                        printField();
                        if (i == 6) {
                            i = 1;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (input.equals("ALLOW_SHOT")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String answer;

            try {
                while (true) {

                    do {
                        System.out.print("Enter coordinates for shot: ");
                        answer = reader.readLine();
                    }
                    while (!answer.matches("^[A-Ja-j][0-9]$"));
                    answer = answer.toUpperCase();

                    if (!shots.contains(answer)) {
                        {
                            shots.add(answer);
                            break;
                        }
                    } else if (shots.contains(answer)) {
                        System.out.println("Already shot");
                    }
                }

                int x = letters.indexOf(String.valueOf(answer.charAt(0)).toUpperCase());
                int y = Integer.parseInt(String.valueOf(answer.charAt(1)));

                out.println(x + "," + y);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (input.startsWith("CHECK_SHOT")) {
            String cords = input.split(":")[1];
            int x = Integer.parseInt(cords.split(",")[0]);
            int y = Integer.parseInt(cords.split(",")[1]) - 1;

            boolean hit = shoot(x, y);

            if (hit)
                out.println(checkField());

            clear();
            printField();

            out.println(hit);

        } else if (input.startsWith("SEND_HIT_STATUS")) {
            boolean hit = Boolean.parseBoolean(input.split(":")[1]);

            if (hit) {
                System.out.println("hit!");
                System.out.println("borde komma hit");
            }
            else if (!hit)
                System.out.println("miss.. \nWait for other player.");
        } else if (input.startsWith("GAME_FINISHED")) {
            String winMessage = input.split(":")[1];
            System.out.println(winMessage);
            //Todo: lägg in meny-metod

        }
    }

    public void createField() {
        //battleship field
        /*System.out.println("   1, 2, 3, 4, 5, 6, 7, 8, 9, 10");
        System.out.println("A |  |  |  |  |  |  |  |  |  |  |");
        System.out.println("B |  |  |  |  |  |  |  |  |  |  |");
        System.out.println("C |  |  |  |  |  |  |  |  |  |  |");
        System.out.println("D |  |  |  |  |  |  |  |  |  |  |");
        System.out.println("E |  |  |  |  |  |  |  |  |  |  |");
        System.out.println("F |  |  |  |  |  |  |  |  |  |  |");
        System.out.println("G |  |  |  |  |  |  |  |  |  |  |");
        System.out.println("H |  |  |  |  |  |  |  |  |  |  |");
        System.out.println("I |  |  |  |  |  |  |  |  |  |  |");
        System.out.println("J |  |  |  |  |  |  |  |  |  |  |");
          */

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = 0;
            }
        }

        letters = new ArrayList<>(List.of("A","B","C","D","E","F","G","H","I","J"));
    }

    public void printField() {

        System.out.println("    1   2   3   4   5   6   7   8   9   10");

        for (int i = 0; i < field.length; i++) {
            System.out.print((letters.get(i) + " | "));
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == 0) {
                    System.out.print("  | ");
                } else if (field[i][j] == 1) {
                    System.out.print("0 | ");
                } else if (field[i][j] == 2) {
                    System.out.print("X | ");
                }
            }
            System.out.println();
        }
    }

    public void placeShip(int x, int y) {
        field[x][y] = 1;
    }

    public void placeShips(String shipPlacement) {
        String[] ships = shipPlacement.split(" ");
        for (String ship : ships) {
            String[] coordinates = ship.split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            placeShip(x, y);
        }
    }

    private void preset(int x) {

        switch (x) {
            case 1:
                // Horizontal and vertical ships
                placeShips("0,0 0,1 0,2 0,3 0,4"); // Carrier (5)
                placeShips("2,2 2,3 2,4 2,5");     // Battleship (4)
                placeShips("4,0 4,1 4,2");         // Cruiser (3)
                placeShips("6,6 6,5");             // Destroyer (2)
                break;
            case 2:
                // Clustered arrangement
                placeShips("1,1 1,2 1,3 1,4");     // Battleship (4)
                placeShips("3,3 3,4 3,5");         // Cruiser (3)
                placeShips("5,6 5,7");             // Destroyer (2)
                placeShips("7,8");                 // Submarine (1)
                break;
            case 3:
                // Diagonal and zigzag pattern
                placeShips("0,0 1,1 2,2 3,3 4,4"); // Carrier (5)
                placeShips("5,1 5,2 5,3 5,4");     // Battleship (4)
                placeShips("6,6 7,6 8,6");         // Cruiser (3)
                placeShips("9,8 9,9");             // Destroyer (2)
                break;
            case 4:
                // Spread-out ships
                placeShips("0,5 1,5 2,5 3,5 4,5"); // Carrier (5)
                placeShips("6,1 7,1 8,1 9,1");     // Battleship (4)
                placeShips("3,7 4,7 5,7");         // Cruiser (3)
                placeShips("8,8 8,9");             // Destroyer (2)
                break;
            case 5:
                // Compact square formation
                placeShips("0,0 0,1 0,2 0,3 0,4"); // Carrier (5)
                placeShips("1,1 1,2 1,3 1,4");     // Battleship (4)
                placeShips("2,2 2,3 2,4");         // Cruiser (3)
                placeShips("3,3 3,4");             // Destroyer (2)
                break;
            default:
                System.out.println("Invalid preset. No ships placed.");
        }

    }

    public boolean shoot(int x, int y) {
        if (field[x][y] == 0) {
            field[x][y] = 2;
            return false;
        } else {
            field[x][y] = 3;
            return true;
        }
    }

    public boolean checkField() {
        boolean gameStillActive = true;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == 1) {
                    gameStillActive = false;
                    break;
                }
            }
        }
        return gameStillActive;
    }

    public void clear() {
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }

//    public void closeConnection() {
//        try {
//            if (in != null)
//                in.close();
//            if (out != null) {
//                out.close();
//            }
//            if (socket != null && !socket.isClosed()) {
//                socket.close();
//            }
//        } catch (IOException e) {
//            System.err.println("Error closing socket or in/out streams: " + e.getMessage());
//        }
//    }
}
