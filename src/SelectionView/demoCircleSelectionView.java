package SelectionView;

import controller.CaterpillarSelectionListener;
import demonstration.demoCircleCircleIntersection;
import view.GraphView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

/**
 * Created by chris on 02.03.2018.
 */
public class demoCircleSelectionView extends JDialog {
    private final int FRAME_WIDTH = 700;
    private final int FRAME_HEIGHT = 800;
    private final int UPPER_BOUND_VERTICES = 200;

    private JLabel textDistance;
    private JLabel textAngle;
    private JLabel textRadius;

    private JTextField distanceField;
    private JTextField angleField;
    private JTextField radiusField;

    private JButton jButton;

    private JPanel top;
    private JPanel center1;
    private JPanel center2;
    private JPanel floor;

    private GraphView graphView;


    public demoCircleSelectionView(GraphView graphView) {

        super(null,"#Fit Equal Circle Intersection Ellipse",ModalityType.APPLICATION_MODAL);
        super.setSize(FRAME_WIDTH,FRAME_HEIGHT);
        super.setLocationRelativeTo(null);

        this.graphView = graphView;
        this.initializeComponents();

        this.setVisible(true);
    }

    private void initializeComponents(){

        this.textDistance = new JLabel("Please select the distance between two centers");
        this.textAngle = new JLabel("Please select the angle between two centers");
        this.textRadius = new JLabel("Please select the radius for those 2 circles");


        this.distanceField = new JTextField();
        this.radiusField = new JTextField();
        this.angleField = new JTextField();

        this.distanceField.setPreferredSize(new Dimension(200,30));
        this.radiusField.setPreferredSize(new Dimension(200,30));
        this.angleField.setPreferredSize(new Dimension(200,30));


        this.top = new JPanel();
        top.add(textDistance);
        top.add(distanceField);

        this.center1 = new JPanel();
        center1.add(textAngle);
        center1.add(angleField);

        this.center2 = new JPanel();
        center2.add(textRadius);
        center2.add(radiusField);

        this.jButton = new JButton("Select");
        this.jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String distanceString = distanceField.getText().toString();
                double distance = Double.parseDouble(distanceString);

                String angleString = angleField.getText().toString();
                double angle = Double.parseDouble(angleString);

                String radiusString = radiusField.getText().toString();
                double radius = Double.parseDouble(radiusString);

                demoCircleCircleIntersection demo = new demoCircleCircleIntersection(graphView,angle,distance,radius);

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
