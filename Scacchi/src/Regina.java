/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pc
 */
public class Regina extends Pedina{
    public Regina(Colore sfondo,int x,int y, Pezzi pezzo,Colore col_pezzo){
       
        super(sfondo,x,y,col_pezzo);
    }
    public int getMovimentoVerticaleX(){
        return getRiga();
    }
    public int getMovimentoVerticaleY(){
        return 8;
    }
    public int getMovimentoLateraleX(){
        return 8;
    }
    public int getMovimentoLateraleY(){
        return getColonna();
    }
}

