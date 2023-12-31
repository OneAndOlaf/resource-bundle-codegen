package com.github.oneandolaf.resourcebundlegen.api;

import com.github.oneandolaf.resourcebundlegen.api.bundlesources.BundleSource;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Input data for the code generation of a single class.
 * <p>
 * Should be created using the static {@link #builder(Path, String)} overloads.
 */
public final class CodeGenerationInput {

    private final Path bundleBasePath;
    private final BundleSource bundleSource;
    private final ParameterSyntax paramSyntax;
    private final String packageName;

    private CodeGenerationInput(
            Path bundleBasePath,
            BundleSource bundleSource,
            ParameterSyntax paramSyntax,
            String packageName
    ) {
        this.bundleBasePath = bundleBasePath;
        this.bundleSource = bundleSource;
        this.paramSyntax = paramSyntax;
        this.packageName = packageName;
    }

    /**
     * Creates a builder.
     *
     * @param bundleBasePath The path to the base bundle (without any locale extensions), regardless of whether that
     *                       file actually exists: Even if only {@code Bundle_en.properties} exists, this should still
     *                       be {@code Bundle.properties}.
     * @param packageName    the package to generate the class into
     * @return a builder
     */
    public static Builder builder(String bundleBasePath, String packageName) {
        return builder(Paths.get(bundleBasePath), packageName);
    }

    /**
     * Creates a builder.
     *
     * @param bundleBasePath The path to the base bundle (without any locale extensions), regardless of whether that
     *                       file actually exists: Even if only {@code Bundle_en.properties} exists, this should still
     *                       be {@code Bundle.properties}.
     * @param packageName    the package to generate the class into
     * @return a builder
     */
    public static Builder builder(Path bundleBasePath, String packageName) {
        return new Builder(bundleBasePath, packageName);
    }

    public Path getBundleBasePath() {
        return bundleBasePath;
    }

    public BundleSource getBundleSource() {
        return bundleSource;
    }

    public ParameterSyntax getParameterSyntax() {
        return paramSyntax;
    }

    public String getPackageName() {
        return packageName;
    }

    public static final class Builder {

        private final Path bundleBasePath;
        private final String packageName;

        private BundleSource bundleSource = BundleSource.STATIC;

        private ParameterSyntax parameterSyntax = ParameterSyntax.NONE;

        private Builder(Path bundleBasePath, String packageName) {
            this.bundleBasePath = bundleBasePath;
            this.packageName = packageName;
        }

        /**
         * Uses a static field for the resource bundle.
         *
         * @return {@code this}, for chaining
         * @see BundleSource#STATIC
         */
        public Builder withStaticBundle() {
            return withCustomBundle(BundleSource.STATIC);
        }

        /**
         * Uses an injected instance field for the resource bundle.
         *
         * @return {@code this}, for chaining
         * @see BundleSource#INJECTED
         */
        public Builder withInjectedBundle() {
            return withCustomBundle(BundleSource.INJECTED);
        }

        /**
         * Uses a custom {@link BundleSource}.
         *
         * @param bundleSource the source to use
         * @return {@code this}, for chaining
         */
        public Builder withCustomBundle(BundleSource bundleSource) {
            this.bundleSource = bundleSource;
            return this;
        }

        /**
         * Disables parameter parsing.
         *
         * @return {@code this}, for chaining
         */
        public Builder withNoParameters() {
            return withCustomParameters(ParameterSyntax.NONE);
        }

        /**
         * Uses custom parameter parsing.
         *
         * @param syntax the parameter syntax to use
         * @return {@code this}, for chaining
         */
        public Builder withCustomParameters(ParameterSyntax syntax) {
            this.parameterSyntax = syntax;
            return this;
        }

        /**
         * Creates the actual {@link CodeGenerationInput}.
         *
         * @return the created instance
         */
        public CodeGenerationInput build() {
            return new CodeGenerationInput(
                    bundleBasePath,
                    bundleSource,
                    parameterSyntax,
                    packageName
            );
        }

    }
}
