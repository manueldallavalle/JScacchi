
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;


import javax.swing.*;

public class DisegnaScacchiera extends JPanel{
    private Pedina[][] scacchi = new Pedina[8][8];
    private JLabel cr[] = new JLabel[9];
    private JLabel cc[] = new JLabel[8];
    private JLabel mossa = new JLabel();
    int cont = 0;
    private JPanel scacchiera = new JPanel();
    private JPanel coordinateRiga = new JPanel();
    private JPanel coordinateColonna = new JPanel();
    private JPanel contatoreMosse = new JPanel();
    
    public DisegnaScacchiera(){
        this.setLayout(new BorderLayout());
        struttura();
        setScacchiera();
    }
    private void struttura(){
            //Imposta le coordinate sopra della scacchiera
           coordinateRiga.setLayout(new FlowLayout());
           JLabel vuoto=new JLabel(" ");
           vuoto.setPreferredSize(new Dimension(35,30));
           coordinateRiga.add(vuoto);
           for(int i=0;i<8;i++){
               cr[i]=new JLabel(""+(char)(i+65)); 
               cr[i].setHorizontalAlignment(JLabel.CENTER);
               cr[i].setPreferredSize(new Dimension(70, 30));
               cr[i].setForeground(java.awt.Color.white);
               coordinateRiga.add(cr[i]);
               coordinateRiga.setBackground(java.awt.Color.decode("#c35817"));
           }
           
           //Imposta le coordinate a sinistra della scacchiera
           
           coordinateColonna.setLayout(new GridLayout(8,1));
           coordinateColonna.setPreferredSize(new Dimension(40,70));
           
           for(int i=0;i<8;i++){
               cc[i]=new JLabel(""+(i+1));
               cc[i].setHorizontalAlignment( JLabel.CENTER );
               cc[i].setForeground(java.awt.Color.white);
               coordinateColonna.add(cc[i]);
               coordinateColonna.setBackground(java.awt.Color.decode("#c35817"));
           }
           
           //imposta la scacchiera vera e propria
           
           add(coordinateColonna,BorderLayout.WEST);
           add(coordinateRiga,BorderLayout.NORTH);
           scacchiera.setBackground(Color.decode("#c35817"));
           add(scacchiera,BorderLayout.CENTER);
           scacchiera.setLayout(new GridLayout(8,8));
           
           //imposta jlabel che conta le mosse
           
           contatoreMosse.setLayout(new FlowLayout(FlowLayout.LEADING,42,0));
           mossa.setText("MOSSE PARTITA: 0");
           mossa.setPreferredSize(new Dimension(200,45));
           mossa.setForeground(java.awt.Color.white);
           contatoreMosse.add(mossa);
           contatoreMosse.setBackground(java.awt.Color.decode("#c35817"));
           add(contatoreMosse,BorderLayout.SOUTH);
           
    }
    
    private void setScacchiera(){
        Colore temp_colore_pp;
        for(int z = 0; z < 8; z++){
            // COLORE CASELLE
            for(int k = 0; k < 8; k++){
                temp_colore_pp = (z >= 6) ? Colore.BIANCO : Colore.NERO;
                
                if(z % 2 == 0){
                    if(k % 2 == 0){
                        scacchi[z][k] = new Pedina(Colore.BIANCO, z, k);
                    }else{
                        scacchi[z][k] = new Pedina(Colore.NERO, z, k);
                    }
                }else{
                    if(k % 2 != 0){
                        scacchi[z][k] = new Pedina(Colore.BIANCO, z, k);
                    }else{
                        scacchi[z][k] = new Pedina(Colore.NERO, z, k);
                    }
                }

                if(z == 0 || z == 7){
                    if(k == 0 || k == 7){
                        (scacchi[z][k]).setPezzo(Pezzi.TORRE, temp_colore_pp);
                    }else if(k == 1 || k == 6){
                        (scacchi[z][k]).setPezzo(Pezzi.CAVALLO, temp_colore_pp);
                    }else if(k == 2 || k == 5){
                        (scacchi[z][k]).setPezzo(Pezzi.ALFIERE, temp_colore_pp);
                    }else if(k == 3){
                        (scacchi[z][k]).setPezzo(Pezzi.REGINA, temp_colore_pp);
                    }else if(k == 4){
                        (scacchi[z][k]).setPezzo(Pezzi.RE, temp_colore_pp);
                    }

                }else if(z == 1 || z == 6){
                    (scacchi[z][k]).setPezzo(Pezzi.PEDONE, temp_colore_pp);
                }
                
                (scacchi[z][k]).addActionListener(new GestoreAzione(scacchi,mossa));
                scacchi[z][k].setBorder(BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 1));
                scacchiera.add(scacchi[z][k]);
            }
            
        }
    }
    
    protected void resetScacchiera(){
        scacchiera.removeAll();
        mossa.setText("MOSSE PARTITA: 0");
        setScacchiera();
    }
    
    public Pedina[][] getScacchiera(){
        return scacchi;
    }
}
