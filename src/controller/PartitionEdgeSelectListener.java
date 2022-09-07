package controller;

import view.GraphView;
import y.base.Edge;
import y.base.EdgeCursor;
import y.base.Node;
import y.view.Graph2D;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by chris on 08.12.2017.
 */
public class PartitionEdgeSelectListener implements ActionListener{

    private GraphView graphView;
    private JTextField textField;
    private JDialog jDialog;


    public PartitionEdgeSelectListener(GraphView graphView, JTextField box, JDialog dialog) {
        this.graphView = graphView;
        this.textField = box;
        this.jDialog = dialog;
    }

    @Override
    public void actionPerformed(ActionEvent event) {

            String element = textField.getText();
            int k = Integer.parseInt(element);

            Node source = this.graphView.getGraph().getSelectedNode();
            Node target = this.graphView.getGraph().getSecondSelectedNode();

            Edge toHandle = null;
            Graph2D graph2D = this.graphView.getGraph2D();
            for(EdgeCursor e = graph2D.edges(); e.ok(); e.next()) {
                Edge currentEdge = e.edge();
                Node startVertex = currentEdge.source();
                Node endVertex = currentEdge.target();

                if( (startVertex.index() == source.index() && endVertex.index() ==  target.index()) ||
                        (startVertex.index() == target.index() && endVertex.index() == source.index()) ) {
                toHandle = currentEdge;
                break;
                }
            }

            if( this.graphView.getRelationships().getAllEdgesInPartitions().contains(toHandle)){
                this.graphView.getRelationships().updateEdge(k,toHandle);
            }
            else{
                this.graphView.getRelationships().insertEdgeInPartition(k,toHandle);
            }

            //System.out.println(""+toHandle);
            //this.graphView.getRelationshipsView().updateEdgeColoring();
            //this.graphView.getRelationshipsView().updatePartitionLabels();
            this.jDialog.setVisible(false);
            this.jDialog.dispose();
        }








}
