package main;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

class Input {
    // Controller objects
    private Controller controller;
    private Component.Identifier xAxis;
    private Component.Identifier yAxis;
    private Component.Identifier rotationAxis;

    // Deadzone value
    static float deadzone = 0.1f;

    Input(String controllerName, String xAxisName, String yAxisName, String rotationAxisName) {
        // All controllers that the program sees
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

        // Select the appropriate controller
        for (Controller controller : controllers) {
            if (controller.getName().equals(controllerName)) this.controller = controller;
        }

        // All of the components of the controller
        Component[] components = controller.getComponents();

        // Get the identifiers of the desired axes
        for (Component component : components) {
            if (component.getName().equals(xAxisName)) xAxis = component.getIdentifier();
            else if (component.getName().equals(yAxisName)) yAxis = component.getIdentifier();
            else if (component.getName().equals(rotationAxisName)) rotationAxis = component.getIdentifier();
        }
    }

    /**
     * Returns the values of the 3 defined axes on the controller.
     *
     * @return A float[] of values between -1 and +1 representing the positions of the axes on the controller.
     */
    float[] getRawValues() {
        controller.poll();

        return new float[]{
                controller.getComponent(xAxis).getPollData(),
                controller.getComponent(yAxis).getPollData(),
                controller.getComponent(rotationAxis).getPollData()
        };
    }

    /**
     * Returns the values of the 3 defined axes on the controller with deadzone clearance.
     *
     * @return A float[] of values between -1 and +1 representing the positions of the axes on the controller,
     * with the deadzone values turned into zero and the range shifted.
     */
    float[] getValues() {
        float[] values = getRawValues();

        // Shift all values using the c + (d - c) / (b - a) (value[i] - a) equation
        for (int i = 0; i < values.length; i++) {
            if (values[i] > deadzone) values[i] = 1 / (1 - deadzone) * (values[i] - deadzone);
            else if (values[i] < -deadzone) values[i] = -1 + 1 / (-deadzone + 1) * (values[i] + 1);
            else values[i] = 0;
        }

        return values;
    }
}
