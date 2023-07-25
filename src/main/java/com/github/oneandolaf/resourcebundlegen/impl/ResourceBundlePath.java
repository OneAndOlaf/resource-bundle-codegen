package com.github.oneandolaf.resourcebundlegen.impl;

import java.nio.file.Path;

public record ResourceBundlePath(
        Path dir,
        String fileName,
        String baseName,
        String extension
) {

    public static ResourceBundlePath get(Path bundlePath) {
        Path dir = bundlePath.getParent();

        String fileName = bundlePath.getFileName().toString();

        int extensionIndex = fileName.lastIndexOf(".");

        if (extensionIndex < 0) {
            throw new IllegalArgumentException(
                    "Resource bundle input path requires an extension, but `" + bundlePath + "` has none"
            );
        }

        String baseName = fileName.substring(0, extensionIndex);

        String extension = fileName.substring(extensionIndex + 1);

        return new ResourceBundlePath(
                dir,
                fileName,
                baseName,
                extension
        );
    }

}
