package model;

import y.base.Node;
import y.view.Graph2D;

/**
 * Created by chris on 06.12.2017.
 */

public class VertexModel {


    private double positionCenterX;
    private double positionCenterY;

    private int id;

    public VertexModel(double positionCenterX,double positionCenterY,int id){
        this.positionCenterX = positionCenterX;
        this.positionCenterY = positionCenterY;
        this.id = id;
    }

    public double getPositionCenterX() {
        return positionCenterX;
    }

    public void setPositionCenterX(double positionCenterX) {
        this.positionCenterX = positionCenterX;
    }

    public double getPositionCenterY() {
        return positionCenterY;
    }

    public void setPositionCenterY(double positionCenterY) {
        this.positionCenterY = positionCenterY;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
