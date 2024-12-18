package ClientSide.factory;

public abstract class Ship {

    private final int size;
    private int[] coordinates;
    private int healthPoints;
    private boolean isAfloat;

    public Ship(int size, int[] coordinates) {
        this.size = size;
        this.healthPoints = size;
        setCoordinates(coordinates);
        isAfloat = true;
    }

    public void takeDamage() {
        healthPoints--;
        if (healthPoints == 0) {
            isAfloat = false;
        }
    }

    public void setCoordinates(int[] coordinates) {
        if (coordinates.length == size * 2) {
            this.coordinates = coordinates;
        }
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public int getSize() {
        return size;
    }

    public int getHealthPoints() {return healthPoints;}

    public boolean isAfloat() {
        return isAfloat;
    }


    public void sinkShip() {
        isAfloat = false;
    }

}
