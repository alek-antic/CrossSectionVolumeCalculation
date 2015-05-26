package Scenes;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;

/**
 * @author Alek
 * @version 5/17/2015
 */
public interface FunctionListener {

    void getFCoefficients(double[] coefficients);
    void getGCoefficients(double[] coefficients);
    double takeIntegral(double a, double b);
    PolynomialFunction getF();
    PolynomialFunction getG();

}
