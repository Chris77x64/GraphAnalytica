package SelectionView;

import controller.PartitionEdgeSelectListener;
import controller.VertexSelectionListener;
import view.GraphView;

import javax.swing.*;
import java.awt.*;

/**
 * Created by chris on 22.12.2017.
 */
public class NodeSelectionView extends JDialog {

    private final int FRAME_WIDTH = 500;
    private final int FRAME_HEIGHT = 400;

    private int UPPER_BOUND_VERTICES;
    private int LOWER_BOUND_VERTICES;

    private String comboBoxItems[];

    private JLabel text;
    private JComboBox nodeComboBox;
    private JButton jButton;
    private LayoutManager layoutManager;

    private JPanel top;
    private JPanel center;
    private JPanel floor;

    private GraphView graphView;
    private int type;

    public NodeSelectionView(int lowerBoundVertices,int upperBoundVertices, GraphView graphView,int type){
        super(null,"#Vertex Selection",ModalityType.APPLICATION_MODAL);
        super.setSize(FRAME_WIDTH,FRAME_HEIGHT);
        super.setLocationRelativeTo(null);

        this.LOWER_BOUND_VERTICES = lowerBoundVertices;
        this.UPPER_BOUND_VERTICES = upperBoundVertices;

        this.graphView = graphView;
        this.type = type;
        this.initializeComponents();
    }

    private void initializeComponents(){
        if( type == 5 || type == 7 || type == 8){
            this.text = new JLabel("Please select the number of cycle vertices");
        }
        else if( type == 6){
            this.text = new JLabel("Please select the number of rectangles");
        }
        else if( type == 12){
            this.text = new JLabel("Please select the number m of vertices");
        }
        else{
            this.text = new JLabel("Please select the number of vertices");
        }


        this.comboBoxItems = new String[UPPER_BOUND_VERTICES-LOWER_BOUND_VERTICES];
        for( int i=0; i < comboBoxItems.length ; i++){
            comboBoxItems[i] = ""+(LOWER_BOUND_VERTICES+i);
        }
        this.nodeComboBox = new JComboBox(comboBoxItems);


        this.top = new JPanel();
        top.add(text);

        this.center = new JPanel();
        center.add(nodeComboBox);

        this.jButton = new JButton("Select");
        this.jButton.addActionListener(new VertexSelectionListener(graphView,nodeComboBox,this,type));
        this.floor = new JPanel();
        floor.add(jButton);

        Container contentPane = super.getContentPane();
        contentPane.add(top,BorderLayout.NORTH);
        contentPane.add(center,BorderLayout.CENTER);
        contentPane.add(floor,BorderLayout.SOUTH);
        this.layoutManager = new CardLayout();
        super.setContentPane(contentPane);
        super.pack();
    }


}
