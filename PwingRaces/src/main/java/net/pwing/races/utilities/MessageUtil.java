package net.pwing.races.utilities;

import me.clip.placeholderapi.PlaceholderAPI;

import net.pwing.races.PwingRaces;
import net.pwing.races.api.race.RacePlayer;
import net.pwing.races.config.RaceConfigurationManager;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MessageUtil {

    private static Map<String, String> messages;

    public static void initMessages(String configPath, RaceConfigurationManager configManager) {
        messages = new HashMap<>();

        FileConfiguration config = configManager.getMessageConfig().getConfig();
        // Adding default from version 1.0.9
        config.addDefault(configPath + ".set-skillpoint-message", "%prefix% &aYou have set %player_name%'s skillpoints to %skillpoints%.");
        config.addDefault(configPath + ".set-level-message", "%prefix% &aYou have set %player_name%'s level to %level%.");
        config.addDefault(configPath + ".set-exp-message", "%prefix% &aYou have set %player_name%'s race exp to %exp%.");

        // Adding default from version 1.1.4
        config.addDefault(configPath + ".no-elements-purchased", "%prefix% &cYou haven't bought any skilltree elements!");

        // Adding default from version 1.1.8
        config.addDefault(configPath + ".menu-confirmation", "Confirmation");
        config.addDefault(configPath + ".menu-confirm", "Confirm");
        config.addDefault(configPath + ".menu-cancel", "&cCancel");
        config.addDefault(configPath + ".menu-confirm-purchase", "&aConfirm Purchase");
        config.addDefault(configPath + ".menu-cancel-purchase", "&cCancel Purchase");
        config.addDefault(configPath + ".menu-reclaim-skillpoints", "&b&lReclaim Skillpoints");
        config.addDefault(configPath + ".menu-reclaim-skillpoints-lore", "&7Reclaim all your spent skillpoints. \n&cResets all your purchased skills.");
        config.addDefault(configPath + ".menu-reclaim-race-items", "&e&lReclaim Race Items");
        config.addDefault(configPath + ".menu-reclaim-race-items-lore", "&7Reclaim your race items if you lost them.");
        config.addDefault(configPath + ".menu-cost-display", "&7Cost: &a");
        config.addDefault(configPath + ".menu-skilltree-skillpoint-cost", "&7Skillpoint Cost: &a");
        config.addDefault(configPath + ".menu-skilltree-purchase", "&eClick to purchase.");
        config.addDefault(configPath + ".menu-skilltree-unlock", "&cYou must unlock %element% before \n&cpurchasing this upgrade.");
        config.addDefault(configPath + ".menu-level", "&7Level: &3");
        config.addDefault(configPath + ".menu-max-level", "Max Level");
        config.addDefault(configPath + ".menu-experience", "&7Experience: &3");
        config.addDefault(configPath + ".menu-remaining-skillpoints", "&7Remaining Skillpoints: &3");

        // Adding default from version 1.2.1
        config.addDefault(configPath + ".not-enough-exp", "%prefix% &cYou do not have enough experience for this transaction!");

        config.options().copyDefaults(true);
        configManager.getMessageConfig().saveConfig();

        for (String str : config.getConfigurationSection(configPath).getKeys(false))
            messages.put(str, config.getString(configPath + "." + str));
    }

    public static void sendMessage(Player player, String message, String fallback) {
        if (fallback.equals("") || fallback.isEmpty())
            return;

        for (String str : messages.keySet())
            message = message.replace("%" + str + "%", messages.get(str));

        if (message.contains("\n")) {
            for (String msg : message.split("\n")) {
                player.sendMessage(getPlaceholderMessage(player, getMessage(msg, fallback)));
            }

        } else {
            player.sendMessage(getPlaceholderMessage(player, getMessage(message, fallback)));
        }
    }

    public static String getReplacementMessage(String message) {
        message = message.replace("%prefix%", getPrefix());
        message = message.replace("%header%", getHeader());

        return colorMessage(message);
    }

    public static String getPlaceholderMessage(OfflinePlayer player, String message) {
        if (PwingRaces.getInstance().isPlaceholderAPILoaded())
            message = PlaceholderAPI.setPlaceholders(player, message);

        // If PlaceholderAPI isn't installed
        if (player != null && player.isOnline()) {
            message = message.replace("%player_name%", player.getName());

            RacePlayer racePlayer = PwingRaces.getInstance().getRaceManager().getRacePlayer(player);
            if (racePlayer != null && racePlayer.getActiveRace() != null)
                message = message.replace("%race%", racePlayer.getActiveRace().getName());
        }

        return getReplacementMessage(message);
    }

    public static String colorMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getMessage(String messageKey, String fallback) {
        String message = fallback;
        if (messages.containsKey(messageKey))
            message = getMessage(messageKey);

        return colorMessage(message);
    }

    public static String getHeader() {
        return getMessage("header", "&5-------------------&7[ &d&lPwing Races &7]&5-------------------");
    }

    public static String getPrefix() {
        return getMessage("prefix", "&7[Pwing Races]&a");
    }

    public static String getMessage(String str) {
        return messages.get(str);
    }
}
