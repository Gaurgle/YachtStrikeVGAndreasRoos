package ClientSide.factory;

public abstract class Ship {

    private final int size;
    private final boolean isVertical;

    public Ship(int size, boolean isVertical) {
        this.size = size;
        this.isVertical = isVertical;
    }

    public int getSize() {
        return size;
    }

    public boolean getIsVertical() {
        return isVertical;
    }

}
