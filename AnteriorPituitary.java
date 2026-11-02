public class AnteriorPituitary {
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
