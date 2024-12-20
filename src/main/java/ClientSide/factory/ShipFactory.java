package ClientSide.factory;

import java.util.ArrayList;
import java.util.List;

public class ShipFactory {

    public List<Ship> createShips(int config) {

        List<Ship> ships = new ArrayList<>();

        switch (config) {
            case 1:
                ships.add(new ShipJP(new int[]{6, 6, 6, 5}));
                ships.add(new ShipBEZ(new int[]{4,0, 4,1, 4,2}));
                ships.add(new ShipMUSK(new int[]{2,2, 2,3, 2,4, 2,5}));
                ships.add(new ShipTRUMP(new int[]{0,0, 0,1, 0,2, 0,3, 0,4}));
                break;
            case 2:
                ships.add(new ShipJP(new int[]{9,8, 9,9}));
                ships.add(new ShipBEZ(new int[]{6,6, 7,6, 8,6}));
                ships.add(new ShipMUSK(new int[]{5,1, 5,2, 5,3, 5,4}));
                ships.add(new ShipTRUMP(new int[]{0,0, 1,1, 2,2, 3,3, 4,4}));
                break;
            case 3:
                ships.add(new ShipJP(new int[]{7,8, 7,9}));
                ships.add(new ShipBEZ(new int[]{2,3, 2,4, 2,5}));
                ships.add(new ShipMUSK(new int[]{6,1, 6,2, 6,3, 6,4}));
                ships.add(new ShipTRUMP(new int[]{9,5, 9,6, 9,7, 9,8, 9,9}));
                break;
            case 4:
                ships.add(new ShipJP(new int[]{8,2, 8,3}));
                ships.add(new ShipBEZ(new int[]{6,5, 6,6, 6,7}));
                ships.add(new ShipMUSK(new int[]{3,3, 3,4, 3,5, 3,6}));
                ships.add(new ShipTRUMP(new int[]{7,0, 7,1, 7,2, 7,3, 7,4}));
                break;
            default:
                break;
        }
        return ships;
    }
}