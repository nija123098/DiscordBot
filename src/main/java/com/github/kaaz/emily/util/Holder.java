package com.github.kaaz.emily.util;

/**
 * Made by nija123098 on 2/24/2017.
 */
public class Holder<E> {
    private E e;
    public Holder(E value) {
        this.e = value;
    }
    public Holder() {
        this(null);
    }
    public E get(){
        return e;
    }
    public void set(E value){
        this.e = value;
    }
    @SafeVarargs
    public static <E> void fillOptional(E value, Holder<E>...optinal){
        if (optinal.length != 0){
            for (Holder<E> holder : optinal) {
                holder.set(value);
            }
        }
    }
}
