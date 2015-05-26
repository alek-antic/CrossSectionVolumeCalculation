package Scenes;

import Shapes.SquareVolumeMesh;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.stage.Stage;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;


/**
 * @author Alek
 * @version 5/12/15
 */
public class VolumeCalc extends Application {

    private final Group root = new Group();
    private final Xform world = new Xform();
    private final Xform meshGroup = new Xform();
    private final Xform cameraXform = new Xform();
    private final Xform cameraXform2 = new Xform();
    private final Xform cameraXform3 = new Xform();
    private final Xform axisGroup = new Xform();

    private final Camera camera = new PerspectiveCamera(true);


    private double axisLength = 5.55;
    private double axisWidth = 0.01;

    private static final double CONTROL_MULTIPLIER = 0.1;
    private static final double SHIFT_MULTIPLIER = 10.0;
    private static final double MOUSE_SPEED = 0.1;
    private static final double ROTATION_SPEED = 2.0;
    private static final double TRACK_SPEED = 0.3;

    private PolynomialFunction pF;
    private PolynomialFunction pG;
    private double start;
    private double end;


    @Override
    public void start(Stage primaryScene) {
        primaryScene.setTitle("Volume Due to a Given Cross Section for Polynomials");

        pF = new PolynomialFunction(new double[]{0, 0, 1});
        pG = new PolynomialFunction(new double[]{0, 0, 0, 1});
        start = -1;
        end = 1;
        buildCamera();
        buildAxes();
        buildMesh();


        GridPane grid = new GridPane();
        StackPane pane = new StackPane();
        GridPane grid2 = new GridPane();

        GraphScene gS = new GraphScene(pane, 400, 400);
        ImageControlScene iCS = new ImageControlScene(grid, 200, 475);
        FunctionControlScene fCS = new FunctionControlScene(grid2, 1000, 200);
        SubScene vS = new SubScene(world, 400, 400, true, SceneAntialiasing.DISABLED);
        vS.setFill(Color.GREY);
        iCS.setControllable(gS);
        fCS.setGraphScene(gS);

        Button rebuildMesh = new Button("Rebuild Volume");
        rebuildMesh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pF = gS.getF();
                pG = gS.getG();

                start = fCS.getStart();
                end = fCS.getEnd();
                buildMesh();
            }
        });

        VBox volumeBox = new VBox(5, vS, rebuildMesh);

        handleMouse(vS, world);

        vS.setCamera(camera);

        HBox hBox = new HBox(10, gS, iCS, volumeBox);

        VBox vBox = new VBox(5, hBox, fCS);

        BorderPane bp = new BorderPane();

        bp.setCenter(hBox);
        bp.setBottom(fCS);

        Scene scene = new Scene(bp, 1008, 637);

        vS.heightProperty().bind(volumeBox.heightProperty());
        vS.widthProperty().bind(volumeBox.heightProperty());

        gS.heightProperty().bind(hBox.heightProperty());
        gS.widthProperty().bind(hBox.heightProperty());

        primaryScene.setScene(scene);
        primaryScene.show();
    }

    private void buildCamera() {
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(camera);
        cameraXform3.setRotateZ(180.0);

        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-10);
        cameraXform.ry.setAngle(320);
        cameraXform.rx.setAngle(45);
    }

    private void buildAxes() {
        world.getChildren().remove(axisGroup);

        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial greenMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKGREEN);
        redMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKBLUE);
        redMaterial.setSpecularColor(Color.BLUE);

        final Box xAxis = new Box(axisLength, axisWidth, axisWidth);
        final Box yAxis = new Box(axisWidth, axisLength, axisWidth);
        final Box zAxis = new Box(axisWidth, axisWidth, axisLength);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        if(axisGroup.getChildren().size() >=1)
            axisGroup.getChildren().remove(axisGroup.getChildren().size()-1);
        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        axisGroup.setVisible(true);
        world.getChildren().addAll(axisGroup);
    }

    private void buildMesh() {

        world.getChildren().remove(meshGroup);

        Xform meshXform = new Xform();

        final PhongMaterial magentaMaterial = new PhongMaterial();
        magentaMaterial.setDiffuseColor(Color.DARKMAGENTA);
        magentaMaterial.setSpecularColor(Color.MAGENTA);

        SquareVolumeMesh m = new SquareVolumeMesh(start, end, 1000, pG, pF);
        m.setMaterial(magentaMaterial);

        m.setCullFace(CullFace.BACK);

        meshXform.getChildren().addAll(m);

        if(meshGroup.getChildren().size() >=1)
            meshGroup.getChildren().remove(meshGroup.getChildren().size()-1);
        meshGroup.getChildren().addAll(meshXform);

        meshGroup.setVisible(true);

        world.getChildren().addAll(meshGroup);

    }


    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;


    private void handleMouse(SubScene scene, final Node root) {

        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseOldX = me.getSceneX();
                mouseOldY = me.getSceneY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseDeltaX = (mousePosX - mouseOldX);
                mouseDeltaY = (mousePosY - mouseOldY);
                int modifierFactor = 1;

                double modifier = 1.0;

                if (me.isControlDown()) {
                    modifier = CONTROL_MULTIPLIER;
                }
                if (me.isShiftDown()) {
                    modifier = SHIFT_MULTIPLIER;
                }
                if (me.isPrimaryButtonDown()) {
                    cameraXform.ry.setAngle(cameraXform.ry.getAngle() -
                            mouseDeltaX * modifierFactor * modifier * ROTATION_SPEED);  //
                    cameraXform.rx.setAngle(cameraXform.rx.getAngle() +
                            mouseDeltaY * modifierFactor * modifier * ROTATION_SPEED);  // -
                } else if (me.isSecondaryButtonDown()) {
                    double z = camera.getTranslateZ();
                    double newZ = z + mouseDeltaX * MOUSE_SPEED * modifier;
                    camera.setTranslateZ(newZ);
                } else if (me.isMiddleButtonDown()) {
                    cameraXform2.t.setX(cameraXform2.t.getX() +
                            mouseDeltaX * MOUSE_SPEED * modifier * TRACK_SPEED);  // -
                    cameraXform2.t.setY(cameraXform2.t.getY() +
                            mouseDeltaY * MOUSE_SPEED * modifier * TRACK_SPEED);  // -
                }
            }
        }); // setOnMouseDragged


        scene.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                camera.setTranslateZ(camera.getTranslateZ() + 0.05 * event.getDeltaY());
                if(event.getDeltaY() > 0)
                    axisLength -= 0.0005;
                    axisWidth -= 0.0001;
                buildAxes();
            }
        });

    } //handleMouse


    public static void main(String[] args) {
        launch(args);
    }

}
