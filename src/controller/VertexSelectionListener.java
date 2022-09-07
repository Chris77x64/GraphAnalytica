package controller;

import graphClasses.*;
import view.GraphView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by chris on 22.12.2017.
 */
public class VertexSelectionListener implements ActionListener {

    private GraphView graphView;
    private JComboBox box;
    private JDialog jDialog;

    /*
    Typs:
    0 = wheel
     */
    private int type;

    public VertexSelectionListener(GraphView graphView, JComboBox box, JDialog dialog, int type){
        this.graphView = graphView;
        this.box = box;
        this.jDialog = dialog;
        this.type = type;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String element = this.box.getSelectedItem().toString();
        int numberVertices = Integer.parseInt(element);
        if( type == 0){
            WheelGraph wheelGraph = new WheelGraph(numberVertices,graphView);
        }
        else if( type == 1){
            CircleGraph circleGraph = new CircleGraph(numberVertices,graphView);
        }
        else if( type == 3){
            TwoTreeGraph twoTreeGraph = new TwoTreeGraph(numberVertices,graphView);
        }
        else if( type == 4){
            RootedTree rootedTree = new RootedTree(graphView,numberVertices);
        }
        else if( type == 5){
            PrismGraph prismGraph = new PrismGraph(numberVertices,graphView);
        }
        else if( type == 6){
            RectangularGraph rectangularGraph = new RectangularGraph(graphView,numberVertices);
        }
        else if( type == 7){
            DoublePrismGraph doublePrismGraph = new DoublePrismGraph(graphView,numberVertices,false);
        }
        else if( type == 8){
            DoublePrismGraph doublePrismGraph = new DoublePrismGraph(graphView,numberVertices,true);
        }
        else if( type == 9){
            LozengeGraph lozengeGraph = new LozengeGraph(graphView,numberVertices);
        }
        else if( type == 12){
            BipartiteK3M bipartiteK3M = new BipartiteK3M(graphView,numberVertices);
        }
        else if( type == 11){
            SunflowerGraph sunflowerGraph = new SunflowerGraph(numberVertices,graphView);
        }

        this.graphView.getRelationshipsView().resetEdgeColoring();
        this.graphView.getRelationshipsView().resetPartitionLabels();
        this.jDialog.setVisible(false);
        this.jDialog.dispose();
    }
}
