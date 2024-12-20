package ClientSide.factory;

import Audio.AudioEnum;
import ServerSide.AudioManager;

import java.util.Random;

public abstract class Ship {

    private final int size;
    private int[] coordinates;
    private int healthPoints;
    private boolean isAfloat;
    private final AudioEnum[] hitSounds;

    public Ship(int size, int[] coordinates, AudioEnum... hitSounds) {
        this.size = size;
        this.healthPoints = size;
        setCoordinates(coordinates);
        isAfloat = true;

        if (hitSounds.length == 0) {
            throw new IllegalArgumentException ("Every ship needs at least 1 specific sound");
        }

        this.hitSounds = hitSounds;
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

    public void playHitSound() {
        int randomIndex = new Random().nextInt(hitSounds.length);
        AudioEnum chosenSound = hitSounds[randomIndex];

        AudioManager.getInstance().playHit(chosenSound);
    }
}