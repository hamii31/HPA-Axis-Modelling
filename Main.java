
import java.util.*;

/**
 * Churilov, A.N., Milton, J.G. Modeling pulsativity in the hypothalamic–pituitary–adrenal hormonal axis. Sci Rep 12, 8480 (2022). https://doi.org/10.1038/s41598-022-12513-w
 */

public class Main {
    public static void ultradianCycle(HypothalamicNuclei h, AnteriorPituitary p, AdrenalGland a) {
        double dt = 0.01;
        int totalTime = 1440;
        int numSteps = (int) (totalTime / dt);
        int delaySteps = (int) Math.round(a.delay / dt);

        double A = p.init_A;
        double A_delayed = 0.0;
        double V = 0;
        double C = a.init_C;

        double[] A_history = new double[numSteps + 1];
        A_history[0] = A;
        List<Double> firingTimes = new ArrayList<>();
        
        for (int n = 0; n < numSteps; n++) {
            double t = n * dt;

            // Hypothalamic input
            double H = h.H_t(t);

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
                firingTimes.add(t + dt);
                V = 0;
            }

            A_history[n + 1] = A; 
        }

        for (double fire : firingTimes) {
            System.out.println(fire);
        }
    }
    public static void main(String[] args) {
        HypothalamicNuclei h = new HypothalamicNuclei();
        AnteriorPituitary p = new AnteriorPituitary();
        AdrenalGland a = new AdrenalGland();

        ultradianCycle(h, p, a);
    }
}

class HypothalamicNuclei {
    final private double k_r = 0.028; // degradation rate of CRH
    final private double T_r = 20; // pulsation period of CRH
    final private double circadian_frequency = (2 * Math.PI) / (double) 1440;

    public double H_t(double t) {
        return Hr_t(t) * Hc_t(t);
    }

    public double Hc_t(double t) {
        return 0.55 - 0.45 * Math.cos(circadian_frequency * (t - 0));
    }

    public double Hr_t(double t) {
        return Math.pow(Math.E, -(k_r * (t - T_r)));
    }
}

class AnteriorPituitary {
    public double init_A = 0.1;
    final public double delta = 3.5; // firing threshold for V
    final private double k_a = 0.7; // degradation rate of ACTH
    final private double k_ah = 60; // drive from hypothalamus to pituitary
    final private double A_0 = 0.6; // permanent component of ACTH pulsation rate
    final private double k_s = 0.1;
    final private double k_v = 0.005; // leakage rate of V
    final private double V_0 = 0.05; // minimum action potential;
    final private double k_vh = 0.055; // drive from hypothalamus to V

    public double A_t(double t, AdrenalGland a, double V, double ACTH, double CRH, double CORT) {
        return -(k_a * ACTH) + (A_0 + k_ah * CRH) * a.F_a(CORT) * F_v(V);
    }
    
    public double F_v(double V) {
        return Math.pow(Math.E, -(k_s * V)); 
    }

    public double V_t(double t, AdrenalGland a, double V, double CRH, double CORT) {
        return -(k_v * V) + (V_0 + k_vh * CRH) * a.F_a(CORT);
    }
}

class AdrenalGland {
    public double init_C = 0.1; // base cort level
    final private double k_c = 0.07; // degradation rate of CORT
    final public double delay = 15.0; // the delay for CORT synthesis in minutes
    final private double k_ca = 0.05; // drive from pituitary to adrenal 
    final private double h_c = 40; // half-range point of repressor function F_a(C)
    final private double F_0 = 0.1; // minimal value of F_a(C)

    /**
     * The concentration C(t) satisfies a differential equation with delay
     * where kc is a degradation rate of CORT and the coefficient kca relates to the driving (feedforward) signal
     * from the pituitary to the adrenal gland. The delay τ reflects the time required for the CORT synthesis
     */
    public double C_t(double t, double CORT, double ACTH_delayed) {
        return -(k_c * CORT) + (k_ca * ACTH_delayed); 
    }

    /**
     * The function Fa(C) describes a feedback from CORT to ACTH and V. It is decreasing in C to ensure an
     * inhibition of the ultradian amplitude and frequency when the level of CORT increases. With the increase of C(t)
     * the right-hand side of (2) and the excitatory input I(t) decrease, so the level of A(t) also decreases (is inhibited)
     * and the impulses of V(t) become sparser. In our simulations the function Fa(C) is taken to represent repression
     * Michaelis—Menten kinetics where F0, hc are some positive parameters.
     */
    public double F_a(double CORT) {
        return F_0 + (1 / (1 + (CORT / h_c)));
    }
}
