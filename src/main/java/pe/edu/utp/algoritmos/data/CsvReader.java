package pe.edu.utp.algoritmos.data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/** CSV UTF-8, coma, cabecera. Admite comillas, comas y comillas escapadas dentro de un campo.
 * El contrato del taller usa un registro por linea fisica (sin saltos de linea en campos).
 */
public final class CsvReader {
    private CsvReader() { }
    public static List<Map<String,String>> read(Path path, String... expected) throws IOException {
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        if (lines.isEmpty()) throw new IllegalArgumentException("CSV sin cabecera: " + path);
        String first = lines.get(0);
        if (first.startsWith("\uFEFF")) first = first.substring(1);
        List<String> header = parseLine(first);
        if (!header.equals(Arrays.asList(expected))) throw new IllegalArgumentException("Cabecera esperada: " + Arrays.toString(expected));
        List<Map<String,String>> result = new ArrayList<>();
        for (int i=1;i<lines.size();i++) {
            if (lines.get(i).isBlank()) continue;
            List<String> cells;
            try { cells = parseLine(lines.get(i)); }
            catch (IllegalArgumentException ex) { throw new IllegalArgumentException("Fila " + (i+1) + ": " + ex.getMessage(), ex); }
            if (cells.size()!=header.size()) throw new IllegalArgumentException("Columnas invalidas en fila " + (i+1));
            Map<String,String> row = new LinkedHashMap<>();
            for (int j=0;j<header.size();j++) row.put(header.get(j),cells.get(j));
            result.add(Collections.unmodifiableMap(row));
        }
        return List.copyOf(result);
    }
    public static List<String> parseLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean quoted=false, closed=false;
        for (int i=0;i<line.length();i++) {
            char c=line.charAt(i);
            if (quoted) {
                if (c=='"') {
                    if (i+1<line.length() && line.charAt(i+1)=='"') {field.append('"');i++;}
                    else {quoted=false;closed=true;}
                } else field.append(c);
            } else if (c==',') {fields.add(field.toString());field.setLength(0);closed=false;}
            else if (c=='"') {
                if (field.length()!=0 || closed) throw new IllegalArgumentException("Comilla fuera de inicio de campo");
                quoted=true;
            } else {
                if (closed) throw new IllegalArgumentException("Texto tras comilla de cierre");
                field.append(c);
            }
        }
        if (quoted) throw new IllegalArgumentException("Comilla sin cerrar; no se admiten campos multilinea");
        fields.add(field.toString());
        return fields;
    }
}
