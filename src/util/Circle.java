package util;

import view.GraphView;
import y.geom.YPoint;
import y.view.ShapeDrawable;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by chris on 10.01.2018.
 */
public class Circle {

    private final Color CIRCLE_COLOR = Color.RED;
    private final float LINE_WIDTH = 3;
    private final float[] DASH_PATTERN = new float[]{10,10};

    private GraphView graphView;

    private YPoint center;
    private double distance;

    private Shape circleShape;
    private ShapeDrawable circleDrawable;

    public Circle(GraphView graphView, YPoint center, double distance) {
        this.graphView = graphView;
        this.center = center;
        this.distance = distance;

        this.initializeCircle();
        this.drawCircle();
    }

    private void initializeCircle(){

        double radius = 2*distance;
        double centerX = center.getX() - (radius/2);
        double centerY = center.getY() - (radius/2);

        Ellipse2D.Double circleEllipse = new Ellipse2D.Double(centerX,centerY,radius,radius);
        BasicStroke basicStroke = new BasicStroke(LINE_WIDTH,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0f,DASH_PATTERN,0.0f);
        this.circleShape = basicStroke.createStrokedShape(circleEllipse);
        this.circleDrawable = new ShapeDrawable(circleShape,CIRCLE_COLOR);
    }

    private void drawCircle(){
        this.graphView.addBackgroundDrawable(circleDrawable);
        this.graphView.updateView();
    }

    public void dispose(){
        this.graphView.removeBackgroundDrawable(circleDrawable);
        this.graphView.updateView();
    }

}
