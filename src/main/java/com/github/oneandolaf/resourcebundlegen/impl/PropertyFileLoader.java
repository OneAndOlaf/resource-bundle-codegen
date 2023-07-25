package com.github.oneandolaf.resourcebundlegen.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public final class PropertyFileLoader {

    private PropertyFileLoader() {
        // hide constructor
    }

    public static Map<String, Properties> loadPropertyFiles(
            ResourceBundlePath bundlePath
    ) throws IOException {

        var directory = bundlePath.dir();
        var baseFileName = bundlePath.baseName();

        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException(
                    "File at `" + directory + "` is not a directory"
            );
        }

        var filePrefix = baseFileName + "_";
        var fileExt = ".properties";

        var result = new HashMap<String, Properties>();


        try (var files = Files.list(directory)) {
            Set<Path> bundlePaths = files.filter(it -> {
                        String fileName = it.getFileName().toString();

                        return fileName.equals(baseFileName + fileExt)
                               || (fileName.startsWith(filePrefix) && fileName.endsWith(fileExt));
                    })
                    .collect(Collectors.toSet());

            for (Path path : bundlePaths) {
                String fileName = path.getFileName().toString();

                String bundleId = fileName.equals(baseFileName + fileExt)
                        ? "" : fileName.substring(filePrefix.length(), fileName.length() - fileExt.length());

                Properties props = new Properties();

                try (var bufferedReader = Files.newBufferedReader(path)) {
                    props.load(bufferedReader);
                }

                result.put(bundleId, props);
            }
        }

        return result;
    }

}
