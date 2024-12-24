package ClientSide;

import ServerSide.AudioManager;

import java.io.*;
import java.net.Socket;

public class Client {
    private final PrintWriter out;
    private final BufferedReader reader;
    private final GameHandler gameHandler;
    private final FieldManager fieldManager;
    private final MenuManager menuManager;

    private Client() throws IOException {
        reader = new BufferedReader(new InputStreamReader(System.in));
        fieldManager = new FieldManager();
        out = new PrintWriter(new Socket("localhost", 23456).getOutputStream(), true);
        gameHandler = new GameHandler(out, reader, fieldManager);
        menuManager = new MenuManager(reader, AudioManager.getInstance());
        menuManager.startMenu();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new Socket("localhost", 23456).getInputStream()))) {
            String input;
            while (true) {
                if (in.ready()) {
                    input = in.readLine();
                    gameHandler.determineAction(input);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            new Client();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}