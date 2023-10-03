package org.framjet.gradle.plugins.repo;

import org.gradle.api.provider.Property;

public interface FramJetGradleRepoProjectExtension {

    Property<Boolean> getAllowPublishToNonFramJetRepository();

}
