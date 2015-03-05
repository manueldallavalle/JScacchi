/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pc
 */
public class Re extends Pedina{
    public Re(Colore sfondo,int x,int y, Pezzi pezzo,Colore col_pezzo){
        //super(sfondo,x,y,pezzo,col_pezzo);
        super(sfondo,x,y,col_pezzo);
    }
    //movimento avanti del re
    public int getMovimentoUpX(){
        return getRiga();
    }
    public int getMovimentoUpY(){
        return getColonna()+1;
    }
    //movimento in diagonale avanti a destra del re
    public int getMovimentoUpRightX(){
        return getRiga()+1;
    }
    public int getMovimentoUpRightY(){
        return getColonna()+1;
    }
    //movimento a destra del re
    public int getMovimentoRightX(){
        return getRiga()+1;
    }
    public int getMovimentoRightY(){
        return getColonna();
    }
    //movimento in diagonale indietro a destra del re
    public int getMovimentoDownRightX(){
        return getRiga()+1;
    }
    public int getMovimentoDownRightY(){
        return getColonna()-1;
    }
    //movimento indietro del re
    public int getMovimentoDownX(){
        return getRiga();
    }
    public int getMovimentoDownY(){
        return getColonna()-1;
    }
    //movimento in diagonale indietro e sinistra del re
    public int getMovimentoDownLeftX(){
        return getRiga()-1;
    }
    public int getMovimentoDownLeftY(){
        return getColonna()-1;
    }
    //movimento a sinistra del re
    public int getMovimentoLeftX(){
        return getRiga()-1;
    }
    public int getMovimentoLeftY(){
        return getColonna();
    }
    //movimento in diagonale avanti e sinistra del re
    public int getMovimentoUpLeftX(){
        return getRiga()-1;
    }
    public int getMovimentoUpLeftY(){
        return getColonna()+1;
    }
}
