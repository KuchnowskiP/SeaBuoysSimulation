package pl.edu.pwr.pkuchnowski.statki.Buoy;

/** @author Piotr Kuchnowski
 *  The Buoy class provides variables and methods for buoy object, such as positioning on the map or indexing*/
public class Buoy {
    int index;      //buoy index
    int posY;       //buoy vertical position on the map
    int posX;       //buoy horizontal position on the map

    public void setBuoyPosition(Buoy buoy){
        int posX = 0;
        int posY = 0;
        for(int i = 0; i < 8; i++) {
            if ((buoy.getIndex() - 1) % 8 == i){
                posX = i * 5 + 2;
            }
            if((buoy.getIndex()- 1) / 8 == i){
                posY = i * 5 + 2;
            }
        }
        buoy.setPosX(posX);
        buoy.setPosY(posY);
    }
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

}
