package Scenes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * @author Alek
 * @version 5/12/15
 *
 * Class to be drawing control panel. Holds RadioButtons.
 */
public class ImageControlScene extends SubScene {

    private TextField[] textBoxes;
    private Text[] texts;
    private GraphArea graphScene;

    public ImageControlScene(GridPane root, int width, int height) {
        super(root, width, height);


        GridPane cameraControls = new GridPane();
        cameraControls.setVgap(15);



        root.setVgap(10);

        GridPane gridControls = new GridPane();

        textBoxes = new TextField[4];

        textBoxes[0] = new TextField("" + -10);
        textBoxes[1] = new TextField("" + 10);
        textBoxes[2] = new TextField("" + -10);
        textBoxes[3] = new TextField("" + 10);



        texts = new Text[4];

        texts[0] = new Text("X min: ");
        texts[1] = new Text("X max: ");
        texts[2] = new Text("Y min: ");
        texts[3] = new Text("Y Max: ");

        for(int i = 0; i < texts.length; i++) {
            textBoxes[i].setMaxWidth(50);
            gridControls.add(texts[i], 0,i);
            gridControls.add(textBoxes[i], 1, i);
        }


        root.add(cameraControls,0,0);
        root.add(gridControls,0,1);

        Button button = new Button("Apply Window Limits");
        root.add(button,0,3);


        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                double[] bounds = {
                        Double.parseDouble(textBoxes[0].getText()),
                        Double.parseDouble(textBoxes[1].getText()),
                        Double.parseDouble(textBoxes[2].getText()),
                        Double.parseDouble(textBoxes[3].getText()),
                };
                graphScene.appplyGraphLimits(bounds);
            }
        });

    }

    public void setControllable(GraphArea c) {
        graphScene = c;
    }




}
