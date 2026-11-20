package pe.edu.utp.algoritmos.data;

import pe.edu.utp.algoritmos.domain.Models.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/** Solo esta capa conoce archivos, cabeceras y conversiones. */
public final class CaseRepository {
    public static final String[] FILES = {"01_tickets_shell.csv","02_pedidos_quick.csv","03_repuestos_lineal.csv","04_catalogo_binaria.csv","05_tienda_integrada.csv","06_factoriales.csv","07_fases_indirecta.csv","08_sensores.csv","09_bloques_backtracking.csv","10_pedidos_optimos.csv"};
    private final Path directory;
    public CaseRepository(Path directory) {this.directory=directory;}
    public Path file(int exercise) {if(exercise<1 || exercise>10) throw new IllegalArgumentException("Ejercicio 1..10");return directory.resolve(FILES[exercise-1]);}
    public List<Entry> entries(int exercise) throws IOException {
        List<Entry> out=new ArrayList<>();Set<String> ids=new HashSet<>();
        for (var r:CsvReader.read(file(exercise),"id","valor")) {
            String id=id(r.get("id"),ids);out.add(new Entry(id,integer(r,"valor")));
        }
        return List.copyOf(out);
    }
    public int[] factorials() throws IOException {
        List<Integer> out=new ArrayList<>();
        for(var r:CsvReader.read(file(6),"n")) out.add(integer(r,"n"));
        return out.stream().mapToInt(Integer::intValue).toArray();
    }
    public List<String> phases() throws IOException {
        List<String> out=new ArrayList<>();int seq=1;
        for(var r:CsvReader.read(file(7),"secuencia","fase")) {
            if(integer(r,"secuencia")!=seq++) throw new IllegalArgumentException("Secuencia debe ser consecutiva desde 1");
            String phase=r.get("fase");
            if(!Set.of("ENSAMBLE","INSPECCION").contains(phase)) throw new IllegalArgumentException("Fase desconocida: "+phase);
            out.add(phase);
        }
        return List.copyOf(out);
    }
    public List<Reading> readings() throws IOException {
        List<Reading> out=new ArrayList<>();Set<String> ids=new HashSet<>();
        for(var r:CsvReader.read(file(8),"id","temperatura_decimas_c")) out.add(new Reading(id(r.get("id"),ids),integer(r,"temperatura_decimas_c")));
        return List.copyOf(out);
    }
    public List<Order> orders() throws IOException {
        List<Order> out=new ArrayList<>();Set<String> ids=new HashSet<>();
        for(var r:CsvReader.read(file(10),"id","ciudad","prioridad","estado","peso_kg","beneficio_soles")) {
            String id=id(r.get("id"),ids);
            if(!Set.of("LIMA","AREQUIPA").contains(r.get("ciudad")) || !Set.of("ALTA","BAJA").contains(r.get("prioridad")) || !Set.of("LISTO","PENDIENTE").contains(r.get("estado"))) throw new IllegalArgumentException("Categoria de pedido invalida");
            int w=integer(r,"peso_kg"),v=integer(r,"beneficio_soles");
            if(w<=0 || v<0) throw new IllegalArgumentException("Peso positivo y beneficio no negativo");
            out.add(new Order(id,r.get("ciudad"),r.get("prioridad"),r.get("estado"),w,v));
        }
        return List.copyOf(out);
    }
    private static int integer(Map<String,String> row,String col) {
        try{return Integer.parseInt(row.get(col));}catch(NumberFormatException ex){throw new IllegalArgumentException("Entero invalido en "+col+": "+row.get(col),ex);}
    }
    private static String id(String id,Set<String> seen) {if(id.isBlank()||!seen.add(id))throw new IllegalArgumentException("ID vacio o duplicado: "+id);return id;}
}
