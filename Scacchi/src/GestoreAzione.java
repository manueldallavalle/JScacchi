
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Math.abs;

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
                    System.out.print(""+(x+1)+(y-1));
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
                                } 
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
            case REGINA:
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
                    
                    //diagonale alto sx
                   
                break;
            case ALFIERE:
                   for(int i=y;i<7;i++){
                       for(int j=y;j>0;j--){
                        scacchi[i+1][j-1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                       }                       
                   }
                   for(int i=y;i>0;i--){
                        scacchi[i-1][i+1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                   }
                   for(int i=x;i<7;i++){
                    scacchi[i+1][i+1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                   }
                   for(int i=x;i>0;i--){
                    scacchi[i-1][i-1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
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
