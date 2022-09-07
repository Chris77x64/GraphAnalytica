package model;

import y.base.Edge;
import y.view.Graph2D;

/**
 * Created by chris on 13.12.2017.
 */
public class EdgeModel {

    private int index;

    private double sourceCenterX;
    private double sourceCenterY;

    private double targetCenterX;
    private double targetCenterY;

    public EdgeModel( int index, double sourceCenterX, double sourceCenterY, double targetCenterX, double targetCenterY){
        this.index = index;
        this.sourceCenterX = sourceCenterX;
        this.sourceCenterY = sourceCenterY;
        this.targetCenterX = targetCenterX;
        this.targetCenterY = targetCenterY;
    }


}
