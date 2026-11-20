package pe.edu.utp.algoritmos.domain;

/** Conteos de operaciones semanticas. Una unidad no equivale a una instruccion CPU. */
public final class Metrics {
    public long comparisons, writes, swaps, calls, nodes, prunes, solutions, multiplications;
    public int depth;
    public void enter(int level) { calls++; nodes++; depth = Math.max(depth, level); }
    public String csv() {
        return comparisons+","+writes+","+swaps+","+calls+","+nodes+","+prunes+","+solutions+","+depth+","+multiplications;
    }
    @Override public String toString() {
        return "comparaciones="+comparisons+" escrituras="+writes+" intercambios="+swaps
            +" llamadas="+calls+" nodos="+nodes+" podas="+prunes+" soluciones="+solutions
            +" profundidad="+depth+" multiplicaciones="+multiplications;
    }
}
