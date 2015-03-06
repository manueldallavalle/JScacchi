
import java.awt.BorderLayout;
import static java.awt.BorderLayout.NORTH;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class DisegnaScacchiera extends JPanel{
    private Pedina[][] scacchi = new Pedina[8][8];
    private int[][] posizione=new int[8][8];
    private JLabel cr[]=new JLabel[9];
    private JLabel cc[]=new JLabel[8];
    private int i,j;
    int cont=0;
    private JPanel scacchiera=new JPanel();
    private JPanel coordinateRiga=new JPanel();
    private JPanel coordinateColonna=new JPanel();
    
    public DisegnaScacchiera(){
        
        this.setLayout(new BorderLayout());
        
        posizione();
        
        Colore temp_colore_pp;
        for(int z = 0; z < 8; z++){
            // COLORE CASELLE
            for(int k = 0; k < 8; k++){
                temp_colore_pp = (z >= 6) ? Colore.BIANCO : Colore.NERO;
                
                if(z % 2 == 0){
                    if(k % 2 == 0){
                        scacchi[z][k] = new Pedina(Colore.BIANCO, z, k, temp_colore_pp);
                    }else{
                        scacchi[z][k] = new Pedina(Colore.NERO, z, k, temp_colore_pp);
                    }
                }else{
                    if(k % 2 != 0){
                        scacchi[z][k] = new Pedina(Colore.BIANCO, z, k, temp_colore_pp);
                    }else{
                        scacchi[z][k] = new Pedina(Colore.NERO, z, k, temp_colore_pp);
                    }
                }

                if(z == 0 || z == 7){
                    if(k == 0 || k == 7){
                        (scacchi[z][k]).setPezzo(Pezzi.TORRE);
                    }else if(k == 1 || k == 6){
                        (scacchi[z][k]).setPezzo(Pezzi.CAVALLO);
                    }else if(k == 2 || k == 5){
                        (scacchi[z][k]).setPezzo(Pezzi.ALFIERE);
                    }else if(k == 3){
                        (scacchi[z][k]).setPezzo(Pezzi.REGINA);
                    }else if(k == 4){
                        (scacchi[z][k]).setPezzo(Pezzi.RE);
                    }

                }else if(z == 1 || z == 6){
                    (scacchi[z][k]).setPezzo(Pezzi.PEDONE);
                }
                
                (scacchi[z][k]).addActionListener(new GestoreAzione(scacchi));
                
                scacchiera.add(scacchi[z][k]);
            }
            
        }
    }



void posizione(){
            
            //Imposta le coordinate sopra della scacchiera
    
           coordinateRiga.setLayout(new FlowLayout());
           JLabel vuoto=new JLabel(" ");
           vuoto.setPreferredSize(new Dimension(35,40));
           coordinateRiga.add(vuoto);
          
           for(int i=0;i<8;i++){
               cr[i]=new JLabel(""+(char)(i+65));                 
               cr[i].setHorizontalAlignment(JLabel.CENTER);
               cr[i].setPreferredSize(new Dimension(70, 40));
               coordinateRiga.add(cr[i]);               
           }
           
           //Imposta le coordinate a sinistra della scacchiera
           
           coordinateColonna.setLayout(new GridLayout(8,1));
           coordinateColonna.setPreferredSize(new Dimension(40,70));
           
           for(int i=0;i<8;i++){
               cc[i]=new JLabel(""+(i+1));
               cc[i].setHorizontalAlignment( JLabel.CENTER );
               coordinateColonna.add(cc[i]);
           }
           
           add(coordinateColonna,BorderLayout.WEST);
           add(coordinateRiga,BorderLayout.NORTH);
           
           add(scacchiera,BorderLayout.CENTER);
           scacchiera.setLayout(new GridLayout(8,8));
    }
}