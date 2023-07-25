package com.github.oneandolaf.resourcebundlegen.api;

import java.nio.file.Path;

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

    public static ProjectStructure create(
            Path genJavaRootDir, Path genResourcesRootDir
    ) {
        return new ProjectStructure(genJavaRootDir, genResourcesRootDir);
    }

    public static ProjectStructure forMaven(Path baseDir) {
        return new ProjectStructure(
                baseDir.resolve("main/java"),
                baseDir.resolve("main/resources")
        );
    }



    public Path getGenJavaRootDir() {
        return genJavaRootDir;
    }

    public Path getGenResourcesRootDir() {
        return genResourcesRootDir;
    }




}
