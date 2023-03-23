package me.xtrm.barman;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import me.xtrm.barman.config.Configuration;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

/**
 * @author xtrm
 */
public class Barman implements DedicatedServerModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("barman");
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("barman.toml");
    public static Configuration CONFIG;

    @Override
    public void onInitializeServer() {
        LOGGER.info("Shaking cocktails...");

        try {
            CONFIG = new Toml()
                    .read(Files.newBufferedReader(CONFIG_PATH))
                    .to(Configuration.class);
        } catch (NoSuchFileException e) {
            CONFIG = new Configuration();
            saveConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            if (!environment.dedicated) {
                LOGGER.warn("Barman is not meant to be used on a client.");
            }

            CommandRegistry.INSTANCE.register(dispatcher);
        });
    }

    public static void saveConfig() {
        try {
            Files.write(CONFIG_PATH, new TomlWriter().write(CONFIG).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
