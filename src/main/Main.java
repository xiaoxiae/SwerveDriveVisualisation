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
