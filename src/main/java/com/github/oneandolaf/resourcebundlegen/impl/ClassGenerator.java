package com.github.oneandolaf.resourcebundlegen.impl;

import com.github.oneandolaf.resourcebundlegen.api.ClassGenContext;
import com.github.oneandolaf.resourcebundlegen.api.CodeGeneratorSettings;
import com.github.oneandolaf.resourcebundlegen.impl.util.JavaIdentifiers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public final class ClassGenerator {

    private final CodeGeneratorSettings settings;

    public ClassGenerator(CodeGeneratorSettings settings) {
        this.settings = settings;
    }

    public String generateClassText(
            ClassGenContext context,
            Map<String, Properties> allProperties
    ) {

        StringBuilder builder = new StringBuilder()
                .append("// Generated class. Please do not edit manually.\n")
                .append("package ").append(context.packageName()).append(";\n\n");

        builder.append(
                settings.bundleSource().isInstantiable()
                        ? "public class "
                        : "public final class ")
                .append(context.simpleClassName()).append(" {\n\n");

        var fieldDecls = settings.bundleSource().getFieldDecls(context);

        for (String fieldDecl : fieldDecls) {
            builder.append("    ")
                    .append(fieldDecl)
                    .append("\n");
        }

        if (!fieldDecls.isEmpty()) {
            builder.append("\n");
        }

        if (!settings.bundleSource().isInstantiable()) {
            builder.append("    private ")
                    .append(context.simpleClassName())
                    .append("() {\n        // hide constructor\n    }\n\n");
        } else {
            var constructorDecls = settings.bundleSource().getConstructorDecls(context);

            for (String constructorDecl : constructorDecls) {
                builder.append(constructorDecl.trim().indent(4))
                        .append("\n");
            }
        }

        addGetters(builder, allProperties);

        builder.append("}\n");

        return builder.toString();
    }

    private void addGetters(
            StringBuilder builder,
            Map<String, Properties> allProperties
    ) {

        Set<String> i18nKeys = new HashSet<>();

        for (Properties props : allProperties.values()) {
            for (Object key : props.keySet()) {
                i18nKeys.add(key.toString());
            }
        }

        List<String> sortedI18nKeys = new ArrayList<>(i18nKeys);
        sortedI18nKeys.sort(Comparator.naturalOrder());

        for (String i18nKey : sortedI18nKeys) {
            String getterName = JavaIdentifiers.toGetter(i18nKey);

            builder.append("    public String ")
                    .append(getterName)
                    .append("() {\n")
                    .append("        return ")
                    .append(settings.bundleSource().generateKeyAccessor(i18nKey))
                    .append(";\n")
                    .append("    }\n\n");
        }
    }

}
