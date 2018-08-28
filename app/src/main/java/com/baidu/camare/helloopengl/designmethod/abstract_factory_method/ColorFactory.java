package com.baidu.camare.helloopengl.designmethod.abstract_factory_method;

/**
 * Created by puchunjie .
 */

public class ColorFactory extends SuperInteface {
    @Override
    public Color getColor(String color) {
        if (color.equals("RED")) {
            return new Red();
        } else if (color.equals("BLUE")) {
            return new Blue();
        }
        return null;
    }

    @Override
    public Shape getShape(String shape) {
        return null;
    }
}
