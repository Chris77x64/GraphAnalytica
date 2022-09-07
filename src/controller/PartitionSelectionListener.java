package controller;

import view.GraphView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by chris on 08.12.2017.
 */
public class PartitionSelectionListener implements ActionListener{

    private GraphView graphView;
    private JTextField box;
    private JDialog jDialog;

    public PartitionSelectionListener(GraphView graphView, JTextField box, JDialog dialog){
        this.graphView = graphView;
        this.box = box;
        this.jDialog = dialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

                String element = this.box.getText().toString();
                int k = Integer.parseInt(element);
                this.graphView.getRelationships().setK(k);
                this.graphView.getRelationshipsView().resetEdgeColoring();
                this.graphView.getRelationshipsView().resetPartitionLabels();
                this.jDialog.setVisible(false);
                this.jDialog.dispose();

            }
    }

