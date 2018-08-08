package com.baidu.camare.helloopengl.designmethod.yuanxing_method;

/**
 * Created by puchunjie .
 */

public class Tube extends Shape {

    public Tube() {
        type = "Tube";
    }

    @Override
    void onDraw() {
        System.out.println("Inside Tube::draw() method.");
    }
}
