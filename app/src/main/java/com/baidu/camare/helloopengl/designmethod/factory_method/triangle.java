package com.baidu.camare.helloopengl.designmethod.factory_method;

/**
 * Created by puchunjie .
 */

public class triangle implements FactoryShape {
    @Override
    public void draw() {
        System.out.println("this is triangle");
    }
}
