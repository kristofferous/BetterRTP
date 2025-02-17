package me.SuperRonanCraft.BetterRTPAddons;

import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.commands.RTPCommand;
import me.SuperRonanCraft.BetterRTPAddons.addons.commands.AddonCommands;
import me.SuperRonanCraft.BetterRTPAddons.addons.extraEffects.AddonExtraEffects;
import me.SuperRonanCraft.BetterRTPAddons.addons.flashback.AddonFlashback;
import me.SuperRonanCraft.BetterRTPAddons.addons.logger.AddonLogger;
import me.SuperRonanCraft.BetterRTPAddons.addons.magicStick.AddonMagicStick;
import me.SuperRonanCraft.BetterRTPAddons.addons.portals.AddonPortals;

import java.util.ArrayList;
import java.util.List;

public class AddonsHandler {

    public List<Addons> addons = new ArrayList<>();
    AddonsCommand cmd = new AddonsCommand();

    public void load() {
        unload();
        //int addonCount = 0;
        for (Addons addon : Addons.values())
            if (addon.isEnabled()) {
                addon.load();
                addons.add(addon);
                //addonCount++;
            }
        //Main.getInstance().getLogger().log(Level.INFO, addonCount + "/" + Addons.values().length + " addons were enabled!");
        BetterRTP.getInstance().getCmd().registerCommand(cmd, false);
    }

    public void unload() {
        for (Addons addon : addons)
            addon.disable();
        addons.clear();
    }

    public enum Addons {
        LOGGER(new AddonLogger()),              //Does this thing work?
        FLASH_BACK(new AddonFlashback()),       //Never get lost adventuring
        PORTALS(new AddonPortals()),            //Fancy walk-in portals
        //INTERFACES(new AddonInterface())
        MAGICSTICK(new AddonMagicStick()),      //Handy teleport want
        EXTRAEFFECTS(new AddonExtraEffects()),  //New cosmetica!
        COMMANDS(new AddonCommands()),          //Commands on rtp events
        PARTY_RTP(new me.SuperRonanCraft.BetterRTPAddons.addons.partyrtp.AddonParty()),
        ;

        Addon addon;

        Addons(Addon addon) {
            this.addon = addon;
        }

        public boolean isEnabled() {
            return addon.isEnabled();
        }

        void load() {
            addon.load();
            //Main.getInstance().getLogger().log(Level.INFO, "Addon " + this.name() + " has been enabled!");
        }

        void disable() {
            addon.unload();
        }

        RTPCommand getCmd() {
            return addon.getCmd();
        }
    }

}
