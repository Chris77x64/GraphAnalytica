package SelectionView;

import controller.CaterpillarSelectionListener;
import controller.kRectangularSelectionListener;
import view.GraphView;

import javax.swing.*;
import java.awt.*;

/**
 * Created by chris on 16.02.2018.
 */
public class kRectangularGraphSelectionView extends JDialog{

    private final int FRAME_WIDTH = 700;
    private final int FRAME_HEIGHT = 400;
    private final int UPPER_BOUND_VERTICES = 200;

    private String circleSizeComboBoxItems[];
    private String numCyclesComboBoxItems[];

    private JLabel textCircleSize;
    private JLabel textNumCycles;

    private JComboBox circleSizeComboBox;
    private JComboBox numCyclesComboBox;

    private JButton jButton;

    private JPanel top;
    private JPanel center;
    private JPanel floor;

    private GraphView graphView;


    public kRectangularGraphSelectionView(GraphView graphView) {

        super(null,"#Create Caterpillar Graph", Dialog.ModalityType.APPLICATION_MODAL);
        super.setSize(FRAME_WIDTH,FRAME_HEIGHT);
        super.setLocationRelativeTo(null);

        this.graphView = graphView;
        this.initializeComponents();

        super.setVisible(true);
    }

    private void initializeComponents(){
        this.textCircleSize = new JLabel("Please select the number vertices in a cycle");
        this.textNumCycles = new JLabel("Please select the number of outer layers");

        this.circleSizeComboBoxItems = new String[UPPER_BOUND_VERTICES];
        this.numCyclesComboBoxItems = new String[UPPER_BOUND_VERTICES];

        for( int i=0; i < circleSizeComboBoxItems.length ; i++){
            circleSizeComboBoxItems[i] = ""+(i+3);
            numCyclesComboBoxItems[i] = ""+(i+2);
        }

        this.circleSizeComboBox = new JComboBox(circleSizeComboBoxItems);
        this.numCyclesComboBox = new JComboBox(numCyclesComboBoxItems);

        this.top = new JPanel();
        top.add(textCircleSize);
        top.add(circleSizeComboBox);

        this.center = new JPanel();
        center.add(textNumCycles);
        center.add(numCyclesComboBox);

        this.jButton = new JButton("Select");
        this.jButton.addActionListener(new kRectangularSelectionListener(this.graphView,numCyclesComboBox,circleSizeComboBox,this));

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
