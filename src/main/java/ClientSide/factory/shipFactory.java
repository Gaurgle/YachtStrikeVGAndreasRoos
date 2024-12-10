package ClientSide.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class shipFactory {
    private final int shipSize;
    private boolean isVertical;
    private String shipName;
    private List<Ship> ships;

    public shipFactory(int shipSize, String shipName, boolean isVertical) {
        this.shipSize = shipSize;
        this.shipName = shipName;
        this.isVertical = isVertical;
        this.ships = new ArrayList<>();
    }

    public int getShipSize() {
        return shipSize;
    }

    public Ship createShip(String ship){

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
        return createdShip;
    }

    // generar random orientation
    public boolean randomDirection(){
        Random random = new Random();
        return random.nextBoolean();
    }

    public List<Ship> getShips(){
        return ships;
    }
}