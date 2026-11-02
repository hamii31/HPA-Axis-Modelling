import java.util.*;
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

    public void record(double time, double h, double v, double a, double c) {
        t[count] = time;
        this.H[count] = h;
        this.V[count] = v;
        this.A[count] = a;
        this.C[count] = c;
        count++;
    }
    
    public void display() {
        for (int n = 0; n < count; n++) {
            System.out.printf("t: %.3f | H: %.3f | V: %.3f | A: %.3f | C: %.3f\n", t[n], this.H[n], this.V[n], this.A[n], this.C[n]);
        }
    }
}
