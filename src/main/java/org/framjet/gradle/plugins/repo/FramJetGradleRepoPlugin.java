package org.framjet.gradle.plugins.repo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.ProjectEvaluationListener;
import org.gradle.api.ProjectState;
import org.gradle.api.initialization.Settings;
import org.gradle.api.provider.Property;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FramJetGradleRepoPlugin implements Plugin<Settings> {

    protected @Nullable Properties properties;

    @Override
    public void apply(@NotNull Settings target) {
        var extension = target
                .getExtensions()
                .create("framJet", FramJetGradleRepoExtension.class);

        try {
            loadParameters(target);
        } catch (IOException e) {
            throw new GradleException("Cannot load Gradle parameters", e);
        }

        String mainRepoUrl = getParam(
                String.class,
                extension.getUrl(),
                "repo.url",
                "https://nexus.framjet.dev/repository/maven/"
        );

        String username = getParam(
                String.class,
                extension.getUsername(),
                "repo.username",
                ""
        );

        String password = getParam(
                String.class,
                extension.getPassword(),
                "repo.password",
                ""
        );

        if (username == null || username.isEmpty()) {
            throw new GradleException(
                    "Cannot find username for FramJet Repository. Neither FRAMJET_REPO_USERNAME"
                    + " env variable nor framjet.repo.username property is set"
            );
        }

        if (password == null || password.isEmpty()) {
            throw new GradleException(
                    "Cannot find password for FramJet Repository. Neither FRAMJET_REPO_PASSWORD"
                    + " env variable nor framjet.repo.password property is set"
            );
        }

        target.getPluginManagement().getRepositories().maven(repo -> {
            repo.setUrl(mainRepoUrl);
            repo.setName("framjet");
            repo.credentials(c -> {
                c.setUsername(username);
                c.setPassword(password);
            });
        });

        target.getDependencyResolutionManagement().getRepositories().maven(repo -> {
            repo.setUrl(mainRepoUrl);
            repo.setName("framjet");
            repo.credentials(c -> {
                c.setUsername(username);
                c.setPassword(password);
            });
        });

        target.getGradle().addProjectEvaluationListener(new ProjectEvaluationListener() {
            @Override
            public void beforeEvaluate(@NotNull Project project) {
                project.getRepositories().maven(repo -> {
                    repo.setUrl(mainRepoUrl);
                    repo.setName("framjet");
                    repo.credentials(c -> {
                        c.setUsername(username);
                        c.setPassword(password);
                    });
                });
            }

            @Override
            public void afterEvaluate(@NotNull Project project, @NotNull ProjectState state) {
                var ext = project
                        .getExtensions()
                        .create("framjetProject", FramJetGradleRepoProjectExtension.class);

                if (project.getPlugins().hasPlugin(MavenPublishPlugin.class)) {
                    var allowPublishToNonFramJetRepository = getParam(
                            Boolean.class,
                            ext.getAllowPublishToNonFramJetRepository(),
                            "allow.publish.non.framjet.repository",
                            false
                    );

                    var releaseUrl = getParam(
                            String.class,
                            extension.getReleaseUrl(),
                            "repo.release.url",
                            "https://nexus.framjet.dev/repository/maven-release/"
                    );

                    var snapshotUrl = getParam(
                            String.class,
                            extension.getSnapshotUrl(),
                            "repo.snapshot.url",
                            "https://nexus.framjet.dev/repository/maven-snapshot/"
                    );

                    var publishExt = project.getExtensions().findByType(PublishingExtension.class);

                    if (publishExt != null) {
                        publishExt.getRepositories().forEach(r -> {
                            if (!r.getName().equals("framjet")
                                && !allowPublishToNonFramJetRepository) {
                                throw new GradleException(
                                        "Project %s is not allowed to publish to non FramJet repository %s"
                                                .formatted(project, r)
                                );
                            }
                        });

                        var url = project.getVersion().toString().endsWith("SNAPSHOT") ? snapshotUrl
                                : releaseUrl;

                        publishExt.getRepositories().maven(repo -> {
                            repo.setUrl(url);
                            repo.setName("framjet");
                            repo.credentials(c -> {
                                c.setUsername(username);
                                c.setPassword(password);
                            });
                        });
                    }
                }
            }
        });
    }

    protected void loadParameters(Settings settings) throws IOException {
        var startParameter = settings.getStartParameter();

        var projectProperties  = new Properties();
        var userHomeProperties = new Properties();

        var projectPropertiesFile = new File(settings.getRootDir(), "gradle.properties");
        if (projectPropertiesFile.exists()) {
            try (var reader = new FileReader(projectPropertiesFile)) {
                projectProperties.load(reader);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        var userHomePropertiesFile = new File(
                startParameter.getGradleUserHomeDir(),
                "gradle.properties"
        );
        if (userHomePropertiesFile.exists()) {
            try (var reader = new FileReader(userHomePropertiesFile)) {
                userHomeProperties.load(reader);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        this.properties = new Properties();
        this.properties.putAll(userHomeProperties);
        this.properties.putAll(projectProperties);
        this.properties.putAll(startParameter.getProjectProperties());
    }

    protected Properties getProperties() {
        if (this.properties == null) {
            throw new IllegalStateException("Properties are not loaded");
        }

        return this.properties;
    }

    protected <T> T getParam(
            Class<T> type,
            Property<T> property,
            String name,
            T defaultValue
    ) {
        return Optional
                .ofNullable(getParamEnv(type, name))
                .or(() -> Optional.ofNullable(property.getOrNull()))
                .or(() -> Optional
                        .ofNullable(paramCast(type, getProperties().get("framjet." + name)))
                )
                .orElse(defaultValue);
    }

    protected <T> @Nullable T getParamEnv(Class<T> type, String name) {
        return paramCast(
                type,
                System.getenv("FRAMJET_" + name.replaceAll("\\.", "_").toUpperCase())
        );
    }

    @SuppressWarnings("unchecked")
    protected <T> @Nullable T paramCast(Class<T> type, @Nullable Object value) {
        if (value == null) {
            return null;
        }

        if (type == String.class) {
            return (T) value;
        }

        if (type == Integer.class) {
            return (T) (Integer) Integer.parseInt(value.toString());
        }

        if (type == Boolean.class) {
            return (T) (Boolean) Boolean.parseBoolean(value.toString());
        }

        throw new IllegalArgumentException("Unknown type: " + type);
    }
}
