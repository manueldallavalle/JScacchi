public class Pedone extends Pedina{
    public Pedone(Colore sfondo,int x,int y, Pezzi pezzo,Colore col_pezzo){
        super(sfondo,x,y);
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
