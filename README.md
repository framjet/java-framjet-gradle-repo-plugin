# FramJet Gradle Plugin

## Overview

The FramJet Gradle Plugin is a versatile tool designed to initialize and configure FramJet framework repositories. This plugin is instrumental for developers working with different development channels such as dev, alpha, and beta when creating frameworks. It allows for smooth transitions and management of different phases of framework development, catering to a broad spectrum of development scenarios and not exclusive to any specific use case or company.

## Installation

To apply the FramJet Gradle Plugin, add the following lines to your `build.gradle` file:

```gradle
plugins {
  id "org.framjet.repo" version "1.0.0"
}
```

## Features

* **Framework Initialization**: Automate the setup of FramJet framework repositories.
* **Channel Management**: Easily switch between dev, alpha, and beta channels, allowing for diverse development and testing environments.
* **Configuration Management**: Provides an extensive configuration setup, enabling developers to manage frameworks efficiently.


## Configuration

The plugin allows extensive configurations allowing developers to adapt the framework initialization and management to their needs. Here is an example configuration:

```gradle
framJet {
  channel = "dev" // default channel
  frameworkVersion = "1.0.0" // default framework version
}
```

## Contribution

Contributions are welcomed! Please read the [Contribution Guide](CONTRIBUTION.md) for detailed information on how you can contribute to the development of this plugin.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) file for details.
