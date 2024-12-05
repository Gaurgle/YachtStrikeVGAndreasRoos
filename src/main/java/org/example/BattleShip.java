package org.example;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.util.ArrayList;

public class BattleShip {

    private int[][] field = new int[10][10];
    private ArrayList<String> letters;

    public BattleShip() {
        createField();

        placeShip(0, 0);
        placeShip(0, 1);
        placeShip(0, 2);

        shoot(0, 1);

        printField();
        clear();
    }


    public static void main(String[] args) {
        BattleShip battle = new BattleShip();
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
                    System.out.print("X | ");
                }
            }
            System.out.println();
        }

    }

    public void placeShip(int x, int y) {
        field[x][y] = 1;
    }

    public void shoot(int x, int y) {
        if (field[x][y] == 0) {
            field[x][y] = 1;
        } else
            field[x][y] = 2;
    }

    public void clear() {
//        for (int i = 0; i < 100; i++) {
//            System.out.println();
//        }

        AnsiConsole.systemInstall();

        // Clear the screen using ANSI escape codes
        System.out.print(Ansi.ansi().eraseScreen().reset());

        System.out.println("\033[H\033[2J");
        // Print something after clearing the screen
        System.out.println("The screen is cleared!");

        // Optionally, reset AnsiConsole (not required, but can be used if needed)
        AnsiConsole.systemUninstall();

    }


}