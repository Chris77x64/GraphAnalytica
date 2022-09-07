package SelectionView;

import controller.HamiltonianCycleSelectionListener;
import controller.HamiltonianPathSelectionListener;
import view.GraphView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by chris on 02.02.2018.
 */
public class HammiltonianPathGraphView extends JDialog{

    private final int UPPER_BOUND_VERTICES = 500;
    private final int INITIAL_NUM_VERTICES = 4;

    private String numVerticesComboBoxItems[];
    private String numInnerEdgesComboBoxItems[];

    private JLabel textSpine;
    private JLabel textLeaf;

    private JComboBox numVerticesComboBox;
    private JComboBox numInnerEdgesComboBox;

    private JButton jButton;

    private JPanel top;
    private JPanel center;
    private JPanel floor;

    private GraphView graphView;

    public HammiltonianPathGraphView(GraphView graphView) {

        super(null,"#Hammiltonian Path Graph", Dialog.ModalityType.APPLICATION_MODAL);
        super.setLocationRelativeTo(null);

        this.graphView = graphView;
        this.initializeComponents();

    }

    /*
      https://www.geeksforgeeks.org/dynamic-programming-set-9-binomial-coefficient/
       */
    private int binomialCoefficient( int n, int k){

        if( n == k || k == 0){
            return 1;
        }

        int[][] bottomUpDP = new int[n+1][k+1];

        for( int i=0; i< bottomUpDP.length; i++ ){

            int upperBound = Math.min(i,k)+1;

            for( int j = 0; j < upperBound; j++){
                if( j == 0|| j== i){
                    bottomUpDP[i][j] = 1;
                }
                else{
                    bottomUpDP[i][j] = bottomUpDP[i-1][j] + bottomUpDP[i-1][j-1];
                }
            }
        }
        return bottomUpDP[n][k];
    }


    private int calculateUpperBoundInnerEdges(int n){
        return binomialCoefficient(n,2)-n+1;
    }

    private void initializeComponents(){
        this.textSpine = new JLabel("Please select the number of vertices");
        this.textLeaf = new JLabel("Please select the number of inner Edges");

        this.numVerticesComboBoxItems = new String[UPPER_BOUND_VERTICES-INITIAL_NUM_VERTICES+1];
        int initialNumInnerEdges = calculateUpperBoundInnerEdges(INITIAL_NUM_VERTICES);
        this.numInnerEdgesComboBoxItems = new String[initialNumInnerEdges+1];

        for( int i=0; i < numVerticesComboBoxItems.length ; i++){
            numVerticesComboBoxItems[i] = ""+(i+INITIAL_NUM_VERTICES);
        }

        for( int i=0 ; i < initialNumInnerEdges+1; i++){
            numInnerEdgesComboBoxItems[i] = ""+i;
        }

        this.numVerticesComboBox = new JComboBox(numVerticesComboBoxItems);
        this.numInnerEdgesComboBox = new JComboBox(numInnerEdgesComboBoxItems);

        this.top = new JPanel();
        top.add(textSpine);
        top.add(numVerticesComboBox);

        this.center = new JPanel();
        center.add(textLeaf);
        center.add(numInnerEdgesComboBox);

        this.jButton = new JButton("Select");
        this.jButton.addActionListener(new HamiltonianPathSelectionListener(graphView,this));

        this.floor = new JPanel();
        floor.add(jButton);

        Container contentPane = super.getContentPane();
        contentPane.add(top,BorderLayout.NORTH);
        contentPane.add(center,BorderLayout.CENTER);
        contentPane.add(floor,BorderLayout.SOUTH);

        super.setContentPane(contentPane);
        super.pack();

        this.numVerticesComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String element = numVerticesComboBox.getSelectedItem().toString();
                int numberVertices = Integer.parseInt(element);
                int bound = calculateUpperBoundInnerEdges(numberVertices);
                numInnerEdgesComboBoxItems = new String[bound+1];
                for( int i=0 ; i < numInnerEdgesComboBoxItems.length; i++){
                    numInnerEdgesComboBoxItems[i] = ""+i;
                }
                center.remove(numInnerEdgesComboBox);
                numInnerEdgesComboBox = new JComboBox(numInnerEdgesComboBoxItems);
                center.add(numInnerEdgesComboBox);
                center.updateUI();
                setSize(getPreferredSize());
                pack();
            }
        });
    }

    public JComboBox getNumVerticesComboBox() {
        return numVerticesComboBox;
    }

    public JComboBox getNumInnerEdgesComboBox() {
        return numInnerEdgesComboBox;
    }

}
