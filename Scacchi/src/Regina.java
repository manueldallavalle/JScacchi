public class Regina extends Pedina{
    public Regina(Colore sfondo,int x,int y, Pezzi pezzo,Colore col_pezzo){
        super(sfondo,x,y);
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

