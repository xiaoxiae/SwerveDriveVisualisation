package main;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import processing.core.*;

public class Main extends PApplet {

    // Controller and component name constants
    final String controllerName = "Logitech Extreme 3D";
    final String xAxisName = "X Axis";
    final String yAxisName = "Y Axis";
    final String rotationAxisName = "Z Rotation";

    // Controller objects
    Controller controller;
    Component.Identifier xAxis;
    Component.Identifier yAxis;
    Component.Identifier rotationAxis;

    static public void main(String[] passedArgs) {
        PApplet.main(Main.class.getName());
    }

    @Override
    public void settings() {
    }

    @Override
    public void setup() {
        // All controllers that the program sees
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

        // Select the Logitech Extreme 3D
        for (Controller controller : controllers) {
            if (controller.getName().equals(controllerName)) this.controller = controller;
        }

        // All of the components of the controller
        Component[] components = controller.getComponents();

        // Get the identifiers of the desired axes
        for (Component component : components) {
            switch (component.getName()) {
                case xAxisName:
                    xAxis = component.getIdentifier();
                    break;
                case yAxisName:
                    yAxis = component.getIdentifier();
                    break;
                case rotationAxisName:
                    rotationAxis = component.getIdentifier();
                    break;
            }
        }
    }

    @Override
    public void draw() {
        // Get fresh values
        controller.poll();

        // Values of the axes of the controller
        float x1 = controller.getComponent(xAxis).getPollData();
        float y1 = controller.getComponent(yAxis).getPollData();
        float x2 = controller.getComponent(rotationAxis).getPollData();

        // Temp for debug
        System.out.println(x1 + " " + y1 + " " + x2);
    }
}
