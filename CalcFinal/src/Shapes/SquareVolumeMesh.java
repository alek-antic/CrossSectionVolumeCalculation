package Shapes;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;


/**
 *
 * @author Alek
 * @version 5/18/15
 */

public class SquareVolumeMesh extends MeshView {

    private final static double DEFAULT_START = 0 ;
    private final static double DEFAULT_END = 1 ;
    private final static int DEFAULT_NUMSTEPS = 1000;
    private final static PolynomialFunction DEFAULT_F = new PolynomialFunction(new double[] {
            0,0,1
    });
    private final static PolynomialFunction DEFAULT_G = new PolynomialFunction(new double[] {
            0,0,1
    });


    private int points = 0;


    public SquareVolumeMesh(){
        this(DEFAULT_START, DEFAULT_END, DEFAULT_NUMSTEPS, DEFAULT_F, DEFAULT_G);
    }

    public SquareVolumeMesh(double start, double end, int numSteps, PolynomialFunction base, PolynomialFunction height){
        setStart(start);
        setEnd(end);
        setNumSteps(numSteps);
        setF(base);
        setG(height);
    }


    private TriangleMesh createVolume(double start, double end, double numSteps, PolynomialFunction base, PolynomialFunction height) {
        points = 0;
        TriangleMesh m = new TriangleMesh();

        double stepSize = (end-start)/numSteps;

        for(double i = start; i < end; i += stepSize) {

            float x = (float) i;
            float x2 = (float) (x+stepSize);
            float gx = (float) height.value(x);
            float gx2 = (float) height.value(x2);
            float fx = (float) base.value(x);
            float fx2 = (float) base.value(x2);


            TriangleMesh t = createVolumeSection(x,x2,gx,gx2,fx,fx2);
            m.getPoints().addAll(t.getPoints());
            points = m.getPoints().size()/3;
            m.getTexCoords().addAll(t.getTexCoords());
            m.getFaces().addAll(t.getFaces());

        }



        return m;
    }


    private TriangleMesh createVolumeSection(double xVal, double x2Val, double gxVal, double gx2Val,
                                             double fxVal, double fx2Val){
        TriangleMesh m = new TriangleMesh();

        float x = ((float)xVal) ;
        float x2 = ((float)x2Val);
        float gx = ((float)gxVal);
        float gx2 = ((float)gx2Val);
        float fx = ((float)fxVal);
        float fx2 = ((float)fx2Val);

        //create Points
        m.getPoints().addAll(
                x,  0,  0,      // A = 0
                x,  0,  gx,	    // B = 1
                x2, 0,  0,	    // C = 2
                x2, 0,  gx2,	// D = 3
                x,  fx, 0,	    // E = 4
                x,  fx, gx,	    // F = 5
                x2, fx2,0,	    // G = 6
                x2, fx2,gx2	    // H = 7
        );

        m.getTexCoords().addAll(0,0);

        m.getFaces().addAll(
                points + 0 , 0 , points + 1 , 0 , points+ 3 , 0 ,		// A-B-D
                points + 0 , 0 , points + 3 , 0 , points+ 2 , 0 ,   	// A-D-C
                points + 0 , 0 , points + 2 , 0 , points+ 6 , 0 ,		// A-C-G
                points + 0 , 0 , points + 6 , 0 , points+ 4 , 0 ,  	    // A-G-E
                points + 0 , 0 , points + 4 , 0 , points+ 1 , 0 ,		// A-E-B
                points + 1 , 0 , points + 4 , 0 , points+ 5 , 0 , 	    // B-E-F
                points + 1 , 0 , points + 5 , 0 , points+ 7 , 0 ,		// B-F-H
                points + 1 , 0 , points + 7 , 0 , points+ 3 , 0 ,		// B-H-D
                points + 3 , 0 , points + 7 , 0 , points+ 6 , 0 ,		// D-H-G
                points + 3 , 0 , points + 6 , 0 , points+ 2 , 0 ,		// D-G-C
                points + 6 , 0 , points + 7 , 0 , points+ 5 , 0 ,		// G-H-F
                points + 6 , 0 , points + 5 , 0 , points+ 4 , 0	    	// G-F-E
        );



        return m ;
    }


    private final DoubleProperty start = new SimpleDoubleProperty(DEFAULT_START){

        @Override
        protected void invalidated() {
            setMesh(createVolume(getStart(),getEnd(),getNumSteps(),getF(),getG()));
        }

    };

    public final double getStart() {
        return start.get();
    }

    public final void setStart(double value) {
        start.set(value);
    }

    public DoubleProperty startProperty() {
        return start;
    }

    private final DoubleProperty end = new SimpleDoubleProperty(DEFAULT_END){

        @Override
        protected void invalidated() {
            setMesh(createVolume(getStart(),getEnd(),getNumSteps(),getF(),getG()));
        }

    };

    public final double getEnd() {
        return end.get();
    }

    public final void setEnd(double value) {
        end.set(value);
    }

    public DoubleProperty endProperty() {
        return end;
    }


    private final DoubleProperty numSteps = new SimpleDoubleProperty(DEFAULT_NUMSTEPS){

        @Override
        protected void invalidated() {
            setMesh(createVolume(getStart(),getEnd(),getNumSteps(),getF(),getG()));
        }

    };

    public final double getNumSteps() {
        return numSteps.get();
    }

    public final void setNumSteps(double value) {
        numSteps.set(value);
    }

    public DoubleProperty numStepsProperty() {
        return numSteps;
    }


    private PolynomialFunction f = DEFAULT_F;

    public final void setF(PolynomialFunction f) {
        this.f = f;
        setMesh(createVolume(getStart(),getEnd(),getNumSteps(), getF(), getG()));
    }

    public final PolynomialFunction getF() {
        return f;
    }

    private PolynomialFunction g = DEFAULT_G;

    public final void setG(PolynomialFunction g) {
        this.g = g;
        setMesh(createVolume(getStart(),getEnd(),getNumSteps(),getF(),getG()));
    }

    public final PolynomialFunction getG() {
        return g;
    }


}