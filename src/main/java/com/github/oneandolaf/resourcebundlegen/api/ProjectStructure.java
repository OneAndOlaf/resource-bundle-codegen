package com.github.oneandolaf.resourcebundlegen.api;

import java.nio.file.Path;

/**
 * Describes the structure of the project to generate files into.
 */
public final class ProjectStructure {
    private final Path genJavaRootDir;
    private final Path genResourcesRootDir;

    private ProjectStructure(
            Path genJavaRootDir,
            Path genResourcesRootDir
    ) {
        this.genJavaRootDir = genJavaRootDir;
        this.genResourcesRootDir = genResourcesRootDir;
    }

    /**
     * Creates a new object.
     *
     * @param genJavaRootDir      the root directory to generate {@code .java} files into
     * @param genResourcesRootDir the root directory to generate resource files into
     * @return the instance created
     */
    public static ProjectStructure create(
            Path genJavaRootDir, Path genResourcesRootDir
    ) {
        return new ProjectStructure(genJavaRootDir, genResourcesRootDir);
    }

    /**
     * Creates a new object based on Maven's directory structure.
     * <p>
     * This will generate the files into {@code $baseDir/main/java} and {@code $baseDir/main/resources}, respectively.
     *
     * @param baseDir the root directory of the generation (e.g. {@code ./src-gen}).
     * @return the instance created
     */
    public static ProjectStructure forMaven(Path baseDir) {
        return create(
                baseDir.resolve("main/java"),
                baseDir.resolve("main/resources")
        );
    }

    /**
     * Gets the root directory to generate {@code .java} files into.
     *
     * @return the directory
     */
    public Path getGenJavaRootDir() {
        return genJavaRootDir;
    }

    /**
     * Gets the root directory to generate resource files into.
     *
     * @return the directory
     */
    public Path getGenResourcesRootDir() {
        return genResourcesRootDir;
    }


}
