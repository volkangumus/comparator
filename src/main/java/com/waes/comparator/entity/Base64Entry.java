package com.waes.comparator.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.Objects;

/**
 * Created by volkangumus on 14.07.2019
 */
@Entity
public class Base64Entry {

    @Id
    private long id;

    @Lob
    private String left;

    @Lob
    private String right;

    public Base64Entry() {
    }

    public long getId() {
        return id;
    }

    public Base64Entry setId(long id) {
        this.id = id;
        return this;
    }

    public String getLeft() {
        return left;
    }

    public Base64Entry setLeft(String left) {
        this.left = left;
        return this;
    }

    public String getRight() {
        return right;
    }

    public Base64Entry setRight(String right) {
        this.right = right;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Base64Entry that = (Base64Entry) o;
        return id == that.id &&
                Objects.equals(left, that.left) &&
                Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, left, right);
    }

    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this)
                .append("id", id)
                .append("left", left)
                .append("right", right)
                .toString();
    }
}
