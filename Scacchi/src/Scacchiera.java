import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Scacchiera extends JFrame {
    private static final long serialVersionUID = 1L; 
    private DisegnaScacchiera scacc;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem nuovaPartita;
    private JMenuItem esciPartita;
    
    public Scacchiera(){
        menuBar = new JMenuBar();
        menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_M); // HOTKEY M
        menuBar.add(menu);
        
        nuovaPartita = new JMenuItem("Nuova Partita", KeyEvent.VK_N);  // HOTKEY N
        esciPartita = new JMenuItem("Esci", KeyEvent.VK_E);  // HOTKEY E
        menu.add(nuovaPartita);
        menu.add(esciPartita);
        
        nuovaPartita.addActionListener(new GestoreAzione());
        nuovaPartita.setActionCommand("nuovaPartita");
        esciPartita.addActionListener(new GestoreAzione());
        esciPartita.setActionCommand("esciPartita");

        this.setJMenuBar(menuBar);
        
        scacc = new DisegnaScacchiera();
        add(scacc);
        pack();
    }
    
    protected void resetScacchi(){
        scacc.resetScacchiera();
    }
}
