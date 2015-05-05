import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
    private int[][] matriceControllo=new int[8][8];
    private int status=0;
    
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
                    caselleAmmesse(pedNew,x,y,0);//0
                    if(!mosseDisponibili()){
                        JOptionPane.showMessageDialog(null,"Questa pedina non può muoversi!", "Errore!",JOptionPane.ERROR_MESSAGE);
                        t_pedina = null;
                    }
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
                if((cont_mosse-1)%2==0){
                    finePartita(Colore.NERO);
                }
                else{
                    finePartita(Colore.BIANCO);
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
    
    private void caselleAmmesse(Pedina ped,int x, int y,int scelta){
        switch (ped.getPezzo()){
            case PEDONE: // [FUNZIONA]
                caselleAmmessePedone(ped,x,y,scelta);
            break;                
            case TORRE: //[FUNZIONA]
                caselleAmmesseTorre(ped,x,y,scelta);
            break;
            case RE:  //[FUNZIONA]         
                caselleAmmesseRe(ped,x,y,scelta);
            break;
            case REGINA: //[FUNZIONA]
                caselleAmmesseRegina(ped,x,y,scelta);
                break;
            case ALFIERE: //[FUNZIONA]
                caselleAmmesseAlfiere(ped,x,y,scelta);
            break;
            case CAVALLO: //[FUNZIONA]
                caselleAmmesseCavallo(ped,x,y,scelta);
            break;
        }
    }
    
    private void caselleAmmessePedone(Pedina ped, int x, int y,int scelta){
        if(ped.colore_pezzo.equals(Colore.NERO)){
                    if(x+1 < 8){ //caselle in basso
                        if(scacchi[x+1][y].getPezzo()==null)
                            disegna(x+1,y,scelta,0,matriceControllo);
                                if(y-1 >= 0){ //caselle a sinistra
                                    if(scacchi[x+1][y-1].getPezzo()!=null && scacchi[x+1][y-1].colore_pezzo.equals(Colore.BIANCO))    
                                            disegna(x+1,y-1,scelta,1,matriceControllo);
                                }                        
                                if(y+1 < 8){ //caselle a destra
                                   if(scacchi[x+1][y+1].getPezzo()!=null && scacchi[x+1][y+1].colore_pezzo.equals(Colore.BIANCO))    
                                        disegna(x+1,y+1,scelta,1,matriceControllo);
                                }
                    }
                    
                }else{
                    if(x-1 >= 0){ //caselle in alto
                        if(scacchi[x-1][y].getPezzo()==null)
                            disegna(x-1,y,scelta,0,matriceControllo);
                                if(y-1 >= 0){ //caselle a sinistra
                                   if(scacchi[x-1][y-1].getPezzo()!=null && scacchi[x-1][y-1].colore_pezzo.equals(Colore.NERO) )    
                                      disegna(x-1,y-1,scelta,1,matriceControllo); 
                                }
                                if(y+1 < 8){ //caselle a destra                                
                                   if(scacchi[x-1][y+1].getPezzo()!=null && scacchi[x-1][y+1].colore_pezzo.equals(Colore.NERO))    
                                     disegna(x-1,y-1,scelta,1,matriceControllo);
                                }
                    }
                       
                }   
    }
    
    private void caselleAmmesseTorre(Pedina ped,int x,int y,int scelta){
        //caselle a destra
                   for(int i=y;i<7;i++){
                        if(scacchi[x][i+1].getPezzo()==null)
                            disegna(x,i+1,scelta,0,matriceControllo);
                        else if((scacchi[x][y].colore_pezzo.equals(scacchi[x][i+1].colore_pezzo))==false){
                            disegna(x,i+1,scelta,1,matriceControllo);
                        break;
                        }
                        else break;
                   }
                   //casella a sinistra 
                   for(int i=y;i>0;i--){
                       if(scacchi[x][i-1].getPezzo()==null)
                        disegna(x,i-1,scelta,0,matriceControllo);
                        else if((scacchi[x][y].colore_pezzo.equals(scacchi[x][i-1].colore_pezzo))==false){
                           disegna(x,i-1,scelta,1,matriceControllo);
                           break;
                       }
                       else break;
                   }
                   //caselle in basso
                   for(int i=x;i<7;i++){
                       if(scacchi[i+1][y].getPezzo()==null)
                        disegna(i+1,y,scelta,0,matriceControllo);
                       else if((scacchi[x][y].colore_pezzo.equals(scacchi[i+1][y].colore_pezzo))==false){
                           disegna(i+1,y,scelta,1,matriceControllo);
                           break;
                       }
                       else break;
                   }
                   //caselle in alto
                   for(int i=x;i>0;i--){
                     if(scacchi[i-1][y].getPezzo()==null)  
                        disegna(i-1,y,scelta,0,matriceControllo);
                     else if((scacchi[x][y].colore_pezzo.equals(scacchi[i-1][y].colore_pezzo))==false){
                           disegna(i-1,y,scelta,1,matriceControllo);
                           break;
                       }
                     else break;
                   }
    }
     
    private void caselleAmmesseRe(Pedina ped,int x,int y,int scelta){
        if(x+1<8){ //caso sotto ok
                    if(x-1>=0){ //caso sopra ok
                        if(y-1>=0){ //caso sinistra ok
                            if(y+1<8){ //caso destra ok
                                for(int i=x-1;i<x+2;i++){
                                    for(int j=y-1;j<y+2;j++){
                                        if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 1));
                                        else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0) disegna(i,j,scelta,0,matriceControllo);
                                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1);
                                                    else{
                                                        disegna(i,j,scelta,1,matriceControllo);
                                                    }
                                    }
                                }
                            }else{ //caso destra out
                               for(int i=x-1;i<x+2;i++){
                                    for(int j=y-1;j<y+1;j++){
                                        if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 1));
                                        else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0) disegna(i,j,scelta,0,matriceControllo);
                                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1);
                                                    else{
                                                       disegna(i,j,scelta,1,matriceControllo);
                                                    }
                                    }
                                }
                            }
                        }else{ //caso sinistra out
                            for(int i=x-1;i<x+2;i++){
                                for(int j=y;j<y+2;j++){
                                    if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 1));
                                        else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0) disegna(i,j,scelta,0,matriceControllo);
                                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1);
                                                    else{
                                                        disegna(i,j,scelta,1,matriceControllo);
                                                    }
                                }
                            }
                        }
                    }else if(y-1<0){ //caso angolino in alto a sinistra out
                        for(int i=x;i<x+2;i++){
                            for(int j=y;j<y+2;j++){
                                if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 1));
                                        else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0) disegna(i,j,scelta,0,matriceControllo);
                                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1);
                                                    else{
                                                        disegna(i,j,scelta,1,matriceControllo);
                                                    }
                            }
                        } 
                    }else if(y+1>7){ //caso angolino in alto a destra out
                        for(int i=x;i<x+2;i++){
                            for(int j=y-1;j<y+1;j++){
                                if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 1));
                                        else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0) disegna(i,j,scelta,0,matriceControllo);
                                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1);
                                                    else{
                                                        disegna(i,j,scelta,1,matriceControllo);
                                                    }
                            }
                        }
                    }else{ //caso sopra out
                        for(int i=x;i<x+2;i++){
                            for(int j=y-1;j<y+2;j++){
                                if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 1));
                                        else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0) disegna(i,j,scelta,0,matriceControllo);
                                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1);
                                                    else{
                                                        disegna(i,j,scelta,1,matriceControllo);
                                                    }
                            }
                        }
                    }
                }else if(y+1>7){ //caso angolino in basso a destra out
                    for(int i=x-1;i<x+1;i++){
                        for(int j=y-1;j<y+1;j++){
                            if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 1));
                                        else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0) disegna(i,j,scelta,0,matriceControllo);
                                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1);
                                                    else{
                                                        disegna(i,j,scelta,1,matriceControllo);
                                                    }
                        }
                    }
                    
                }else if(y-1<0){ //caso angolino in basso a sinistra out
                   for(int i=x-1;i<x+1;i++){
                        for(int j=y;j<y+2;j++){
                            if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 1));
                                        else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0) disegna(i,j,scelta,0,matriceControllo);
                                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1);
                                                    else{
                                                        disegna(i,j,scelta,1,matriceControllo);
                                                    }
                        }
                    } 
                
                }else{ //caso sotto out
                    for(int i=x-1;i<x+1;i++){
                        for(int j=y-1;j<y+2;j++){
                            if(i==x && j==y) scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 1));
                                        else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0) disegna(i,j,scelta,0,matriceControllo);
                                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1);
                                                    else{
                                                        disegna(i,j,scelta,1,matriceControllo);
                                                    }
                        }
                    }
                }
    }
    
    private void caselleAmmesseRegina(Pedina ped,int x,int y,int scelta){
        //caselle a destra
                   for(int i=y;i<7;i++){
<<<<<<< HEAD
                        if(scacchi[x][i+1].getPezzo()==null)
                            disegna(x,i+1,scelta,0,matriceControllo);
                        else if((scacchi[x][y].colore_pezzo.equals(scacchi[x][i+1].colore_pezzo))==false){
                            disegna(x,i+1,scelta,1,matriceControllo);
                            break;
                        }
                        else break;
                   }
                   //casella a sinistra 
                   for(int i=y;i>0;i--){
                        if(scacchi[x][i-1].getPezzo()==null)
                            disegna(x,i-1,scelta,0,matriceControllo);
                        else if((scacchi[x][y].colore_pezzo.equals(scacchi[x][i-1].colore_pezzo))==false){
                            disegna(x,i-1,scelta,1,matriceControllo);
                            break;
                        }
                        else break;
                    }
                   //caselle in basso
                   for(int i=x;i<7;i++){
                        if(scacchi[i+1][y].getPezzo()==null)
                            disegna(i+1,y,scelta,0,matriceControllo);
                        else if((scacchi[x][y].colore_pezzo.equals(scacchi[i+1][y].colore_pezzo))==false){
                            disegna(i+1,y,scelta,1,matriceControllo);
                            break;
                        }
                        else break;
                    }
                   //caselle in alto
                   for(int i=x;i>0;i--){
                        if(scacchi[i-1][y].getPezzo()==null)  
                            disegna(i-1,y,scelta,0,matriceControllo);
                        else if((scacchi[x][y].colore_pezzo.equals(scacchi[i-1][y].colore_pezzo))==false){
                            disegna(i-1,y,scelta,1,matriceControllo);
                            break;
                        }
                        else break;
                    }
=======
                       if(scacchi[x][i+1].getPezzo()==null)
                        disegna(x,i+1,0,0,matriceControllo);
                       else if((scacchi[x][y].colore_pezzo.equals(scacchi[x][i+1].colore_pezzo))==false){
                           disegna(x,i+1,scelta,1,matriceControllo);
                           break;
                       }
                       else break;
                   }
                   //casella a sinistra 
                   for(int i=y;i>0;i--){
                       if(scacchi[x][i-1].getPezzo()==null)
                        disegna(x,i-1,0,0,matriceControllo);
                        else if((scacchi[x][y].colore_pezzo.equals(scacchi[x][i-1].colore_pezzo))==false){
                           disegna(x,i-1,scelta,1,matriceControllo);
                           break;
                       }
                       else break;
                   }
                   //caselle in basso
                   for(int i=x;i<7;i++){
                       if(scacchi[i+1][y].getPezzo()==null)
                        disegna(i+1,y,0,0,matriceControllo);
                       else if((scacchi[x][y].colore_pezzo.equals(scacchi[i+1][y].colore_pezzo))==false){
                          disegna(i+1,y,scelta,1,matriceControllo);
                           break;
                       }
                       else break;
                   }
                   //caselle in alto
                   for(int i=x;i>0;i--){
                     if(scacchi[i-1][y].getPezzo()==null)  
                        disegna(i-1,y,0,0,matriceControllo);
                     else if((scacchi[x][y].colore_pezzo.equals(scacchi[i-1][y].colore_pezzo))==false){
                          disegna(i-1,y,scelta,1,matriceControllo);
                           break;
                       }
                     else break;
                   }
