import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class noteabut extends JFrame {
 
    noteabut() {
         ImageIcon noteappi=new ImageIcon(ClassLoader.getSystemResource("myicon.jpeg"));
          Image icon=noteappi.getImage();
    setIconImage(icon);

        setSize(500, 500);
        setLocation(450, 150);
      //  setIconImage(null);
      
        // Add the image
        ImageIcon backgroundImage = new ImageIcon(ClassLoader.getSystemResource("ABOUT2.png")); // Replace "about.jpeg" with the path to your image
        Image i = backgroundImage.getImage().getScaledInstance(500, 500, Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(i);
        JLabel i3 = new JLabel(i2);

        // Add mouse listener to the label
        i3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setVisible(false); // Hide the window when clicked
            }
        });

        // Add key listener to the label
        i3.setFocusable(true); // Allow the label to gain focus for key events
        i3.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    setVisible(false); // Hide the window when Enter key is pressed
                }
            }
        });

        add(i3);
        setUndecorated(true);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new noteabut();
    }
}
