package me.SuperRonanCraft.BetterRTP.references.invs.enums;

import me.SuperRonanCraft.BetterRTP.references.invs.RTP_INV_SETTINGS;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.PlayerInfo;
import me.SuperRonanCraft.BetterRTP.references.systems.HelperPlayer;
import me.SuperRonanCraft.BetterRTP.references.systems.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public interface RTPInventory_Defaults {

    void show(Player p);

    void clickEvent(InventoryClickEvent event);

    default ItemStack createItem(String item, int amount, String name, List<String> lore) {
        Material mat = Material.valueOf(item.toUpperCase());
        ItemStack _stack = new ItemStack(mat, amount);
        ItemMeta _meta = _stack.getItemMeta();
        if (_meta != null) {
            if (lore != null)
                _meta.setLore(lore);
            if (name != null)
                _meta.setDisplayName(BetterRTP.getInstance().getText().color(name));
        }
        _stack.setItemMeta(_meta);
        return _stack;
    }

    default void cacheInv(Player p, Inventory inv, RTP_INV_SETTINGS type) {
        PlayerData info = HelperPlayer.getData(p);
        info.getMenu().setInv(inv);
        info.getMenu().setInvType(type);
    }

    default Inventory createInv(int size, String title) {
        title = BetterRTP.getInstance().getText().color(title);
        return Bukkit.createInventory(null, size, title);
    }
}
