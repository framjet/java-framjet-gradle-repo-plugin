package org.framjet.gradle.plugins.repo;

import org.gradle.api.provider.Property;

public interface FramJetGradleRepoPluginExtension {

    Property<String> getUrl();

    Property<String> getUsername();

    Property<String> getPassword();

}
