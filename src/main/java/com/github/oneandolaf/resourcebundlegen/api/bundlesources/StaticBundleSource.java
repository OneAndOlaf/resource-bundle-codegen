package com.github.oneandolaf.resourcebundlegen.api.bundlesources;

import com.github.oneandolaf.resourcebundlegen.api.ClassGenContext;

import java.util.List;
import java.util.function.Function;

/**
 * A {@link BundleSource} where the resource bundle is a static field.
 */
public class StaticBundleSource implements BundleSource {

    protected final String canonicalBundleClass;

    protected final String getterName;

    protected final Function<ClassGenContext, String> factoryExpression;

    /**
     * Creates a new instance.
     *
     * @param canonicalBundleClass the canonical name of the resource bundle class
     * @param getterName           the name of the bundle class' resource getter method (should accept a single {@code String} as
     *                             a parameter, and return a {@code String})
     * @param factoryExpression    a method to generate an initializer for the static field
     */
    public StaticBundleSource(
            String canonicalBundleClass,
            String getterName,
            Function<ClassGenContext, String> factoryExpression
    ) {
        this.canonicalBundleClass = canonicalBundleClass;
        this.getterName = getterName;
        this.factoryExpression = factoryExpression;
    }

    @Override
    public boolean isStaticAccess() {
        return true;
    }

    @Override
    public List<String> getFieldDecls(ClassGenContext context) {
        return List.of(
                "private static final "
                + canonicalBundleClass
                + " BUNDLE = "
                + factoryExpression.apply(context)
                + ";"
        );
    }

    @Override
    public String generateKeyAccessor(String key) {
        return "BUNDLE." + getterName + "(\"" + key + "\")";
    }
}
