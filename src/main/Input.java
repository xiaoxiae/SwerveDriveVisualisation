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

        // Maps the x and y inputs from a square to a circle and clears deadzone using scaled radial deadzone
        float[] xyInputs = scaledRadialDeadzone(squareToCircle(new float[]{values[0], values[1]}));

        // Scale the rotation axis by the deadzone
        float zInput = scaleAxis(values[2]);

        return new float[]{xyInputs[0], xyInputs[1], zInput};
    }

    /**
     * Map the coordinate pair form a square to a circle.
     *
     * @param coords The coordinates of the point from the square.
     * @return A float[2], where arr[0] is the x coordinate and arr[1] is the y coordinate mapped from square to a
     * circle.
     * @see <a href="http://mathproofs.blogspot.com/2005/07/mapping-square-to-circle.html">Mapping square to circle</a>
     */
    private float[] squareToCircle(float[] coords) {
        float mappedX = coords[0] * (float) Math.sqrt(1 - (coords[1] * coords[1]) / 2);
        float mappedY = coords[1] * (float) Math.sqrt(1 - (coords[0] * coords[0]) / 2);

        return new float[]{mappedX, mappedY};
    }

    /**
     * Scales the rotation axis by the dead zone.
     *
     * @param rotationAxis The value of the axis.
     * @return The scaled value of the axis.
     */
    private float scaleAxis(float rotationAxis) {
        if (Math.abs(rotationAxis) < deadzone) return 0;
        else return rotationAxis * (Math.abs(rotationAxis) - deadzone) / (1 - deadzone);
    }

    /**
     * Adjusts 2 axis inputs to include a deadzone.
     *
     * @param inputs The inputs to remove the dead zones from.
     * @return The sanitized inputs.
     * @see <a href="http://www.third-helix.com/2013/04/12/doing-thumbstick-dead-zones-right.html">Doing thumbstick dead zones right</a>
     */
    private float[] scaledRadialDeadzone(float[] inputs) {
        // The magnitude of the x/y vector
        float magnitude = (float) Math.sqrt(inputs[0] * inputs[0] + inputs[1] * inputs[1]);

        // Scale the x and y axis vectors
        if (magnitude < deadzone) {
            inputs[0] = 0;
            inputs[1] = 0;
        } else {
            inputs[0] *= (magnitude - deadzone) / (1 - deadzone);
            inputs[1] *= (magnitude - deadzone) / (1 - deadzone);
        }

        return inputs;
    }
}
