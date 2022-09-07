package controller;

import SelectionView.HammiltonianPathGraphView;
import graphClasses.HamiltonianCircleGraph;
import graphClasses.HamiltonianPathGraph;
import view.GraphView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by chris on 02.02.2018.
 */
public class HamiltonianPathSelectionListener implements ActionListener{

    private GraphView graphView;
    private HammiltonianPathGraphView hammiltonianPathGraphView;

    public HamiltonianPathSelectionListener(GraphView graphView, HammiltonianPathGraphView hammiltonianPathGraphView) {
        this.graphView = graphView;
        this.hammiltonianPathGraphView = hammiltonianPathGraphView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String elementVertices = this.hammiltonianPathGraphView.getNumVerticesComboBox().getSelectedItem().toString();
        int numVertices = Integer.parseInt(elementVertices);
        String elementInnerEgde = this.hammiltonianPathGraphView.getNumInnerEdgesComboBox().getSelectedItem().toString();
        int numInnerEdges = Integer.parseInt(elementInnerEgde);

        HamiltonianPathGraph hamiltonianPathGraph = new HamiltonianPathGraph(graphView,numVertices,numInnerEdges);

        this.hammiltonianPathGraphView.setVisible(false);
        this.hammiltonianPathGraphView.dispose();

    }
}

