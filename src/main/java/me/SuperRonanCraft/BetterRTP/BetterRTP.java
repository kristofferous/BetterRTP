package me.SuperRonanCraft.BetterRTP;

import lombok.Getter;
import me.SuperRonanCraft.BetterRTP.player.PlayerInfo;
import me.SuperRonanCraft.BetterRTP.player.commands.Commands;
import me.SuperRonanCraft.BetterRTP.player.events.Listener;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTP;
import me.SuperRonanCraft.BetterRTP.references.Permissions;
import me.SuperRonanCraft.BetterRTP.references.Updater;
import me.SuperRonanCraft.BetterRTP.references.database.DatabaseCooldowns;
import me.SuperRonanCraft.BetterRTP.references.depends.DepEconomy;
import me.SuperRonanCraft.BetterRTP.references.file.Files;
import me.SuperRonanCraft.BetterRTP.references.file.Messages;
import me.SuperRonanCraft.BetterRTP.references.invs.RTPInventories;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.CooldownHandler;
import me.SuperRonanCraft.BetterRTP.references.settings.Settings;
import me.SuperRonanCraft.BetterRTP.references.systems.playerdata.PlayerDataManager;
import me.SuperRonanCraft.BetterRTP.references.web.Metrics;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class BetterRTP extends JavaPlugin {
    private final Permissions perms = new Permissions();
    private final Messages text = new Messages();
    private final DepEconomy eco = new DepEconomy();
    private final Commands cmd = new Commands(this);
    private final RTP rtp = new RTP();
    private final Listener listener = new Listener();
    private static BetterRTP instance;
    private final Files files = new Files();
    private final RTPInventories invs = new RTPInventories();
    private final PlayerInfo pInfo = new PlayerInfo();
    @Getter private final PlayerDataManager playerDataManager = new PlayerDataManager();
    private final Settings settings = new Settings();
    @Getter private final DatabaseCooldowns databaseCooldowns = new DatabaseCooldowns();
    @Getter private final CooldownHandler cooldowns = new CooldownHandler();

    public void onEnable() {
        instance = this;
        new Updater(this);
        new Metrics(this);
        loadAll();
        listener.registerEvents(this);
    }

    public void onDisable() {
        invs.closeAll();
    }

    public Files getFiles() {
        return files;
    }

    public static BetterRTP getInstance() {
        return instance;
    }

    @Override
    public boolean onCommand(CommandSender sendi, Command cmd, String label, String[] args) {
        try {
            this.cmd.commandExecuted(sendi, label, args);
        } catch (NullPointerException e) {
            e.printStackTrace();
            text.error(sendi);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return this.cmd.onTabComplete(sender, args);
    }

    public Permissions getPerms() {
        return perms;
    }

    public Messages getText() {
        return text;
    }

    public DepEconomy getEco() {
        return eco;
    }

    public Commands getCmd() {
        return cmd;
    }

    public RTP getRTP() {
        return rtp;
    }

    public Settings getSettings() {
        return settings;
    }

    public void reload(CommandSender sendi) {
        invs.closeAll();
        loadAll();
        text.getReload(sendi);
    }

    public RTPInventories getInvs() {
        return invs;
    }

    public PlayerInfo getpInfo() {
        return pInfo;
    }

    //(Re)Load all plugin systems/files/cache
    private void loadAll() {
        playerDataManager.clear();
        files.loadAll();
        settings.load();
        invs.load();
        rtp.load();
        cooldowns.load();
        cmd.load();
        listener.load();
        eco.load();
        perms.register();
    }

    public static void debug(String str) {
        if (getInstance().getSettings().debug)
            getInstance().getLogger().info(str);
    }
}
