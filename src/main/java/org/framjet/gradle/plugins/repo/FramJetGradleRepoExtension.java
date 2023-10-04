package org.framjet.gradle.plugins.repo;

import org.gradle.api.provider.Property;

/**
 * The {@code FramJetGradleRepoExtension} interface defines the contract for handling
 * Gradle repository configuration in the FramJet plugin.
 * <p>
 * This interface exposes methods to retrieve properties related to the configuration
 * of repository URLs, authentication credentials, and local settings file.
 * Using Gradle's {@link Property} type allows us to manage these configurations
 * in a lazy-evaluated and build-system-agnostic manner.
 * <p>
 * All methods should return a {@link Property<String>} which will enable the calling
 * class to set or retrieve its String value or a value provider. It also allows
 * consumers to handle the property’s value as it changes.
 */
public interface FramJetGradleRepoExtension {

    /**
     * Retrieve the URL of the repository.
     *
     * @return a {@link Property<String>} representing the repository URL
     */
    Property<String> getUrl();

    /**
     * Retrieve the URL for snapshot artifacts within the repository.
     * <p>
     * This is often a distinct URL within a repository that houses in-development
     * (snapshot) versions of artifacts.
     *
     * @return a {@link Property<String>} representing the snapshot repository URL
     */
    Property<String> getSnapshotUrl();

    /**
     * Retrieve the URL for release artifacts within the repository.
     * <p>
     * This URL typically houses stable, released versions of artifacts.
     *
     * @return a {@link Property<String>} representing the release repository URL
     */
    Property<String> getReleaseUrl();

    /**
     * Retrieve the username used for authenticating with the repository.
     *
     * @return a {@link Property<String>} representing the username for repository authentication
     */
    Property<String> getUsername();

    /**
     * Retrieve the password used for authenticating with the repository.
     * <p>
     * Ensure that any implementation storing or handling this password does so securely,
     * adhering to best practices for credential management.
     *
     * @return a {@link Property<String>} representing the password for repository authentication
     */
    Property<String> getPassword();

    /**
     * Retrieve the filename of a local settings/configuration file.
     * <p>
     * The local settings might include configurations or credentials that should not
     * be included directly within build scripts or source control.
     *
     * @return a {@link Property<String>} representing the filename of the local settings
     */
    Property<String> getLocalSettingsFilename();

}
