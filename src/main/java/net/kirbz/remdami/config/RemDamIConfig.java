package net.kirbz.remdami.config;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.yaml.snakeyaml.Yaml;

/*
* Code copied from ProfHugo's original 1.14 NoDamI fabric port
* https://github.com/ProfHugo/NoDamI/blob/1.14.4-fabric/src/main/java/net/profhugo/nodami/config/NodamiConfig.java
*
* All comments and some minor code adjustments added by Kirbz
 */


public class RemDamIConfig {

    public static int iFrameInterval;
    public static boolean excludePlayers, excludeAllMobs, debugMode;
    public static float attackCancelThreshold, knockbackCancelThreshold;

    public static String[] attackExcludedEntities, dmgReceiveExcludedEntities, damageSrcWhitelist;

    // Class used as data object used to carry the data read from the config file
    private static class ConfigCarrier {
        public int iFrameInterval;
        public boolean excludePlayers, excludeAllMobs, debugMode;
        public float attackCancelThreshold, knockbackCancelThreshold;
        public String[] attackExcludedEntities, dmgReceiveExcludedEntities, damageSrcWhitelist;

        public ConfigCarrier() {
            this.setToDefault();
        }

        private void setToDefault() {
            iFrameInterval = 0;
            excludePlayers = false;
            excludeAllMobs = false;
            attackCancelThreshold = 0.1f;
            knockbackCancelThreshold = 0.75f;
            attackExcludedEntities = new String[] {"minecraft:slime", "tconstruct:blueslime", "thaumcraft:thaumslime"};
            dmgReceiveExcludedEntities = new String[] {};
            damageSrcWhitelist = new String[] {"inFire", "lava", "cactus", "lightningBolt", "inWall", "hotFloor"};
            debugMode = false;
        }
    }

    // Class entry point, called by ..RemDamI.java
    public static void preInit() {
        File configFile = new File(FabricLoader.getInstance().getConfigDirectory(), "remdami.cfg");
        readConfig(configFile);
    }

    // Copy data from carrier to main class static variables
    private static void setToCarrier(ConfigCarrier carrier) {
        iFrameInterval = carrier.iFrameInterval;
        excludePlayers = carrier.excludePlayers;
        excludeAllMobs = carrier.excludeAllMobs;
        attackCancelThreshold = carrier.attackCancelThreshold;
        knockbackCancelThreshold = carrier.knockbackCancelThreshold;
        attackExcludedEntities = carrier.attackExcludedEntities.clone();
        dmgReceiveExcludedEntities = carrier.dmgReceiveExcludedEntities.clone();
        damageSrcWhitelist = carrier.damageSrcWhitelist.clone();
        debugMode = carrier.debugMode;
    }

    // Set all the main class static variables to their default values
    private static void setToDefault() {
        ConfigCarrier temp = new ConfigCarrier();
        setToCarrier(temp);
    }

    // Attempt to find and read an existing config file
    private static void readConfig(File configFile) {
        Yaml yamlParser = new Yaml();

        // Checking for existing config file
        if (configFile.exists()) {
            System.out.println("RemDamI: Found existing config file. Reading...");
            FileInputStream fStream;

            // Attempting to read the existing config file
            try {
                fStream = new FileInputStream(configFile);
                ConfigCarrier cfg = yamlParser.load(fStream); // Create data object to store info pulled from yaml
                setToCarrier(cfg); // Copy data from object to this class's static values
                fStream.close();

                String dump = yamlParser.dump(cfg);
                int firstEntryIndex = dump.indexOf("attackCancelThreshold");
                if (dump.indexOf("attackCancelThreshold") > 0 || dump.indexOf("attackCancelThreshold") < dump.length()) {
                    System.out.println("RemDamI: Done reading config files. Here are the loaded settings:");
                    System.out.println("\n" + dump.substring(firstEntryIndex));
                } else {
                    System.out.println("RemDamI: Warning, config file may potentially be corrupted. Loading default values.");
                    setToDefault();
                }

            // Failed to read the config file
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("RemDamI: Caught exception while reading config file. Loading default values.");
                setToDefault();
            } catch (IndexOutOfBoundsException e) {
                System.out.println("RemDamI: Config file was potentially corrupted. ");
                setToDefault();
            }

        // No existing config file found
        } else {

            // Create new config file
            System.out.println("RemDamI: Did not found config file. Writing first time config file...");
            ConfigCarrier firstTime = new ConfigCarrier();
            try {
                FileWriter writer = new FileWriter(configFile);
                writer.write(yamlParser.dump(firstTime));
                writer.close();
                System.out.println("RemDamI: Done writing first time config file.");
                setToCarrier(firstTime);

            // Failed to write first time config file
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("RemDamI: Caught exception while writing first time config file. Loading default values.");
                setToDefault();
            }

        }
    }

}
