package pe.edu.utp.algoritmos.algorithms;

import pe.edu.utp.algoritmos.domain.Models.Entry;
import pe.edu.utp.algoritmos.domain.Metrics;
import java.util.*;
import java.util.function.Consumer;

public final class Sorting {
    private Sorting() { }
    /** Gaps n/2,n/4,...,1. Ordena registros completos por valor. */
    public static void shell(Entry[] a, Metrics m, Consumer<String> trace) {
        Objects.requireNonNull(a);
        for(int gap=a.length/2;gap>0;gap/=2) {
            for(int i=gap;i<a.length;i++) {
                Entry temp=a[i];int j=i;
                while(j>=gap) {
                    m.comparisons++;
                    if(a[j-gap].value()<=temp.value()) break;
                    a[j]=a[j-gap];m.writes++;j-=gap;
                }
                if(j!=i){a[j]=temp;m.writes++;}
            }
            trace.accept("gap="+gap+" "+Arrays.toString(values(a)));
        }
    }
    public static void quick(Entry[] a, Metrics m, Consumer<String> trace) {
        Objects.requireNonNull(a);
        if(a.length>1000)throw new IllegalArgumentException("Quick Lomuto didactico limitado a 1000 registros");
        quick(a,0,a.length-1,m,trace,1);
    }
    private static void quick(Entry[] a,int lo,int hi,Metrics m,Consumer<String> trace,int depth) {
        m.enter(depth);
        if(lo>=hi)return;
        int p=partition(a,lo,hi,m);
        trace.accept("rango=["+lo+","+hi+"] pivote="+a[p].value()+" indice="+p+" "+Arrays.toString(values(a)));
        quick(a,lo,p-1,m,trace,depth+1);quick(a,p+1,hi,m,trace,depth+1);
    }
    public static int partition(Entry[] a,int lo,int hi,Metrics m) {
        int pivot=a[hi].value(),i=lo-1;
        for(int j=lo;j<hi;j++) {m.comparisons++;if(a[j].value()<=pivot)swap(a,++i,j,m);}
        swap(a,i+1,hi,m);return i+1;
    }
    private static void swap(Entry[] a,int i,int j,Metrics m) {
        if(i==j)return;Entry t=a[i];a[i]=a[j];a[j]=t;m.swaps++;m.writes+=2;
    }
    public static int[] values(Entry[] a){return Arrays.stream(a).mapToInt(Entry::value).toArray();}
    public static boolean sorted(Entry[] a){for(int i=1;i<a.length;i++)if(a[i-1].value()>a[i].value())return false;return true;}
}
