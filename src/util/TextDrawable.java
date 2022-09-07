package util;

import y.geom.YPoint;
import y.view.ShapeDrawable;

import java.awt.*;
import java.awt.geom.*;

/**
 * Created by chris on 10.01.2018.
 */
public class TextDrawable extends ShapeDrawable {

    private final Font TEXT_FONT = new Font("Arial",Font.BOLD,20);
    private final Color TEXT_COLOR = Color.RED;

    private double positionX;
    private double positionY;
    private String text;

    public TextDrawable( String text,YPoint position){
        super(new RectangularShape() {
            @Override
            public double getX() {
                return position.getX();
            }

            @Override
            public double getY() {
                return position.getY();
            }

            @Override
            public double getWidth() {
                return 150;
            }

            @Override
            public double getHeight() {
                return 150;
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

        this.positionX = position.getX();
        this.positionY = position.getY();
        this.text = text;

    }

    @Override
    public void paint(Graphics2D var1) {
        var1.setFont(TEXT_FONT);
        var1.setColor(TEXT_COLOR);
        var1.drawString(text,(float) positionX, (float) positionY);
    }

    public Shape getShape(){
        return this.getShape();
    }
}
