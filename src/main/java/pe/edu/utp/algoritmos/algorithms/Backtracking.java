package pe.edu.utp.algoritmos.algorithms;

import pe.edu.utp.algoritmos.domain.Metrics;
import pe.edu.utp.algoritmos.domain.Models.*;
import java.util.*;

public final class Backtracking {
    private Backtracking() { }
    public static List<List<String>> subsets(List<Entry> blocks,int target,Metrics m) {
        if(target<0||blocks.size()>20)throw new IllegalArgumentException("Meta no negativa, maximo 20 bloques, sin truncar");
        for(var b:blocks)if(b.value()<=0)throw new IllegalArgumentException("La poda exige capacidades positivas");
        List<List<String>> result=new ArrayList<>();
        subsets(blocks,0,target,new ArrayList<>(),result,m,1);return List.copyOf(result);
    }
    private static void subsets(List<Entry> a,int i,int remaining,List<String> current,List<List<String>> out,Metrics m,int depth) {
        m.enter(depth);
        if(remaining==0){out.add(List.copyOf(current));m.solutions++;return;}
        if(remaining<0){m.prunes++;return;}
        if(i==a.size())return;
        current.add(a.get(i).id());
        subsets(a,i+1,remaining-a.get(i).value(),current,out,m,depth+1);
        current.remove(current.size()-1); // restaurar antes de omitir
        subsets(a,i+1,remaining,current,out,m,depth+1);
    }
    /** Filtro de elegibilidad del modelo. Nunca toma solo las primeras N filas. */
    public static List<Order> eligible(List<Order> all) {
        List<Order> selected=all.stream().filter(o->o.city().equals("LIMA")&&o.priority().equals("ALTA")&&o.state().equals("LISTO")).toList();
        if(selected.size()>20)throw new IllegalArgumentException("Mas de 20 elegibles: se rechaza la carga, no se trunca");
        return selected;
    }
    private static void validate(List<Order> a,int capacity){if(capacity<0||a.size()>20)throw new IllegalArgumentException("Capacidad >= 0 y maximo 20 pedidos");for(var o:a)if(o.weight()<=0||o.benefit()<0)throw new IllegalArgumentException("Peso positivo y beneficio no negativo");}
    public static Selection knapsack(List<Order> eligible,int capacity,Metrics m){
        validate(eligible,capacity);long[] suffix=new long[eligible.size()+1];
        for(int i=eligible.size()-1;i>=0;i--)suffix[i]=suffix[i+1]+eligible.get(i).benefit();
        Selection[] best={new Selection(0,0,List.of())};
        knapsack(eligible,0,0,0,capacity,suffix,new ArrayList<>(),best,m,1);return best[0];
    }
    private static void knapsack(List<Order> a,int i,int weight,long value,int cap,long[] suffix,List<String> current,Selection[] best,Metrics m,int depth){
        m.enter(depth);
        if(weight>cap){m.prunes++;return;}
        if(i==a.size()){m.solutions++;if(value>best[0].benefit())best[0]=new Selection(value,weight,current);return;}
        // Suma de beneficios pendientes: cota superior admisible aunque ignore el peso.
        if(value+suffix[i]<=best[0].benefit()){m.prunes++;return;}
        var o=a.get(i);current.add(o.id());
        // Comparacion en long evita overflow al sumar pesos int antes de comprobar capacidad.
        if((long)weight+o.weight()<=cap)knapsack(a,i+1,weight+o.weight(),value+o.benefit(),cap,suffix,current,best,m,depth+1);
        else m.prunes++;
        current.remove(current.size()-1);
        knapsack(a,i+1,weight,value,cap,suffix,current,best,m,depth+1);
    }
    /** Oraculo independiente: evalua todos los subconjuntos, sin podas. */
    public static Selection exhaustive(List<Order> a,int capacity,Metrics m){
        validate(a,capacity);Selection best=new Selection(0,0,List.of());
        for(long mask=0;mask<(1L<<a.size());mask++){
            m.nodes++;long weight=0,value=0;List<String> ids=new ArrayList<>();
            for(int i=0;i<a.size();i++)if((mask&(1L<<i))!=0){weight+=a.get(i).weight();value+=a.get(i).benefit();ids.add(a.get(i).id());}
            if(weight<=capacity){m.solutions++;if(value>best.benefit())best=new Selection(value,(int)weight,ids);}
        }return best;
    }
}
