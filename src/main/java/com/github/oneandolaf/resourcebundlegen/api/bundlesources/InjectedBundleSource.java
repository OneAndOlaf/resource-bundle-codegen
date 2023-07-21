package com.github.oneandolaf.resourcebundlegen.api.bundlesources;

import com.github.oneandolaf.resourcebundlegen.api.ClassGenContext;

import java.util.List;

public class InjectedBundleSource implements BundleSource {

    protected final String canonicalBundleClass;

    protected final String getterName;

    public InjectedBundleSource(
            String canonicalBundleClass,
            String getterName
    ) {
        this.canonicalBundleClass = canonicalBundleClass;
        this.getterName = getterName;
    }


    @Override
    public boolean isStaticAccess() {
        return false;
    }

    @Override
    public List<String> getFieldDecls(ClassGenContext context) {
        return List.of(
                "private final " + canonicalBundleClass + " bundle;"
        );
    }

    @Override
    public List<String> getConstructorDecls(ClassGenContext context) {
        return List.of(
                "public " + context.simpleClassName() + "(" + canonicalBundleClass
                    + " bundle) {\n    this.bundle = bundle;\n}"
        );
    }

    @Override
    public String generateKeyAccessor(String key) {
        return "bundle." + getterName + "(\"" + key + "\")";
    }
}
