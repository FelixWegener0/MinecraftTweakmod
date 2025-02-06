package org.felixWegener.tweakmod.Config;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ReadConfig {

    private static final List<String> config = getConfigFile();

    private static List<String> getConfigFile() {
        List<String> config = new ArrayList<>();

        Path path = FabricLoader.getInstance().getConfigDir().resolve("tweakModConfig.txt");

        try {
            File configFile = new File(String.valueOf(path));
            Scanner read = new Scanner(configFile);

            while (read.hasNextLine()) {
                config.add(read.nextLine());
            }

            return config;
        } catch (FileNotFoundException e) {
            System.out.println("Error in read config file: " + e);
        }

        return config;
    }

    public static int getTeleportRangeMulti() {
        for (int i = 0; i < config.size(); i++) {
            if (Objects.equals(config.get(i), "teleportRangeMulti")) {
                return Integer.parseInt(config.get(i + 1));
            }
        }
        return 1;
    }

}
