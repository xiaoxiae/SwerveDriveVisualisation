package main;

import processing.core.*;

import java.text.DecimalFormat;

public class Main extends PApplet {
    // Input object
    private Input input;

    // Variables of the swerve drivebase
    private int W = 0;
    private int H = 0;

    // The coordinates of the motors (calculated from W and H)
    private float[][] motorCoordinates;

    static public void main(String[] passedArgs) {
        PApplet.main(Main.class.getName());
    }

    @Override
    public void settings() {
        size(720, 480);
    }

    @Override
    public void setup() {
        input = new Input("Logitech Extreme 3D", "X Axis", "Y Axis", "Z Rotation");
    }

    @Override
    public void draw() {
        background(255);

        // Update W and H variables if needed
        if (mousePressed && mouseButton == LEFT) {
            W = Math.abs(mouseX - width / 2) * 2;
            H = Math.abs(mouseY - height / 2) * 2;

            // Update the coordinates of the motors
            motorCoordinates = new float[][]{
                    {width / 2 + W / 2, height / 2 - H / 2},
                    {width / 2 - W / 2, height / 2 - H / 2},
                    {width / 2 + W / 2, height / 2 + H / 2},
                    {width / 2 - W / 2, height / 2 + H / 2}
            };
        }

        // Values of the controller
        float[] values = input.getValues();

        float x1 = values[0];
        float y1 = values[1];
        float x2 = values[2];

        // Draw the vector of the direction to which the robot is heading
        if (x1 != 0 || y1 != 0) drawVector(width / 2, height / 2, x1, y1, 50);

        // The speed vectors of the motors
        float[][] speedVectors = swerveCalculation(x1, y1, x2, W, H);

        // Draw all motors and their vectors
        if (motorCoordinates != null) {
            for (int i = 0; i < motorCoordinates.length; i++) {
                drawMotor(motorCoordinates[i][0], motorCoordinates[i][1], (float) Math.atan2(speedVectors[i][0], speedVectors[i][1]));
                drawVector(motorCoordinates[i][0], motorCoordinates[i][1], speedVectors[i][0], speedVectors[i][1], 30);
            }
        }

        // Draw the chassis of the robot
        if (W != 0 && H != 0) drawChassis(W, H, width / 2, height / 2);

        // Draw angle that the robot is trying to turn
        if (x2 != 0) drawRotationDirection(width / 2, height / 2, x2);
    }

    /**
     * Draw a motor at specified coordinates, turned by an angle.
     *
     * @param x1    The x coordinate of the motor.
     * @param y1    The y coordinate of the motor.
     * @param angle The angle that the motor is turned by.
     */
    private void drawMotor(float x1, float y1, float angle) {
        fill(255);
        stroke(0);
        rectMode(CENTER);
        strokeWeight(2.5f);
        strokeJoin(ROUND);

        // Translate and rotate the rectangle
        pushMatrix();
        translate(x1, y1);
        rotate(-angle);
        rect(0, 0, 20, 45);
        popMatrix();
    }

    /**
     * Calculates the speeds and the angles of the swerve chassis depending on the 3 axis inputs.
     * <p>
     * Mainly inspired by the brilliant Ether from Chief Delphi.
     *
     * @param x1 The left/right axis.
     * @param y1 The forward/backward axis.
     * @param x2 The rotation axis.
     * @param W  Width of the chassis.
     * @param H  Height of the chassis.
     * @return A float[][], where each index is a vector of the speed and direction of the motor.
     * @see <a href="https://www.chiefdelphi.com/media/papers/2426">Derivation of Inverse Kinematics for Swerve drive</a>
     */
    private float[][] swerveCalculation(float x1, float y1, float x2, float W, float H) {
        // Normalize the W/H ratio
        float vectorLength = (float)Math.sqrt(W * W + H * H);
        H /= vectorLength;
        W /= vectorLength;

        // Variables to calculate the speeds of the motors
        float[] variables = new float[]{
                x1 - x2 * H,
                x1 + x2 * H,
                y1 - x2 * W,
                y1 + x2 * W
        };

        // Speeds of the motors
        float[] speeds = new float[]{
                (float) Math.sqrt(variables[1] * variables[1] + variables[3] * variables[3]),
                (float) Math.sqrt(variables[1] * variables[1] + variables[2] * variables[2]),
                (float) Math.sqrt(variables[0] * variables[0] + variables[3] * variables[3]),
                (float) Math.sqrt(variables[0] * variables[0] + variables[2] * variables[2])
        };

        // Get the maximum speed of the motors
        float maxMotorSpeed = speeds[0];
        for (float speed : speeds) if (speed > maxMotorSpeed) maxMotorSpeed = speed;

        // If the speed of the fastest motor is bigger than 1, normalize them all to under (or at) 1
        if (maxMotorSpeed > 1) for (int i = 0; i < speeds.length; i++) variables[i] /= maxMotorSpeed;

        // The x and y coordinates
        return new float[][]{
                {variables[1], variables[3]},
                {variables[1], variables[2]},
                {variables[0], variables[3]},
                {variables[0], variables[2]}
        };
    }

