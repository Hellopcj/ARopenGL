package com.baidu.camare.helloopengl.designmethod.bridging_method;

/**
 * Created by puchunjie .
 */

public class Pig extends Soul {
    private int number;

    public Pig(int number, body body) {
        super(body);
        this.number = number;
    }

    @Override
    public void createPig() {
        bridge.turnPeople(number);
    }
}
