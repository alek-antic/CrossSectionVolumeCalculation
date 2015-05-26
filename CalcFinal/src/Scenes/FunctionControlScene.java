package Scenes;

import com.sun.corba.se.impl.orb.PropertyOnlyDataCollector;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;


/**
 * @author Alek
 * @version 5/17/2015
 */
public class FunctionControlScene extends SubScene {


    private TextField[] coefficients;
    private Text[] labels;
    private TextArea funcOut;
    private TextField[] limits;
    private Text[] limLabels;
    private FunctionListener graphScene , volumeScene;



    public FunctionControlScene(GridPane root, int width, int height){
        super(root, width, height);


        GridPane functionInput = new GridPane();
        functionInput.setHgap(5);

        coefficients = new TextField[12];
        labels = new Text[14];



        for(int i = 0; i < coefficients.length; i++) {
            coefficients[i] = new TextField();
        }

        labels[0] = new Text("f(x): ");
        for(int i =1; i < labels.length / 2 - 1; i++){
            labels[i] = new Text("x^" + (i-1) + " +");
            labels[i+7] = new Text("x^" + (i-1) + " +");

        }
        labels[6] = new Text("x^5");
        labels[13] = new Text("x^5");
        labels[7] = new Text("g(x): ");


        for(int i = 0; i < labels.length/2; i++) {
            functionInput.add(labels[i], 2*i, 0);
        }



        for(int i = 7; i < labels.length; i++) {
            functionInput.add(labels[i], 2*(i-7), 1);
        }



        for(int i = 0; i < coefficients.length/2; i++) {
            coefficients[i].setMaxWidth(75);
            coefficients[i].setText("0");
            functionInput.add(coefficients[i], 2 * i + 1,0);
        }
        for(int i = 6; i < coefficients.length; i++) {
            coefficients[i].setMaxWidth(75);
            coefficients[i].setText("0");
            functionInput.add(coefficients[i], 2*(i-6) + 1, 1);
        }

        coefficients[2].setText("1");
        coefficients[9].setText("1");

        Button button = new Button("Apply Function Changes");
        button.setMaxWidth(250);

        VBox box = new VBox(functionInput, button);

        GridPane limitsAndOutput = new GridPane();
        limitsAndOutput.setHgap(25);
        funcOut = new TextArea();
        funcOut.setEditable(false);
        funcOut.setMaxHeight(75);
        funcOut.setMaxWidth(500);

        GridPane lims = new GridPane();
        lims.setVgap(10);
        lims.setHgap(10);

        Button integrate = new Button("Take Integral");
        integrate.setMinWidth(100);

        limits = new TextField[2];
        limits[0] = new TextField();
        limits[1] = new TextField();
        limLabels = new Text[2];
        limLabels[0] = new Text("a");
        limLabels[1] = new Text("b");

        for(int i = 0; i < limits.length; i++) {
            limits[i].setMaxWidth(50);
            lims.add(limLabels[i], 0, i);
            lims.add(limits[i], 1, i);
        }

        limits[0].setText("-1");
        limits[1].setText("1");

        lims.add(integrate, 2, 1);

        limitsAndOutput.add(funcOut, 0, 0);
        limitsAndOutput.add(lims, 1, 0);

        root.add(limitsAndOutput,0,0);
        root.add(box, 0, 1);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                graphScene.getFCoefficients(getFCoefficients());
                graphScene.getGCoefficients(getGCoefficients());
            }
        });

        integrate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                graphScene.getFCoefficients(getFCoefficients());
                funcOut.setFont(new Font(24));
                funcOut.setText("\u222B" + "(" + graphScene.getF().toString() + ")*(" + graphScene.getG().toString() + ")dx \u2248 " +
                        (int)(1000 * graphScene.takeIntegral(Double.parseDouble(limits[0].getText()),
                        Double.parseDouble(limits[1].getText()))) / 1000.0);
            }
        });
    }


    public void setGraphScene(FunctionListener l) {
        graphScene = l;
    }

    public void setVolumeScene(FunctionListener l) {
        volumeScene = l;
    }


    private double[] getFCoefficients() {
        double [] coeffs = {
                Double.parseDouble(coefficients[0].getText()),
                Double.parseDouble(coefficients[1].getText()),
                Double.parseDouble(coefficients[2].getText()),
                Double.parseDouble(coefficients[3].getText()),
                Double.parseDouble(coefficients[4].getText()),
                Double.parseDouble(coefficients[5].getText())
        };

        return coeffs;
    }

    private double[] getGCoefficients() {
        double[] coeffs =  {
                Double.parseDouble(coefficients[6].getText()),
                Double.parseDouble(coefficients[7].getText()),
                Double.parseDouble(coefficients[8].getText()),
                Double.parseDouble(coefficients[9].getText()),
                Double.parseDouble(coefficients[10].getText()),
                Double.parseDouble(coefficients[11].getText())
        };
        return coeffs;
    }

    public double getStart() {
        return Double.parseDouble(limits[0].getText());
    }

    public double getEnd() {
        return Double.parseDouble(limits[1].getText());
    }

}
