package com.github.nija123098.evelyn.botConfiguration;

import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.context.environment.Environment;
import org.cfg4j.source.context.environment.ImmutableEnvironment;
import org.cfg4j.source.context.filesprovider.ConfigFilesProvider;
import org.cfg4j.source.files.FilesConfigurationSource;
import org.cfg4j.source.reload.ReloadStrategy;
import org.cfg4j.source.reload.strategy.PeriodicalReloadStrategy;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.github.nija123098.evelyn.util.PlatformDetector.ConvertPath;

/**
 * @Author: Celestialdeath99
 * Made on 11/28/2017
 */

public class ConfigLoader {

    public static ConfigurationProvider configurationProvider() {

        String configPaths = ConvertPath(System.getProperty("user.dir")) + "/Configs/";

        ConfigFilesProvider configFilesProvider = () -> Arrays.asList(Paths.get("BotConfig.yaml"), Paths.get("URLs.yaml"));
        ConfigurationSource source = new FilesConfigurationSource(configFilesProvider);
        Environment environment = new ImmutableEnvironment(configPaths);
        ReloadStrategy reloadStrategy = new PeriodicalReloadStrategy(1, TimeUnit.MINUTES);

        return new ConfigurationProviderBuilder()
                .withConfigurationSource(source)
                .withReloadStrategy(reloadStrategy)
                .withEnvironment(environment)
                .build();
    }
}
