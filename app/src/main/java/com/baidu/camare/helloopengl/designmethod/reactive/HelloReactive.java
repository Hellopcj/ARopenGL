package com.baidu.camare.helloopengl.designmethod.reactive;import java.util.HashMap;import java.util.List;/** *  反射 */public  class HelloReactive {    private String var1 = "Hello world";    private int var2 = 40;    private String var3;    public int b = 100;    private HashMap ha = new HashMap();    public void sendMessage() {    }    public int messageNumber(int var2){        this.var2 = var2;        return var2;    }    public HashMap hehe(){        return ha;    }    public void setMessage(String constant){        this.var1 = constant;    }    public String getMassage() {        return var1;    }    private  int add (int a,int b ) {        return a + b;    }    private void sayHello(){     System.out.println("hello this is HelloReactive.class say!");    }    public  HelloReactive(){    }    public HelloReactive(int a,int b){    }    public HelloReactive(int var2){    }    public void test(String[] pramas, List<String> b, HashMap<Integer,Hello> maps) {    }    public void testException () throws IllegalAccessException {        throw new IllegalAccessException("You have some problem.");    }    public static void testStatic () {        System.out.println("this is  static method");    }    public static class Hello{        String name = " this is inner class";        public String getName(){            return name;        }    }    @Override    public String toString() {        return super.toString();    }}