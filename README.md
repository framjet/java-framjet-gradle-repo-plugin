# FramJet Gradle Plugin

## Overview

FramJet Gradle Plugin is a custom Gradle plugin designed to facilitate the seamless setup of repositories, optimized for FramJet projects. It manages interactions, credentials, configurations, and provides flexible publishing options to enhance the user and developer experience.

## Features

- **Easy Repository Setup**: Automates the setup of the main repository URL for all your Gradle projects.
- **Secure Credential Management**: Manages and validates repository credentials effectively.
- **Flexible Publishing Options**: Offers customizable configurations for publishing to different repository types.
- **Extensive Configuration Support**: Supports configurations via environment variables, project properties, and `gradle.properties`.

## Getting Started

### Applying the Plugin

To apply the plugin, add the following to your `settings.gradle` file:

```gradle
pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id "org.framjet.repo" version "1.1.0"
}
```

### Configuring the Plugin

Configure the plugin using the `framjet` extension in your `settings.gradle` file:

```groovy
framjet {
    url = "<main-repo-url>"
    releaseUrl = "<release-repo-url>"
    snapshotUrl = "<snapshot-repo-url>"
    username = "<repo-username>"
    password = "<repo-password>"
}
```

Configure the `framjetProject` extension for project-specific settings:

```groovy
framjetProject {
    allowPublishToNonFramJetRepository = <true/false>
}
```

### Alternative Configurations

You can also configure the plugin properties in `gradle.properties` or via environment variables:

| Property                    | Environment Variable        | Description                            |
|-----------------------------|-----------------------------|----------------------------------------|
| `framjet.repo.url`          | `FRAMJET_REPO_URL`          | Specifies the main repository URL.     |
| `framjet.repo.release.url`  | `FRAMJET_REPO_RELEASE_URL`  | Specifies the release repository URL.  |
| `framjet.repo.snapshot.url` | `FRAMJET_REPO_SNAPSHOT_URL` | Specifies the snapshot repository URL. |
| `framjet.repo.username`     | `FRAMJET_REPO_USERNAME`     | Specifies the username.                |
| `framjet.repo.password`     | `FRAMJET_REPO_PASSWORD`     | Specifies the password.                |

## Compatibility

This plugin is developed and tested with Gradle 8.3. Compatibility with other Gradle versions is not guaranteed.

## Issues and Contributions

For any issues, bugs, or feature requests, please use the project's issue tracker. Contributions are welcome; adhere to the project's contribution guidelines when submitting any changes.

## Contribution

Contributions are welcomed! Please read the [Contribution Guide](CONTRIBUTION.md) for detailed information on how you can contribute to the development of this plugin.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) file for details.
