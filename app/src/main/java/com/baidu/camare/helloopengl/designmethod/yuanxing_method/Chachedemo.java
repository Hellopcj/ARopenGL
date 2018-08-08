package com.baidu.camare.helloopengl.designmethod.yuanxing_method;

/**
 * 原型模式 demo
 * Created by puchunjie .
 */

public class Chachedemo {
    public static void main(String[] args) throws CloneNotSupportedException {
    //  入口
        ShapeChache.loadChache();
        Shape shape1 = ShapeChache.getShape("1");
        System.out.println("this is "+shape1.getType() );


        Shape shape2 = ShapeChache.getShape("2");
        System.out.println("this is "+ shape2.getType());
    }
}
