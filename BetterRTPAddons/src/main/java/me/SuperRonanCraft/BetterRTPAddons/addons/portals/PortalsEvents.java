package me.SuperRonanCraft.BetterRTPAddons.addons.portals;

import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTP_TYPE;
import me.SuperRonanCraft.BetterRTP.references.customEvents.RTP_CancelledEvent;
import me.SuperRonanCraft.BetterRTP.references.customEvents.RTP_TeleportPostEvent;
import me.SuperRonanCraft.BetterRTPAddons.Main;
import me.SuperRonanCraft.BetterRTPAddons.addons.portals.region.PortalsRegionInfo;
import me.SuperRonanCraft.BetterRTPAddons.util.Files;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

public class PortalsEvents implements Listener {

    AddonPortals addonPortals;
    private final HashMap<Player, PortalsRegionInfo> playerPortaling = new HashMap<>();
    private boolean ignoreDelay, ignoreCooldown;

    PortalsEvents(AddonPortals addonPortals) {
        this.addonPortals = addonPortals;
    }

    public void load() {
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
        ignoreDelay = Main.getInstance().getFiles().getType(Files.FILETYPE.CONFIG).getBoolean("Portals.IgnoreDelay");
        ignoreCooldown = Main.getInstance().getFiles().getType(Files.FILETYPE.CONFIG).getBoolean("Portals.IgnoreCooldown");
    }

    public void unregiter() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler (priority = EventPriority.MONITOR)
    void move(PlayerMoveEvent e) {
        if (e.getTo() != null)
            if (e.getFrom().getBlockX() != e.getTo().getBlockX()
                    || e.getFrom().getBlockY() != e.getTo().getBlockY()
                    || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
                if (playerPortaling.containsKey(e.getPlayer()))
                    return;
                for (PortalsRegionInfo portal : addonPortals.getPortals().getRegisteredPortals()) {
                    Location loc1 = portal.getLoc1(), loc2 = portal.getLoc2(), ploc = e.getTo();
                    assert ploc != null;

                    if (ploc.getBlockX() <= Math.max(loc1.getBlockX(), loc2.getBlockX())
                            && ploc.getBlockX() >= Math.min(loc1.getBlockX(), loc2.getBlockX()))
                    if (ploc.getBlockZ() <= Math.max(loc1.getBlockZ(), loc2.getBlockZ())
                            && ploc.getBlockZ() >= Math.min(loc1.getBlockZ(), loc2.getBlockZ()))
                    if (ploc.getBlockY() <= Math.max(loc1.getBlockY(), loc2.getBlockY())
                            && ploc.getBlockY() >= Math.min(loc1.getBlockY(), loc2.getBlockY())) {
                        playerPortaling.put(e.getPlayer(), portal);
                        BetterRTP.getInstance().getCmd().tp(e.getPlayer(), e.getPlayer(),
                                portal.getWorld(), null, RTP_TYPE.ADDON_PORTAL, ignoreCooldown, ignoreDelay);
                        return;
                    }
                }
            }
    }

    @EventHandler
    void teleport(RTP_TeleportPostEvent e) {
        playerPortaling.remove(e.getPlayer());
    }

    @EventHandler
    void cancelled(RTP_CancelledEvent e) {
        playerPortaling.remove(e.getPlayer());
    }
}
