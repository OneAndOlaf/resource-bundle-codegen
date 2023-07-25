package com.github.oneandolaf.resourcebundlegen;

import com.github.oneandolaf.resourcebundlegen.api.BundleCodeGenerator;
import com.github.oneandolaf.resourcebundlegen.api.CodeGenerationInput;
import com.github.oneandolaf.resourcebundlegen.api.ProjectStructure;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IntegrationTest {

    @Test
    void testGeneration(@TempDir Path dir) throws IOException {

        BundleCodeGenerator gen = new BundleCodeGenerator(ProjectStructure.create(
                dir.resolve("java"),
                dir.resolve("res")
        ));

        gen.generateClass(
                CodeGenerationInput.builder(
                                testPath("bundles/noparams").resolve("Bundle1.properties"),
                                "org.example")
                        .withStaticBundle()
                        .build()
        );

        MatcherAssert.assertThat(
                "Bundle1.java file should exist",
                Files.isRegularFile(dir.resolve("java/org/example/Bundle1.java")),
                Matchers.is(true)
        );

        MatcherAssert.assertThat(
                "Gen_Bundle1.properties file should exist",
                Files.isRegularFile(dir.resolve("res/org/example/Gen_Bundle1.properties")),
                Matchers.is(true)
        );

        MatcherAssert.assertThat(
                "Gen_Bundle1_es.properties file should exist",
                Files.isRegularFile(dir.resolve("res/org/example/Gen_Bundle1_es.properties")),
                Matchers.is(true)
        );

        // TODO verify their contents
    }


    private Path testPath(String path) {
        try {
            return Paths.get(getClass().getClassLoader().getResource(path).toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Invalid URI syntax", e);
        }
    }

}
