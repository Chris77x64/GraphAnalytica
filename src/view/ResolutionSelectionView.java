package view;

import controller.VertexSelectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by chris on 04.06.2018.
 */
public class ResolutionSelectionView extends JDialog {

    private final int FRAME_WIDTH = 500;
    private final int FRAME_HEIGHT = 400;

    private JPanel top;
    private JPanel center;
    private JPanel bottom;

    private JButton button;

    private Dimension dimension;

    private String comboBoxItems[];
    private JComboBox resolutionComboBox;

    private JLabel text;

    public ResolutionSelectionView(){
        super(null,"#Resolution Selection",ModalityType.APPLICATION_MODAL);

        super.setSize(FRAME_WIDTH,FRAME_HEIGHT);

        this.initializeComponents();

        super.setLocationRelativeTo(null);
        super.pack();
        super.setVisible(true);
    }

    private void initializeComboBox(){
        this.comboBoxItems = new String[4];
        this.comboBoxItems[0] = "2560 x 1440";
        this.comboBoxItems[1] = "1920 x 1080";
        this.comboBoxItems[2] = "1600 x 1024";
        this.comboBoxItems[3] = "1280 x 1024";

        this.resolutionComboBox = new JComboBox(comboBoxItems);
    }

    private void initializeComponents(){
        this.initializeComboBox();
        this.text = new JLabel("Please select the Resolution of your choice");

        this.top = new JPanel();
        top.add(text);

        this.center = new JPanel();
        center.add(resolutionComboBox);

        this.button = new JButton("Select");
        this.button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedResolution = (String) resolutionComboBox.getSelectedItem();

                switch(selectedResolution){
                    case "2560 x 1440":{
                        dimension = new Dimension(1850,1000);
                        break;
                    }
                    case "1920 x 1080":{
                        dimension = new Dimension(1000,1000);
                        break;
                    }
                    case "1600 x 1024":{
                        dimension = new Dimension(900,900);
                        break;
                    }
                    case "1200 x 1024":{
                        dimension = new Dimension(800,800);
                        break;
                    }
                }

                setVisible(false);

            }
        });
        this.bottom = new JPanel();
        bottom.add(button);

        Container contentPane = super.getContentPane();
        contentPane.add(top,BorderLayout.NORTH);
        contentPane.add(center,BorderLayout.CENTER);
        contentPane.add(bottom,BorderLayout.SOUTH);
        super.setContentPane(contentPane);
    }


    public Dimension getDimension() {
        return dimension;
    }

}
