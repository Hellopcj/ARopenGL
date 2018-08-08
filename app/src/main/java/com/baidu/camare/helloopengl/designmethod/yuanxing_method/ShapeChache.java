package com.baidu.camare.helloopengl.designmethod.yuanxing_method;

import java.util.Hashtable;

/**
 * 对象缓存
 * Created by puchunjie .
 */

public class ShapeChache {

    private static Hashtable<String, Shape> hashtable
            = new Hashtable<>();

    public static Shape getShape(String id) throws CloneNotSupportedException {
        Shape chacheShape = hashtable.get(id);
        return (Shape) chacheShape.clone();
    }

    // 对每种形状都运行数据库查询，并创建该形状
    // shapeMap.put(shapeKey, shape);
    // 例如，我们要添加三种形状
    //  给出每个shape的构造方式
    public static void loadChache() {
        Square square = new Square();
        square.setId("1");
        hashtable.put(square.getId(), square);

        Tube tube = new Tube();
        tube.setId("2");
        hashtable.put(tube.getId(), tube);
    }
}
