package ClientSide;

import ClientSide.factory.Ship;
import ClientSide.factory.ShipFactory;
import ServerSide.AudioManager;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class Client {

    private final int[][] clientField = new int[10][10];
    private final int[][] opponentField = new int[10][10];
    private ArrayList<String> letters = new ArrayList<>(List.of("A","B","C","D","E","F","G","H","I","J"));
    private ArrayList<String> shots = new ArrayList<>();
    private List<Ship> ships;
    private PrintWriter out;
    private BufferedReader reader;
    private boolean isFirstShot = true;

    private final AudioManager audioManager;


    private Client() {
        audioManager = AudioManager.getInstance();
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
            int presetIndex = 1;
            clear();
            createField();
            preset(presetIndex);
            printField(clientField);

            String answer;

            try {
                while (true) {
                    System.out.println("Do you want to use this preset? (Y/N)");
                    answer = reader.readLine().toUpperCase();

                    if  (answer.equals("Y")) {
                        audioManager.playYesNo("yes");
                        out.println("PRESET_SELECTED:" + presetIndex);
                        break;

                    } else if (answer.equalsIgnoreCase("N")) {
                        audioManager.playYesNo("no");
                        presetIndex++;
                        if (presetIndex == 4) {
                            presetIndex = 1;
                        }

                        clear();
                        createField();
                        preset(presetIndex);
                        printField(clientField);
                    } else {
                        System.out.println("Invalid input. type Y or N.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if (input.equals("ALLOW_SHOT")) {
            String answer;

            try {
                while (true) {
                    do {
                        if (isFirstShot){
                            System.out.println("Press enter to continue");
                            reader.readLine();
                            isFirstShot = false;
                            printField(opponentField);
                        }

                        System.out.print("Enter coordinates for shot: ");
                        answer = reader.readLine();
                    }
                    while(!answer.matches("^[A-Ja-j](10|[0-9])$"));

                    if (!shots.contains(answer)) {
                        shots.add(answer);
                        break;

                    } else {
                        System.out.println("Already shot");
                        audioManager.playShot(1);
                    }
                }

                int x = letters.indexOf(String.valueOf(answer.charAt(0)).toUpperCase());
                int y = Integer.parseInt(answer.substring(1));

                out.println(x+","+y);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (input.startsWith("CHECK_SHOT")) {
            String cords = input.split(":")[1];
            int x = Integer.parseInt(cords.split(",")[0]);
            int y = Integer.parseInt(cords.split(",")[1])-1;

            boolean hit = shoot(x,y);

            clear();
            printField(clientField);

            out.println(x+","+y);
            out.println(hit);
            out.println(checkForDestroyedShips());
            out.println(checkField());

        }
        else if (input.startsWith("SEND_HIT_STATUS")){
            boolean hit = Boolean.parseBoolean(input.split(":")[1]);
            String cords = input.split(":")[2];
            int x = Integer.parseInt(cords.split(",")[0]);
            int y = Integer.parseInt(cords.split(",")[1]);

            if (hit) {
                clear();
                System.out.println("hit!");
                opponentField[x][y] = 3;
                printField(opponentField);
            }

            else if (!hit) {
                clear();
                System.out.println("miss.. \nWait for other player.");
                audioManager.playSplash();
                opponentField[x][y] = 2;
                printField(opponentField);
                isFirstShot = true;
            }

        }
        else if (input.startsWith("SEND_SUNKEN_SHIP")) {
            System.out.println("Ship sunk!");
        }

        if (input.startsWith("GAME_FINISHED")) {
            String winMessage = input.split(":")[1];
            System.out.println(winMessage);
            System.out.println("Press enter to continue.");
            try {
                reader.readLine();
                audioManager.playYesNo("yes");
            } catch (IOException e) {
                e.printStackTrace();
            }
            startMenu();
        }
    }

    public static void main(String[] args) {
        new Client();
    }


    private void createField() {
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
    }

    private void printField(int[][] field) {

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

    private void placeShip(int x, int y) {
        clientField[x][y] = 1;
    }

    private void placeShips(List<Ship> ships) {

        for (Ship ship : ships) {
            for (int i = 0; i < ship.getCoordinates().length; i += 2) {
                int x = ship.getCoordinates()[i];
                int y = ship.getCoordinates()[i + 1];
                placeShip(x, y);
            }
        }
    }

    private void preset(int x){
        ShipFactory shipFactory = new ShipFactory();

        switch (x) {
            case 1:
                // Horizontal and vertical ships
                ships = shipFactory.createShips(1);
                break;
            case 2:
                ships = shipFactory.createShips(2);
                break;
            case 3: ships = shipFactory.createShips(3);
                break;
            default:
                System.out.println("Invalid preset. No ships placed.");
                audioManager.playYesNo("no");
        }


        placeShips(ships);
    }

    private boolean  shoot(int x, int y) {
        if (clientField[x][y] == 0) {
            clientField[x][y] = 2;
            return false;
        }
        else {
            clientField[x][y] = 3;
            findShipThatGotHitAndDamageIt(x,y);
            return true;
        }
    }

    private void findShipThatGotHitAndDamageIt(int x, int y) {

        for (Ship ship : ships) {
            for (int i = 0; i < ship.getCoordinates().length; i += 2) {
                if (ship.getCoordinates()[i] == x && ship.getCoordinates()[i + 1] == y) {
                    ship.takeDamage();
                    int hp = ship.getHealthPoints();

                    String hitType = switch (hp){
                        case 0 -> "final";
                        case 1 -> "medium";
                        default -> "small";
                    };
                    audioManager.playHit(hitType);

                    if (hp == 2) {
                        ship.playHitSound();
                    }

                    System.out.println(ship + " took 1 damage");
                    break;
                }
            }
        }
    }

    private boolean checkForDestroyedShips() {
        for (Ship ship : ships) {
            if (!ship.isAfloat()) {
                ships.remove(ship);
                return true;
            }
        }
        return false;
    }



    private boolean checkField() {
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

    private void clear() {
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }
    private static void typeWriterEffect(String text) throws InterruptedException {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            Thread.sleep(15);
        }
        System.out.println();
    }

    private void startMenu(){
        boolean running = true;
        audioManager.playThemeSong("play");
        audioManager.themeFadeUp();
        Scanner scanner = new Scanner(System.in);


        while (running) {
            clear();
            System.out.println("WELCOME TO BATTLESHIP\n");

            System.out.println("Please select an option:");
            System.out.println("1. Info about the game");
            System.out.println("2. Start the game");
            System.out.println("3. Exit");
            System.out.println("TIP: Press 'm' to mute/unmute music while in menu");

            String line;
            try {
                line = reader.readLine();
                if (line.equals("m")) {
                    audioManager.playThemeSong("mute");
                    continue;
                }
                int choice;
                try {
                    choice = Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input, please enter, 1, 2, 3, or m");
                    continue;
                }

                switch (choice) {
                    case 1:
                        showGameInfo();
                        break;

                    case 2:
                        audioManager.playYesNo("yes");
                        System.out.println("Starting game");
                        audioManager.themeFadeDown(-6.0f);
                        return;

                    case 3:
                        System.out.println("Exiting the program. Goodbye!");
                        audioManager.playYesNo("no");
                        audioManager.themeFadeDown(-90.0f);
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please select 1, 2, or 3.");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

private void showGameInfo() {
    String[] steps = {
            "Step 1: Introduction\n" +
                    "Welcome to Battleship! This game is played on a 10x10 grid with coordinates (A-J, 1-10).",

            "Step 2: The Grid\n" +
                    "The grid is 10x10 with rows labeled A-J and columns labeled 1-10. Example: A5.",

            "Step 3: Setting Up\n" +
                    "Please Choose your layout",

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
        for (int i = 0; i < steps.length; i++) {
            try {
                audioManager.themeFadeDown(-7.0f);
                audioManager.playClack(i +1);
                typeWriterEffect(steps[i]);
                audioManager.themeFadeUp();
            } catch (InterruptedException e) {
                System.err.println("Typing interrupted: " + e.getMessage());
            }
            System.out.println("\nPress enter to continue");
            try {
                reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}