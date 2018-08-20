package com.baidu.camare.helloopengl.designmethod.bridging_method;

/**
 * 猪八戒的灵魂
 * Created by puchunjie .
 */

public abstract class Soul {
    protected body bridge;

    public Soul(body body) {
        this.bridge = body;
    }

    public abstract void createPig();
}
