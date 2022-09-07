package util;

import model.MetricCollection;
import view.GraphView;
import y.base.Node;
import y.geom.YPoint;
import y.view.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by chris on 10.01.2018.
 */
public class Text {

    private final float BOUNDING_BOX_LINE_WIDTH = 3;
    private final float BOUNDING_BOX_WIDTH_TYPE_0 = 80;
    private final float BOUNDING_BOX_WIDTH_TYPE1 = 2*BOUNDING_BOX_WIDTH_TYPE_0;
    private final float BOUNDING_BOX_HEIGHT = 30;

    private final double X_AXIS_THRESHOLD = 150;
    private final double Y_AXIS_THRESHOLD = 50;
    private final double TEXT_RIGHT_OFFSET = 30;
    private final double DISTANCE_SELECTED_NODE_THRESHOLD = 150;

    private GraphView graphView;

    private YPoint position;

    private Shape boundingBoxShape;
    private Drawable boundingBoxDrawable;

    private String textString;
    private TextDrawable text2Draw;

    private int type;

    public Text(GraphView graphView, String text, YPoint position, int type) {
        this.graphView = graphView;
        this.textString = text;
        this.position = modifiedPosition(position);
        this.type = type;

        this.initializeText();
        this.initializeBoundingBox();
        this.draw();
    }


    private YPoint modifiedPosition( YPoint destination){

        double worldHeight = this.graphView.getWorldRect2D().getHeight();
        double worldWidth = this.graphView.getWorldRect2D().getWidth();

        Node selectedNode = this.graphView.getGraph().getSelectedNode();
        YPoint selectedVertexPosition = this.graphView.getGraph2D().getCenter(selectedNode);
        double distanceSelectedNode = MetricCollection.euklideanDistanceR2(selectedVertexPosition.x, selectedVertexPosition.y, destination.x,destination.y);

        if( worldWidth-destination.x < X_AXIS_THRESHOLD ){
            return new YPoint( destination.getX()-4*TEXT_RIGHT_OFFSET,destination.getY());
        }
        else if( destination.y < Y_AXIS_THRESHOLD){
            return new YPoint(destination.getX()+TEXT_RIGHT_OFFSET,destination.getY()+TEXT_RIGHT_OFFSET);
        }
        else if(worldHeight-destination.y < Y_AXIS_THRESHOLD){
            return new YPoint(destination.getX()+TEXT_RIGHT_OFFSET,destination.getY()-Y_AXIS_THRESHOLD);
        }
        else if( distanceSelectedNode < DISTANCE_SELECTED_NODE_THRESHOLD && destination.x < selectedVertexPosition.x){
            return new YPoint(destination.getX()-4*TEXT_RIGHT_OFFSET,destination.getY());
        }
        else{
            return new YPoint(destination.getX()+TEXT_RIGHT_OFFSET,destination.getY());
        }

    }

    private void initializeText(){
        this.text2Draw =  new TextDrawable(textString,position);
    }

    private void initializeBoundingBox(){

        double rectangleX = position.x -2;
        double rectangleY= position.y - 22;
        Rectangle2D.Double boundingBox = null;

        BasicStroke basicStroke = new BasicStroke(BOUNDING_BOX_LINE_WIDTH);

        if(type == 0){
            boundingBox = new Rectangle2D.Double( rectangleX,rectangleY, BOUNDING_BOX_WIDTH_TYPE_0,BOUNDING_BOX_HEIGHT);
        }
        else if( type==1){
            boundingBox = new Rectangle2D.Double( rectangleX,rectangleY, BOUNDING_BOX_WIDTH_TYPE1,BOUNDING_BOX_HEIGHT);
        }



        this.boundingBoxShape = basicStroke.createStrokedShape(boundingBox);
        this.boundingBoxDrawable = new ShapeDrawable(boundingBoxShape,Color.BLACK);
    }

    private void draw(){
        this.graphView.addBackgroundDrawable(text2Draw);
        this.graphView.addBackgroundDrawable(boundingBoxDrawable);
        this.graphView.updateView();
    }

    public void dispose(){
        this.graphView.removeBackgroundDrawable(this.text2Draw);
        this.graphView.removeBackgroundDrawable(boundingBoxDrawable);
        this.graphView.updateView();
    }

    public void move( YPoint newPosition, String newDistance){
        this.position = modifiedPosition(newPosition);
        this.textString = newDistance;

        this.dispose();
        this.initializeText();
        this.initializeBoundingBox();
        this.draw();
        this.graphView.updateView();

    }


}
