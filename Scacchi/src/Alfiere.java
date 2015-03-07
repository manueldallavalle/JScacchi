public class Alfiere extends Pedina{
    public Alfiere(Colore sfondo,int x,int y, Pezzi pezzo,Colore col_pezzo){
        super(sfondo,x,y);
    }
    public int getMovimentotX(){
        return getRiga();
    }
    public int getMovimentoY(){
        return getColonna();
    }
}
