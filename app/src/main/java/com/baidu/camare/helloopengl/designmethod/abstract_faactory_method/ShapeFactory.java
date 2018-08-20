package com.baidu.camare.helloopengl.designmethod.abstract_faactory_method;

/**
 * Created by puchunjie .
 */

public class ShapeFactory extends SuperInteface {
    @Override
    public Color getColor(String color) {
        return null;
    }

    @Override
    public Shape getShape(String shape) {
        Shape shape1 = null;
        switch (shape) {
            case "CIRCLE":
                shape1 = new Circle();
                break;
            case "TRIANGLE":
                shape1 = new Triangle();
                break;
        }
        return shape1;
    }
}
