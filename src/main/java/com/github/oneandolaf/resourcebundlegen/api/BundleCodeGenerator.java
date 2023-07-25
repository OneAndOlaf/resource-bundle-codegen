package com.github.oneandolaf.resourcebundlegen.api;

import com.github.oneandolaf.resourcebundlegen.impl.ClassGenerator;
import com.github.oneandolaf.resourcebundlegen.impl.PropertyFileLoader;
import com.github.oneandolaf.resourcebundlegen.impl.ResourceBundlePath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

/**
 * Entry point for the code generator.
 */
public final class BundleCodeGenerator {

    private final ProjectStructure projectStructure;

    /**
     * Creates a new generator.
     *
     * @param projectStructure the structure of the project to generate into
     */
    public BundleCodeGenerator(ProjectStructure projectStructure) {
        this.projectStructure = projectStructure;
    }

    /**
     * Generates a single class.
     *
     * @param input the input parameters for the generation
     * @throws IOException if an IO error occurs
     */
    public void generateClass(
            CodeGenerationInput input
    ) throws IOException {
        var resourceBundlePathStats = ResourceBundlePath.get(input.getBundleBasePath());

        if (!resourceBundlePathStats.extension().equalsIgnoreCase("properties")) {
            throw new IllegalArgumentException("Only .properties files are supported for generation");
        }

        Map<String, Properties> propertiesContent = PropertyFileLoader.loadPropertyFiles(resourceBundlePathStats);

        var classGen = new ClassGenerator(input);

        String generatedBundleBaseName = "Gen_" + resourceBundlePathStats.baseName();

        String generatedClass = classGen.generateClassText(
                new ClassGenContext(
                        input,
                        resourceBundlePathStats.baseName(),
                        input.getPackageName() + "." + generatedBundleBaseName
                ),
                propertiesContent
        );

        String subDir = input.getPackageName().replace(".", "/");

        Path parsedJavaOutputPath = projectStructure.getGenJavaRootDir()
                .resolve(subDir)
                .resolve(resourceBundlePathStats.baseName() + ".java");

        Files.createDirectories(parsedJavaOutputPath.getParent());

        Files.writeString(parsedJavaOutputPath, generatedClass);

        Path parsedResourcesOutputPath = projectStructure.getGenResourcesRootDir()
                .resolve(subDir);

        Files.createDirectories(parsedResourcesOutputPath);

        for (var entry : propertiesContent.entrySet()) {
            String fileBaseName = entry.getKey().isEmpty()
                    ? generatedBundleBaseName
                    : generatedBundleBaseName + "_" + entry.getKey();

            Path path = parsedResourcesOutputPath.resolve(
                    fileBaseName + ".properties"
            );

            try (var writer = Files.newBufferedWriter(path)) {
                entry.getValue().store(writer, "Generated file. Please do not modify manually.");
            }
        }
    }

}
