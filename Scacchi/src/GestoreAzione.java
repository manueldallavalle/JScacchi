import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.LineBorder;


public class GestoreAzione implements ActionListener{
    private Pedina[][] scacchi;
    private JLabel text;
    private static int cont_mosse = 1;
    private static Pedina t_pedina = null;
    private static int cont_nere = 0,cont_bianche = 0;
    private JLabel ped_mangiate_bianche[] = new JLabel[16];
    private JLabel ped_mangiate_nere[] = new JLabel[16];
    private boolean flag=true;
    
    public GestoreAzione(){}
    
    public GestoreAzione(Pedina[][] scacchi,JLabel text,JLabel[] ped_mangiate_bianche,JLabel[] ped_mangiate_nere){
        this.scacchi = scacchi;
        this.text=text;
        this.ped_mangiate_bianche=ped_mangiate_bianche;
        this.ped_mangiate_nere=ped_mangiate_nere;
    }
    
    private Window resetWindowScacchi(Component c) {
        Class cls = c.getClass();        
        if((cls.getName()).equals("Scacchiera")){
            Scacchiera jf = (Scacchiera)c;
            jf.resetScacchi();
            jf.pack();
        }
        
        if (c instanceof Window) {
            return (Window) c;
        } else if (c instanceof JPopupMenu) {
            JPopupMenu pop = (JPopupMenu) c;
            return resetWindowScacchi(pop.getInvoker());
        } else {
            Container parent = c.getParent();
            return parent == null ? null : resetWindowScacchi(parent);
        }
    }
       
