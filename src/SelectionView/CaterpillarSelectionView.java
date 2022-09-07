package SelectionView;

import controller.CaterpillarSelectionListener;
import controller.VertexSelectionListener;
import view.GraphView;

import javax.swing.*;
import java.awt.*;

/**
 * Created by chris on 03.01.2018.
 */
public class CaterpillarSelectionView extends JDialog {

    private final int FRAME_WIDTH = 700;
    private final int FRAME_HEIGHT = 400;
    private final int UPPER_BOUND_VERTICES = 200;

    private String spineComboBoxItems[];
    private String leafComboBoxItems[];

    private JLabel textSpine;
    private JLabel textLeaf;

    private JComboBox spineVertexComboBox;
    private JComboBox leafVertexComboBox;

    private JButton jButton;

    private JPanel top;
    private JPanel center;
    private JPanel floor;

    private GraphView graphView;


    public CaterpillarSelectionView(GraphView graphView) {

        super(null,"#Create Caterpillar Graph",ModalityType.APPLICATION_MODAL);
        super.setSize(FRAME_WIDTH,FRAME_HEIGHT);
        super.setLocationRelativeTo(null);

        this.graphView = graphView;
        this.initializeComponents();
    }

    private void initializeComponents(){
        this.textSpine = new JLabel("Please select the number of spine vertices");
        this.textLeaf = new JLabel("Please select the number of leaf vertices");

        this.spineComboBoxItems = new String[UPPER_BOUND_VERTICES];
        this.leafComboBoxItems = new String[UPPER_BOUND_VERTICES];

        for( int i=0; i < spineComboBoxItems.length ; i++){
            spineComboBoxItems[i] = ""+(i+1);
            leafComboBoxItems[i] = ""+(i+1);
        }

        this.spineVertexComboBox = new JComboBox(spineComboBoxItems);
        this.leafVertexComboBox = new JComboBox(leafComboBoxItems);

        this.top = new JPanel();
        top.add(textSpine);
        top.add(spineVertexComboBox);

        this.center = new JPanel();
        center.add(textLeaf);
        center.add(leafVertexComboBox);

        this.jButton = new JButton("Select");
        this.jButton.addActionListener(new CaterpillarSelectionListener(this.graphView,this.spineVertexComboBox,this.leafVertexComboBox,this));

        this.floor = new JPanel();
        floor.add(jButton);

        Container contentPane = super.getContentPane();
        contentPane.add(top,BorderLayout.NORTH);
        contentPane.add(center,BorderLayout.CENTER);
        contentPane.add(floor,BorderLayout.SOUTH);

        super.setContentPane(contentPane);
        super.pack();
    }
}
