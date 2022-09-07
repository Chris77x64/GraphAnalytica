package controller;

import graphClasses.CaterpillarGraph;
import graphClasses.kRectangularGraph;
import view.GraphView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by chris on 16.02.2018.
 */
public class kRectangularSelectionListener implements ActionListener {

    private GraphView graphView;
    private JComboBox spineVertices;
    private JComboBox leafVertices;
    private JDialog jDialog;

    public kRectangularSelectionListener( GraphView graphView,JComboBox spineVertices,JComboBox leafVertices,JDialog jDialog ) {
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

        kRectangularGraph rectangularGraph = new kRectangularGraph(graphView,numberLeafVertices,numberSpineVertices);


        this.jDialog.setVisible(false);
        this.jDialog.dispose();
    }
}
