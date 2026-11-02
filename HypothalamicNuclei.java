public class HypothalamicNuclei {
    final private double k_r = 0.028; // degradation rate of CRH
    final private double T_r = 20; // pulsation period of CRH
    final private double circadian_frequency = (2 * Math.PI) / (double) 1440;

    public double H_t(double t) {
        return Hr_t(t) * Hc_t(t);
    }

    public double Hc_t(double t) {
        return 0.55 - 0.45 * Math.cos(circadian_frequency * (t - 0)); // needs to be fixed
    }

    public double Hr_t(double t) {
        return Math.pow(Math.E, -(k_r * (t - T_r)));
    }
}
