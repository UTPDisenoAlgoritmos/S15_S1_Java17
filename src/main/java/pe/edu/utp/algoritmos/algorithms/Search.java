package pe.edu.utp.algoritmos.algorithms;

import pe.edu.utp.algoritmos.domain.Metrics;
import java.util.function.Consumer;

public final class Search {
    private Search() { }
    public static int linear(int[] a,int target,Metrics m) {
        for(int i=0;i<a.length;i++){m.comparisons++;if(a[i]==target)return i;}return -1;
    }
    /** Devuelve cualquier coincidencia. Comprobar orden fuera de la medicion de consulta. */
    public static int binary(int[] sorted,int target,Metrics m,Consumer<String> trace) {
        int lo=0,hi=sorted.length-1;
        while(lo<=hi){int mid=lo+(hi-lo)/2;m.comparisons++;
            trace.accept("inicio="+lo+" medio="+mid+" fin="+hi+" valor="+sorted[mid]);
            if(sorted[mid]==target)return mid;
            if(sorted[mid]<target)lo=mid+1;else hi=mid-1;
        }return -1;
    }
    public static void requireSorted(int[] a){for(int i=1;i<a.length;i++)if(a[i-1]>a[i])throw new IllegalArgumentException("Busqueda binaria requiere datos ordenados");}
}
