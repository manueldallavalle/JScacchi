
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Math.abs;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class GestoreAzione implements ActionListener{
    private Pedina[][] scacchi;
    private JLabel text;
    private static int cont_mosse = 1;
    private static Pedina t_pedina = null;
    
    public GestoreAzione(Pedina[][] scacchi,JLabel text) {
        this.scacchi = scacchi;
        this.text=text;
    }
       
    public void actionPerformed(ActionEvent e) {
        Boolean errore = false;
        Pedina ped = (Pedina)e.getSource();
        // CONTROLLO PEDINA VUOTA O SE STA MANGIANDO
       /* if((ped.getPezzo() == null) || (t_pedina != null)){
            errore = false;
        }else if(((cont_mosse % 2 == 0) && !(ped.getColore().equals(Colore.NERO))) ||
                ((cont_mosse % 2 != 0) && !(ped.getColore().equals(Colore.BIANCO)))){
            errore = true;
        }
        
        if(errore){
            String plurale = (ped.getColore().equals(Colore.NERO)) ? "bianchi" : "neri";
            JOptionPane.showMessageDialog(null,"E' il turno dei " + plurale + "!", "Errore!",JOptionPane.ERROR_MESSAGE);
        }else{*/
            cambiaPezzo(ped, ped.getRiga(), ped.getColonna());
       // }
       
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
                    t_pedina = null;
                    resetBordo();
                }else if((sovrascriviPezzo(t_pedina,pedNew)) == true){
                    (scacchi[x][y]).setPezzo(t_pedina.getPezzo(), t_pedina.getColore());
                    t_pedina.Elimina();
                    t_pedina = null;
                    cont_mosse++;
                    resetBordo();
                }else{
                    JOptionPane.showMessageDialog(null,"Non puoi mangiare i tuoi pezzi!", "Errore!",JOptionPane.ERROR_MESSAGE);
                    //RESET
                    resetBordo();
                    t_pedina = null;
                }
            } 
            text.setText("\t\tMOSSE PARTITA: "+(cont_mosse - 1));
    }
    
    protected boolean sovrascriviPezzo(Pedina old, Pedina nn){
        // STESSO COLORE -- MOSSA NON VALIDA
        // COLORE DIVERSO -- MOSSA VALIDA
        return (old.getColore() != nn.getColore());
    }
    
    private void caselleAmmesse(Pedina ped,int x, int y){
        switch (ped.getPezzo()){
            case PEDONE: //[FUNZIONA]
                if(ped.colore_pezzo.equals(Colore.NERO)){
                    if(x+1 < 8) scacchi[x+1][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                }else{
                    if(x-1 > 0) scacchi[x-1][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2)); 
                }
                break;
            case TORRE: //[FUNZIONA]
                   //caselle in basso
                   for(int i=y;i<7;i++){
                        scacchi[x][i+1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                   }
                   //casella in alto
                   for(int i=y;i>0;i--){
                        scacchi[x][i-1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                   }
                   //caselle a destra
                   for(int i=x;i<7;i++){
                    scacchi[i+1][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                   }
                   //caselle a sinistra
                   for(int i=x;i>0;i--){
                    scacchi[i-1][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                   }
                   break;
            case RE: //[FUNZIONA]               
                
                if(x+1<8){ //caso sotto ok
                    if(x-1>=0){ //caso sopra ok
                        if(y-1>=0){ //caso sinistra ok
                            if(y+1<8){ //caso destra ok
                                for(int i=x-1;i<x+2;i++){
                                    for(int j=y-1;j<y+2;j++){
                                        if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.black, 2));
                                        else scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                                    }
                                }
                            }else{ //caso destra out
                               for(int i=x-1;i<x+2;i++){
                                    for(int j=y-1;j<y+1;j++){
                                        if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.black, 2));
                                        else scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                                    }
                                }                     //scacchi[xx][yy] = null;
                            }
                        }else{ //caso sinistra out
                            for(int i=x-1;i<x+2;i++){
                                for(int j=y;j<y+2;j++){
                                    if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.black, 2));
                                    else scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                                }
                            }
                        }
                    }else if(y-1<=0){ //caso angolino in alto a sinistra out
                        for(int i=x;i<x+2;i++){
                            for(int j=y;j<y+2;j++){
                                if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.black, 2));
                                else scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                            }
                        } 
                    }else if(y+1>7){ //caso angolino in alto a destra out
                        for(int i=x;i<x+2;i++){
                            for(int j=y-1;j<y+1;j++){
                                if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.black, 2));
                                else scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                            }
                        }
                    }else{ //caso sopra out
                        for(int i=x;i<x+2;i++){
                            for(int j=y-1;j<y+2;j++){
                                if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.black, 2));
                                else scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                            }
                        }
                    }
                }else if(y+1>7){ //caso angolino in basso a destra out
                    for(int i=x-1;i<x+1;i++){
                        for(int j=y-1;j<y+1;j++){
                            if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.black, 2));
                            else scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                        }
                    }
                    
                }else if(y-1<=0){ //caso angolino in basso a sinistra out
                   for(int i=x-1;i<x+1;i++){
                        for(int j=y;j<y+2;j++){
                            if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.black, 2));
                            else scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                        }
                    } 
                
                }else{ //caso sotto out
                    for(int i=x-1;i<x+1;i++){
                        for(int j=y-1;j<y+2;j++){
                            if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.black, 2));
                            else scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                        }
                    }
                }break;
            case REGINA: //[FUNZIONA]
                    //caselle in basso
                   for(int i=y;i<7;i++){
                        scacchi[x][i+1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                   }
                   //caselle in alto
                   for(int i=y;i>0;i--){
                        scacchi[x][i-1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                   }
                   //caselle a destra
                   for(int i=x;i<7;i++){
                    scacchi[i+1][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                   }
                   //caselle a sinistra
                   for(int i=x;i>0;i--){
                    scacchi[i-1][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                   }
                    //diagonale basso dx
                    for(int i=x+1;i<8;i++){
                        for(int j=y+1;j<8;j++){
                            if(j-i==y-x) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                        }
                    }
                    //diagonale alto dx
                   for(int i=x-1;i>=0;i--){
                        for(int j=y+1;j<8;j++){
                            if(i+j==x+y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                        }
                    }
                    //diagonale alto sx
                   for(int i=x-1;i>=0;i--){
                        for(int j=y-1;j>=0;j--){
                            if(j-i==y-x) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                        }
                    }
                   
                    //diagonale basso sx
                   for(int i=x+1;i<8;i++){
                        for(int j=y-1;j>=0;j--){
                            if(i+j==y+x) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                        }
                    }
                   
                break;
            case ALFIERE: //[FUNZIONA]
                 //diagonale basso dx
                    for(int i=x+1;i<8;i++){
                        for(int j=y+1;j<8;j++){
                            if(j-i==y-x) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                        }
                    }
                    //diagonale alto dx
                   for(int i=x-1;i>=0;i--){
                        for(int j=y+1;j<8;j++){
                            if(i+j==x+y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                        }
                    }
                    //diagonale alto sx
                   for(int i=x-1;i>=0;i--){
                        for(int j=y-1;j>=0;j--){
                            if(j-i==y-x) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                        }
                    }
                   
                    //diagonale basso sx
                   for(int i=x+1;i<8;i++){
                        for(int j=y-1;j>=0;j--){
                            if(i+j==y+x) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                        }
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
