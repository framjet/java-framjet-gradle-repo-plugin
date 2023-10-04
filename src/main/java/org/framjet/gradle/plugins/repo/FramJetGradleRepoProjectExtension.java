package org.framjet.gradle.plugins.repo;

import org.gradle.api.provider.Property;

/**
 * The {@code FramJetGradleRepoProjectExtension} interface outlines the configuration options
 * related to publishing artifacts within the context of the FramJet Gradle plugin.
 * <p>
 * This interface provides methods to retrieve properties related to the publishing permissions
 * and restrictions in the repository, utilizing Gradle's {@link Property} type to allow
 * lazy-evaluated and build-system-agnostic configurations.
 * <p>
 * Methods return a {@link Property<Boolean>} to allow for the configuration to be set
 * or retrieved, and for the calling class to observe changes to its value.
 */
public interface FramJetGradleRepoProjectExtension {

    /**
     * Retrieve the allowance status for publishing to non-FramJet repositories.
     * <p>
     * The returned property controls whether the build script is permitted to publish
     * artifacts to repositories other than those specified for FramJet. When the property
     * is set to {@code true}, publishing to external or third-party repositories is allowed.
     * When set to {@code false}, publishing to any repository that is not part of FramJet
     * is restricted.
     * <p>
     * It is crucial to configure this property carefully, as it governs the security and
     * distribution scope of the artifacts.
     *
     * @return a {@link Property<Boolean>} representing the allowance status for
     *         publishing to non-FramJet repositories
     */
    Property<Boolean> getAllowPublishToNonFramJetRepository();

}

