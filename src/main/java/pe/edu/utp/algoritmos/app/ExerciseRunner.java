package pe.edu.utp.algoritmos.app;

import pe.edu.utp.algoritmos.algorithms.*;
import pe.edu.utp.algoritmos.data.CaseRepository;
import pe.edu.utp.algoritmos.domain.Metrics;
import pe.edu.utp.algoritmos.domain.Models.*;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ExerciseRunner {
    public static final String[] TITLES={"Tickets: Shell Sort","Pedidos: QuickSort Lomuto","Repuestos: busqueda lineal","Catalogo: busqueda binaria vs lineal","Tienda: ordenar y buscar","Factorial: recursion, pila e iteracion","Fases: recursion indirecta","Sensores: maximo divide y venceras vs iteracion","Bloques: backtracking de suma exacta","Pedidos: seleccion optima 0/1"};
    private final CaseRepository repository;
    private final boolean trace;
    private final Integer target,capacity;
    private final List<Measurement> output=new ArrayList<>();
    public ExerciseRunner(CaseRepository repository,boolean trace,Integer target,Integer capacity){this.repository=repository;this.trace=trace;this.target=target;this.capacity=capacity;}
    public List<Measurement> results(){return List.copyOf(output);}
    private <T> T measure(int ex,String variant,Metrics m,Supplier<T> algorithm){return measure(ex,variant,m,algorithm,String::valueOf);}
    private <T> T measure(int ex,String variant,Metrics m,Supplier<T> algorithm,Function<T,String> format){long start=System.nanoTime();T value=algorithm.get();long nanos=System.nanoTime()-start;String result=format.apply(value);var row=new Measurement(ex,variant,result,m,nanos);output.add(row);System.out.printf(Locale.ROOT,"%s: %s%n  %s tiempo_ms=%.6f%n",variant,result,m,nanos/1_000_000.0);return value;}
    public void run(int ex) throws IOException {
        if(ex<1||ex>10)throw new IllegalArgumentException("Ejercicio 1..10");
        System.out.println("\nE"+ex+" - "+TITLES[ex-1]);
        System.out.println("CSV: "+repository.file(ex));
        if(ex<=5)runArray(ex);
        else if(ex==6)runFactorials();
        else if(ex==7)runPhases();
        else if(ex==8)runSensors();
        else if(ex==9)runBlocks();
        else runOrders();
    }
    private void runArray(int ex) throws IOException {
        Entry[] source=repository.entries(ex).toArray(Entry[]::new);
        System.out.println("Registros="+source.length+" entrada="+Arrays.toString(Sorting.values(source)));
        List<String> events=new ArrayList<>();Consumer<String> sink=trace?events::add:s->{};
        if(ex==1||ex==2||ex==5){
            Entry[] a=source.clone();Metrics m=new Metrics();
            measure(ex,"Shell",m,()->{Sorting.shell(a,m,sink);return a;},arr->Arrays.toString(Sorting.values(arr)));verifySort(source,a);
            if(ex!=1){Entry[] b=source.clone();Metrics q=new Metrics();measure(ex,"Quick",q,()->{Sorting.quick(b,q,sink);return b;},arr->Arrays.toString(Sorting.values(arr)));verifySort(source,b);}
            if(ex==5){int[] keys=Sorting.values(a);Search.requireSorted(keys);for(int t:targets(125,145,999)){
                search(ex,Sorting.values(source),t,false,sink,"Lineal_original");
                search(ex,keys,t,true,sink,"Binaria_ordenado");
            }}
        } else if(ex==3) for(int t:targets(736,799))search(ex,Sorting.values(source),t,false,sink,"Lineal_original");
        else {int[] keys=Sorting.values(source);Search.requireSorted(keys);for(int t:targets(125,126)){int a=search(ex,keys,t,false,sink,"Lineal");int b=search(ex,keys,t,true,sink,"Binaria");if((a<0)!=(b<0))throw new IllegalStateException("Busquedas discrepan");}}
        events.forEach(System.out::println);
    }
    private int[] targets(int... defaults){return target==null?defaults:new int[]{target};}
    private int search(int ex,int[] a,int t,boolean binary,Consumer<String> trace,String label){Metrics m=new Metrics();return measure(ex,label+"_objetivo_"+t,m,()->binary?Search.binary(a,t,m,trace):Search.linear(a,t,m));}
    private void verifySort(Entry[] source,Entry[] sorted){
        int[] expected=Sorting.values(source);Arrays.sort(expected);
        if(!Arrays.equals(expected,Sorting.values(sorted)))throw new IllegalStateException("Orden incorrecto");
        var before=new HashSet<>(Arrays.asList(source));var after=new HashSet<>(Arrays.asList(sorted));
        if(!before.equals(after)||source.length!=sorted.length)throw new IllegalStateException("Se perdieron registros");
    }
    private void runFactorials() throws IOException {
        for(int n:repository.factorials()){
            Metrics r=new Metrics(),i=new Metrics(),s=new Metrics();
            long a=measure(6,"Recursivo_n_"+n,r,()->Recursion.factorial(n,r));
            long b=measure(6,"Iterativo_n_"+n,i,()->Recursion.factorialIterative(n,i));
            long c=measure(6,"Pila_explicita_n_"+n,s,()->Recursion.factorialStack(n,s));
            if(a!=b||a!=c)throw new IllegalStateException("Factoriales discrepan");
        }
    }
    private void runPhases() throws IOException {
        var phases=repository.phases();Metrics r=new Metrics(),i=new Metrics();
        int a=measure(7,"Indirecta",r,()->Recursion.indirect(phases,r));
        int b=measure(7,"Iterativa",i,()->Recursion.phasesIterative(phases,i));
        if(a!=b)throw new IllegalStateException("Auditorias discrepan");
        System.out.println(a<0?"Sin anomalia":"Indice="+a+" secuencia="+(a+1)+" linea_fisica_csv="+(a+2));
    }
    private void runSensors() throws IOException {
        var readings=repository.readings();Metrics r=new Metrics(),i=new Metrics();
        int a=measure(8,"Maximo_recursivo",r,()->Recursion.maxRecursive(readings,r));
        int b=measure(8,"Maximo_iterativo",i,()->Recursion.maxIterative(readings,i));
        if(a!=b)throw new IllegalStateException("Maximos discrepan");
        if(a>=0)System.out.printf(Locale.ROOT,"Registro=%s temperatura_C=%.1f%n",readings.get(a).id(),readings.get(a).tenthsCelsius()/10.0);
        else System.out.println("Sin lecturas");
    }
    private void runBlocks() throws IOException {
        var blocks=repository.entries(9);int goal=target==null?8:target;Metrics m=new Metrics();
        System.out.println("Meta="+goal+" uso 0/1 por ID");
        measure(9,"Subconjuntos",m,()->Backtracking.subsets(blocks,goal,m));
    }
    private void runOrders() throws IOException {
        var all=repository.orders();var eligible=Backtracking.eligible(all);int cap=capacity==null?50:capacity;
        System.out.println("Leidos="+all.size()+" elegibles="+eligible.size()+" excluidos="+(all.size()-eligible.size())+" filtro=ciudad=LIMA AND prioridad=ALTA AND estado=LISTO capacidad_kg="+cap);
        Metrics r=new Metrics(),i=new Metrics();
        Selection a=measure(10,"Mochila_con_poda",r,()->Backtracking.knapsack(eligible,cap,r));
        Selection b=measure(10,"Enumeracion_sin_poda",i,()->Backtracking.exhaustive(eligible,cap,i));
        if(a.benefit()!=b.benefit())throw new IllegalStateException("Los optimos discrepan");
        System.out.println("Optimo exacto del conjunto elegible. Empates pueden devolver IDs diferentes.");
    }
}
