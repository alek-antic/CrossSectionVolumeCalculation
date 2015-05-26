package Scenes;


import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;

/**
 * @author Alek
 * @version 5/12/15
 *          <p>
 *          A class to represent the region of the screen where the volume or the graph of the function will be drawn
 */
public class GraphScene extends SubScene implements GraphArea, FunctionListener {

    private Camera camera;
    private Canvas graph;
    private PolynomialFunction pF, pG;
    private double xMin, xMax, yMin, yMax;
    private double[] coefficientsf, coefficientsg;

    public GraphScene(Pane root, int width, int height) {
        super(root, width, height);
        camera = new PerspectiveCamera(false);
        graph = new Canvas(width, height);
        root.setStyle("-fx-background-color: grey;");
        root.getChildren().add(graph);
        coefficientsf = new double[6];
        coefficientsg = new double[6];

        xMin = -10;
        xMax = 10;
        yMin = 10;
        yMax = 10;

        double[] fcoefficients = {0,0,1,0,0,0};
        double[] gcoefficients = {0,0,0,1,0,0};

        pF = new PolynomialFunction(fcoefficients);
        pG = new PolynomialFunction(gcoefficients);

        draw();
    }

    @Override
    public void appplyGraphLimits(double[] bounds) {
        xMin = bounds[0];
        xMax = bounds[1];
        yMin = bounds[2];
        yMax = bounds[3];
        draw();
    }

    @Override
    public void draw() {
        GraphicsContext gc = graph.getGraphicsContext2D();
        double width = getWidth();
        double height = getHeight();
        gc.clearRect(0, 0, width, height);
            gc.setStroke(Color.BLACK);
            gc.strokeLine(width * Math.abs(xMin/(Math.abs(xMin) + Math.abs(xMax))), 0,
                    width * Math.abs(xMin/(Math.abs(xMin) + Math.abs(xMax))), height);
            gc.strokeLine(0, height * Math.abs(yMax / (Math.abs(yMin) + Math.abs(yMax))),
                    width, height * Math.abs(yMax / (Math.abs(yMin) + Math.abs(yMax))));


            gc.setStroke(Color.RED);
            for (double d = xMin; d < xMax; d += 0.1) {
                gc.strokeLine(d * width * 4.0 / (Math.abs(xMax) + Math.abs(xMin)) + width * Math.abs(xMin/(Math.abs(xMin) + Math.abs(xMax))),


                       -(pF.value(d) - coefficientsf[0]) * height * 4.0 / (Math.abs(yMax) + Math.abs(yMin)) -
                               coefficientsf[0] *  height  / (Math.abs(yMax) + Math.abs(yMin)) +
                        height * Math.abs(yMax/(Math.abs(yMin) + Math.abs(yMax))),


                        (d + 0.1) * width * 4.0 / (Math.abs(xMax) + Math.abs(xMin)) + width * Math.abs(xMin/(Math.abs(xMin) + Math.abs(xMax))),


                        -(pF.value(d + 0.1) - coefficientsf[0]) * height * 4.0/ (Math.abs(yMax) + Math.abs(yMin)) -
                                coefficientsf[0] *  height  / (Math.abs(yMax) + Math.abs(yMin))
                                + height * Math.abs(yMax/(Math.abs(yMin) + Math.abs(yMax))));
            }

            gc.setStroke(Color.SKYBLUE);
            for (double d = xMin; d < xMax; d += 0.1) {
                gc.strokeLine(d * width * 4.0 / (Math.abs(xMax) + Math.abs(xMin)) + width * Math.abs(xMin / (Math.abs(xMin) + Math.abs(xMax))),


                        -(pG.value(d) - coefficientsg[0]) * height * 4.0 / (Math.abs(yMax) + Math.abs(yMin)) -
                                coefficientsg[0] * height / (Math.abs(yMax) + Math.abs(yMin)) +
                                height * Math.abs(yMax / (Math.abs(yMin) + Math.abs(yMax))),


                        (d + 0.1) * width * 4.0 / (Math.abs(xMax) + Math.abs(xMin)) + width * Math.abs(xMin / (Math.abs(xMin) + Math.abs(xMax))),


                        -(pG.value(d + 0.1) - coefficientsg[0]) * height * 4.0 / (Math.abs(yMax) + Math.abs(yMin)) -
                                coefficientsg[0] * height / (Math.abs(yMax) + Math.abs(yMin))
                                + height * Math.abs(yMax / (Math.abs(yMin) + Math.abs(yMax))));
            }
        }


    @Override
    public void getFCoefficients(double[] coefficients) {
        this.coefficientsf = coefficients;
        pF = new PolynomialFunction(coefficients);
        draw();
    }

    @Override
    public void getGCoefficients(double[] coefficients) {
        this.coefficientsg = coefficients;
        pG = new PolynomialFunction(coefficients);
        draw();
    }

    @Override
    public double takeIntegral(double a, double b) {
        SimpsonIntegrator simpson = new SimpsonIntegrator();
        PolynomialFunction mult = pF.multiply(pG);

        double sum = 0;
        for(double i = a; i < b; i+=0.000001) {
            sum += Math.abs(simpson.integrate(SimpsonIntegrator.DEFAULT_MAX_ITERATIONS_COUNT,mult,i,i+0.000001));
        }
        return sum;
    }

    @Override
    public PolynomialFunction getF() {
        return pF;
    }

    @Override
    public PolynomialFunction getG() {
        return pG;
    }
}
