package com.baidu.camare.helloopengl.designmethod.factory_method;

/**
 * Created by puchunjie .
 */

public class FactoryDemo {
    public static void main(String[] args){
        Factory factory = new Factory();
        FactoryShape shape = factory.getShape(1);
        shape.draw();
    }
}
