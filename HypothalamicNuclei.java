public class HypothalamicNuclei {
    final private double k_r = 0.028; // degradation rate of CRH
    final private double T_r = 20; // pulsation period of CRH
    final private double circadian_frequency = (2 * Math.PI) / (double) 1440;
    final private double t_0 = 30; // because of the exponential function Fv(V)

    public double H_t(double t) {
        return Hr_t(t) * Hc_t(t);
    }

    public double Hc_t(double t) {
        return 0.55 - 0.45 * Math.cos(circadian_frequency * (t - t_0));
    }

    public double Hr_t(double t) {
        return Math.pow(Math.E, -(k_r * (t % T_r)));
    }
}
