package pe.edu.utp.algoritmos.algorithms;

import pe.edu.utp.algoritmos.domain.Metrics;
import pe.edu.utp.algoritmos.domain.Models.Reading;
import java.util.*;

public final class Recursion {
    private Recursion() { }
    private static void domain(int n){if(n<0||n>20)throw new IllegalArgumentException("Factorial exige 0 <= n <= 20 para long");}
    public static long factorial(int n,Metrics m){domain(n);return fact(n,m,1);}
    private static long fact(int n,Metrics m,int depth){m.enter(depth);if(n==0)return 1;m.multiplications++;return Math.multiplyExact(n,fact(n-1,m,depth+1));}
    public static long factorialIterative(int n,Metrics m){domain(n);long v=1;for(int i=1;i<=n;i++){m.nodes++;m.multiplications++;v=Math.multiplyExact(v,i);}return v;}
    public static long factorialStack(int n,Metrics m){domain(n);Deque<Integer> stack=new ArrayDeque<>();for(int i=n;i>0;i--){stack.push(i);m.depth=Math.max(m.depth,stack.size());}long v=1;while(!stack.isEmpty()){m.nodes++;m.multiplications++;v=Math.multiplyExact(v,stack.pop());}return v;}
    /** Primera anomalia de una secuencia que empieza por ENSAMBLE. */
    public static int indirect(List<String> phases,Metrics m){if(phases.size()>500)throw new IllegalArgumentException("Recursion indirecta limitada a 500 eventos");return expectAssembly(phases,0,m,1);}
    private static int expectAssembly(List<String> a,int i,Metrics m,int depth){m.enter(depth);if(i==a.size())return -1;m.comparisons++;if(!a.get(i).equals("ENSAMBLE"))return i;return expectInspection(a,i+1,m,depth+1);}
    private static int expectInspection(List<String> a,int i,Metrics m,int depth){m.enter(depth);if(i==a.size())return -1;m.comparisons++;if(!a.get(i).equals("INSPECCION"))return i;return expectAssembly(a,i+1,m,depth+1);}
    public static int phasesIterative(List<String> a,Metrics m){for(int i=0;i<a.size();i++){m.nodes++;m.comparisons++;if(!a.get(i).equals(i%2==0?"ENSAMBLE":"INSPECCION"))return i;}return -1;}
    /** Ante empates elige el menor indice de entrada. Vacio devuelve -1. */
    public static int maxRecursive(List<Reading> a,Metrics m){if(a.isEmpty())return -1;return max(a,0,a.size(),m,1);}
    private static int max(List<Reading> a,int lo,int hi,Metrics m,int depth){m.enter(depth);if(hi-lo==1)return lo;int mid=lo+(hi-lo)/2;int l=max(a,lo,mid,m,depth+1),r=max(a,mid,hi,m,depth+1);m.comparisons++;return a.get(l).tenthsCelsius()>=a.get(r).tenthsCelsius()?l:r;}
    public static int maxIterative(List<Reading> a,Metrics m){if(a.isEmpty())return -1;int best=0;m.nodes++;for(int i=1;i<a.size();i++){m.nodes++;m.comparisons++;if(a.get(i).tenthsCelsius()>a.get(best).tenthsCelsius())best=i;}return best;}
}
