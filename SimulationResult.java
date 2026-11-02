import java.util.*;
/*
* Holds the records for all parameters during the ultradian cycle simulation
*/
public class SimulationResult {
    final double[] t, H, V, A, C;
    final List<Double> firingTimes = new ArrayList<>();
    private int count = 0;

    SimulationResult(int size) {
        t = new double[size];
        H = new double[size];
        V = new double[size];
        A = new double[size];
        C = new double[size];
    }

    void record(double time, double h, double v, double a, double c) {
        t[count] = time;
        H[count] = h;
        V[count] = v;
        A[count] = a;
        C[count] = c;
        count++;
    }
}
