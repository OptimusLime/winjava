package eplex.win.FastCPPNJava.activation.functions;

import eplex.win.FastCPPNJava.activation.ActivationFunction;

/**
 * Created by paul on 8/18/14.
 */
public class AbsoluteValue implements ActivationFunction {
    @Override
    public String functionID() {
        return "AbsoluteValue";
    }

    @Override
    public double calculate(double val) {
        return Math.abs(val);
    }
}
