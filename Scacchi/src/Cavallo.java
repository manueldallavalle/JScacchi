/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pc
 */
public class Cavallo extends Pedina{
    public Cavallo(Colore sfondo,int x,int y, Pezzi pezzo,Colore col_pezzo){
        //super(sfondo,x,y,pezzo,col_pezzo);
        super(sfondo,x,y,col_pezzo);
    }
    //uno dei 2 movimenti avanti del cavallo
    public int getMovimentoUpRightX(){
        return getRiga()+1;
    }
    public int getMovimentoUpRightY(){
        return getColonna()+2;
    }
    //l'altro movimento avanti del cavallo
    public int getMovimentoUpLeftX(){
        return getRiga()-1;
    }
    public int getMovimentoUpLeftY(){
        return getColonna()+2;
    }
    //uno dei 2 movimenti a sinistra del cavallo
    public int getMovimentoLeftUpX(){
        return getRiga()-2;
    }
    public int getMovimentoLeftUpY(){
        return getColonna()+1;
    }
    //l'altro movimento a sinistra del cavallo
    public int getMovimentoLeftDownX(){
        return getRiga()-2;
    }
    public int getMovimentoLeftDownY(){
        return getColonna()-1;
    }
    //uno dei 2 movimenti a destra del cavallo
    public int getMovimentoRightUpX(){
        return getRiga()+2;
    }
    public int getMovimentorightUpY(){
        return getColonna()+1;
    }
    //l'altro movimento a destra del cavallo
    public int getMovimentorightDownX(){
        return getRiga()+2;
    }
    public int getMovimentoRightDownY(){
        return getColonna()-1;
    }
    //uno dei 2 movimenti indietro del cavallo
    public int getMovimentoDownLeftX(){
        return getRiga()-1;
    }
    public int getMovimentoDownLeftY(){
        return getColonna()-2;
    }
    //l'altro movimento indietro del cavallo
    public int getMovimentoDownRightX(){
        return getRiga()+1;
    }
    public int getMovimentoDownRightY(){
        return getColonna()-2;
    }
}
    

