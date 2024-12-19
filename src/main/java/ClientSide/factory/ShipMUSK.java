package ClientSide.factory;

import Audio.AudioEnum;

public class ShipMUSK extends Ship {
    public ShipMUSK(int[] coordinates) {
        super(4, coordinates, AudioEnum.MUSK1, AudioEnum.MUSK2);
    }
}
