
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pc
 */
public class GestoreAzione implements ActionListener{
    private Pedina[][] scacchi;
    private int x_vecchia,y_vecchia,a,b,c,d;
    private static Pedina t_pedina = null;
    
    public GestoreAzione(Pedina[][] scacchi) {
        this.scacchi = scacchi;
    }
       
    public void actionPerformed(ActionEvent e) {
        Pedina p_premuta = (Pedina)e.getSource();        
        System.out.println("("+p_premuta.getRiga()+","+p_premuta.getColonna()+")");
        cambiaPezzo(p_premuta, p_premuta.getRiga(), p_premuta.getColonna());
        
    }

   
    public void cambiaPezzo(Pedina pedNew,int x,int y){
            if(t_pedina == null){
                t_pedina = pedNew; 
                caselleAmmesse(pedNew,x,y);
                x_vecchia=x;
                y_vecchia=y;
            }
            else{                                                        
                    cancellaBordo();
                    (scacchi[x][y]).setPezzo(t_pedina.getPezzo());
                    t_pedina.Elimina();
                    t_pedina = null;
            } 
    }
    public boolean controllaMossa(Pedina ped,int x, int y,int x_old,int y_old){
        switch (ped.getPezzo())
        {
            case PEDONE:
                Pedone pedone=new Pedone(Colore.NERO,x_old,y_old,ped.getPezzo(),Colore.NERO);
                if(pedone.getMovimentoUpX()==x)
                    return true;
             break;
        }
        return false;
    }
    public void caselleAmmesse(Pedina ped,int x, int y){
        switch (ped.getPezzo())
        {
            case PEDONE:
                if(ped.colore_pezzo.equals(Colore.NERO)){
                    if(x+1<9)
                    scacchi[x+1][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                }
                else{
                    if(x-1>0)
                    scacchi[x-1][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3)); 
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
    
    public void cancellaBordo(){
        for(int i=0;i<8;i++){
           for(int j=0;j<8;j++)
            scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK, 1)); 
        }
    }
}
