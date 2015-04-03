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
    private JLabel ped_mangiate_bianche[]=new JLabel[16];
    private JLabel ped_mangiate_nere[]=new JLabel[16];
    private JPanel pedineMangiate=new JPanel();
    
    public DisegnaScacchiera(){
        this.setLayout(new BorderLayout());
        struttura();
        setScacchiera();
    }
    private void struttura(){
            //Imposta le coordinate sopra della scacchiera
           coordinateRiga.setLayout(new FlowLayout());
           JLabel vuoto=new JLabel("PEZZI MANGIATI");
           vuoto.setHorizontalAlignment(2);
           vuoto.setForeground(java.awt.Color.decode("#ffffb3"));
           JLabel vuoto1=new JLabel("");
           vuoto.setPreferredSize(new Dimension(100,30));
           vuoto1.setPreferredSize(new Dimension(57,30));
           coordinateRiga.add(vuoto1);
           for(int i=0;i<8;i++){
               cr[i]=new JLabel(""+(char)(i+65)); 
               cr[i].setHorizontalAlignment(JLabel.CENTER);
               cr[i].setHorizontalAlignment(2);
               cr[i].setPreferredSize(new Dimension(70, 30));
               cr[i].setForeground(java.awt.Color.decode("#ffffb3"));
               coordinateRiga.add(cr[i]);
               coordinateRiga.setBackground(java.awt.Color.decode("#762825"));
           }         
           coordinateRiga.add(vuoto);
           coordinateColonna.setLayout(new GridLayout(8,1));
           coordinateColonna.setPreferredSize(new Dimension(40,70));
           
           for(int i=0;i<8;i++){
               cc[i]=new JLabel(""+(i+1));
               cc[i].setHorizontalAlignment( JLabel.CENTER );
               cc[i].setForeground(java.awt.Color.decode("#ffffb3"));
               coordinateColonna.add(cc[i]);
               coordinateColonna.setBackground(java.awt.Color.decode("#762825"));
           }
             
           //imposta jlabel che conta le mosse
           
           contatoreMosse.setLayout(new FlowLayout(FlowLayout.LEADING,42,0));
           mossa.setText("MOSSE PARTITA: 0");
           mossa.setPreferredSize(new Dimension(200,45));
           mossa.setForeground(java.awt.Color.decode("#ffffb3"));
           contatoreMosse.add(mossa);
           contatoreMosse.setBackground(java.awt.Color.decode("#762825"));
           add(contatoreMosse,BorderLayout.SOUTH);
           
           // imposta barra a dx che contiene i pezzi mangiati
           pedineMangiate.setLayout(new GridLayout(16,2));

           for(int i=0;i<16;i++){
               ped_mangiate_bianche[i]=new JLabel();
               ped_mangiate_nere[i]=new JLabel();
               ped_mangiate_bianche[i].setHorizontalAlignment( JLabel.CENTER );
               ped_mangiate_nere[i].setHorizontalAlignment( JLabel.CENTER );
               ped_mangiate_bianche[i].setPreferredSize(new Dimension(70, 40));
               ped_mangiate_nere[i].setPreferredSize(new Dimension(70, 40));
               pedineMangiate.add(ped_mangiate_bianche[i]);
               pedineMangiate.add(ped_mangiate_nere[i]);
               pedineMangiate.setBackground(java.awt.Color.decode("#762825"));
           }
           add(coordinateColonna,BorderLayout.WEST);
           add(coordinateRiga,BorderLayout.NORTH);
           add(pedineMangiate,BorderLayout.EAST);
           scacchiera.setBackground(Color.decode("#762825"));
           add(scacchiera,BorderLayout.CENTER);
           scacchiera.setLayout(new GridLayout(8,8));    
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
                
                (scacchi[z][k]).addActionListener(new GestoreAzione(scacchi,mossa,ped_mangiate_bianche,ped_mangiate_nere));
                scacchi[z][k].setBorder(BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 1));
                scacchiera.add(scacchi[z][k]);
            }
            
        }
    }
    
    protected void resetScacchiera(){
        scacchiera.removeAll();
        mossa.setText("MOSSE PARTITA: 0");
        setScacchiera();
        for(int i=0;i<16;i++){
           this.ped_mangiate_bianche[i].setIcon(new javax.swing.ImageIcon(getClass().getResource("")));
           this.ped_mangiate_nere[i].setIcon(new javax.swing.ImageIcon(getClass().getResource("")));
        }
    }
    
    public Pedina[][] getScacchiera(){
        return scacchi;
    }
}
