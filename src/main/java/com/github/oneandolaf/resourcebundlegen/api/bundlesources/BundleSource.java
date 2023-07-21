package com.github.oneandolaf.resourcebundlegen.api.bundlesources;

import com.github.oneandolaf.resourcebundlegen.api.ClassGenContext;

import java.util.List;
import java.util.ResourceBundle;

public interface BundleSource {

    BundleSource STATIC = new StaticBundleSource(
            ResourceBundle.class.getCanonicalName(),
            "getString",
            ctx -> ResourceBundle.class.getCanonicalName() + ".getBundle(\"" + ctx.resourceBundlePath() + "\")"
    );
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
