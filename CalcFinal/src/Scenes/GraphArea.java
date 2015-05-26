package Scenes;

import com.sun.corba.se.impl.io.TypeMismatchException;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

/**
 * @author Alek
 * @version 5/12/15
 */
public interface GraphArea {

    void appplyGraphLimits(double[] bounds);

    void draw();
}
