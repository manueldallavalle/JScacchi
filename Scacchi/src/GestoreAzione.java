
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;

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
        }else{
           (scacchi[x][y]).setPezzo(t_pedina.getPezzo());
           t_pedina.Elimina();
           t_pedina = null;
        } 
        
    }
}