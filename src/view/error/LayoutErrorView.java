package view.error;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by chris on 05.02.2018.
 */
public class LayoutErrorView extends JDialog {

    private JLabel errorText;

    private BufferedImage errorImage;

    private JButton jButton;

    private JPanel top;
    private JPanel center;
    private JPanel floor;

    public LayoutErrorView(){
        super(null,"Layout Error",ModalityType.APPLICATION_MODAL);
        this.centerWindows();
        this.initializeComponents();
        super.setVisible(true);
    }


    private void centerWindows(){
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - this.getWidth()) / 2;
        final int y = (screenSize.height - this.getHeight()) / 2;
        this.setLocation(x, y);
    }

    private void initializeComponents(){


        this.errorText = new JLabel("Can't apply Layout to the given Graph");


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
