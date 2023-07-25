package com.github.oneandolaf.resourcebundlegen.api;

public record ClassGenContext(
        CodeGenerationInput settings,
        String simpleClassName,
        String resourceBundlePath
) {

    public String packageName() {
        return settings().getPackageName();
    }

}
