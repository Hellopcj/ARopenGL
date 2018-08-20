package com.baidu.camare.helloopengl.designmethod.factory_method;

/**
 * Created by puchunjie .
 */

public class Factory {
    public  FactoryShape getShape(int type) {
        if (type == 1) {
            return new triangle();
        } else if (type == 2) {
            return new Tube();
        }
        return null;
    }
}
