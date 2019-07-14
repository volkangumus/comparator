package com.waes.comparator.controller.request;

/**
 * Created by volkangumus on 14.07.2019
 */
public enum AspectEnum {

    LEFT("left"),
    MIDDLE("middle"), // test purpose only
    RIGHT("right");

    private String value;

    AspectEnum(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
