package main;

import processing.core.*;

import java.text.DecimalFormat;

public class Main extends PApplet {
    // Input object
    Input input;

    // Variables of the swerve drivebase
    int W = 0;
    int H = 0;

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
        }

        // Draw the chassis of the robot
        drawChassis(W, H, width / 2, height / 2);

        // Values of the controller
        float[] values = input.getValues();

        float x1 = values[0];
        float y1 = values[1];
        float x2 = values[2];

        // Draw the vector of the direction to which the robot is heading
        if (x1 != 0 || y1 != 0) drawVector(width / 2, height / 2, x1, y1, 50);
    }

    /**
     * Draws a normalized vector from certain coordinates, scaled to pixels with the scale variable.
     *
     * @param x_origin The x coordinate of the origin of the vector.
     * @param y_origin The y coordinate of the origin of the vector.
     * @param x_vector The x component of a normalized vector.
     * @param y_vector The y component of a normalized vector.
     */
    void drawVector(float x_origin, float y_origin, float x_vector, float y_vector, float scale) {
        strokeWeight(2);
        line(x_origin, y_origin, x_origin + x_vector * scale, y_origin + y_vector * scale);

        pushMatrix();
        translate(x_origin + x_vector * scale, y_origin + y_vector * scale);
        rotate(atan2(y_vector, x_vector));
        triangle(0, 0, -8, 4, -8, -4);
        popMatrix();
    }

    /**
     * Draws the chasis of these parameters
     *
     * @param W The width of the chassis (in pixels).
     * @param H The height of the chassis (in pixels).
     * @param center_x The x value of the center of the chassis.
     * @param center_y The y value of the center of the chassis.
     */
    void drawChassis(int W, int H, int center_x, int center_y) {
        // The big chassis rectangle
        fill(255);
        stroke(0);
        rectMode(CENTER);
        strokeWeight(3.5f);
        strokeJoin(ROUND);

        rect(center_x, center_y, W, H);

        // Draw the sizes of W and H
        fill(0);

        // Text for the right line (H)
        textAlign(LEFT, CENTER);
        text("H = " + H, center_x + W / 2 + 10, center_y);

        // Text for the top line (W)
        textAlign(CENTER, BOTTOM);
        text("W = " + W, center_x, center_y - H / 2 - 10);

        strokeWeight(1.5f);
        line(center_x - W / 2, center_y, center_x + W / 2, center_y);
        line(center_x, center_y - H / 2, center_x, center_y + H / 2);
        line(center_x - W/2, center_y + H/2, center_x + W/2, center_y - H/2);

        textAlign(CENTER, TOP);
        pushMatrix();
        translate(center_x + W/4, center_y - H/4);
        rotate((float)-Math.atan2(H, W));
        text("r = " + new DecimalFormat("#.00").format(Math.sqrt(W * W + H * H)), 0, 0);
        popMatrix();
    }
}
