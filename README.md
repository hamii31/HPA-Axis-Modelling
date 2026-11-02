# HPA - Hypothalamic–Pituitary–Adrenal Axis Pulsatility Model
[![DOI](https://zenodo.org/badge/1443112988.svg)](https://doi.org/10.5281/zenodo.23890132)
A computational implementation of the HPA axis pulsatility model described in:

> Churilov, A.N. and Milton, J.G. (2022) 'Modeling pulsativity in the hypothalamic–pituitary–adrenal hormonal axis', *Scientific Reports*, 12, 8480.

## Overview of HPA
The program simulates the hormonal interaction between the hypothalamus, the anterior pituitary, and the adrenal gland over a 24-hour period. The hypothalamus provides a CRH input with both ultradian (roughly hourly) and circadian (daily) components. A pulse generator in the anterior pituitary integrates this input and fires when it reaches a threshold, producing pulses of ACTH. ACTH then drives cortisol (CORT) production in the adrenal gland after a 15-minute synthesis delay, and CORT feeds back to suppress both ACTH and the pulse generator.

The simulation runs in six stages for every time step:

1. **Hypothalamic input** - The CRH signal H(t) is computed as the product of an ultradian sawtooth and a circadian cosine.
2. **Delayed ACTH lookup** - The ACTH value from 15 minutes earlier is retrieved from a stored history, to model the time needed for CORT synthesis.
3. **Rate computation** - The rates of change of ACTH, the pulse generator, and CORT are all computed from the current state.
4. **State update** - All three variables are advanced together using the forward Euler method (dt = 0.01 min).
5. **Firing check** - If the pulse generator reaches its threshold, the firing time is recorded and the generator resets to zero.
6. **Recording** - The state is saved every 0.1 min for analysis.

Note: All model parameters are hardcoded. The simulation is deterministic, so every run produces the same results.

## Sample Output
```
t: 0.000 | H: 0.104 | V: 0.000 | A: 0.100 | C: 0.100
t: 5.000 | H: 0.089 | V: 0.300 | A: 8.406 | C: 0.092
t: 10.000 | H: 0.077 | V: 0.588 | A: 6.603 | C: 0.086
t: 15.000 | H: 0.066 | V: 0.866 | A: 5.043 | C: 0.081
t: 20.000 | H: 0.100 | V: 1.130 | A: 3.801 | C: 1.559
t: 25.000 | H: 0.087 | V: 1.387 | A: 4.607 | C: 2.677
t: 30.000 | H: 0.076 | V: 1.631 | A: 3.594 | C: 3.098
t: 35.000 | H: 0.066 | V: 1.864 | A: 2.819 | C: 3.104
t: 40.000 | H: 0.100 | V: 2.089 | A: 2.232 | C: 3.190
t: 45.000 | H: 0.088 | V: 2.317 | A: 2.854 | C: 3.103
t: 50.000 | H: 0.077 | V: 2.537 | A: 2.309 | C: 2.855
t: 55.000 | H: 0.067 | V: 2.751 | A: 1.859 | C: 2.538
t: 60.000 | H: 0.104 | V: 2.958 | A: 1.503 | C: 2.396
t: 65.000 | H: 0.091 | V: 3.171 | A: 1.960 | C: 2.229
t: 70.000 | H: 0.081 | V: 3.376 | A: 1.608 | C: 2.006
t: 75.000 | H: 0.071 | V: 0.108 | A: 5.717 | C: 1.764
...
t: 1420.000 | H: 0.111 | V: 2.740 | A: 1.754 | C: 3.235
t: 1425.000 | H: 0.094 | V: 2.955 | A: 2.231 | C: 2.954
t: 1430.000 | H: 0.081 | V: 3.162 | A: 1.781 | C: 2.610
t: 1435.000 | H: 0.069 | V: 3.362 | A: 1.414 | C: 2.254
t: 1440.000 | H: 0.104 | V: 0.082 | A: 4.418 | C: 2.065
```

## Project Requirements
Java Development Kit (JDK) 8 or later.

## How to Run The Project
```
javac Main.java
java Main
```

## Thoughts
Running the simulation with the parameters for an exponential Fv(V) results in action potentials firing on average every 62.4 minutes (23 total firings in the 24h experiment), with the time between each AP firing shrinking as the experiment approached the middle of the 24 hours (firing every 55 minutes). At the beginning and the end of the 24h experiment, the AP firing is at its most staggered timing. 
The paper states that the peak of their simulation was an AP every 51 minutes, however the peak that my implementation reaches is an AP every 55 minutes. However, both the paper and my implementation agree that the AP fire every 74-75 minutes at nadir. 

## Reference
Churilov, A.N. and Milton, J.G. (2022) 'Modeling pulsativity in the hypothalamic–pituitary–adrenal hormonal axis', *Scientific Reports*, 12, 8480. Available at: https://doi.org/10.1038/s41598-022-12513-w (Accessed: 2 November 2026).
