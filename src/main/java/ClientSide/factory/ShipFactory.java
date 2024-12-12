package ClientSide.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShipFactory {

    public List<Ship> createShips(int config) {

        List<Ship> ships = new ArrayList<>();

        switch (config) {
            case 1:
                ships.add(new ShipJP(new int[]{6, 6, 6, 5}));
                ships.add(new ShipBEZ(new int[]{4,0,4,1,4,2}));
                ships.add(new ShipMUSK(new int[]{2,2,2,3,2,4,2,5}));
                ships.add(new ShipTRUMP(new int[]{0,0,0,1,0,2,0,3,0,4}));
                break;
            case 2:
                ships.add(new ShipJP(new int[]{9,8,9,9}));
                ships.add(new ShipBEZ(new int[]{6,6,7,6,8,6}));
                ships.add(new ShipMUSK(new int[]{5,1,5,2,5,3,5,4}));
                ships.add(new ShipTRUMP(new int[]{0,0,1,1,2,2,3,3,4,4}));
                break;
            default:
                break;
        }

        return ships;
    }

    // generate random orientation
    public boolean randomDirection() {
        Random random = new Random();
        return random.nextBoolean();
    }
}