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
    private static float deadzone = 0.1f;

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

        // To prevent NaN in the results
        if (values[0] != 0 && values[1] != 0) {
            // A constant to scale the pair of coordinates to their maximum length (one or both will be one)
            float maximumCoordDistanceConstant = 1 / (Math.max(Math.abs(values[0]), Math.abs(values[1])));

            // Scale the newX and newY to their maximum length within the -1 and 1 range
            float newX = values[0] * maximumCoordDistanceConstant;
            float newY = values[1] * maximumCoordDistanceConstant;

            // The proportion by which to multiply the original coordinates to scale them back
            float lengthProportion = 1 / (float) Math.sqrt(newX * newX + newY * newY);

            // Scale x1 and y1 to fit into the circle
            values[0] *= lengthProportion;
            values[1] *= lengthProportion;
        }

        return values;
    }
}
