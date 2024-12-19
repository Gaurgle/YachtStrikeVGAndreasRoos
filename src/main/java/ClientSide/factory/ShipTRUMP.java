package ClientSide.factory;

import Audio.AudioEnum;

public class ShipTRUMP extends Ship {
    public ShipTRUMP(int[] coordinates) {
        super(5, coordinates, AudioEnum.TRUMP1, AudioEnum.TRUMP2);
    }
}