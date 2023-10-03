package org.framjet.gradle.plugins.repo;

import org.gradle.api.provider.Property;

public interface FramJetGradleRepoExtension {

    Property<String> getUrl();

    Property<String> getSnapshotUrl();

    Property<String> getReleaseUrl();

    Property<String> getUsername();

    Property<String> getPassword();

}
