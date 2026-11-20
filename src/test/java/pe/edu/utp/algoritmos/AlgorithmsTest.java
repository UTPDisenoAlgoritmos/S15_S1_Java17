package pe.edu.utp.algoritmos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pe.edu.utp.algoritmos.algorithms.*;
import pe.edu.utp.algoritmos.data.CaseRepository;
import pe.edu.utp.algoritmos.domain.Metrics;
import pe.edu.utp.algoritmos.domain.Models.*;
import java.nio.file.Path;
import java.math.BigInteger;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class AlgorithmsTest {
    private final CaseRepository repo=new CaseRepository(Path.of("datos"));
    private Entry[] entries(int... a){Entry[] out=new Entry[a.length];for(int i=0;i<a.length;i++)out[i]=new Entry("ID"+i,a[i]);return out;}
    @Test void sortRandomizedAndPreserveWholeRecords(){
        Random rng=new Random(1501);
        for(int n=0;n<100;n++){
            Entry[] input=entries(rng.ints(n,-10,11).toArray());int[] expected=Sorting.values(input);Arrays.sort(expected);
            for(boolean quick:new boolean[]{false,true}){
                Entry[] actual=input.clone();if(quick)Sorting.quick(actual,new Metrics(),s->{});else Sorting.shell(actual,new Metrics(),s->{});
                assertArrayEquals(expected,Sorting.values(actual));assertEquals(new HashSet<>(List.of(input)),new HashSet<>(List.of(actual)));
            }
        }
    }
    @Test void extremeIntegerKeys(){Entry[] a=entries(Integer.MAX_VALUE,0,Integer.MIN_VALUE,-1,Integer.MAX_VALUE);Sorting.quick(a,new Metrics(),s->{});assertArrayEquals(new int[]{Integer.MIN_VALUE,-1,0,Integer.MAX_VALUE,Integer.MAX_VALUE},Sorting.values(a));}
    @Test void lomutoExactFirstPartition(){Entry[] a=entries(42,18,65,27,55,34,73,39);assertEquals(3,Sorting.partition(a,0,7,new Metrics()));assertArrayEquals(new int[]{18,27,34,39,55,65,73,42},Sorting.values(a));}
    @Test void shellPhaseTrace(){List<String> trace=new ArrayList<>();Sorting.shell(entries(42,18,65,27,55,34,73,39),new Metrics(),trace::add);assertEquals(List.of("gap=4 [42, 18, 65, 27, 55, 34, 73, 39]","gap=2 [42, 18, 55, 27, 65, 34, 73, 39]","gap=1 [18, 27, 34, 39, 42, 55, 65, 73]"),trace);}
    @Test void quickOrderedWorstCaseCount(){Entry[] a=entries(1,2,3,4,5);Metrics m=new Metrics();Sorting.quick(a,m,s->{});assertEquals(10,m.comparisons);assertTrue(Sorting.sorted(a));}
    @Test void searchAbsentEmptyAndDuplicates(){int[] a={1,2,2,4};assertEquals(1,Search.linear(a,2,new Metrics()));int found=Search.binary(a,2,new Metrics(),s->{});assertEquals(2,a[found]);assertEquals(-1,Search.binary(a,3,new Metrics(),s->{}));assertEquals(-1,Search.linear(new int[]{},1,new Metrics()));assertEquals(-1,Search.binary(new int[]{},1,new Metrics(),s->{}));}
    @Test void binaryRejectsUnsortedAtBoundary(){assertThrows(IllegalArgumentException.class,()->Search.requireSorted(new int[]{125,101,104,140,121}));}
    @Test void binarySearchMatchesIndependentMembership(){Random r=new Random(12);for(int k=0;k<50;k++){int[] a=r.ints(k,-20,21).sorted().toArray();for(int t=-21;t<=21;t++){int b=Search.binary(a,t,new Metrics(),s->{});assertEquals(Arrays.binarySearch(a,t)>=0,b>=0);if(b>=0)assertEquals(t,a[b]);}}}
    @Test void allFactorialsUseIndependentBigIntegerOracle(){BigInteger v=BigInteger.ONE;for(int n=0;n<=20;n++){if(n>0)v=v.multiply(BigInteger.valueOf(n));assertEquals(v.longValueExact(),Recursion.factorial(n,new Metrics()));assertEquals(v.longValueExact(),Recursion.factorialIterative(n,new Metrics()));assertEquals(v.longValueExact(),Recursion.factorialStack(n,new Metrics()));}}
    @ParameterizedTest @ValueSource(ints={-1,21,Integer.MAX_VALUE}) void factorialRejectsInvalidDomain(int n){assertThrows(IllegalArgumentException.class,()->Recursion.factorial(n,new Metrics()));assertThrows(IllegalArgumentException.class,()->Recursion.factorialIterative(n,new Metrics()));assertThrows(IllegalArgumentException.class,()->Recursion.factorialStack(n,new Metrics()));}
    @Test void factorialDepthIncludesBase(){Metrics m=new Metrics();assertEquals(24,Recursion.factorial(4,m));assertEquals(5,m.depth);assertEquals(5,m.calls);assertEquals(4,m.multiplications);}
    @Test void indirectEmptyValidAndFirstWrong(){assertEquals(-1,Recursion.indirect(List.of(),new Metrics()));assertEquals(-1,Recursion.indirect(List.of("ENSAMBLE","INSPECCION"),new Metrics()));assertEquals(0,Recursion.indirect(List.of("INSPECCION"),new Metrics()));assertEquals(2,Recursion.indirect(List.of("ENSAMBLE","INSPECCION","INSPECCION","ENSAMBLE"),new Metrics()));}
    @Test void indirectRejectsExcessiveDepth(){assertThrows(IllegalArgumentException.class,()->Recursion.indirect(Collections.nCopies(501,"ENSAMBLE"),new Metrics()));}
    @Test void maximumEmptySingletonNegativeAndTie(){assertEquals(-1,Recursion.maxRecursive(List.of(),new Metrics()));var a=List.of(new Reading("A",-8),new Reading("B",-2),new Reading("C",-2));assertEquals(1,Recursion.maxRecursive(a,new Metrics()));assertEquals(1,Recursion.maxIterative(a,new Metrics()));assertEquals(0,Recursion.maxRecursive(List.of(new Reading("X",0)),new Metrics()));}
    @Test void maximumOddLengthsMatchOracle(){Random r=new Random(13);for(int n=1;n<130;n++){List<Reading> a=new ArrayList<>();for(int i=0;i<n;i++)a.add(new Reading("S"+i,r.nextInt(31)-15));int expected=0;for(int i=1;i<n;i++)if(a.get(i).tenthsCelsius()>a.get(expected).tenthsCelsius())expected=i;Metrics m=new Metrics();assertEquals(expected,Recursion.maxRecursive(a,m));assertEquals(n-1,m.comparisons);assertEquals(expected,Recursion.maxIterative(a,new Metrics()));}}
    @Test void subsetsUseEachIdOnceAndRestoreState(){var a=List.of(new Entry("B1",2),new Entry("B2",3),new Entry("B3",5),new Entry("B4",6));assertEquals(Set.of(List.of("B1","B4"),List.of("B2","B3")),new HashSet<>(Backtracking.subsets(a,8,new Metrics())));assertEquals(List.of(List.of()),Backtracking.subsets(a,0,new Metrics()));assertEquals(List.of(),Backtracking.subsets(List.of(new Entry("A",2)),3,new Metrics()));}
    @Test void equalValuesRemainDifferentResources(){assertEquals(Set.of(List.of("A"),List.of("B")),new HashSet<>(Backtracking.subsets(List.of(new Entry("A",2),new Entry("B",2)),2,new Metrics())));}
    @Test void rejectsUnsafeSubsetPruningAndOversize(){assertThrows(IllegalArgumentException.class,()->Backtracking.subsets(List.of(new Entry("A",-2)),5,new Metrics()));assertThrows(IllegalArgumentException.class,()->Backtracking.subsets(List.of(new Entry("A",0)),5,new Metrics()));assertThrows(IllegalArgumentException.class,()->Backtracking.subsets(Collections.nCopies(21,new Entry("A",1)),5,new Metrics()));}
    private Order o(String id,int weight,int benefit){return new Order(id,"LIMA","ALTA","LISTO",weight,benefit);}
    @Test void knapsackBeatsGreedyAndCapacityZero(){var a=List.of(o("A",6,8),o("B",4,7),o("C",3,6));Selection s=Backtracking.knapsack(a,7,new Metrics());assertEquals(13,s.benefit());assertEquals(7,s.weight());assertEquals(Set.of("B","C"),new HashSet<>(s.ids()));assertEquals(0,Backtracking.knapsack(a,0,new Metrics()).benefit());}
    @Test void knapsackMatchesExhaustiveAcrossRandomInstances(){Random rng=new Random(14);for(int trial=0;trial<80;trial++){List<Order> a=new ArrayList<>();int n=rng.nextInt(11);for(int i=0;i<n;i++)a.add(o("P"+i,1+rng.nextInt(10),rng.nextInt(30)));int capacity=rng.nextInt(25);Selection actual=Backtracking.knapsack(a,capacity,new Metrics());assertEquals(Backtracking.exhaustive(a,capacity,new Metrics()).benefit(),actual.benefit());assertTrue(actual.weight()<=capacity);assertEquals(actual.ids().size(),new HashSet<>(actual.ids()).size());long value=0;int w=0;for(Order item:a)if(actual.ids().contains(item.id())){value+=item.benefit();w+=item.weight();}assertEquals(value,actual.benefit());assertEquals(w,actual.weight());}}
    @Test void knapsackNoFeasibleNonemptySolutionAndLargeWeights(){var a=List.of(o("A",Integer.MAX_VALUE,4),o("B",Integer.MAX_VALUE,5));assertEquals(0,Backtracking.knapsack(a,50,new Metrics()).benefit());assertEquals(5,Backtracking.knapsack(a,Integer.MAX_VALUE,new Metrics()).benefit());assertEquals(5,Backtracking.exhaustive(a,Integer.MAX_VALUE,new Metrics()).benefit());}
    @Test void knapsackBenefitUsesLong(){var a=List.of(o("A",1,Integer.MAX_VALUE),o("B",1,Integer.MAX_VALUE));assertEquals(4294967294L,Backtracking.knapsack(a,2,new Metrics()).benefit());}
    @Test void eligibleRulesDoNotTruncate(){var a=List.of(o("A",1,2),new Order("B","AREQUIPA","ALTA","LISTO",1,9999));assertEquals(List.of(a.get(0)),Backtracking.eligible(a));assertThrows(IllegalArgumentException.class,()->Backtracking.eligible(Collections.nCopies(21,o("A",1,1))));}
    @Test void knapsackRejectsInvalidModel(){assertThrows(IllegalArgumentException.class,()->Backtracking.knapsack(List.of(o("A",0,1)),5,new Metrics()));assertThrows(IllegalArgumentException.class,()->Backtracking.knapsack(List.of(o("A",1,-1)),5,new Metrics()));assertThrows(IllegalArgumentException.class,()->Backtracking.knapsack(List.of(),-1,new Metrics()));}
    @Test void tenCsvIntegrationAndExpectedResults() throws Exception {
        for(int ex=1;ex<=5;ex++)assertFalse(repo.entries(ex).isEmpty());
        assertEquals(16,Search.linear(repo.entries(3).stream().mapToInt(Entry::value).toArray(),736,new Metrics()));
        int[] a=repo.entries(4).stream().mapToInt(Entry::value).toArray();Metrics l=new Metrics(),b=new Metrics();assertEquals(6,Search.linear(a,125,l));assertEquals(6,Search.binary(a,125,b,s->{}));assertEquals(7,l.comparisons);assertEquals(3,b.comparisons);
        assertArrayEquals(new int[]{0,1,4,5,10,20},repo.factorials());assertEquals(45,Recursion.indirect(repo.phases(),new Metrics()));
        Metrics max=new Metrics();assertEquals(37,Recursion.maxRecursive(repo.readings(),max));assertEquals(63,max.comparisons);assertEquals(7,max.depth);
        assertEquals(2,Backtracking.subsets(repo.entries(9),8,new Metrics()).size());
        assertEquals(20,repo.orders().size());var eligible=Backtracking.eligible(repo.orders());assertEquals(16,eligible.size());var best=Backtracking.knapsack(eligible,50,new Metrics());assertEquals(1901,best.benefit());assertEquals(50,best.weight());assertEquals(Set.of("P02","P04","P06","P08","P09","P13","P14"),new HashSet<>(best.ids()));
        assertEquals(best.benefit(),Backtracking.exhaustive(eligible,50,new Metrics()).benefit());
    }
}
