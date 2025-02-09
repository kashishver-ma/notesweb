
// Add these imports at the top of your file
import javax.swing.text.rtf.RTFEditorKit;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.text.AttributeSet;
// Add these imports at the top of your file
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.event.ChangeListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.undo.UndoManager;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
// import javax.swing.text.DefaultCaret;
import javax.swing.text.DocumentFilter;
import javax.swing.text.StyledDocument;
// import javax.swing.text.rtf.RTFEditorKit;

//import javafx.scene.image.Image;
//import javafx.scene.text.Font;

import java.awt.*;
import java.awt.event.*;
// import java.awt.font.*;
// import java.security.KeyStore;
import java.io.*;

public class Noteapp extends JFrame implements MouseListener, ActionListener {

    JTextPane textpane;
    String text = "";
    JButton imagebutton;
    JFileChooser fileChooser;
    JToggleButton themeToggleButton;
    boolean isDarkTheme = false;
    JMenuBar menubar;
    JLabel headerLabel;
    JSlider zoomSlider;
    JPanel southPanel;
    Color selectedColor = Color.BLACK; // Default text color
    private UndoManager undoManager;
    String lighttheme;
    private int stickyNoteCounter = 0;

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        JMenuItem item = (JMenuItem) e.getSource();
        item.setFont(item.getFont().deriveFont(Font.BOLD)); // Change font style
    }

    @Override
    public void mouseExited(MouseEvent e) {
        JMenuItem item = (JMenuItem) e.getSource();
        item.setForeground(Color.BLACK); // Reset text color
        item.setFont(item.getFont().deriveFont(Font.PLAIN)); // Reset font style
    }

    Noteapp() {
        setTitle("NoteApp");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon noteappi = new ImageIcon(ClassLoader.getSystemResource("myicon.jpeg"));
        Image icon = noteappi.getImage();
        setIconImage(icon);

        textpane = new JTextPane();
        textpane.getDocument().addUndoableEditListener(new UndoableEditListener() {
            public void undoableEditHappened(UndoableEditEvent e) {
                undoManager.addEdit(e.getEdit());
            }
        });

        textpane.setFont(new Font("SAN_SERIF", Font.PLAIN, 20));
        JScrollPane scrollpane = new JScrollPane(textpane);
        add(scrollpane, BorderLayout.CENTER);

        headerLabel = new JLabel("......Collect Your Thoughts......");
        Font font = new Font("Blackadder ITC", Font.BOLD, 24);
        headerLabel.setFont(font);
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(221, 221, 245));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(headerLabel, BorderLayout.NORTH);

        themeToggleButton = new JToggleButton();
        themeToggleButton.setOpaque(false);
        themeToggleButton.setContentAreaFilled(false);
        themeToggleButton.setBorderPainted(false);
        lighttheme = "dark.png";
        ImageIcon icontheme = new ImageIcon(lighttheme);
        Image scaledIcon = icontheme.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon scaledIconImage = new ImageIcon(scaledIcon);
        themeToggleButton.setIcon(scaledIconImage);
        themeToggleButton.addActionListener(this);
        themeToggleButton.setFocusPainted(false);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        headerPanel.add(themeToggleButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        southPanel = new JPanel(new BorderLayout());
        zoomSlider = new JSlider(JSlider.HORIZONTAL, 10, 60, 20);
        zoomSlider.setMajorTickSpacing(10);
        zoomSlider.setMinorTickSpacing(5);
        zoomSlider.setPaintTicks(true);
        southPanel.setOpaque(true);
        southPanel.setBackground(new Color(221, 221, 255));
        southPanel.add(zoomSlider, BorderLayout.EAST);
        add(southPanel, BorderLayout.SOUTH);

        setupMenuBar();
        setupZoomSlider();

        undoManager = new UndoManager();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupZoomSlider() {
        zoomSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int zoomLevel = zoomSlider.getValue();
                textpane.setFont(textpane.getFont().deriveFont((float) zoomLevel));
            }
        });
    }

    private void setupMenuBar() {
        menubar = new JMenuBar();
        menubar.setBackground(new Color(221, 221, 255));
        menubar.setPreferredSize(new Dimension(10, 20));

        // File Menu
        JMenu file = new JMenu("File");
        file.addMouseListener(this);
        addMenuItem(file, "New", KeyEvent.VK_N);
        addMenuItem(file, "Open", KeyEvent.VK_O);
        addMenuItem(file, "Save", KeyEvent.VK_S);
        addMenuItem(file, "Exit", KeyEvent.VK_E);
        menubar.add(file);

        // Edit Menu
        JMenu edit = new JMenu("Edit");
        edit.addMouseListener(this);
        addMenuItem(edit, "Undo", KeyEvent.VK_Z);
        addMenuItem(edit, "Redo", KeyEvent.VK_Y);
        addMenuItem(edit, "Select All", KeyEvent.VK_A);
        addMenuItem(edit, "Copy", KeyEvent.VK_C);
        addMenuItem(edit, "Paste", KeyEvent.VK_V);
        menubar.add(edit);

        // Insert Menu
        JMenu insert = new JMenu("Insert");
        insert.addMouseListener(this);
        addMenuItem(insert, "Snap", KeyEvent.VK_I);
        addMenuItem(insert, "Stickie", KeyEvent.VK_M);

        // Color chooser menu item
        JMenuItem colour = new JMenuItem("Color");
        colour.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        colour.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Choose Text Color", selectedColor);
            if (newColor != null) {
                selectedColor = newColor;
                textpane.setForeground(selectedColor);
            }
        });
        insert.add(colour);
        menubar.add(insert);

        // Help Menu
        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(this);
        help.add(about);
        menubar.add(help);

        setJMenuBar(menubar);
    }

    private void addMenuItem(JMenu menu, String name, int keyEvent) {
        JMenuItem item = new JMenuItem(name);
        item.setAccelerator(KeyStroke.getKeyStroke(keyEvent, ActionEvent.CTRL_MASK));
        item.addActionListener(this);
        menu.add(item);
    }

    private void createStickyNote() {
        JPanel stickyPanel = new JPanel();
        stickyPanel.setLayout(new BorderLayout());
        stickyPanel.setBackground(new Color(214, 205, 234));

        // Create sticky note header with close button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(194, 185, 214));
        JLabel titleLabel = new JLabel("Sticky Note " + (++stickyNoteCounter));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 0));

        JButton closeButton = new JButton("×");
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setFont(new Font("Arial", Font.BOLD, 16));
        closeButton.setForeground(Color.BLACK);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(closeButton, BorderLayout.EAST);

        // Create text area for sticky note
        JTextPane notePane = new JTextPane();
        notePane.setFont(new Font("SansSerif", Font.PLAIN, 16));
        notePane.setBackground(new Color(214, 205, 234));
        notePane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Add components to sticky panel
        stickyPanel.add(headerPanel, BorderLayout.NORTH);
        stickyPanel.add(notePane, BorderLayout.CENTER);

        // Set preferred size for the sticky note
        stickyPanel.setPreferredSize(new Dimension(200, 150));

        // Add document filter for text handling
        ((AbstractDocument) notePane.getDocument()).setDocumentFilter(new DocumentFilter() {
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                super.insertString(fb, offset, string, attr);
                if (string.equals("\n")) {
                    notePane.setCaretPosition(offset);
                }
            }

            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                super.replace(fb, offset, length, text, attrs);
                notePane.setCaretPosition(offset + text.length());
            }
        });

        // Add key listener for Enter key
        notePane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    textpane.requestFocusInWindow();
                }
            }
        });

        // Add close button action
        closeButton.addActionListener(e -> {
            Container parent = stickyPanel.getParent();
            if (parent != null) {
                parent.remove(stickyPanel);
                parent.revalidate();
                parent.repaint();
            }
        });

        // Add drag functionality
        MouseAdapter dragAdapter = new MouseAdapter() {
            private Point offset;

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    offset = e.getPoint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (offset != null) {
                    Component component = (Component) e.getSource();
                    Container parent = component.getParent();
                    Point location = parent.getLocation();
                    location.translate(e.getX() - offset.x, e.getY() - offset.y);
                    parent.setLocation(location);
                }
            }
        };

        headerPanel.addMouseListener(dragAdapter);
        headerPanel.addMouseMotionListener(dragAdapter);

        // Insert the sticky note at the current caret position
        textpane.insertComponent(stickyPanel);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();

        switch (cmd) {
            case "New":
                handleNew();
                break;
            case "Save":
                savetofile();
                break;
            case "Open":
                loadfromfile();
                break;
            case "Undo":
                if (undoManager.canUndo())
                    undoManager.undo();
                break;
            case "Redo":
                if (undoManager.canRedo())
                    undoManager.redo();
                break;
            case "Select All":
                textpane.selectAll();
                break;
            case "Copy":
                textpane.copy();
                break;
            case "Paste":
                textpane.paste();
                break;
            case "Snap":
                handleImageInsertion();
                break;
            case "Stickie":
                createStickyNote();
                break;
            case "About":
                new noteabut();
                break;
        }

        if (ae.getSource() == themeToggleButton) {
            isDarkTheme = !isDarkTheme;
            updateTheme();
        }
    }

    private void savetofile() {
        JFileChooser saveChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Styled Document Files", "ser");
        saveChooser.setFileFilter(filter);
        int option = saveChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File fileToSave = saveChooser.getSelectedFile();
            if (!fileToSave.getName().endsWith(".ser")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".ser");
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileToSave))) {
                oos.writeObject(textpane.getStyledDocument());
                JOptionPane.showMessageDialog(this, "File saved successfully!");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadfromfile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Styled Document Files", "ser");
        chooser.setFileFilter(filter);
        int action = chooser.showOpenDialog(this);
        if (action != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = chooser.getSelectedFile();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            StyledDocument doc = (StyledDocument) ois.readObject();
            textpane.setStyledDocument(doc);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error opening file: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleNew() {
        if (!textpane.getText().isEmpty()) {
            int option = JOptionPane.showConfirmDialog(this,
                    "Do you want to save existing data?",
                    "Save Changes",
                    JOptionPane.YES_NO_CANCEL_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                savetofile();
                textpane.setText("");
            } else if (option == JOptionPane.NO_OPTION) {
                textpane.setText("");
            }
        }
    }

    private void handleImageInsertion() {
        // Modified Snap action code

        fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Image Files", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Load the original image
                ImageIcon originalIcon = new ImageIcon(selectedFile.getAbsolutePath());

                // Calculate the desired width (45% of text pane width)
                int textPaneWidth = textpane.getWidth();
                int targetWidth = (int) (textPaneWidth * 0.45);

                // Calculate the proportional height to maintain aspect ratio
                double aspectRatio = (double) originalIcon.getIconHeight() / originalIcon.getIconWidth();
                int targetHeight = (int) (targetWidth * aspectRatio);

                // Resize the image
                Image scaledImage = originalIcon.getImage().getScaledInstance(
                        targetWidth,
                        targetHeight,
                        Image.SCALE_SMOOTH);

                // Create the resized icon
                ImageIcon resizedIcon = new ImageIcon(scaledImage);

                // Insert a table to hold the image and allow text beside it
                String tableHtml = String.format(
                        "<html><table><tr><td><img src='%s' width='%d' height='%d'></td><td></td></tr></table></html>",
                        selectedFile.toURI().toString(),
                        targetWidth,
                        targetHeight);

                // Insert at current caret position
                textpane.insertComponent(new JLabel(resizedIcon));

                // Move caret to position after image (in the same line)
                textpane.setCaretPosition(textpane.getCaretPosition());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        null,
                        "Error inserting image: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ... (keep your existing mouse listener methods and updateTheme method)

    private void updateTheme() {
        Color backgroundColor;
        Color textColor;
        Color labells;
        Color labell;
        Color menuc;
        Color southpan;

        if (isDarkTheme) {
            backgroundColor = new Color(10, 10, 10);
            textColor = selectedColor;
            labells = Color.WHITE;
            labell = new Color(166, 166, 227);
            menuc = new Color(166, 166, 227);
            textpane.setCaretColor(Color.WHITE);
            southpan = new Color(166, 166, 227);
            lighttheme = "suny.png";

        } else {
            backgroundColor = Color.WHITE;
            textColor = selectedColor;
            labells = Color.BLACK;
            labell = new Color(221, 221, 245);
            menuc = new Color(221, 221, 255);
            textpane.setCaretColor(Color.BLACK);
            southpan = new Color(221, 221, 255);
            lighttheme = "dark.png";

        }
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource(lighttheme));
        Image scaledIcon = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon scaledIconImage = new ImageIcon(scaledIcon);
        themeToggleButton.setIcon(scaledIconImage);
        getContentPane().setBackground(backgroundColor);
        textpane.setForeground(textColor);
        textpane.setBackground(backgroundColor);
        headerLabel.setForeground(labells);
        headerLabel.setBackground(labell);
        menubar.setBackground(menuc);
        southPanel.setBackground(southpan);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Noteapp());
    }
}
