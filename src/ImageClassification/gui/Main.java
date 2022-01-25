package ImageClassification.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * @author Alexandre Blansché
 * Programme principal et fenêtre principale
 * Classe singleton
 */
public class Main extends JFrame
{
    private static Main instance = null;
    
    private JPanel mainPanel;

    /**
     * @return Accès à l'instance
     */
    public static Main getInstance ()
    {
        if (Main.instance == null)
            Main.instance = new Main ();
        return Main.instance;
    }

    private Main ()
    {
        Main.instance = this;
        this.setTitle ("Classification d'image");
        this.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        this.mainPanel = new JPanel ();
        this.mainPanel.setLayout (new BorderLayout ());
        JPanel commandPanel = new JPanel ();
        JButton openFileButton = new JButton ("Ouvrir une image");
        commandPanel.add (openFileButton);
        
        this.mainPanel.add (commandPanel, BorderLayout.PAGE_START);
        openFileButton.addActionListener (new ActionListener ()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Main.getImageFile ();
            }
        });
        this.setContentPane (this.mainPanel);
        Main.setImagePanel (new File ("data/cat.png"));
        this.pack ();
        this.setResizable (false);
    }

    private void view ()
    {
        this.setVisible (true);
    }
    
    private static void setImagePanel (File file)
    {
        try
        {
            Main main = Main.getInstance ();
            Component imagePanel = ((BorderLayout) main.mainPanel.getLayout ()).getLayoutComponent (BorderLayout.CENTER);
            if (imagePanel != null)
                main.mainPanel.remove (imagePanel);
            main.mainPanel.add (new MultipleViewPanel (ImageIO.read (file), file.getName ()), BorderLayout.CENTER);
            main.pack ();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private static void getImageFile ()
    {
        Main main = Main.getInstance ();
        JFileChooser fileChooser = new JFileChooser ();
        int returnVal = fileChooser.showDialog (main, "Ouvrir");
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            File file = fileChooser.getSelectedFile();
            Main.setImagePanel (file);
        }
    }

    /**
     * @param args
     */
    public static void main (String [] args)
    {
        SwingUtilities.invokeLater (new Runnable ()
        {
            @Override
            public void run()
            {
                Main main = Main.getInstance ();
                main.view ();               
            }
        });
    }
}
