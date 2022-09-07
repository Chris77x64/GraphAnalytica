package SelectionView;

import demonstration.demoBiggerCircleCircleIntersection;
import demonstration.demoCircleCircleIntersection;
import view.GraphView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by chris on 02.03.2018.
 */
public class demoCircleBiggerSelectionView extends JDialog {

    private final int FRAME_WIDTH = 700;
    private final int FRAME_HEIGHT = 800;
    private final int UPPER_BOUND_VERTICES = 200;

    private JLabel textDistance;
    private JLabel textAngle;
    private JLabel textRadiusBigger;
    private JLabel textRadiusSmaller;

    private JTextField distanceField;
    private JTextField angleField;
    private JTextField radiusBiggerField;
    private JTextField radiusSmallerField;

    private JButton jButton;

    private JPanel top;
    private JPanel center1;
    private JPanel center2;
    private JPanel center3;
    private JPanel floor;

    private GraphView graphView;


    public demoCircleBiggerSelectionView(GraphView graphView) {

        super(null,"#Fit Unequal Circle Intersection Ellipse",ModalityType.APPLICATION_MODAL);
        super.setSize(FRAME_WIDTH,FRAME_HEIGHT);
        super.setLocationRelativeTo(null);

        this.graphView = graphView;
        this.initializeComponents();

        this.setVisible(true);
    }

    private void initializeComponents(){

        this.textDistance = new JLabel("Please select the distance between two centers");
        this.textAngle = new JLabel("Please select the angle between two centers");
        this.textRadiusBigger = new JLabel("Please select the bigger radius");
        this.textRadiusSmaller = new JLabel("Please select the smaller radius");


        this.distanceField = new JTextField();
        this.radiusBiggerField = new JTextField();
        this.radiusSmallerField = new JTextField();
        this.angleField = new JTextField();

        this.distanceField.setPreferredSize(new Dimension(200,30));
        this.radiusBiggerField.setPreferredSize(new Dimension(200,30));
        this.radiusSmallerField.setPreferredSize(new Dimension(200,30));
        this.angleField.setPreferredSize(new Dimension(200,30));


        this.top = new JPanel();
        top.add(textDistance);
        top.add(distanceField);

        this.center1 = new JPanel();
        center1.add(textAngle);
        center1.add(angleField);

        this.center2 = new JPanel();
        center2.add(textRadiusBigger);
        center2.add(radiusBiggerField);

        this.center3 = new JPanel();
        center3.add(textRadiusSmaller);
        center3.add(radiusSmallerField);


        this.jButton = new JButton("Select");
        this.jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String distanceString = distanceField.getText().toString();
                double distance = Double.parseDouble(distanceString);

                String angleString = angleField.getText().toString();
                double angle = Double.parseDouble(angleString);

                String radiusBiggerString = radiusBiggerField.getText().toString();
                double radiusBigger = Double.parseDouble(radiusBiggerString);

                String radiusSmallerString = radiusSmallerField.getText().toString();
                double radiusSmaller = Double.parseDouble(radiusSmallerString);

                demoBiggerCircleCircleIntersection demo = new demoBiggerCircleCircleIntersection(graphView,angle,distance,radiusSmaller,radiusBigger);

                setVisible(false);
                dispose();

            }
        });

        this.floor = new JPanel();
        floor.add(jButton);

        Container contentPane = super.getContentPane();


        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.add(top);
        contentPane.add(center1);
        contentPane.add(center2);
        contentPane.add(center3);
        contentPane.add(floor);


       /*
        contentPane.add(top,BorderLayout.NORTH);
        contentPane.add(center1,BorderLayout.EAST);
        contentPane.add(center2,BorderLayout.CENTER);
        contentPane.add(floor,BorderLayout.SOUTH);
        */

        super.setContentPane(contentPane);
        super.pack();
    }


}
