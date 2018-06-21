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
    private static float deadzone = 0.06f;

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

        // Map the square input to a circle input
        // (see http://mathproofs.blogspot.com/2005/07/mapping-square-to-circle.html)
        if (values[0] != 0 && values[1] != 0) {
            float mappedX = values[0] * (float) Math.sqrt(1 - 0.5 * values[1] * values[1]);
            float mappedY = values[1] * (float) Math.sqrt(1 - 0.5 * values[0] * values[0]);

            values[0] = mappedX;
            values[1] = mappedY;
        }

        // The magnitude of the x/y vector
        float magnitude = (float)Math.sqrt(values[0] * values[0] + values[1] * values[1]);

        // Scale the x and y axis vectors
        if (magnitude < deadzone) {
            values[0] = 0;
            values[1] = 0;
        } else {
            values[0] *= (magnitude - deadzone) / (1 - deadzone);
            values[1] *= (magnitude - deadzone) / (1 - deadzone);
        }

        // Scale the rotation axis
        if (Math.abs(values[2]) < deadzone) values[2] = 0;
        else values[2] *= (Math.abs(values[2]) - deadzone) / (1 - deadzone);

        return values;
    }
}
