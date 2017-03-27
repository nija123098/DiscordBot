package com.github.kaaz.emily.util;

/**
 * Used for when triples are not enough.
 *
 * @author nija123098
 */
public class Quadruple<A, B, C, D> {
    public A a;
    public B b;
    public C c;
    public D d;
    public Quadruple(A a, B b, C c, D d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
    public A getA() {
        return this.a;
    }
    public void setA(A a) {
        this.a = a;
    }
    public B getB() {
        return this.b;
    }
    public void setB(B b) {
        this.b = b;
    }
    public C getC() {
        return this.c;
    }
    public void setC(C c) {
        this.c = c;
    }
    public D getD() {
        return this.d;
    }
    public void setD(D d) {
        this.d = d;
    }
}
