package pe.edu.utp.algoritmos.domain;

import java.util.List;

public final class Models {
    private Models() { }
    public record Entry(String id, int value) { }
    public record Reading(String id, int tenthsCelsius) { }
    public record Order(String id, String city, String priority, String state, int weight, int benefit) { }
    public record Selection(long benefit, int weight, List<String> ids) {
        public Selection { ids = List.copyOf(ids); }
    }
    public record Measurement(int exercise, String variant, String result, Metrics metrics, long nanos) { }
}
