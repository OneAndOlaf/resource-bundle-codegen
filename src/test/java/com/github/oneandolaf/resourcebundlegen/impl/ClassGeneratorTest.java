package com.github.oneandolaf.resourcebundlegen.impl;

import com.github.oneandolaf.resourcebundlegen.api.ClassGenContext;
import com.github.oneandolaf.resourcebundlegen.api.CodeGenerationInput;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ClassGeneratorTest {

    @Test
    void testEmpty() {
        var settings = CodeGenerationInput.builder("", "org.example")
                .withStaticBundle()
                .build();

        String text = new ClassGenerator(settings).generateClassText(
                new ClassGenContext(
                        settings,
                        "Foo",
                        "Bundle"
                ),
                Map.of()
        );

        assertThat(
                text, is(
                        // language=java
                        """
                                // Generated class. Please do not edit manually.
                                package org.example;
                                                        
                                public final class Foo {
                                            
                                    private static final java.util.ResourceBundle BUNDLE = java.util.ResourceBundle.getBundle("Bundle");
                                            
                                    private Foo() {
                                        // hide constructor
                                    }
                                                        
                                }
                                """)
        );
    }

    @Test
    void testSinglePropStatic() {
        var settings = CodeGenerationInput.builder("", "org.example")
                .withStaticBundle()
                .build();

        String text = new ClassGenerator(settings).generateClassText(
                new ClassGenContext(
                        settings,
                        "Foo",
                        "Bundle"
                ),
                Map.of("", createProperties(Map.of("key1", "Hello")))
        );

        assertThat(
                text, is(
                        // language=java
                        """
                                // Generated class. Please do not edit manually.
                                package org.example;
                                                        
                                public final class Foo {
                                            
                                    private static final java.util.ResourceBundle BUNDLE = java.util.ResourceBundle.getBundle("Bundle");
                                            
                                    private Foo() {
                                        // hide constructor
                                    }
                                                        
                                    public static String key1() {
                                        return BUNDLE.getString("key1");
                                    }
                                                        
                                }
                                """)
        );
    }

    @Test
    void testSinglePropInjected() {
        var settings = CodeGenerationInput.builder("", "org.example")
                .withInjectedBundle()
                .build();

        String text = new ClassGenerator(settings).generateClassText(
                new ClassGenContext(
                        settings,
                        "Foo",
                        "Bundle"
                ),
                Map.of("", createProperties(Map.of("key1", "Hello")))
        );

        assertThat(
                text, is(
                        // language=java
                        """
                                // Generated class. Please do not edit manually.
                                package org.example;
                                                        
                                public class Foo {
                                            
                                    private final java.util.ResourceBundle bundle;
                                            
                                    public Foo(java.util.ResourceBundle bundle) {
                                        this.bundle = bundle;
                                    }
                                                        
                                    public String key1() {
                                        return bundle.getString("key1");
                                    }
                                                        
                                }
                                """)
        );
    }

    private static Properties createProperties(Map<String, String> map) {
        Properties props = new Properties();

        props.putAll(map);

        return props;
    }

}
