package ClientSide.factory;

import Audio.AudioEnum;

public class ShipBEZ extends Ship {
    public ShipBEZ(int[] coordinates) {
        super(3, coordinates, AudioEnum.BEZ1, AudioEnum.BEZ2);
    }
}
