/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pc
 */
public class Torre extends Pedina{
    public Torre(Colore sfondo,int x,int y, Pezzi pezzo,Colore col_pezzo){
        //super(sfondo,x,y,pezzo,col_pezzo);
        super(sfondo,x,y,col_pezzo);
    }
    
    public int getMovimentoAvantiX(){
        return getRiga();
    }
    public int getMovimentoAvantiY(){
        return 8;
    }
    public int getMovimentoLateraleX(){
        return 8;
    }
    public int getMovimentoLateraleY(){
        return getColonna();
    }
}
    

