package pe.edu.utp.algoritmos.app;

import pe.edu.utp.algoritmos.data.CaseRepository;
import pe.edu.utp.algoritmos.domain.Models.Measurement;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public final class Main {
    private Main(){ }
    public static void main(String[] args){
        try{execute(args);}catch(Exception ex){System.err.println("Error: "+ex.getMessage());System.exit(1);}
    }
    private static void execute(String[] args) throws IOException {
        Path data=Path.of("datos"),report=null;Integer target=null,capacity=null;boolean all=false,trace=false;Integer exercise=null;
        for(int i=0;i<args.length;i++){
            switch(args[i]){
                case "--help"->{usage();return;}
                case "--all"->all=true;
                case "--trace"->trace=true;
                case "--data"->{data=Path.of(next(args,++i));}
                case "--report"->{report=Path.of(next(args,++i));}
                case "--target"->{target=Integer.parseInt(next(args,++i));}
                case "--capacity"->{capacity=Integer.parseInt(next(args,++i));}
                default->{if(exercise!=null)throw new IllegalArgumentException("Solo un ejercicio o --all");exercise=Integer.parseInt(args[i]);}
            }
        }
        if(all&&exercise!=null)throw new IllegalArgumentException("Usar un ejercicio o --all");
        var runner=new ExerciseRunner(new CaseRepository(data),trace,target,capacity);
        if(all)for(int ex=1;ex<=10;ex++)runner.run(ex);
        else if(exercise!=null)runner.run(exercise);
        else {
            Scanner scanner=new Scanner(System.in,StandardCharsets.UTF_8);
            while(true){
                System.out.println("\nUTP - S15 S1 - Resolucion de problemas");
                for(int i=0;i<10;i++)System.out.println((i+1)+") "+ExerciseRunner.TITLES[i]);
                System.out.print("11) Todos\n0) Salir\nElija: ");
                if(!scanner.hasNextLine())break;
                String choice=scanner.nextLine().trim();
                if(choice.equals("0"))break;
                try{int ex=Integer.parseInt(choice);if(ex==11)for(int j=1;j<=10;j++)runner.run(j);else runner.run(ex);}
                catch(IllegalArgumentException|IOException ex){System.out.println("Entrada invalida: "+ex.getMessage());}
            }
        }
        if(report!=null)writeReport(report,runner.results());
    }
    private static String next(String[] args,int i){if(i>=args.length)throw new IllegalArgumentException("Falta valor de opcion");return args[i];}
    private static void usage(){System.out.println("java -jar target/utp-s15-s1.jar [1..10 | --all] [--data carpeta] [--target entero] [--capacity kg] [--trace] [--report archivo.csv]\nSin numero: menu. --target: busquedas E3-E5 o meta E9. --capacity: E10. Ejecutar desde la carpeta del pom.xml.");}
    private static String cell(String s){return "\""+s.replace("\"","\"\"")+"\"";}
    private static void writeReport(Path path,List<Measurement> rows) throws IOException {
        if(path.toAbsolutePath().getParent()!=null)Files.createDirectories(path.toAbsolutePath().getParent());
        List<String> lines=new ArrayList<>();lines.add("ejercicio,variante,resultado,comparaciones,escrituras,intercambios,llamadas,nodos,podas,soluciones,profundidad,multiplicaciones,tiempo_ns");
        for(var r:rows)lines.add(r.exercise()+","+cell(r.variant())+","+cell(r.result())+","+r.metrics().csv()+","+r.nanos());
        Files.write(path,lines,StandardCharsets.UTF_8);System.out.println("Reporte: "+path);
    }
}
