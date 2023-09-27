package org.framjet.gradle.plugins.repo;

import java.util.Optional;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.provider.Property;

public class FramJetGradleRepoPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        var extension = project
                .getExtensions()
                .create("framJetRepo", FramJetGradleRepoPluginExtension.class);

        project.getAllprojects().forEach(p -> applyToProject(p, extension));
    }

    protected void applyToProject(Project project, FramJetGradleRepoPluginExtension config) {
        var repositories = project.getRepositories();

        var url = getParam(project, config.getUrl(), "url", "https://maven.framjet.dev");
        var username = getParam(project, config.getUsername(), "username", "");
        var password = getParam(project, config.getPassword(), "password", "");

        repositories.maven(repo -> {
            repo.setUrl(url);
            repo.setName("framjet");
            repo.credentials(c -> {
                c.setUsername(username);
                c.setPassword(password);
            });
        });
    }

    protected String getParam(
            Project project,
            Property<String> property,
            String name,
            String defaultValue
    ) {
        return Optional
                .ofNullable(System.getenv("FRAMJET_REPO_" + name.toUpperCase()))
                .or(() -> Optional.ofNullable(property.getOrNull()))
                .or(() -> Optional
                        .ofNullable(project.findProperty("framjet.repo." + name))
                        .map(Object::toString))
                .orElse(defaultValue);
    }
}
