package main;

import processing.core.*;

public class Main extends PApplet {
    // Our input object
    Input input;

    static public void main(String[] passedArgs) {
        PApplet.main(Main.class.getName());
    }

    @Override
    public void settings() {
    }

    @Override
    public void setup() {
        input = new Input("Logitech Extreme 3D", "X Axis", "Y Axis", "Z Rotation");
    }

    @Override
    public void draw() {
        // Values of the controller
        float[] values = input.getValues();

        float x1 = values[0];
        float y1 = values[1];
        float x2 = values[2];

        // Temp for debug
        System.out.println(x1 + " " + y1 + " " + x2);
    }
}
