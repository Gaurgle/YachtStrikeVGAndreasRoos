package ClientSide;

import ClientSide.factory.Ship;
import ClientSide.factory.ShipFactory;
import ServerSide.AudioManager;

import java.util.List;

public class FieldManager {
    private final int[][] clientField;
    private final int[][] opponentField;
    private List<Ship> ships;
    private final AudioManager audioManager;

    public FieldManager() {
        this.clientField = new int[10][10];
        this.opponentField = new int[10][10];
        this.audioManager = AudioManager.getInstance();
    }

    public int[][] getClientField() {
        return clientField;
    }

    public int[][] getOpponentField() {
        return opponentField;
    }

    public void createField() {
        for (int i = 0; i < clientField.length; i++) {
            for (int j = 0; j < clientField[i].length; j++) {
                clientField[i][j] = 0;
            }
        }
    }

    public void printField(int[][] field) {
        System.out.println("    1   2   3   4   5   6   7   8   9   10");
        for (int i = 0; i < field.length; i++) {
            System.out.print((char) ('A' + i) + " | ");
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

    public void preset(int x) {
        ShipFactory shipFactory = new ShipFactory();
        switch (x) {
            case 1:
                ships = shipFactory.createShips(1);
                break;
            case 2:
                ships = shipFactory.createShips(2);
                break;
            case 3:
                ships = shipFactory.createShips(3);
                break;
            default:
                System.out.println("Invalid preset. No ships placed.");
                audioManager.playYesNo("no");
        }
        placeShips(ships);
    }

    public boolean shoot(int x, int y) {
        if (clientField[x][y] == 0) {
            clientField[x][y] = 2;
            return false;
        } else {
            clientField[x][y] = 3;
            findShipThatGotHitAndDamageIt(x, y);
            return true;
        }
    }

    public boolean checkForDestroyedShips() {
        for (Ship ship : ships) {
            if (!ship.isAfloat()) {
                ships.remove(ship);
                return true;
            }
        }
        return false;
    }

    public boolean checkField() {
        for (int[] row : clientField) {
            for (int cell : row) {
                if (cell == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public void clear() {
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }

    private void placeShips(List<Ship> ships) {
        for (Ship ship : ships) {
            for (int i = 0; i < ship.getCoordinates().length; i += 2) {
                int x = ship.getCoordinates()[i];
                int y = ship.getCoordinates()[i + 1];
                clientField[x][y] = 1;
            }
        }
    }

    private void findShipThatGotHitAndDamageIt(int x, int y) {
        for (Ship ship : ships) {
            for (int i = 0; i < ship.getCoordinates().length; i += 2) {
                if (ship.getCoordinates()[i] == x && ship.getCoordinates()[i + 1] == y) {
                    ship.takeDamage();
                    int hp = ship.getHealthPoints();
                    String hitType = switch (hp) {
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
}