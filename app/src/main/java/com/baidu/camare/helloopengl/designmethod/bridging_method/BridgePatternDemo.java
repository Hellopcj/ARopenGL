package com.baidu.camare.helloopengl.designmethod.bridging_method;

/**
 * Created by puchunjie .
 */

public class BridgePatternDemo {
    public static void main(String[] args) {
        Soul soul = new Pig(1, new GoodPig());
        Soul soul1 = new Pig(2, new BadPig());
        soul.createPig();
        soul1.createPig();
    }
}
