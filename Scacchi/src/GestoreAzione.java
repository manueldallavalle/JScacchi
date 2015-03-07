
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class GestoreAzione implements ActionListener{
    private Pedina[][] scacchi;
    private JLabel text;
    private static int cont_mosse=0;
    private static Pedina t_pedina = null;
    
    public GestoreAzione(Pedina[][] scacchi,JLabel text) {
        this.scacchi = scacchi;
        this.text=text;
    }
       
    public void actionPerformed(ActionEvent e) {
        Pedina p_premuta = (Pedina)e.getSource();        
        cambiaPezzo(p_premuta, p_premuta.getRiga(), p_premuta.getColonna());
        
    }
       
    private void cambiaPezzo(Pedina pedNew,int x,int y){
            //1° CLICK
            if(t_pedina == null){
                if(pedNew.getPezzo() != null){
                    t_pedina = pedNew; 
                    caselleAmmesse(pedNew,x,y);
                }else{
                    JOptionPane.showMessageDialog(null,"Seleziona una casella valida!", "Errore!",JOptionPane.ERROR_MESSAGE);
                }
            //2° CLICK
            }else{
                if(x == t_pedina.getRiga() && y == t_pedina.getColonna()){
                    JOptionPane.showMessageDialog(null,"Seleziona delle coordinate diverse dalle originali!", "Errore!",JOptionPane.ERROR_MESSAGE);
                    //RESET
                    resetBordo();
                    t_pedina = null;
                }else if((sovrascriviPezzo(t_pedina,pedNew)) == true){
                    resetBordo();
                    (scacchi[x][y]).setPezzo(t_pedina.getPezzo(), t_pedina.getColore());
                    t_pedina.Elimina();
                    t_pedina = null;
                    cont_mosse++;
                }else{
                    JOptionPane.showMessageDialog(null,"Non puoi mangiare i tuoi pezzi!", "Errore!",JOptionPane.ERROR_MESSAGE);
                    //RESET
                    resetBordo();
                    t_pedina = null;
                }
            } 
            text.setText("MOSSE PARTITA: "+(cont_mosse));
    }
    
    protected boolean sovrascriviPezzo(Pedina old, Pedina nn){
        // STESSO COLORE -- MOSSA NON VALIDA
        // COLORE DIVERSO -- MOSSA VALIDA
        return (old.getColore() != nn.getColore());
    }
    
    private boolean controllaMossa(Pedina ped,int x, int y,int x_old,int y_old){
        switch (ped.getPezzo()) {
            case PEDONE:
                Pedone pedone=new Pedone(Colore.NERO,x_old,y_old,ped.getPezzo(),Colore.NERO);
                if(pedone.getMovimentoUpX()==x)
                    return true;
             break;
        }
        return false;
    }
    private void caselleAmmesse(Pedina ped,int x, int y){
        switch (ped.getPezzo()){
            case PEDONE:
                if(ped.colore_pezzo.equals(Colore.NERO)){
                    if(x+1 < 9) scacchi[x+1][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                }else{
                    if(x-1 > 0) scacchi[x-1][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3)); 
                }
                break;
            case TORRE:
                   //caselle in basso
                   for(int i=y;i<7;i++){
                        scacchi[x][i+1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                   }
                   //casella in alto
                   for(int i=y;i>0;i--){
                        scacchi[x][i-1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                   }
                   //caselle a destra
                   for(int i=x;i<7;i++){
                    scacchi[i+1][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                   }
                   //caselle a sinistra
                   for(int i=x;i>0;i--){
                    scacchi[i-1][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                   }
                   break;
            case RE:
                /*while()    
                scacchi[x-1][]
                break;*/
            case REGINA:
                    //caselle in basso
                   for(int i=y;i<7;i++){
                        scacchi[x][i+1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                   }
                   //casella in alto
                   for(int i=y;i>0;i--){
                        scacchi[x][i-1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                   }
                   //caselle a destra
                   for(int i=x;i<7;i++){
                    scacchi[i+1][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                   }
                   //caselle a sinistra
                   for(int i=x;i>0;i--){
                    scacchi[i-1][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                   }
                   //diagonale basso dx
                    for(int i=x;i<7;i++){
                    scacchi[i+1][i+1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                   }
                    //diagonale alto sx
                   for(int i=x;i>0;i--){
                    scacchi[i-1][i-1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                   }
                break;
            case ALFIERE:
                   for(int i=y;i<7;i++){
                       for(int j=y;j>0;j--){
                        scacchi[i+1][j-1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                       }                       
                   }
                   for(int i=y;i>0;i--){
                        scacchi[i-1][i+1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                   }
                   for(int i=x;i<7;i++){
                    scacchi[i+1][i+1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                   }
                   for(int i=x;i>0;i--){
                    scacchi[i-1][i-1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                   }
                break;
        }
    }
    
    private void resetBordo(){
        for(int i=0;i<8;i++){
           for(int j=0;j<8;j++)
            scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK, 1)); 
        }
    }
}
