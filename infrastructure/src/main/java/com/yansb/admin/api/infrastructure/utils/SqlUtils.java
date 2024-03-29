package com.yansb.admin.api.infrastructure.utils;

public final class SqlUtils {
    private SqlUtils() {
    }

    public static String upper(String terms) {
        if (terms == null) return null;
        return terms.toUpperCase();
    }

    public static String like(String terms) {
        if (terms == null) return null;
        return "%" + terms + "%";
    }

}