    /**
     * Draws a normalized vector from certain coordinates, scaled to pixels with the scale variable.
     *
     * @param x1  The x coordinate of the origin of the vector.
     * @param y1  The y coordinate of the origin of the vector.
     * @param x_v The x component of a normalized vector.
     * @param y_v The y component of a normalized vector.
     */
    private void drawVector(float x1, float y1, float x_v, float y_v, float scale) {
        // If the vector is of length 0
        if (x_v == 0 && y_v == 0) return;

        float x2 = x1 + x_v * scale;
        float y2 = y1 + y_v * scale;

        // Draw the line of the vector
        strokeWeight(2);
        line(x1, y1, x2, y2);

        // The length of the vector
        strokeWeight(1);
        fill(0);
        textAlign(LEFT, CENTER);
        
        //Draw the size of the vector
        text(new DecimalFormat("0.00").format(Math.sqrt(x_v * x_v + y_v * y_v)), x2 + 7, y2);

        // Draw the angle that the vector is in
        double angle = Math.toDegrees(Math.atan2(x_v, y_v) + PI);
        if (angle > 180) angle -= 360;
        text(new DecimalFormat("0.00").format(angle) + "°", x2 + 7, y2 + 13);

        // For drawing the triangle of the arrow and the text
        pushMatrix();
        translate(x2, y2);
        strokeWeight(1.5f);
        rotate(atan2(y_v, x_v));
        triangle(0, 0, -8, 4, -8, -4);
        popMatrix();
    }

    /**
     * Draws the chasis of these parameters
     *
     * @param W        The width of the chassis (in pixels).
     * @param H        The height of the chassis (in pixels).
     * @param center_x The x value of the center of the chassis.
     * @param center_y The y value of the center of the chassis.
     */
    private void drawChassis(int W, int H, int center_x, int center_y) {
        // The big chassis rectangle
        noFill();
        stroke(0);
        rectMode(CENTER);
        strokeWeight(3.5f);
        strokeJoin(ROUND);

        rect(center_x, center_y, W, H);

        // Draw the sizes of W and H
        fill(0);

        // Text for the right line (H)
        textAlign(LEFT, CENTER);
        text("H = " + H, center_x + W / 2 + 7, center_y);

        // Text for the top line (W)
        textAlign(CENTER, BOTTOM);
        text("W = " + W, center_x, center_y - H / 2 - 4.5f);

        strokeWeight(1.5f);
        line(center_x - W / 2, center_y, center_x + W / 2, center_y);
        line(center_x, center_y - H / 2, center_x, center_y + H / 2);
        line(center_x - W / 2, center_y + H / 2, center_x + W / 2, center_y - H / 2);

        textAlign(CENTER, TOP);
        pushMatrix();
        translate(center_x + W / 4, center_y - H / 4);
        rotate((float) -Math.atan2(H, W));
        text("r = " + new DecimalFormat("#.00").format(Math.sqrt(W * W + H * H)), 0, 0);
        popMatrix();
    }

    /**
     * Draws a visualisation of the direction the robot is trying to turn at specified coordinates.
     *
     * @param x     The x coordinate of the center of the arc.
     * @param y     The y coordinate of the center of the arc.
     * @param angle The size of the angle (from -1 to 1 - -PI to +PI).
     */
    private void drawRotationDirection(float x, float y, float angle) {
        stroke(0);
        strokeWeight(2f);

        float start = -HALF_PI;
        float end = -HALF_PI + angle * PI;

        if (start > end) {
            float temp = start;
            start = end;
            end = temp;
        }

        // For drawing the triangle of the arrow and the text
        pushMatrix();
        translate(x + cos(angle * PI - PI / 2) * 25, y + sin(angle * PI - PI / 2) * 25);

        textAlign(LEFT, CENTER);
        text(new DecimalFormat("0.00").format(angle), 7, 0);

        if (angle > 0) rotate(angle * PI);
        else rotate(angle * PI - PI);

        strokeWeight(1.5f);
        fill(0);
        triangle(0, 0, -8, 4, -8, -4);

        popMatrix();

        noFill();
        arc(x, y, 50, 50, start, end);
    }
}
