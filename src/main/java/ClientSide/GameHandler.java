package ClientSide;

import ServerSide.AudioManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class GameHandler {
    private final List<String> letters;
    private final List<String> shots;
    private final AudioManager audioManager;
    private final PrintWriter out;
    private final BufferedReader reader;
    private final FieldManager fieldManager;
    private boolean isFirstShot = true;

    public GameHandler(PrintWriter out, BufferedReader reader, FieldManager fieldManager) {
        this.letters = new ArrayList<>(List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J"));
        this.shots = new ArrayList<>();
        this.audioManager = AudioManager.getInstance();
        this.out = out;
        this.reader = reader;
        this.fieldManager = fieldManager;
    }

    public void determineAction(String input) {
        try {
            switch (input) {
                case "ALLOW_SELECT_PRESET":
                    handlePresetSelection();
                    break;
                case "ALLOW_SHOT":
                    handleShot();
                    break;
                default:
                    if (input.startsWith("CHECK_SHOT")) {
                        handleCheckShot(input);
                    } else if (input.startsWith("SEND_HIT_STATUS")) {
                        handleHitStatus(input);
                    } else if (input.startsWith("SEND_SUNKEN_SHIP")) {
                        System.out.println("Ship sunk!");
                    } else if (input.startsWith("GAME_FINISHED")) {
                        handleGameFinished(input);
                    }
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handlePresetSelection() throws IOException {
        int presetIndex = 1;
        fieldManager.clear();
        fieldManager.createField();
        fieldManager.preset(presetIndex);
        fieldManager.printField(fieldManager.getClientField());

        while (true) {
            System.out.println("Do you want to use this preset? (Y/N)");
            String answer = reader.readLine().toUpperCase();

            if (answer.equals("Y")) {
                audioManager.playYesNo("yes");
                out.println("PRESET_SELECTED:" + presetIndex);
                break;
            } else if (answer.equals("N")) {
                audioManager.playYesNo("no");
                presetIndex = (presetIndex % 3) + 1;
                fieldManager.clear();
                fieldManager.createField();
                fieldManager.preset(presetIndex);
                fieldManager.printField(fieldManager.getClientField());
            } else {
                System.out.println("Invalid input. type Y or N.");
            }
        }
    }

    private void handleShot() throws IOException {
        while (true) {
            if (isFirstShot) {
                System.out.println("Press enter to continue");
                reader.readLine();
                isFirstShot = false;
                fieldManager.printField(fieldManager.getOpponentField());
            }

            System.out.print("Enter coordinates for shot: ");
            String answer = reader.readLine();

            if (answer.matches("^[A-Ja-j](10|[0-9])$") && !shots.contains(answer)) {
                shots.add(answer);
                int x = letters.indexOf(String.valueOf(answer.charAt(0)).toUpperCase());
                int y = Integer.parseInt(answer.substring(1));
                out.println(x + "," + y);
                break;
            } else {
                System.out.println("Invalid or already shot");
                audioManager.playShot(1);
            }
        }
    }

    private void handleCheckShot(String input) {
        String[] parts = input.split(":")[1].split(",");
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]) - 1;

        boolean hit = fieldManager.shoot(x, y);
        fieldManager.clear();
        fieldManager.printField(fieldManager.getClientField());

        out.println(x + "," + y);
        out.println(hit);
        out.println(fieldManager.checkForDestroyedShips());
        out.println(fieldManager.checkField());
    }

    private void handleHitStatus(String input) {
        String[] parts = input.split(":");
        boolean hit = Boolean.parseBoolean(parts[1]);
        String[] coords = parts[2].split(",");
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);

        fieldManager.clear();
        if (hit) {
            System.out.println("hit!");
            fieldManager.getOpponentField()[x][y] = 3;
        } else {
            System.out.println("miss.. \nWait for other player.");
            audioManager.playSplash();
            fieldManager.getOpponentField()[x][y] = 2;
            isFirstShot = true;
        }
        fieldManager.printField(fieldManager.getOpponentField());
    }

    private void handleGameFinished(String input) throws IOException {
        String winMessage = input.split(":")[1];
        System.out.println(winMessage);
        System.out.println("Press enter to continue.");
        reader.readLine();
        audioManager.playYesNo("yes");
        new ClientSide.MenuManager(reader, audioManager).startMenu();
    }
}