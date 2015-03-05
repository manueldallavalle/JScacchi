
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class DisegnaScacchiera extends JPanel{
    private Pedina[][] scacchi = new Pedina[8][8];
    private int[][] posizione=new int[8][8];
    private int i,j;
    int cont=0;
    
    public DisegnaScacchiera(){

        this.setLayout(new GridLayout(8,8));
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
                
                add(scacchi[z][k]);
            }
            
        }
    }
}