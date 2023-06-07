package pl.edu.pwr.pkuchnowski.statki.Ship;

import java.util.Random;

/** @author Piotr Kuchnowski
 *  The Ship class provides variables and methods for ship object*/

public class Ship {
    int posX;
    int posY;
    int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public String move(){
        Random random = new Random();
        int direction = Math.abs(random.nextInt()%8);
        return "move;" + direction;
    }

    public String scan(){
        return "scan;scan";
    }
}
