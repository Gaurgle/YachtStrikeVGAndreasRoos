package ClientSide.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class shipFactory {
    private final List<Ship> ships;

    public shipFactory() {
        this.ships = new ArrayList<>();
    }


    public void createShip(String ship){
        Ship createdShip = switch(ship){
            case "JP" -> new shipJP(randomDirection());
            case "BEZ" -> new shipBEZ(randomDirection());
            case "MUSK" -> new shipMUSK(randomDirection());
            case "TRUMP" -> new shipTRUMP(randomDirection());
            case "WARREN" -> new shipWARREN(randomDirection());
            default -> null;
        };

        if (createdShip != null) {
            ships.add(createdShip);
        }
    }

    // generate random orientation
    public boolean randomDirection(){
        Random random = new Random();
        return random.nextBoolean();
    }

    public List<Ship> getShips(){
        return ships;
    }

    public static void main(String[] args) {
    }
}