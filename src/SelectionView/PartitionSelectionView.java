package SelectionView;

import controller.PartitionSelectionListener;
import view.GraphView;

import javax.swing.*;
import java.awt.*;

/**
 * Created by chris on 08.12.2017.
 */
public class PartitionSelectionView extends JDialog {

    private final int FRAME_WIDTH = 500;
    private final int FRAME_HEIGHT = 400;

    private JLabel text;
    private JTextField jTextField;
    private JButton jButton;
    private LayoutManager layoutManager;

    private JPanel top;
    private JPanel center;
    private JPanel floor;

    private GraphView graphView;

    public PartitionSelectionView(GraphView graphView){
        super(null,"Select your Partition",ModalityType.APPLICATION_MODAL);
        super.setSize(FRAME_WIDTH,FRAME_HEIGHT);
        super.setLocationRelativeTo(null);
        //super.setLocation();
        this.graphView = graphView;
        this.initializeComponents();
    }

    private void initializeComponents(){
        this.text = new JLabel("Please select the number of Partitions,you want to handle with");
        this.jTextField = new JTextField();
        //this.jTextField.setSize(super.getWidth()/2,super.getHeight()/2);
        this.jTextField.setPreferredSize(new Dimension(200,30));


        this.top = new JPanel();
        top.add(text);

        this.center = new JPanel();
        center.add(jTextField);

        this.jButton = new JButton("Select");
        this.jButton.addActionListener(new PartitionSelectionListener(this.graphView,this.jTextField,this));
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
