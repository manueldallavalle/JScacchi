
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Pedina extends Casella{
    private Colore colore_pezzo;
    private Pezzi cPezzo;
    
    public Pedina(Colore sfondo, int x, int y, Colore col_pezzo){
        super(sfondo,x,y);
        colore_pezzo = col_pezzo;
    }
    
    public void setPezzo(Pezzi pp){
        cPezzo = pp;
        switch (pp)
        {
            case PEDONE:
                if(colore_pezzo.equals(Colore.NERO)){
                    setIcon(new javax.swing.ImageIcon(getClass().getResource("pedone_nero.gif")));
                }else{
                    setIcon(new javax.swing.ImageIcon(getClass().getResource("pedone_bianco.gif")));
                }
                break;
            case TORRE:
                if(colore_pezzo.equals(Colore.NERO)){
                    setIcon(new javax.swing.ImageIcon(getClass().getResource("torre_nero.gif")));
                }else{ 
                    setIcon(new javax.swing.ImageIcon(getClass().getResource("torre_bianco.gif")));
                }
                break;
            case CAVALLO:
                if(colore_pezzo.equals(Colore.NERO)){
                    setIcon(new javax.swing.ImageIcon(getClass().getResource("cavallo_nero.gif")));
                }else{ 
                    setIcon(new javax.swing.ImageIcon(getClass().getResource("cavallo_bianco.gif")));
                }
                break;
            case ALFIERE:
                if(colore_pezzo.equals(Colore.NERO)){
                    setIcon(new javax.swing.ImageIcon(getClass().getResource("alfiere_nero.gif")));
                }else{
                    setIcon(new javax.swing.ImageIcon(getClass().getResource("alfiere_bianco.gif")));
                }
                break;
            case RE:
                if(colore_pezzo.equals(Colore.NERO)){
                    setIcon(new javax.swing.ImageIcon(getClass().getResource("re_nero.gif")));
                }else{
                    setIcon(new javax.swing.ImageIcon(getClass().getResource("re_bianco.gif")));
                }
                break;
            case REGINA:
                if(colore_pezzo.equals(Colore.NERO)){
                    setIcon(new javax.swing.ImageIcon(getClass().getResource("regina_nero.gif")));
                }else{
                    setIcon(new javax.swing.ImageIcon(getClass().getResource("regina_bianco.gif")));
                }
                break;
            case VUOTO:
                break;
            default:
                JOptionPane.showMessageDialog(null,"ERRORE PEDINA");
        }
    }
    
    public Pezzi getPezzo(){
        return cPezzo;
    }
    
    public void Elimina(){
        setIcon(new javax.swing.ImageIcon(getClass().getResource("")));
    }
    public void Sostituisci(String file){
        setIcon(new javax.swing.ImageIcon(file));
    }
}
