package controller;

import graphClasses.HamiltonianCircleGraph;
import view.GraphView;
import SelectionView.HammiltonianCircleGraphView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by chris on 16.01.2018.
 */
public class HamiltonianCycleSelectionListener implements ActionListener {

    private GraphView graphView;
    private HammiltonianCircleGraphView hammiltonianCircleGraphView;


    public HamiltonianCycleSelectionListener(GraphView graphView, HammiltonianCircleGraphView hammiltonianCircleGraphView) {
        this.graphView = graphView;
        this.hammiltonianCircleGraphView = hammiltonianCircleGraphView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String elementVertices = this.hammiltonianCircleGraphView.getNumVerticesComboBox().getSelectedItem().toString();
        int numVertices = Integer.parseInt(elementVertices);
        String elementInnerEgde = this.hammiltonianCircleGraphView.getNumInnerEdgesComboBox().getSelectedItem().toString();
        int numInnerEdges = Integer.parseInt(elementInnerEgde);

        HamiltonianCircleGraph hamiltonianCircleGraph = new HamiltonianCircleGraph(graphView,numVertices,numInnerEdges);

        this.hammiltonianCircleGraphView.setVisible(false);
        this.hammiltonianCircleGraphView.dispose();
    }



}
