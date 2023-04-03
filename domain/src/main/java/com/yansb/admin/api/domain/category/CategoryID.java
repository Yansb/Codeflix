package com.yansb.admin.api.domain.category;

import com.yansb.admin.api.domain.Identifier;
import com.yansb.admin.api.domain.utils.IdUtils;

import java.util.Objects;

public class CategoryID extends Identifier {
    private final String value;

    private CategoryID(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static CategoryID unique() {
        return CategoryID.from(IdUtils.uuid());
    }

    public static CategoryID from(final String anID) {
        return new CategoryID(anID);
    }


    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CategoryID that = (CategoryID) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
