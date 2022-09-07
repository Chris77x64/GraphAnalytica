package util;

import y.view.ShapeDrawable;

import java.awt.*;
import java.awt.geom.*;

/**
 * Created by chris on 07.12.2017.
 */
public class String2D extends ShapeDrawable {

    private final Font TEXT_FONT = new Font("Arial",Font.BOLD,15);
    private final Color TEXT_COLOR = Color.RED;

    private double positionX;
    private double positionY;

    private double rotationAngle;

    private String text;

    public String2D( double positionX, double positionY,double rotationAngle, String text){
        super(new RectangularShape() {
            @Override
            public double getX() {
                return 0;
            }

            @Override
            public double getY() {
                return 0;
            }

            @Override
            public double getWidth() {
                return 0;
            }

            @Override
            public double getHeight() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public void setFrame(double x, double y, double w, double h) {

            }

            @Override
            public Rectangle2D getBounds2D() {
                return null;
            }

            @Override
            public boolean contains(double x, double y) {
                return false;
            }

            @Override
            public boolean intersects(double x, double y, double w, double h) {
                return false;
            }

            @Override
            public boolean contains(double x, double y, double w, double h) {
                return false;
            }

            @Override
            public PathIterator getPathIterator(AffineTransform at) {
                return null;
            }
        }, Color.BLACK, Color.BLACK);
        this.positionX = positionX;
        this.positionY = positionY;
        this.text = text;
        this.rotationAngle = rotationAngle;

    }

    @Override
    public void paint(Graphics2D var1) {

        //var1.translate((float) positionX, (float) positionY);
        //var1.rotate(this.rotationAngle);
        var1.setFont(TEXT_FONT);
        var1.setColor(TEXT_COLOR);
        var1.drawString(text,(float) positionX, (float) positionY);
       // var1.drawString(text,positionX,positionY);
       // var1.translate(-(float) positionX, -(float) positionY);
        /*
        AffineTransform originalTransformation = var1.getTransform();
        var1.rotate(this.rotationAngle,(float) positionX, (float) positionY);
        //var1.rotate(Math.toRadians(90));
        var1.setFont(TEXT_FONT);
        var1.setColor(TEXT_COLOR);

        var1.setTransform(originalTransformation);
        */
    }
}
