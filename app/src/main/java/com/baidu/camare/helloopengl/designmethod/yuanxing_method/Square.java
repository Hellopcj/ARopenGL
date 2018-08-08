package com.baidu.camare.helloopengl.designmethod.yuanxing_method;

/**
 * Created by puchunjie .
 */

public class Square extends Shape {
    public Square(){
        type = "Square";
    }
    @Override
    void onDraw() {
        System.out.println("Inside Square::draw() method.");
    }
}
