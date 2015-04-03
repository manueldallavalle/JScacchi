
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class Casella extends JButton{
    public int riga,
            colonna;
    public Casella(){
    }
    public Casella(Colore sfondo,int riga,int colonna){
        this.riga = riga;
        this.colonna = colonna;
        setPreferredSize(new Dimension(70,70));
        switch(sfondo){
            case NERO:
                this.setBackground(Color.decode("#c46f38"));
                break;
            case BIANCO:
                this.setBackground(Color.decode("#f6d5a5"));
                break;
            default:
                JOptionPane.showMessageDialog(null,"ERRORE CASELLA");
        }
    }
    public int getRiga(){
        return riga;
    }
    public int getColonna(){
        return colonna;
    }
}
