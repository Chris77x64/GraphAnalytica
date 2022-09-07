package view.error;

import realizations.RealizationError;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by chris on 19.01.2018.
 */
public class RealizationErrorView extends JDialog {

    private JLabel errorText;
    private int errorType;

    private BufferedImage errorImage;

    private JButton jButton;

    private JPanel top;
    private JPanel center;
    private JPanel floor;


    private RealizationError error;

    public RealizationErrorView(RealizationError error) {
        super(null,"Realization Error", Dialog.ModalityType.APPLICATION_MODAL);


        this.error = error;
        this.initializeComponents();

        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - this.getWidth()) / 2;
        final int y = (screenSize.height - this.getHeight()) / 2;
        this.setLocation(x, y);

        super.setVisible(true);
    }


    private void initializeComponents(){

        switch (error){
            case ERROR_4_COLORING: {
                this.errorText = new JLabel("The given graph is not 4-Colorable");
                break;
            }
            case ERROR_PARTITION:{
                this.errorText = new JLabel("The given relationships are not valid");
                break;
            }
            case ERROR_CATERPILLAR:{
                this.errorText = new JLabel("The subgraph induced by short Edges is not a spanning caterpillar");
                break;
            }
            case ERROR_SUBGRAPH_INDUCED_SHORT_EDGES_IS_NOT_A_TREE: {
                this.errorText = new JLabel("The subgraph induced by short Edges is not a tree");
                break;
            }
            case ERROR_HAMILTONIAN:{
                this.errorText = new JLabel("The subgraph induced by short Edges is not a Hammiltonial Circle");
                break;
            }
            case ERROR_NOT_CIRCULAR:{
                this.errorText = new JLabel("The given Graph is not circular");
                break;
            }
            case ERROR_NOT_WHEEL:{
                this.errorText = new JLabel("The given Graph is not a wheel graph");
                break;
            }
            case ERROR_NOT_2TREE: {
                this.errorText = new JLabel("The given Graph is not a 2-Tree");
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
