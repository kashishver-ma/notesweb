
import java.awt.Image;
import javax.swing.*;

public class noteentry extends JFrame implements Runnable {
  Thread t;

  noteentry() {
    ImageIcon noteappi = new ImageIcon(ClassLoader.getSystemResource("myicon.jpeg"));
    Image icon = noteappi.getImage();
    setIconImage(icon);
    setSize(500, 500);
    setLocation(450, 150);
    ImageIcon backgroundImage = new ImageIcon(ClassLoader.getSystemResource("entry.jpg"));
    Image i11 = backgroundImage.getImage().getScaledInstance(500, 500, Image.SCALE_DEFAULT);
    ImageIcon i2 = new ImageIcon(i11);
    JLabel i3 = new JLabel(i2);
    add(i3);
    setUndecorated(true);
    setVisible(true);
    t = new Thread(this);
    t.start();
  }

  public void run() {
    try {
      Thread.sleep(5000);
      setVisible(false);
      new Noteapp();
    } catch (Exception e) {
      System.out.println("hi" + e);
    }
  }

  public static void main(String[] args) {
    new noteentry();
  }
}
