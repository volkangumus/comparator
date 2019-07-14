package com.waes.comparator.controller.response;

import java.util.Objects;

/**
 * Created by volkangumus on 14.07.2019
 */
public class DataResponse {

    private String message;

    public String getMessage() {
        return message;
    }

    public DataResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataResponse that = (DataResponse) o;
        return Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }
}
