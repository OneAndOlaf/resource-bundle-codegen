package com.github.oneandolaf.resourcebundlegen.api.bundlesources;

import com.github.oneandolaf.resourcebundlegen.api.ClassGenContext;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Describes how the data from the resource bundle is accessed in the generated class.
 * <p>
 * This interface contains fields for different {@code BundleSource}s covering the most common use-cases.
 * If a slightly higher degree of customization is necessary, the implementation classes in this package may be
 * instantiated or subclasses. Finally, this interface can be implemented from scratch to allow for the most
 * customization.
 */
public interface BundleSource {

    /**
     * The resource bundle is a static field using the built-in {@link ResourceBundle} class in the generated class.
     * <p>
     * The key accessors are static methods.
     *
     * @see StaticBundleSource
     */
    BundleSource STATIC = new StaticBundleSource(
            ResourceBundle.class.getCanonicalName(),
            "getString",
            ctx -> ResourceBundle.class.getCanonicalName() + ".getBundle(\"" + ctx.resourceBundlePath() + "\")"
    );

    /**
     * The resource bundle is an instance of the built-in {@link ResourceBundle} class that is injected from the
     * constructor.
     * <p>
     * The key accessors are instance methods.
     *
     * @see InjectedBundleSource
     */
    BundleSource INJECTED = new InjectedBundleSource(
            ResourceBundle.class.getCanonicalName(),
            "getString"
    );

    /**
     * Whether the resource getters are accessed via static methods or via instance methods.
     */
    boolean isStaticAccess();

    /**
     * Whether the generated class has an accessible constructor.
     * <p>
     * By default, the class will have a constructor if {@link #isStaticAccess()} returns {@code false}.
     */
    default boolean isInstantiable() {
        return !isStaticAccess();
    }

    /**
     * Gets a list of field declarations to include in a generated class.
     * <p>
     * The list items returned should be valid Java field declarations, but they should not be indented.
     *
     * @return the field declarations
     */
    default List<String> getFieldDecls(ClassGenContext context) {
        return List.of();
    }

    /**
     * Gets a list of constructor declarations to include in a generated class.
     * <p>
     * The list items returned should be valid Java field declarations.
     * <p>
     * Will be ignored if {@link #isInstantiable()} returns {@code false}.
     */
    default List<String> getConstructorDecls(ClassGenContext context) {
        return List.of();
    }

    /**
     * Generates an expression that will return the value for a given resource bundle key.
     *
     * @param key the key (without surrounding string literal quotes)
     * @return the expression
     */
    String generateKeyAccessor(String key);

}
