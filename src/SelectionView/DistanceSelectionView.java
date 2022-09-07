package SelectionView;

import controller.DistanceSelectionListener;
import controller.PartitionSelectionListener;
import view.GraphView;
import y.base.Node;

import javax.swing.*;
import java.awt.*;

/**
 * Created by chris on 11.01.2018.
 */
public class DistanceSelectionView extends JDialog {


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

        private int type;


    public DistanceSelectionView(GraphView graphView, int type) {
        super(null,"Select the preferred Distance",ModalityType.APPLICATION_MODAL);
        this.graphView = graphView;
        super.setSize(FRAME_WIDTH,FRAME_HEIGHT);
        super.setLocationRelativeTo(null);
        this.type = type;
        this.initializeComponents();

    }


    private void initializeComponents(){
        if( type == 0) {
            Node selectedNode = this.graphView.getGraph().getSelectedNode();
            this.text = new JLabel("Please select the desired distance from Node: " + selectedNode.index());
        }
        else if( type==1){
            Node firstSelectedNode = this.graphView.getGraph().getSelectedNode();
            Node secondSelectedNode = this.graphView.getGraph().getSecondSelectedNode();
            this.text = new JLabel("Please select the desired distance from Node: "+firstSelectedNode.index()+" and "+secondSelectedNode.index());
        }
        this.jTextField = new JTextField();
        //this.jTextField.setSize(super.getWidth()/2,super.getHeight()/2);
        this.jTextField.setPreferredSize(new Dimension(200,30));


        this.top = new JPanel();
        top.add(text);

        this.center = new JPanel();
        center.add(jTextField);

        this.jButton = new JButton("Select");
        this.jButton.addActionListener(new DistanceSelectionListener(this.graphView,this.jTextField,this,type));
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
