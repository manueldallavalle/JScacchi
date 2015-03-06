
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
                    cancellaBordo(x,y);
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
                Pedone pedone=new Pedone(Colore.NERO,x,y,ped.getPezzo(),Colore.NERO);
                a=pedone.getMovimentoUpX();
                scacchi[a][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 4));
            break;
            case TORRE:
                Torre torre=new Torre(Colore.NERO,x,y,ped.getPezzo(),Colore.NERO);
                a=torre.getMovimentoAvantiX();
                b=torre.getMovimentoAvantiY();
                c=torre.getMovimentoLateraleX();
                d=torre.getMovimentoLateraleY();
                
        }
    }
    public void cancellaBordo(int x,int y){
        scacchi[x][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK, 1));
    }
}
