package ClientSide;

import ServerSide.AudioManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;

public class MenuManager {
    private final BufferedReader reader;
    private final AudioManager audioManager;

    public MenuManager(BufferedReader reader, AudioManager audioManager) {
        this.reader = reader;
        this.audioManager = audioManager;
    }

    public void startMenu() {
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

            try {
                String line = reader.readLine();
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
                audioManager.playClack(i + 1);
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
}