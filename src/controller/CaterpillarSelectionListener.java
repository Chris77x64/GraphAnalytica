package controller;

import graphClasses.CaterpillarGraph;
import graphClasses.CircleGraph;
import graphClasses.WheelGraph;
import view.GraphView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by chris on 03.01.2018.
 */
public class CaterpillarSelectionListener implements ActionListener{

    private GraphView graphView;
    private JComboBox spineVertices;
    private JComboBox leafVertices;
    private JDialog jDialog;

    public CaterpillarSelectionListener( GraphView graphView,JComboBox spineVertices,JComboBox leafVertices,JDialog jDialog ) {
        this.graphView = graphView;
        this.spineVertices = spineVertices;
        this.leafVertices = leafVertices;
        this.jDialog = jDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String elementSpine = this.spineVertices.getSelectedItem().toString();
        int numberSpineVertices = Integer.parseInt(elementSpine);
        String elementLeaf = this.leafVertices.getSelectedItem().toString();
        int numberLeafVertices = Integer.parseInt(elementLeaf);

        CaterpillarGraph caterpillarGraph = new CaterpillarGraph(numberLeafVertices,numberSpineVertices,this.graphView);

        this.graphView.getRelationshipsView().resetEdgeColoring();
        this.graphView.getRelationshipsView().resetPartitionLabels();
        this.jDialog.setVisible(false);
        this.jDialog.dispose();
    }
}
