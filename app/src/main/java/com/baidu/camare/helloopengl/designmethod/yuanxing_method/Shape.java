package com.baidu.camare.helloopengl.designmethod.yuanxing_method;

/**
 * implememts cloneable  or serialble
 * Created by puchunjie .
 */

public abstract class Shape implements Cloneable {
    private String id;
    protected String type;

    abstract void onDraw();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clone;
    }
}
