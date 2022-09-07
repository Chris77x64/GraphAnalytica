package view.error;

import controller.CaterpillarSelectionListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * Created by chris on 18.01.2018.
 */
public class ColoringErrorView extends JDialog {

    private JLabel errorText;
    private int errorType;

    private BufferedImage errorImage;

    private JButton jButton;

    private JPanel top;
    private JPanel center;
    private JPanel floor;



    public ColoringErrorView(int errorType) {
        super(null,"Coloring Error",ModalityType.APPLICATION_MODAL);
        //super.setLocationRelativeTo();

        this.errorType = errorType;
        this.initializeComponents();

        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - this.getWidth()) / 2;
        final int y = (screenSize.height - this.getHeight()) / 2;
        this.setLocation(x, y);

        super.setVisible(true);

        //super.setLocation();

    }

    private void initializeComponents(){

        switch (errorType){
            case 2: {
                this.errorText = new JLabel("The given graph is not Bipatit");
                break;
            }
            case 3:{
                this.errorText = new JLabel("The given graph is not 3-Colorable");
                break;
            }
            case 4:{
                this.errorText = new JLabel("The given graph is not 4-Colorable");
                break;
            }
        }

        this.center = new JPanel();
        center.setBackground(Color.WHITE);
        this.top = new JPanel();
        top.setBackground(Color.WHITE);
        this.errorText.setFont(new Font("Arial",1,20));
        center.add(errorText);



        try {
            this.errorImage = ImageIO.read(new File("res/error.jpg"));

        }catch (IOException e){

        }
        if( errorImage != null){
            ImageIcon imageIcon = new ImageIcon(errorImage);
            JLabel label = new JLabel(imageIcon);
            top.add(label);

        }
        this.jButton = new JButton("Okay");
        this.jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });

        this.floor = new JPanel();
        this.floor.setBackground(Color.WHITE);
        jButton.setBackground(Color.BLACK);
        jButton.setForeground(Color.WHITE);
        floor.add(jButton);

        Container contentPane = super.getContentPane();
        contentPane.add(top,BorderLayout.NORTH);
        contentPane.add(center,BorderLayout.CENTER);
        contentPane.add(floor,BorderLayout.SOUTH);

        super.setContentPane(contentPane);
        super.pack();
    }
}