    public void actionPerformed(ActionEvent e) {
        // CONTROLLO SE HO PREMUTO MENU O PEDINA
        if((e.getActionCommand()).equals("nuovaPartita")){
            int confermaNuova = JOptionPane.showConfirmDialog(null, "Vuoi iniziare una nuova partita?", "Nuova Partita", JOptionPane.YES_NO_OPTION);
            if (confermaNuova == 0){
                // RESET SCACCHIERA + VARIABILI
                cont_mosse = 1;
                t_pedina = null;
                Window w = resetWindowScacchi((Component) e.getSource());
            }
        }else if((e.getActionCommand()).equals("esciPartita")){
            System.exit(0);
        }else{
            Boolean errore = false;
            Pedina ped = (Pedina)e.getSource();
            // CONTROLLO PEDINA VUOTA O SE STA MANGIANDO
            if((ped.getPezzo() == null) || (t_pedina != null)){
                errore = false;
            }else if(((cont_mosse % 2 == 0) && !(ped.getColore().equals(Colore.NERO))) ||
                    ((cont_mosse % 2 != 0) && !(ped.getColore().equals(Colore.BIANCO)))){
                errore = true;
            }

            if(errore){
                String plurale = (ped.getColore().equals(Colore.NERO)) ? "bianchi" : "neri";
                JOptionPane.showMessageDialog(null,"E' il turno dei " + plurale + "!", "Errore!",JOptionPane.ERROR_MESSAGE);
            }else{
                cambiaPezzo(ped, ped.getRiga(), ped.getColonna());
            }
        }
       
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
                }else if((sovrascriviPezzo(t_pedina,pedNew)) >= 0){
                    // CONTROLLO CASELLA VALIDA
                    Color lp = ((LineBorder)pedNew.getBorder()).getLineColor();
        
                    if((lp.equals(Color.green)) || (lp.equals(Color.red))){
                        // AGGIORNAMENTO PEDINE MANGIATE
                        String imglw = (pedNew.getPezzo() != null) ? ((pedNew.getPezzo()).toString().toLowerCase()) + "_" + ((pedNew.getColore()).toString().toLowerCase()) + ".gif" : "";                                                
                        if((sovrascriviPezzo(t_pedina,pedNew)) == 1){
                            ped_mangiate_nere[cont_nere].setIcon(new javax.swing.ImageIcon(getClass().getResource(imglw)));
                            cont_nere++;
                        }else if((sovrascriviPezzo(t_pedina,pedNew)) == 2){
                            ped_mangiate_bianche[cont_bianche].setIcon(new javax.swing.ImageIcon(getClass().getResource(imglw)));
                            cont_bianche++;
                        }
                        // SPOSTAMENTO PEDINA DOMINANTE
                        (scacchi[x][y]).setPezzo(t_pedina.getPezzo(), t_pedina.getColore());
                        t_pedina.Elimina();
                        cont_mosse++;
                    }else{
                        JOptionPane.showMessageDialog(null,"Seleziona una casella valida!", "Errore!",JOptionPane.ERROR_MESSAGE);
                    }
                    t_pedina = null;
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
    
    protected int sovrascriviPezzo(Pedina old, Pedina nw){
        /**
          * STESSO COLORE -- MOSSA NON VALIDA
          * COLORE DIVERSO -- MOSSA VALIDA
          * -1 = STESSO COLORE
          * 0 = MANGIATA VUOTA
          * 1 = MANGIATA NERA
          * 2 = MANGIATA BIANCA
        */
        if((old.getColore()).equals(nw.getColore())){
            return -1;
        }else if(nw.getColore() == null){
           return 0;
        }else if(nw.getColore().equals(Colore.NERO)){
           return 1;
        }else if(nw.getColore().equals(Colore.BIANCO)){
           return 2;
        }
        
        return -1;
    }
    
    private void caselleAmmesse(Pedina ped,int x, int y){
        switch (ped.getPezzo()){
            case PEDONE: // [FUNZIONA]
                if(ped.colore_pezzo.equals(Colore.NERO)){
                    if(x+1 < 8){ //caselle in basso
                        if(scacchi[x+1][y].getPezzo()==null)
                            scacchi[x+1][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2)); 
                                if(y-1 >= 0){ //caselle a sinistra
                                    if(scacchi[x+1][y-1].getPezzo()!=null && scacchi[x+1][y-1].colore_pezzo.equals(Colore.BIANCO))    
                                            scacchi[x+1][y-1].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                                }                        
                                if(y+1 < 8){ //caselle a destra
                                   if(scacchi[x+1][y+1].getPezzo()!=null && scacchi[x+1][y+1].colore_pezzo.equals(Colore.BIANCO))    
                                        scacchi[x+1][y+1].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                                }
                    }
                    
                }else{
                    if(x-1 >= 0){ //caselle in alto
                        if(scacchi[x-1][y].getPezzo()==null)
                            scacchi[x-1][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                                if(y-1 >= 0){ //caselle a sinistra
                                   if(scacchi[x-1][y-1].getPezzo()!=null && scacchi[x-1][y-1].colore_pezzo.equals(Colore.NERO) )    
                                      scacchi[x-1][y-1].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2)); 
                                }
                                if(y+1 < 8){ //caselle a destra                                
                                   if(scacchi[x-1][y+1].getPezzo()!=null && scacchi[x-1][y+1].colore_pezzo.equals(Colore.NERO))    
                                     scacchi[x-1][y+1].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                                }
                    }
                       
                }   
             
            break;                
            case TORRE: //[FUNZIONA]
                   //caselle a destra
                   for(int i=y;i<7;i++){
                       if(scacchi[x][i+1].getPezzo()==null)
                        scacchi[x][i+1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                       else if((scacchi[x][y].colore_pezzo.equals(scacchi[x][i+1].colore_pezzo))==false){
                           scacchi[x][i+1].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                           break;
                       }
                       else break;
                   }
                   //casella a sinistra 
                   for(int i=y;i>0;i--){
                       if(scacchi[x][i-1].getPezzo()==null)
                        scacchi[x][i-1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                        else if((scacchi[x][y].colore_pezzo.equals(scacchi[x][i-1].colore_pezzo))==false){
                           scacchi[x][i-1].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                           break;
                       }
                       else break;
                   }
                   //caselle in basso
                   for(int i=x;i<7;i++){
                       if(scacchi[i+1][y].getPezzo()==null)
                        scacchi[i+1][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                       else if((scacchi[x][y].colore_pezzo.equals(scacchi[i+1][y].colore_pezzo))==false){
                           scacchi[i+1][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                           break;
                       }
                       else break;
                   }
                   //caselle in alto
                   for(int i=x;i>0;i--){
                     if(scacchi[i-1][y].getPezzo()==null)  
                        scacchi[i-1][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                     else if((scacchi[x][y].colore_pezzo.equals(scacchi[i-1][y].colore_pezzo))==false){
                           scacchi[i-1][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                           break;
                       }
                     else break;
                   }
                   
            break;
            case RE:           
                
                if(x+1<8){ //caso sotto ok
                    if(x-1>=0){ //caso sopra ok
                        if(y-1>=0){ //caso sinistra ok
                            if(y+1<8){ //caso destra ok
                                flag=true;
                                for(int i=x-1;i<x+2 && flag;i++){
                                    for(int j=y-1;j<y+2 && flag;j++){
                                        if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 2));
                                        else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1) flag=false;
                                                    else{
                                                        scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                                                        flag=false;
                                                    }
                                    }
                                }
                            }else{ //caso destra out
                               flag=true; 
                               for(int i=x-1;i<x+2 && flag;i++){
                                    for(int j=y-1;j<y+1 && flag;j++){
                                        if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 2));
                                        else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1) flag=false;
                                                    else{
                                                        scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                                                        flag=false;
                                                    }
                                    }
                                }
                            }
                        }else{ //caso sinistra out
                            flag=true;
                            for(int i=x-1;i<x+2;i++){
                                for(int j=y;j<y+2;j++){
                                    if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 2));
                                        else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1) flag=false;
                                                    else{
                                                        scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                                                        flag=false;
                                                    }
                                }
                            }
                        }
                    }else if(y-1<=0){ //caso angolino in alto a sinistra out
                        flag=true;
                        for(int i=x;i<x+2 && flag;i++){
                            for(int j=y;j<y+2 && flag;j++){
                                if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 2));
                                        else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1) flag=false;
                                                    else{
                                                        scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                                                        flag=false;
                                                    }
                            }
                        } 
                    }else if(y+1>7){ //caso angolino in alto a destra out
                        flag=true;
                        for(int i=x;i<x+2 && flag;i++){
                            for(int j=y-1;j<y+1 && flag;j++){
                                if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 2));
                                        else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1) flag=false;
                                                    else{
                                                        scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                                                        flag=false;
                                                    }
                            }
                        }
                    }else{ //caso sopra out
                        flag=true;
                        for(int i=x;i<x+2 && flag;i++){
                            for(int j=y-1;j<y+2 && flag;j++){
                                if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 2));
                                        else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1) flag=false;
                                                    else{
                                                        scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                                                        flag=false;
                                                    }
                            }
                        }
                    }
                }else if(y+1>7){ //caso angolino in basso a destra out
                    flag=true;
                    for(int i=x-1;i<x+1 && flag;i++){
                        for(int j=y-1;j<y+1 && flag;j++){
                            if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 2));
                                        else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1) flag=false;
                                                    else{
                                                        scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                                                        flag=false;
                                                    }
                        }
                    }
                    
                }else if(y-1<=0){ //caso angolino in basso a sinistra out
                    flag=true;
                   for(int i=x-1;i<x+1 && flag;i++){
                        for(int j=y;j<y+2 && flag;j++){
                            if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 2));
                                        else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1) flag=false;
                                                    else{
                                                        scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                                                        flag=false;
                                                    }
                        }
                    } 
                
                }else{ //caso sotto out
                    flag=true;
                    for(int i=x-1;i<x+1 && flag;i++){
                        for(int j=y-1;j<y+2 && flag;j++){
                            if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 2));
                                        else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1) flag=false;
                                                    else{
                                                        scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                                                        flag=false;
                                                    }
                        }
                    }
                }break;
            case REGINA: //[FUNZIONA]
                   //caselle a destra
                   for(int i=y;i<7;i++){
                       if(scacchi[x][i+1].getPezzo()==null)
                        scacchi[x][i+1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                       else if((scacchi[x][y].colore_pezzo.equals(scacchi[x][i+1].colore_pezzo))==false){
                           scacchi[x][i+1].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                           break;
                       }
                       else break;
                   }
                   //casella a sinistra 
                   for(int i=y;i>0;i--){
                       if(scacchi[x][i-1].getPezzo()==null)
                        scacchi[x][i-1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                        else if((scacchi[x][y].colore_pezzo.equals(scacchi[x][i-1].colore_pezzo))==false){
                           scacchi[x][i-1].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                           break;
                       }
                       else break;
                   }
                   //caselle in basso
                   for(int i=x;i<7;i++){
                       if(scacchi[i+1][y].getPezzo()==null)
                        scacchi[i+1][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                       else if((scacchi[x][y].colore_pezzo.equals(scacchi[i+1][y].colore_pezzo))==false){
                           scacchi[i+1][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                           break;
                       }
                       else break;
                   }
                   //caselle in alto
                   for(int i=x;i>0;i--){
                     if(scacchi[i-1][y].getPezzo()==null)  
                        scacchi[i-1][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
                     else if((scacchi[x][y].colore_pezzo.equals(scacchi[i-1][y].colore_pezzo))==false){
                           scacchi[i-1][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                           break;
                       }
                     else break;
                   }
                    //diagonale basso dx
                   flag=true;
                    for(int i=x+1;i<8 && flag;i++){
                        for(int j=y+1;j<8 && flag;j++){
                            if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0 && j-i==y-x) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1 && j-i==y-x) flag=false;
                            else if(j-i==y-x){
                                scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                                flag=false;
                            }
                            
                        }
                    }
                   //diagonale alto dx
                   flag=true; 
                   for(int i=x-1;i>=0 && flag;i--){
                        for(int j=y+1;j<8 && flag;j++){
                            if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0 && j+i==y+x) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1 && j+i==y+x) flag=false;
                            else if(j+i==y+x){
                                scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                                flag=false;
                            }
                        }
                   }
                   
                    //diagonale alto sx
                   flag=true; 
                   for(int i=x-1;i>=0 && flag;i--){
                        for(int j=y-1;j>=0 && flag;j--){
                            if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0 && j-i==y-x) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1 && j-i==y-x) flag=false;
                            else if(j-i==y-x){
                                scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                                flag=false;
                            }
                        }
                    }
                   
                    //diagonale basso sx
                   flag=true; 
                   for(int i=x+1;i<8 && flag;i++){
                        for(int j=y-1;j>=0 && flag;j--){
                               if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0 && j+i==y+x) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1 && j+i==y+x) flag=false;
                            else if(j+i==y+x){
                                scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                                flag=false;
                            }
                        }
                    }
                   
                break;
            case ALFIERE: //[FUNZIONA]
                  //diagonale basso dx
                 flag=true;
                    for(int i=x+1;i<8 && flag;i++){
                        for(int j=y+1;j<8 && flag;j++){
                            if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0 && j-i==y-x) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1 && j-i==y-x) flag=false;
                            else if(j-i==y-x){
                                scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                                flag=false;
                            }
                            
                        }
                    }
                   //diagonale alto dx
                   flag=true; 
                   for(int i=x-1;i>=0 && flag;i--){
                        for(int j=y+1;j<8 && flag;j++){
                            if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0 && j+i==y+x) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1 && j+i==y+x) flag=false;
                            else if(j+i==y+x){
                                scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                                flag=false;
                            }
                        }
                   }
                   
                    //diagonale alto sx
                   flag=true; 
                   for(int i=x-1;i>=0 && flag;i--){
                        for(int j=y-1;j>=0 && flag;j--){
                            if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0 && j-i==y-x) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1 && j-i==y-x) flag=false;
                            else if(j-i==y-x){
                                scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                                flag=false;
                            }
                        }
                    }
                   
                    //diagonale basso sx
                   flag=true; 
                   for(int i=x+1;i<8 && flag;i++){
                        for(int j=y-1;j>=0 && flag;j--){
                               if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0 && j+i==y+x) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1 && j+i==y+x) flag=false;
                            else if(j+i==y+x){
                                scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
                                flag=false;
                            }
                        }
                    }
                   
                break;
            case CAVALLO: //[FUNZIONA]
                //parti alte                    
                    if(x-1>=0){ 
                        if(y+2<8) //prima linea parte destra
                            if(sovrascriviPezzo(scacchi[x][y],scacchi[x-1][y+2])==0) scacchi[x-1][y+2].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[x-1][y+2])>0) scacchi[x-1][y+2].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 3));
                        if(y-2>=0) //prima linea parte sinistra
                             if(sovrascriviPezzo(scacchi[x][y],scacchi[x-1][y-2])==0) scacchi[x-1][y-2].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[x-1][y-2])>0) scacchi[x-1][y-2].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 3));}                    
                
                 
                    if(x-2>=0){
                        if(y+1<8) //seconda linea parte destra
                            if(sovrascriviPezzo(scacchi[x][y],scacchi[x-2][y+1])==0) scacchi[x-2][y+1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[x-2][y+1])>0) scacchi[x-2][y+1].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 3));
                        if(y-1>=0) //seconda linea parte sinistra
                            if(sovrascriviPezzo(scacchi[x][y],scacchi[x-2][y-1])==0) scacchi[x-2][y-1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[x-2][y-1])>0) scacchi[x-2][y-1].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 3));
                        
                   }                    
                //parti basse
                   if(x+1<8){
                       if(y+2<8) //prima linea parte destra
                           if(sovrascriviPezzo(scacchi[x][y],scacchi[x+1][y+2])==0) scacchi[x+1][y+2].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[x+1][y+2])>0) scacchi[x+1][y+2].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 3));
                        if(y-2>=0) //prima linea parte sinistra
                           if(sovrascriviPezzo(scacchi[x][y],scacchi[x+1][y-2])==0) scacchi[x+1][y-2].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[x+1][y-2])>0) scacchi[x+1][y-2].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 3));
                        }
                   
                   if(x+2<8){
                      if(y+1<8) //seconda linea parte destra
                           if(sovrascriviPezzo(scacchi[x][y],scacchi[x+2][y+1])==0) scacchi[x+2][y+1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[x+2][y+1])>0) scacchi[x+2][y+1].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 3));
                        if(y-1>=0) //seconda linea parte sinistra
                           if(sovrascriviPezzo(scacchi[x][y],scacchi[x+2][y-1])==0) scacchi[x+2][y-1].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 3));
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[x+2][y-1])>0) scacchi[x+2][y-1].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 3));
                        }
                    
                    
                break;
        }
    }
    
    private void resetBordo(){
        for(int i=0;i<8;i++){
           for(int j=0;j<8;j++)
            scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 1)); 
        }
    }

}