>>>>>>> origin/master
                    //diagonale basso dx
                   flag=true;
                    for(int i=x+1;i<8 && flag;i++){
                        for(int j=y+1;j<8 && flag;j++){
                            if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0 && j-i==y-x) disegna(i,j,scelta,0,matriceControllo);
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1 && j-i==y-x) flag=false;
                            else if(j-i==y-x){
                                disegna(i,j,scelta,1,matriceControllo);
                                flag=false;
                            }
                            
                        }
                    }
                   //diagonale alto dx
                   flag=true; 
                   for(int i=x-1;i>=0 && flag;i--){
                        for(int j=y+1;j<8 && flag;j++){
                            if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0 && j+i==y+x) disegna(i,j,scelta,0,matriceControllo);
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1 && j+i==y+x) flag=false;
                            else if(j+i==y+x){
                                disegna(i,j,scelta,1,matriceControllo);
                                flag=false;
                            }
                        }
                   }
                   
                    //diagonale alto sx
                   flag=true; 
                   for(int i=x-1;i>=0 && flag;i--){
                        for(int j=y-1;j>=0 && flag;j--){
                            if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0 && j-i==y-x) disegna(i,j,scelta,0,matriceControllo);
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1 && j-i==y-x) flag=false;
                            else if(j-i==y-x){
                                disegna(i,j,scelta,1,matriceControllo);
                                flag=false;
                            }
                        }
                    }
                   
                    //diagonale basso sx
                   flag=true; 
                   for(int i=x+1;i<8 && flag;i++){
                        for(int j=y-1;j>=0 && flag;j--){
                               if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0 && j+i==y+x) disegna(i,j,scelta,0,matriceControllo);
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1 && j+i==y+x) flag=false;
                            else if(j+i==y+x){
                                disegna(i,j,scelta,1,matriceControllo);
                                flag=false;
                            }
                        }
                    }
                   
    }
    
    private void caselleAmmesseAlfiere(Pedina ped,int x,int y,int scelta){
         //diagonale basso dx
                 flag=true;
                    for(int i=x+1;i<8 && flag;i++){
                        for(int j=y+1;j<8 && flag;j++){
                            if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0 && j-i==y-x) disegna(i,j,scelta,0,matriceControllo);
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1 && j-i==y-x) flag=false;
                            else if(j-i==y-x){
                                disegna(i,j,scelta,1,matriceControllo);
                                flag=false;
                            }
                            
                        }
                    }
                   //diagonale alto dx
                   flag=true; 
                   for(int i=x-1;i>=0 && flag;i--){
                        for(int j=y+1;j<8 && flag;j++){
                            if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0 && j+i==y+x) disegna(i,j,scelta,0,matriceControllo);
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1 && j+i==y+x) flag=false;
                            else if(j+i==y+x){
                                disegna(i,j,scelta,1,matriceControllo);
                                flag=false;
                            }
                        }
                   }
                   
                    //diagonale alto sx
                   flag=true; 
                   for(int i=x-1;i>=0 && flag;i--){
                        for(int j=y-1;j>=0 && flag;j--){
                            if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0 && j-i==y-x) disegna(i,j,scelta,0,matriceControllo);
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1 && j-i==y-x) flag=false;
                            else if(j-i==y-x){
                                disegna(i,j,scelta,1,matriceControllo);
                                flag=false;
                            }
                        }
                    }
                   
                    //diagonale basso sx
                   flag=true; 
                   for(int i=x+1;i<8 && flag;i++){
                        for(int j=y-1;j>=0 && flag;j--){
                               if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==0 && j+i==y+x) disegna(i,j,scelta,0,matriceControllo);
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[i][j])==-1 && j+i==y+x) flag=false;
                            else if(j+i==y+x){
                                disegna(i,j,scelta,1,matriceControllo);
                                flag=false;
                            }
                        }
                    }
                   
    }
    
    private void caselleAmmesseCavallo(Pedina ped,int x,int y,int scelta){
        //parti alte                    
                    if(x-1>=0){ 
                        if(y+2<8) //prima linea parte destra
                            if(sovrascriviPezzo(scacchi[x][y],scacchi[x-1][y+2])==0) disegna(x-1,y+2,scelta,0,matriceControllo);
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[x-1][y+2])>0) disegna(x-1,y+2,scelta,1,matriceControllo);
                        if(y-2>=0) //prima linea parte sinistra
                             if(sovrascriviPezzo(scacchi[x][y],scacchi[x-1][y-2])==0) disegna(x-1,y-2,scelta,0,matriceControllo);
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[x-1][y-2])>0) disegna(x-1,y-2,scelta,1,matriceControllo);}                    
                
                 
                    if(x-2>=0){
                        if(y+1<8) //seconda linea parte destra
                            if(sovrascriviPezzo(scacchi[x][y],scacchi[x-2][y+1])==0) disegna(x-2,y+1,scelta,0,matriceControllo);
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[x-2][y+1])>0) disegna(x-2,y+1,scelta,1,matriceControllo);
                        if(y-1>=0) //seconda linea parte sinistra
                            if(sovrascriviPezzo(scacchi[x][y],scacchi[x-2][y-1])==0) disegna(x-2,y-1,scelta,0,matriceControllo);
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[x-2][y-1])>0) disegna(x-2,y-1,scelta,1,matriceControllo);
                        
                   }                    
                //parti basse
                   if(x+1<8){
                       if(y+2<8) //prima linea parte destra
                           if(sovrascriviPezzo(scacchi[x][y],scacchi[x+1][y+2])==0) disegna(x+1,y+2,scelta,0,matriceControllo);
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[x+1][y+2])>0) disegna(x+1,y+2,scelta,1,matriceControllo);
                        if(y-2>=0) //prima linea parte sinistra
                           if(sovrascriviPezzo(scacchi[x][y],scacchi[x+1][y-2])==0) disegna(x+1,y-2,scelta,0,matriceControllo);
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[x+1][y-2])>0) disegna(x+1,y-2,scelta,1,matriceControllo);
                        }
                   
                   if(x+2<8){
                      if(y+1<8) //seconda linea parte destra
                           if(sovrascriviPezzo(scacchi[x][y],scacchi[x+2][y+1])==0) disegna(x+2,y+1,scelta,0,matriceControllo);
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[x+2][y+1])>0) disegna(x+2,y+1,scelta,1,matriceControllo);
                        if(y-1>=0) //seconda linea parte sinistra
                           if(sovrascriviPezzo(scacchi[x][y],scacchi[x+2][y-1])==0) disegna(x+2,y-1,scelta,0,matriceControllo);
                            else if(sovrascriviPezzo(scacchi[x][y],scacchi[x+2][y-1])>0) disegna(x+2,y-1,scelta,1,matriceControllo);
                        }
    }
    
     private void disegna(int x,int y,int scelta,int colore,int[][] matriceControllo){
        if(scelta==0){
            if(colore==0)
                scacchi[x][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.green, 2));
            else
                scacchi[x][y].setBorder(BorderFactory.createLineBorder(java.awt.Color.red, 2));
        }
        else{
            matriceControllo[x][y]=1;
        }
    }
    
    private void resetBordo(){
        for(int i=0;i<8;i++){
           for(int j=0;j<8;j++)
            scacchi[i][j].setBorder(BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 1)); 
        }
    }

    private boolean mosseDisponibili(){
        int cont_caselle = 0;
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                Color tmp = ((LineBorder)scacchi[i][j].getBorder()).getLineColor();
                if((tmp.equals(Color.green)) || (tmp.equals(Color.red))) cont_caselle++;
            }
        } 
        return (cont_caselle > 0);
    }
    
    private void finePartita(Colore color){
        int xRe=0,yRe=0,i,j;
        status=0;
        for(i=0;i<8;i++){
            for(j=0;j<8;j++){
                matriceControllo[i][j]=0;
            }
        }
        for(i=0;i<8;i++){
            for(j=0;j<8;j++){
                if(color.equals(Colore.BIANCO)){
                    if(scacchi[i][j].getPezzo()!=null && scacchi[i][j].getPezzo().equals(Pezzi.RE) && scacchi[i][j].getColore().equals(Colore.NERO) && scacchi[i][j].getColore()!=null){
                        xRe=i;
                        yRe=j;
                    }
                }
                else{
                    if(scacchi[i][j].getPezzo()!=null && scacchi[i][j].getPezzo().equals(Pezzi.RE) && scacchi[i][j].getColore().equals(Colore.BIANCO) && scacchi[i][j].getColore()!=null){
                        xRe=i;
                        yRe=j;
                    }
                }
            }
        }
        JOptionPane.showMessageDialog(null,"y:"+yRe+" x:"+xRe);
        for(i=0;i<8;i++){
            for(j=0;j<8;j++){
                if(color.equals(Colore.BIANCO)){
                    if(scacchi[i][j].getPezzo()!=null && scacchi[i][j].getColore().equals(Colore.BIANCO) && scacchi[i][j].getColore()!=null){
                        caselleAmmesse(scacchi[i][j],i,j,1);
                        resetBordo();
                    }
                }
                else{
                    if(scacchi[i][j].getPezzo()!=null && scacchi[i][j].getColore().equals(Colore.NERO) && scacchi[i][j].getColore()!=null){
                        caselleAmmesse(scacchi[i][j],i,j,1);
                        resetBordo();
                    }
                }
            }
        }
<<<<<<< HEAD
        for(i=0;i<8;i++){
            for(j=0;j<8;j++){
                JOptionPane.showMessageDialog(null,matriceControllo[i][j]+" riga"+i+" colonna"+j);
            }
        }
=======
>>>>>>> origin/master
        if(xRe==7 && yRe==7)
            status=1; //angolo basso dx
        if(xRe==7 && yRe==0)
            status=2; //angolo basso sx
        if(xRe==0 && yRe==7)
            status=3; //angolo alto dx
        if(xRe==0 && yRe==0)
            status=4; //angolo alto sx
        if(xRe==7 && (yRe<7 && yRe>0))
            status=5; //bordo basso 
        if(xRe==0 && (yRe<7 && yRe>0))
            status=6; // bordo alto
        if(yRe==7 && (xRe<7 && xRe>0))
            status=7; // bordo dx
        if(yRe==0 && (xRe<7 && xRe>0))
            status=8; // bordo sx
        switch (status)
        {
            case 1:
<<<<<<< HEAD
                JOptionPane.showMessageDialog(null,+matriceControllo[xRe][yRe]+" normale\n"+matriceControllo[xRe-1][yRe]+" alto\n"+matriceControllo[xRe-1][yRe-1]+" alto sx\n"+matriceControllo[xRe][yRe-1]+" sinistra");
                if(matriceControllo[xRe][yRe]==1 && (matriceControllo[xRe-1][yRe]==1 || scacchi[xRe-1][yRe].getPezzo()!=null) && (matriceControllo[xRe-1][yRe-1]==1 || scacchi[xRe-1][yRe-1].getPezzo()!=null) && (matriceControllo[xRe][yRe-1]==1 || scacchi[xRe][yRe-1].getPezzo()!=null))
                    JOptionPane.showMessageDialog(null,"scaco matto!!!");
            break;
            case 2:
                 JOptionPane.showMessageDialog(null,+matriceControllo[xRe][yRe]+" normale\n"+matriceControllo[xRe-1][yRe]+" alto\n"+matriceControllo[xRe-1][yRe+1]+" alto dx\n"+matriceControllo[xRe][yRe+1]+" destra");
                if(matriceControllo[xRe][yRe]==1 && (matriceControllo[xRe-1][yRe]==1 || scacchi[xRe-1][yRe].getPezzo()!=null) && (matriceControllo[xRe-1][yRe+1]==1 || scacchi[xRe-1][yRe+1].getPezzo()!=null) && (matriceControllo[xRe][yRe+1]==1 || scacchi[xRe][yRe+1].getPezzo()!=null))
                    JOptionPane.showMessageDialog(null,"scaco matto!!!");
            break;
            case 3:
                 JOptionPane.showMessageDialog(null,+matriceControllo[xRe][yRe]+" normale\n"+matriceControllo[xRe+1][yRe]+" basso\n"+matriceControllo[xRe+1][yRe-1]+" basso sx\n"+matriceControllo[xRe][yRe-1]+" sinistra");
                if(matriceControllo[xRe][yRe]==1 && (matriceControllo[xRe+1][yRe]==1 || scacchi[xRe+1][yRe].getPezzo()!=null) && (matriceControllo[xRe+1][yRe-1]==1 || scacchi[xRe+1][yRe-1].getPezzo()!=null) && (matriceControllo[xRe][yRe-1]==1 || scacchi[xRe-1][yRe-1].getPezzo()!=null))
                    JOptionPane.showMessageDialog(null,"scaco matto!!!");
            break;
            case 4:
                 JOptionPane.showMessageDialog(null,+matriceControllo[xRe][yRe]+" normale\n"+matriceControllo[xRe+1][yRe]+" basso\n"+matriceControllo[xRe+1][yRe+1]+" basso dx\n"+matriceControllo[xRe][yRe+1]+" destra");
                if(matriceControllo[xRe][yRe]==1 && (matriceControllo[xRe+1][yRe]==1 || scacchi[xRe+1][yRe].getPezzo()!=null) && (matriceControllo[xRe+1][yRe+1]==1 || scacchi[xRe+1][yRe+1].getPezzo()!=null) && (matriceControllo[xRe][yRe+1]==1 || scacchi[xRe][yRe+1].getPezzo()!=null))
                    JOptionPane.showMessageDialog(null,"scaco matto!!!");
            break;
            case 5:
                 JOptionPane.showMessageDialog(null,+matriceControllo[xRe][yRe]+" normale\n"+matriceControllo[xRe-1][yRe-1]+" alto sinistra\n"+matriceControllo[xRe-1][yRe]+" alto\n"+matriceControllo[xRe-1][yRe+1]+" alto dx\n"+matriceControllo[xRe][yRe+1]+" destra\n"+matriceControllo[xRe][yRe-1]+" sinistra");
                if(matriceControllo[xRe][yRe]==1 && (matriceControllo[xRe-1][yRe-1]==1 || scacchi[xRe-1][yRe-1].getPezzo()!=null) && (matriceControllo[xRe-1][yRe]==1 || scacchi[xRe-1][yRe].getPezzo()!=null) && (matriceControllo[xRe-1][yRe+1]==1 || scacchi[xRe-1][yRe+1].getPezzo()!=null) && (matriceControllo[xRe][yRe+1]==1 || scacchi[xRe][yRe+1].getPezzo()!=null) && (matriceControllo[xRe][yRe-1]==1 || scacchi[xRe][yRe-1].getPezzo()!=null))
                    JOptionPane.showMessageDialog(null,"scaco matto!!!");
            break;
            case 6:
                JOptionPane.showMessageDialog(null,+matriceControllo[xRe][yRe]+" normale\n"+matriceControllo[xRe+1][yRe-1]+" basso sx\n"+matriceControllo[xRe+1][yRe]+" basso\n"+matriceControllo[xRe+1][yRe+1]+" basso dx\n"+matriceControllo[xRe][yRe+1]+" destra\n"+matriceControllo[xRe][yRe-1]+" sinistra");
                if(matriceControllo[xRe][yRe]==1 && (matriceControllo[xRe+1][yRe-1]==1 || scacchi[xRe+1][yRe-1].getPezzo()!=null) && (matriceControllo[xRe+1][yRe]==1 || scacchi[xRe+1][yRe].getPezzo()!=null) && (matriceControllo[xRe+1][yRe+1]==1 || scacchi[xRe+1][yRe+1].getPezzo()!=null) && (matriceControllo[xRe][yRe+1]==1 || scacchi[xRe][yRe+1].getPezzo()!=null) && (matriceControllo[xRe][yRe-1]==1 || scacchi[xRe][yRe-1].getPezzo()!=null))
                    JOptionPane.showMessageDialog(null,"scaco matto!!!");
            break;
            case 7:
                JOptionPane.showMessageDialog(null,+matriceControllo[xRe][yRe]+" normale\n"+matriceControllo[xRe-1][yRe]+" alto\n"+matriceControllo[xRe-1][yRe-1]+" alto sx\n"+matriceControllo[xRe][yRe-1]+" sinistra\n"+matriceControllo[xRe+1][yRe-1]+" basso sinistra\n"+matriceControllo[xRe+1][yRe]+" basso");
                if(matriceControllo[xRe][yRe]==1 && (matriceControllo[xRe-1][yRe]==1 || scacchi[xRe-1][yRe].getPezzo()!=null) && (matriceControllo[xRe-1][yRe-1]==1 || scacchi[xRe-1][yRe-1].getPezzo()!=null) && (matriceControllo[xRe][yRe-1]==1 || scacchi[xRe][yRe-1].getPezzo()!=null) && (matriceControllo[xRe+1][yRe-1]==1 || scacchi[xRe+1][yRe-1].getPezzo()!=null) && (matriceControllo[xRe+1][yRe]==1 || scacchi[xRe+1][yRe].getPezzo()!=null))
                    JOptionPane.showMessageDialog(null,"scaco matto!!!");
            break;
            case 8:
                JOptionPane.showMessageDialog(null,+matriceControllo[xRe][yRe]+" normale\n"+matriceControllo[xRe-1][yRe]+" alto\n"+matriceControllo[xRe-1][yRe+1]+" alto dx\n"+matriceControllo[xRe][yRe+1]+" destra\n"+matriceControllo[xRe+1][yRe+1]+" basso dx\n"+matriceControllo[xRe+1][yRe]+" basso");
                if(matriceControllo[xRe][yRe]==1 && (matriceControllo[xRe-1][yRe]==1 || scacchi[xRe-1][yRe].getPezzo()!=null) && (matriceControllo[xRe-1][yRe+1]==1 || scacchi[xRe-1][yRe+1].getPezzo()!=null) && (matriceControllo[xRe][yRe+1]==1 || scacchi[xRe][yRe+1].getPezzo()!=null) && (matriceControllo[xRe+1][yRe+1]==1 || scacchi[xRe+1][yRe+1].getPezzo()!=null) && (matriceControllo[xRe+1][yRe]==1 || scacchi[xRe+1][yRe].getPezzo()!=null))
                    JOptionPane.showMessageDialog(null,"scaco matto!!!");
            break;
            default:
                JOptionPane.showMessageDialog(null,+matriceControllo[xRe][yRe]+" normale\n"+matriceControllo[xRe-1][yRe]+" alto\n"+matriceControllo[xRe-1][yRe+1]+" alto dx\n"+matriceControllo[xRe][yRe+1]+" destra\n"+matriceControllo[xRe+1][yRe+1]+"basso dx\n"+matriceControllo[xRe+1][yRe]+" basso\n"+matriceControllo[xRe+1][yRe-1]+"basso sx\n"+matriceControllo[xRe][yRe-1]+" sinistra\n"+matriceControllo[xRe-1][yRe-1]+"alto sx");
                if(matriceControllo[xRe][yRe]==1 && (matriceControllo[xRe-1][yRe]==1 || scacchi[xRe-1][yRe].getPezzo()!=null) && (matriceControllo[xRe-1][yRe+1]==1 || scacchi[xRe-1][yRe+1].getPezzo()!=null) && (matriceControllo[xRe][yRe+1]==1 || scacchi[xRe][yRe+1].getPezzo()!=null) && (matriceControllo[xRe+1][yRe+1]==1 || scacchi[xRe+1][yRe+1].getPezzo()!=null) && (matriceControllo[xRe+1][yRe]==1 || scacchi[xRe+1][yRe].getPezzo()!=null) && (matriceControllo[xRe+1][yRe-1]==1 || scacchi[xRe+1][yRe-1].getPezzo()!=null) && (matriceControllo[xRe][yRe-1]==1 || scacchi[xRe][yRe-1].getPezzo()!=null) && (matriceControllo[xRe-1][yRe-1]==1 || scacchi[xRe-1][yRe-1].getPezzo()!=null))
=======
                if(matriceControllo[xRe][yRe]==1 && matriceControllo[xRe-1][yRe]==1 && matriceControllo[xRe-1][yRe-1]==1 && matriceControllo[xRe][yRe-1]==1)
                    JOptionPane.showMessageDialog(null,"scaco matto!!!");
            break;
            case 2:
                if(matriceControllo[xRe][yRe]==1 && matriceControllo[xRe-1][yRe]==1 && matriceControllo[xRe-1][yRe+1]==1 && matriceControllo[xRe][yRe+1]==1)
                    JOptionPane.showMessageDialog(null,"scaco matto!!!");
            break;
            case 3:
                if(matriceControllo[xRe][yRe]==1 && matriceControllo[xRe+1][yRe]==1 && matriceControllo[xRe+1][yRe-1]==1 && matriceControllo[xRe][yRe-1]==1)
                    JOptionPane.showMessageDialog(null,"scaco matto!!!");
            break;
            case 4:
                if(matriceControllo[xRe][yRe]==1 && matriceControllo[xRe+1][yRe]==1 && matriceControllo[xRe+1][yRe+1]==1 && matriceControllo[xRe][yRe+1]==1)
                    JOptionPane.showMessageDialog(null,"scaco matto!!!");
            break;
            case 5:
                if(matriceControllo[xRe][yRe]==1 && matriceControllo[xRe-1][yRe-1]==1 && matriceControllo[xRe-1][yRe]==1 && matriceControllo[xRe-1][yRe+1]==1 && matriceControllo[xRe][yRe+1]==1 && matriceControllo[xRe][yRe-1]==1)
                    JOptionPane.showMessageDialog(null,"scaco matto!!!");
            break;
            case 6:
                if(matriceControllo[xRe][yRe]==1 && matriceControllo[xRe+1][yRe-1]==1 && matriceControllo[xRe+1][yRe]==1 && matriceControllo[xRe+1][yRe+1]==1 && matriceControllo[xRe][yRe+1]==1 && matriceControllo[xRe][yRe-1]==1)
                    JOptionPane.showMessageDialog(null,"scaco matto!!!");
            break;
            case 7:
                if(matriceControllo[xRe][yRe]==1 && matriceControllo[xRe-1][yRe]==1 && matriceControllo[xRe-1][yRe-1]==1 && matriceControllo[xRe][yRe-1]==1 && matriceControllo[xRe+1][yRe-1]==1 && matriceControllo[xRe+1][yRe]==1)
                    JOptionPane.showMessageDialog(null,"scaco matto!!!");
            break;
            case 8:
                if(matriceControllo[xRe][yRe]==1 && matriceControllo[xRe-1][yRe]==1 && matriceControllo[xRe-1][yRe+1]==1 && matriceControllo[xRe][yRe+1]==1 && matriceControllo[xRe+1][yRe+1]==1 && matriceControllo[xRe+1][yRe]==1)
                    JOptionPane.showMessageDialog(null,"scaco matto!!!");
            break;
            default:
                if(matriceControllo[xRe][yRe]==1 && matriceControllo[xRe-1][yRe]==1 && matriceControllo[xRe-1][yRe+1]==1 && matriceControllo[xRe][yRe+1]==1 && matriceControllo[xRe+1][yRe+1]==1 && matriceControllo[xRe+1][yRe]==1 && matriceControllo[xRe+1][yRe-1]==1 && matriceControllo[xRe][yRe-1]==1 && matriceControllo[xRe-1][yRe-1]==1)
>>>>>>> origin/master
                    JOptionPane.showMessageDialog(null,"scaco matto!!!");     
        }
    }
}
