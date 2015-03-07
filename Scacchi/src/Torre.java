public class Torre extends Pedina{
    public Torre(Colore sfondo,int x,int y, Pezzi pezzo,Colore col_pezzo){
        super(sfondo,x,y);
    }
    
    public int getMovimentoUpX(){
        
           return getRiga()+1; 
        
    }
    public int getMovimentoDownX(){
        
           return getRiga()-1;
    }
    
    public int getMovimentoLateraleDx(){
        
           return getColonna()+1;
    }
    public int getMovimentoLateraleSx(){
        
           return getColonna()-1;
    }
}
    

