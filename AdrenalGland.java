public class AdrenalGland {
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
