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

        // Draw the chassis
        rectMode(CENTER);
        strokeWeight(6);
        strokeJoin(ROUND);
        rect(width / 2, height / 2, W, H);

        // Values of the controller
        float[] values = input.getValues();

        float x1 = values[0];
        float y1 = values[1];
        float x2 = values[2];
    }
}
