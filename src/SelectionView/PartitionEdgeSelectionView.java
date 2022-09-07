package SelectionView;

import controller.PartitionEdgeSelectListener;
import controller.PartitionSelectionListener;
import view.GraphView;

import javax.swing.*;
import java.awt.*;

/**
 * Created by chris on 08.12.2017.
 */
public class PartitionEdgeSelectionView extends JDialog {

    private final int FRAME_WIDTH = 500;
    private final int FRAME_HEIGHT = 400;

    private String comboBoxItems[];

    private JLabel text;
    private JTextField textField;
    private JButton jButton;
    private LayoutManager layoutManager;

    private JPanel top;
    private JPanel center;
    private JPanel floor;

    private GraphView graphView;

    public PartitionEdgeSelectionView (GraphView graphView){
        super(null,"Select your Partition",ModalityType.APPLICATION_MODAL);
        super.setSize(FRAME_WIDTH,FRAME_HEIGHT);
        super.setLocationRelativeTo(null);
        //super.setLocation();
        this.graphView = graphView;
        this.initializeComponents();
    }

    private void initializeComponents(){
        this.text = new JLabel("Please select the partition where the specified edge should belong to");
        this.textField = new JTextField();
        //this.textField.setSize(super.getWidth()/2,super.getHeight()/2);
        this.textField.setPreferredSize(new Dimension(200,30));

        this.top = new JPanel();
        top.add(text);

        this.center = new JPanel();
        center.add(textField);

        this.jButton = new JButton("Select");
        this.jButton.addActionListener(new PartitionEdgeSelectListener(this.graphView,this.textField,this));
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
