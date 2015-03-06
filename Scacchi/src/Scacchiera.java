import javax.swing.JFrame;

public class Scacchiera extends JFrame {
    private static final long serialVersionUID = 1L; 
    private DisegnaScacchiera scacc;
    
    public Scacchiera(){
        scacc = new DisegnaScacchiera();
        add(scacc);
        pack();
    }
}