package ClientSide;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {

    private final int[][] field = new int[10][10];
    private ArrayList<String> letters;
    private ArrayList<String> shots = new ArrayList<>();
    PrintWriter out;

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


            switch (choice) {
                case 1:
                    String step1 = "Step 1: Introduction\n" +
                            "Welcome to Battleship! This game is played on a 10x10 grid with coordinates (A-J, 1-10).";

                    String step2 = "Step 2: The Grid\n" +
                            "The grid is 10x10 with rows labeled A-J and columns labeled 1-10. Example: A5.";

                    String step3 = "Step 3: Setting Up\n" +
                            "Please Choose your layout: \n TIPS: Layout ISAAC always wins the game";

                    String step4 = "Step 4: Taking Turns\n" +
                            "Players take turns calling out coordinates (e.g., A5). Game will tell you either 'hit' or 'miss'. while it will update your grid.";

                    String step5 = "Step 5: Objective\n" +
                            "Sink all of your opponent’s ships by hitting all the squares of each ship.";

                    String step6 = "Step 6: Example Turn\n" +
                            "Player 1: 'I fire at C7'. Opponent checks, says 'hit' or 'miss'. Player updates grid.";

                    String step7 = "Step 7: End of the Game\n" +
                            "The game ends when all of one player’s ships are sunk. The other player wins.";


                    clear();
                    System.out.println(step1);
                    System.out.println("Press enter to continue");
                    scanner.nextLine();
                    scanner.nextLine();

                    System.out.println(step2);
                    System.out.println("Press enter to continue");
                    scanner.nextLine();

                    System.out.println(step3);
                    System.out.println("Press enter to continue");
                    scanner.nextLine();

                    System.out.println(step4);
                    System.out.println("Press enter to continue");
                    scanner.nextLine();

                    System.out.println(step5);
                    System.out.println("Press enter to continue");
                    scanner.nextLine();

                    System.out.println(step6);
                    System.out.println("Press enter to continue");
                    scanner.nextLine();

                    System.out.println(step7);
                    System.out.println("Press enter to continue");
                    scanner.nextLine();

                    break;
                case 2:
                    System.out.println("Starting the game... Get ready!");

                    int portNumber = 23456;
                    try (Socket socket = new Socket("localhost", portNumber);
                         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                        out = new PrintWriter(socket.getOutputStream(), true);

                        String input;
                        while (true) {
                            if (in.ready()) {
                                input = in.readLine();
                                System.out.println(input);
                                determineAction(input);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
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

    public Client() {
        startMenu();
    }

    private void determineAction(String input) {
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
                        System.out.println("Preset selected.");
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
                    } while (!answer.matches("^[A-Ja-j][0-9]$"));
                    answer = answer.toUpperCase();

                    if (!shots.contains(answer)) {
                        shots.add(answer);
                        break;
                    } else {
                        System.out.println("Already shot");
                    }
                }

                System.out.println(answer);
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

            clear();
            printField();
            out.println(hit);
        } else if (input.startsWith("SEND_HIT_STATUS")) {
            boolean hit = Boolean.parseBoolean(input.split(":")[1]);

            if (hit) {
                System.out.println("hit!");
            } else {
                System.out.println("miss..");
            }
        }
    }

    public static void main(String[] args) {
        new Client();
    }

    public void createField() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = 0;
            }
        }

        letters = new ArrayList<>();
        letters.add("A");
        letters.add("B");
        letters.add("C");
        letters.add("D");
        letters.add("E");
        letters.add("F");
        letters.add("G");
        letters.add("H");
        letters.add("I");
        letters.add("J");
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
                    System.out.print("* | ");
                } else if (field[i][j] == 3) {
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
                placeShips("0,0 0,1 0,2 0,3 0,4"); // Carrier (5)
                placeShips("2,2 2,3 2,4 2,5");     // Battleship (4)
                placeShips("4,0 4,1 4,2");         // Cruiser (3)
                placeShips("6,6 6,5");             // Destroyer (2)
                break;
            case 2:
                placeShips("1,1 1,2 1,3 1,4");     // Battleship (4)
                placeShips("3,3 3,4 3,5");         // Cruiser (3)
                placeShips("5,6 5,7");             // Destroyer (2)
                placeShips("7,8");                 // Submarine (1)
                break;
            case 3:
                placeShips("0,0 1,1 2,2 3,3 4,4"); // Carrier (5)
                placeShips("5,1 5,2 5,3 5,4");     // Battleship (4)
                placeShips("6,6 7,6 8,6");         // Cruiser (3)
                placeShips("9,8 9,9");             // Destroyer (2)
                break;
            case 4:
                placeShips("0,5 1,5 2,5 3,5 4,5"); // Carrier (5)
                placeShips("6,1 7,1 8,1 9,1");     // Battleship (4)
                placeShips("3,7 4,7 5,7");         // Cruiser (3)
                placeShips("8,8 8,9");             // Destroyer (2)
                break;
            case 5:
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

    public void clear() {
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }
}
