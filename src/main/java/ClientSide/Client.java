package ClientSide;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class Client {

    private final int[][] clientField = new int[10][10];
    private ArrayList<String> letters;
    private ArrayList<String> shots = new ArrayList<>();
    PrintWriter out;
    BufferedReader reader;

    public Client() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        startMenu();

        int portNumber = 23456;
        try (Socket socket = new Socket("localhost", portNumber);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out = new PrintWriter(socket.getOutputStream(), true);

            String input;
            while (true) {
                if (in.ready()) {
                    input = in.readLine();
                    determineAction(input);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void determineAction(String input) {

        if (input.equals("ALLOW_SELECT_PRESET")) {
            {
                int i = 1;
                clear();
                createField();
                preset(i);
                printField(clientField);

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
                            printField(clientField);
                            if (i == 6) {
                                i = 1;
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (input.equals("ALLOW_SHOT")) {
            String answer;

            try {
                while (true) {
                    do {
                        System.out.print("Enter coordinates for shot: ");
                        answer = reader.readLine();
                    }
                    while(!answer.matches("^[A-Ja-j][0-9]$"));

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

                out.println(x+","+y);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if (input.startsWith("CHECK_SHOT")) {
            String cords = input.split(":")[1];
            int x = Integer.parseInt(cords.split(",")[0]);
            int y = Integer.parseInt(cords.split(",")[1])-1;

            boolean hit = shoot(x,y);

            clear();
            printField(clientField);

            out.println(hit);
            out.println(checkField());
        }
        else if (input.startsWith("SEND_HIT_STATUS")){
            boolean hit = Boolean.parseBoolean(input.split(":")[1]);

            if (hit)
                System.out.println("hit!");
            else if (!hit)
                System.out.println("miss.. \nWait for other player.");
        }
        else if (input.startsWith("GAME_FINISHED")) {
            String winMessage = input.split(":")[1];
            System.out.println(winMessage);
            System.out.println("Press enter to continue.");
            try {
                reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            startMenu();
        }

    }

    public static void main(String[] args) {
        new Client();
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

        for (int i = 0; i < clientField.length; i++) {
            for (int j = 0; j < clientField[i].length; j++) {
                clientField[i][j] = 0;
            }
        }
        letters = new ArrayList<>(List.of("A","B","C","D","E","F","G","H","I","J"));
    }

    public void printField(int[][] field) {

        System.out.println("    1   2   3   4   5   6   7   8   9   10");

        for (int i = 0; i < field.length; i++) {
            System.out.print((letters.get(i) + " | "));
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == 0) {
                    System.out.print("  | ");
                } else if (field[i][j] == 1) {
                    System.out.print("0 | ");
                } else if (field[i][j] == 2) {
                    System.out.print("* | ");
                }
                else if (field[i][j] == 3) {
                    System.out.print("X | ");
                }
            }
            System.out.println();
        }
    }

    public void placeShip(int x, int y) {
        clientField[x][y] = 1;
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

    private void preset(int x){

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
        if (clientField[x][y] == 0) {
            clientField[x][y] = 2;
            return false;
        }
        else {
            clientField[x][y] = 3;
            return true;
        }
    }

    public boolean checkField() {
        boolean gameStillActive = false;
        for (int i = 0; i < clientField.length; i++) {
            for (int j = 0; j < clientField[i].length; j++) {
                if (clientField[i][j] == 1) {
                    gameStillActive = true;
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
    public static void typeWriterEffect(String text) throws InterruptedException {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            Thread.sleep(15);
        }
        System.out.println();
    }

    private void startMenu(){
        boolean running = true;
        Scanner scanner = new Scanner(System.in);

        while (running) {
            clear();
            System.out.println("WELCOME TO BATTLESHIP\n");

            System.out.println("Please select an option:");
            System.out.println("1. Info about the game");
            System.out.println("2. Start the game");
            System.out.println("3. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();


            switch (choice) {
                case 1:
                    String[] steps = {
                            "Step 1: Introduction\n" +
                                    "Welcome to Battleship! This game is played on a 10x10 grid with coordinates (A-J, 1-10).",

                            "Step 2: The Grid\n" +
                                    "The grid is 10x10 with rows labeled A-J and columns labeled 1-10. Example: A5.",

                            "Step 3: Setting Up\n" +
                                    "Please Choose your layout: \nTIPS: Layout ISAAC always wins the game",

                            "Step 4: Taking Turns\n" +
                                    "Players take turns calling out coordinates (e.g., A5). Game will tell you either 'hit' or 'miss'. while it will update your grid.",

                            "Step 5: Objective\n" +
                                    "Sink all of your opponent’s ships by hitting all the squares of each ship.",

                            "Step 6: Example Turn\n" +
                                    "Player 1: 'I fire at C7'. Opponent checks, says 'hit' or 'miss'. Player updates grid.",

                            "Step 7: End of the Game\n" +
                                    "The game ends when all of one player’s ships are sunk. The other player wins."
                    };

                    clear();
                    for (String step : steps) {
                        try {
                            typeWriterEffect(step);
                        } catch (InterruptedException e) {
                            System.err.println("Typing interrupted: " + e.getMessage());
                        }
                        System.out.println("\nPress enter to continue");
                        scanner.nextLine();
                    }
                    break;
                case 2:
                    System.out.println("Starting the game... Get ready!");
                    return;
                case 3:
                    System.out.println("Exiting the program. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1, 2, or 3.");
            }
        }
        scanner.close();
    }
}
