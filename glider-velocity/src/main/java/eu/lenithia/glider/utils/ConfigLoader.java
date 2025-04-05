package eu.lenithia.glider.utils;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class ConfigLoader {

    /**
     * Creates a versioned configuration file.
     *
     * @param configDirectory Directory where config file will be stored
     * @param fileName Name of the config file (without extension)
     * @param defaultConfig Input stream with default configuration
     * @return Loaded YamlDocument
     * @throws IOException If file operations fail
     */
    public static YamlDocument getVersionedConfig(Path configDirectory, String fileName, InputStream defaultConfig) throws IOException {
        // Ensure directory exists
        Files.createDirectories(configDirectory);

        File configFile = configDirectory.resolve(fileName + ".yml").toFile();

        YamlDocument yamlDocument = YamlDocument.create(
                configFile,
                Objects.requireNonNull(defaultConfig, "Default config resource cannot be null"),
                GeneralSettings.DEFAULT,
                LoaderSettings.builder().setAutoUpdate(true).build(),
                DumperSettings.DEFAULT,
                UpdaterSettings.builder()
                        .setVersioning(new BasicVersioning("config-version"))
                        .setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS)
                        .build()
        );

        yamlDocument.update();
        yamlDocument.save();
        return yamlDocument;
    }

    /**
     * Creates an unversioned configuration file.
     *
     * @param configDirectory Directory where config file will be stored
     * @param fileName Name of the config file (without extension)
     * @param defaultConfig Input stream with default configuration
     * @return Loaded YamlDocument
     * @throws IOException If file operations fail
     */
    public static YamlDocument getUnversionedConfig(Path configDirectory, String fileName, InputStream defaultConfig) throws IOException {
        // Ensure directory exists
        Files.createDirectories(configDirectory);

        File configFile = configDirectory.resolve(fileName + ".yml").toFile();

        YamlDocument yamlDocument = YamlDocument.create(
                configFile,
                Objects.requireNonNull(defaultConfig, "Default config resource cannot be null"),
                GeneralSettings.DEFAULT,
                LoaderSettings.builder().setAutoUpdate(true).build(),
                DumperSettings.DEFAULT
        );

        yamlDocument.update();
        yamlDocument.save();
        return yamlDocument;
    }

}
