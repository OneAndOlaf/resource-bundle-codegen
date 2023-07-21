package com.github.oneandolaf.resourcebundlegen.impl;

import com.github.oneandolaf.resourcebundlegen.api.ClassGenContext;
import com.github.oneandolaf.resourcebundlegen.api.CodeGeneratorSettings;
import com.github.oneandolaf.resourcebundlegen.api.bundlesources.BundleSource;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ClassGeneratorTest {

    @Test
    void testEmpty() {
        String text = new ClassGenerator(new CodeGeneratorSettings(
                BundleSource.STATIC
        )).generateClassText(
                new ClassGenContext(
                        "org.example",
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
        String text = new ClassGenerator(new CodeGeneratorSettings(
                BundleSource.STATIC
        )).generateClassText(
                new ClassGenContext(
                        "org.example",
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
                                                        
                                    public String getKey1() {
                                        return BUNDLE.getString("key1");
                                    }
                                                        
                                }
                                """)
        );
    }

    @Test
    void testSinglePropInjected() {
        String text = new ClassGenerator(new CodeGeneratorSettings(
                BundleSource.INJECTED
        )).generateClassText(
                new ClassGenContext(
                        "org.example",
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
                                                        
                                    public String getKey1() {
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
