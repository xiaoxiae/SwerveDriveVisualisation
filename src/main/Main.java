package main;

import processing.core.*;

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
        drawChassis(W, H);

        // Values of the controller
        float[] values = input.getValues();

        float x1 = values[0];
        float y1 = values[1];
        float x2 = values[2];
    }

    /**
     * Draws the chasis of these parameters
     * @param W The width of the chassis (in pixels).
     * @param H The height of the chassis (in pixels).
     */
    void drawChassis(int W, int H) {
        // The big chassis rectangle
        fill(255);
        rectMode(CENTER);
        strokeWeight(3.5f);
        strokeJoin(ROUND);

        rect(width / 2, height / 2, W, H);

        // Draw the sizes of W and H
        fill(0);

        // Text for the right line (H)
        textAlign(LEFT, CENTER);
        text("H = " + H, width / 2 + W / 2 + 10, height / 2);

        // Text for the top line (W)
        textAlign(CENTER, BOTTOM);
        text("W = " + W, width / 2, height / 2 - H / 2 - 10);
    }
}
