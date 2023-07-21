package com.github.oneandolaf.resourcebundlegen.api;

public record ClassGenContext(
        String packageName,
        String simpleClassName,
        String resourceBundlePath
) {
}
