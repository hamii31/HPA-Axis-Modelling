/**
 * Churilov, A.N., Milton, J.G. Modeling pulsativity in the hypothalamic–pituitary–adrenal hormonal axis. Sci Rep 12, 8480 (2022). https://doi.org/10.1038/s41598-022-12513-w
 */

public class Main {
    public static SimulationResult simulation(HypothalamicNuclei h, AnteriorPituitary p, AdrenalGland a) {
        double dt = 0.01;
        int totalTime = 1440; // 24h
        int numSteps = (int) (totalTime / dt);
        int delaySteps = (int) Math.round(a.delay / dt);
        int recordEvery = 500; // save every 5 minutes

        double A = p.init_A;
        double A_delayed;
        double V = 0;
        double C = a.init_C;

        double[] A_history = new double[numSteps + 1];
        A_history[0] = A;

        SimulationResult result = new SimulationResult(numSteps / recordEvery + 1);

        for (int n = 0; n < numSteps; n++) {
            double t = n * dt;

            // Hypothalamic input
            double H = h.H_t(t);

            // Record before update
            if (n % recordEvery == 0) result.record(t, H, V, A, C);

            // Compute delayed ACTH
            if (n >= delaySteps) A_delayed = A_history[n - delaySteps];
            else A_delayed = p.init_A;

            // Compute rates from current values
            double dA = p.A_t(t, a, V, A, H, C);
            double dV = p.V_t(t, a, V, H, C);
            double dC = a.C_t(t, C, A_delayed);

            // update values
            A = A + dA * dt;
            V = V + dV * dt;
            C = C + dC * dt;

            // Keep track of firing and reset on fire
            if (V >= p.delta) {
                result.firingTimes.add(t + dt);
                V = 0;
            }

            A_history[n + 1] = A; 
        }

        double tEnd = numSteps * dt;
        result.record(tEnd, h.H_t(tEnd), V, A, C);

        return result;
    }

    public static void main(String[] args) {
        HypothalamicNuclei h = new HypothalamicNuclei();
        AnteriorPituitary p = new AnteriorPituitary();
        AdrenalGland a = new AdrenalGland();

        SimulationResult result = simulation(h, p, a);
        result.display();
    }
}
