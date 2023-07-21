package com.github.oneandolaf.resourcebundlegen.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PropertyFileLoaderTest {

    @Test
    void simpleTest() throws IOException {

        var map = PropertyFileLoader.loadPropertyFiles(
                testPath("bundles/noparams"),
                "Bundle1"
        );

        assertThat(map, aMapWithSize(2));
        assertThat(map, hasKey(""));
        assertThat(map, hasKey("es"));

        var baseProps = map.get("");
        var esProps = map.get("es");

        assertThat(baseProps, aMapWithSize(1));
        assertThat(baseProps.get("key1"), is("Hello"));

        assertThat(esProps, aMapWithSize(1));
        assertThat(esProps.get("key1"), is("Hola"));
    }

    @Nested
    class ErrorTests {

        @Test
        void exceptionThrownForInvalidDirectory() {
            var ex = assertThrows(IllegalArgumentException.class, () -> {
                PropertyFileLoader.loadPropertyFiles(
                        testPath("bundles/noparams").resolve("foo"),
                        "Bundle"
                );
            });

            assertThat(
                    ex.getMessage(), containsString("is not a directory")
            );
        }

    }

    private Path testPath(String path) {
        try {
            return Paths.get(getClass().getClassLoader().getResource(path).toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Invalid URI syntax", e);
        }
    }

}
