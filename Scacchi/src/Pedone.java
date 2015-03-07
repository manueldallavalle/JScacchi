/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pc
 */
public class Pedone extends Pedina{
    public Pedone(){
        //super();
    }
    public Pedone(Colore sfondo,int x,int y, Pezzi pezzo,Colore col_pezzo){
        //super(sfondo,x,y,pezzo,col_pezzo);
        super(sfondo,x,y,col_pezzo);
    }
    public int getMovimentoDownX(){
        return getRiga()+1;
    }
    
    public int getMovimentoUpX(){
        return getRiga()-1;
    }
    public int getMovimentoUpY(){
        return getColonna();
    }
}
