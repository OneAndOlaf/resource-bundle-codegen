package com.github.oneandolaf.resourcebundlegen.impl.util;

public final class JavaIdentifiers {

    private JavaIdentifiers() {
        // hide constructor
    }

    public static String toGetter(String key) {

        String upperCaseKey = key.substring(0, 1).toUpperCase() + key.substring(1);

        return "get" + upperCaseKey;
    }

}
