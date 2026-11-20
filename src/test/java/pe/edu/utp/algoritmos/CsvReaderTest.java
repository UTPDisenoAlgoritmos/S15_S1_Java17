package pe.edu.utp.algoritmos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pe.edu.utp.algoritmos.data.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class CsvReaderTest {
    @TempDir Path tmp;
    private Path file(String text) throws Exception {Path p=tmp.resolve("input.csv");Files.writeString(p,text,StandardCharsets.UTF_8);return p;}
    @Test void quotedCommaAndEscapedQuote(){assertEquals(List.of("A,B","dice \"hola\"",""),CsvReader.parseLine("\"A,B\",\"dice \"\"hola\"\"\", ".trim()));}
    @Test void bomCrLfAndBlankLines() throws Exception {var rows=CsvReader.read(file("\uFEFFid,valor\r\nA,5\r\n\r\nB,-2\r\n"),"id","valor");assertEquals(2,rows.size());assertEquals("-2",rows.get(1).get("valor"));}
    @Test void headerOnlyIsEmptyDataset() throws Exception {assertTrue(CsvReader.read(file("id,valor\n"),"id","valor").isEmpty());}
    @Test void absentHeaderFails() throws Exception {Path p=file("");assertThrows(IllegalArgumentException.class,()->CsvReader.read(p,"id","valor"));}
    @Test void wrongHeaderFails() throws Exception {Path p=file("valor,id\n1,A\n");assertThrows(IllegalArgumentException.class,()->CsvReader.read(p,"id","valor"));}
    @Test void wrongColumnCountFails() throws Exception {Path p=file("id,valor\nA,1,extra\n");assertThrows(IllegalArgumentException.class,()->CsvReader.read(p,"id","valor"));}
    @Test void malformedQuotesFail(){for(String line:List.of("\"sin cerrar","a\"b,2","\"a\"b,2"))assertThrows(IllegalArgumentException.class,()->CsvReader.parseLine(line));}
    @Test void repositoryRejectsDuplicateIdAndBadInteger() throws Exception {Path p=tmp.resolve(CaseRepository.FILES[0]);Files.writeString(p,"id,valor\nA,1\nA,2\n");assertThrows(IllegalArgumentException.class,()->new CaseRepository(tmp).entries(1));Files.writeString(p,"id,valor\nA,no_entero\n");assertThrows(IllegalArgumentException.class,()->new CaseRepository(tmp).entries(1));}
    @Test void repositoryRejectsMissingSequenceAndUnknownPhase() throws Exception {Path p=tmp.resolve(CaseRepository.FILES[6]);Files.writeString(p,"secuencia,fase\n2,ENSAMBLE\n");assertThrows(IllegalArgumentException.class,()->new CaseRepository(tmp).phases());Files.writeString(p,"secuencia,fase\n1,OTRA\n");assertThrows(IllegalArgumentException.class,()->new CaseRepository(tmp).phases());}
}
