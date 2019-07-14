package com.waes.comparator.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * Class will be used to get base 64 encoded data as request
 * Created by volkangumus on 14.07.2019
 */
public class EncodedDataRequest {

    @NotBlank(message = "data can not be empty")
    @JsonProperty("data")
    private String data;

    public String getData() {
        return data;
    }

    public EncodedDataRequest setData(String data) {
        this.data = data;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EncodedDataRequest that = (EncodedDataRequest) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}
