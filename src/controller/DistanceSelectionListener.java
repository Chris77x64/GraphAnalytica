package controller;

import model.State;
import view.GraphView;
import y.base.Node;
import y.geom.YPoint;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by chris on 11.01.2018.
 */
public class DistanceSelectionListener implements ActionListener {

    private GraphView graphView;
    private JTextField box;
    private JDialog jDialog;
    private int type;

    public DistanceSelectionListener(GraphView graphView, JTextField box, JDialog jDialog,int type) {
        this.graphView = graphView;
        this.box = box;
        this.jDialog = jDialog;
        this.type = type;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String element = this.box.getText().toString();
        double distance  = Double.parseDouble(element);
        this.graphView.setAddVertexDistance(distance);

        if( type == 0) {
            this.graphView.setSelectState(State.SELECT_SECOND_VERTEX);
            Node selectedNode = this.graphView.getGraph().getSelectedNode();
            YPoint selectedNodePosition = this.graphView.getGraph2D().getCenter(selectedNode);
            YPoint initialPositionText = new YPoint(selectedNodePosition.getX(), selectedNodePosition.getY() - 50);

            this.graphView.setAddVertexDistanceText(initialPositionText, "0",0);
            this.graphView.setAddVertexDistanceCircle(selectedNodePosition, distance);
        }
        else if( type == 1){
            this.graphView.setSelectState(State.SELECT_THIRD_VERTEX);
            Node firstSelectedNode = this.graphView.getGraph().getSelectedNode();
            Node secondSelectedNode = this.graphView.getGraph().getSecondSelectedNode();

            YPoint selectedNodePosition = this.graphView.getGraph2D().getCenter(firstSelectedNode);
            YPoint secondSelectedNodePosition = this.graphView.getGraph2D().getCenter(secondSelectedNode);

            YPoint initialPositionText = new YPoint(selectedNodePosition.getX(), selectedNodePosition.getY() - 50);
            this.graphView.setAddVertexDistanceText(initialPositionText, "0",1);

            this.graphView.setAddVertexDistanceCircle(selectedNodePosition, distance);
            this.graphView.setAddVertexDistanceCircle2(secondSelectedNodePosition,distance);

        }


        this.jDialog.setVisible(false);
        this.jDialog.dispose();
    }
}
