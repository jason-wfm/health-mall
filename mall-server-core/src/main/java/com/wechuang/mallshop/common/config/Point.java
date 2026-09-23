package com.wechuang.mallshop.common.config;


public class Point {
    private double x;
    private double y;

    // 无参构造函数
    public Point() {
    }

    // 带参数的构造函数
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // getter 和 setter 方法
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
