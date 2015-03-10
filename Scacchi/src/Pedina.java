
import javax.swing.JOptionPane;

public class Pedina extends Casella{
    public Colore colore_pezzo;
    private Pezzi cPezzo;

    public Pedina(Colore sfondo, int x, int y){
        super(sfondo,x,y);
    }
    
    public void setPezzo(Pezzi pp, Colore col_pezzo){
        cPezzo = pp;
        colore_pezzo = col_pezzo;
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
    
    public Colore getColore(){
        return colore_pezzo;
    }
    
    public void Elimina(){
        setIcon(new javax.swing.ImageIcon(getClass().getResource("")));
        cPezzo = null;
        colore_pezzo = null;
    }
    public void Sostituisci(String file){
        setIcon(new javax.swing.ImageIcon(file));
    }
}
