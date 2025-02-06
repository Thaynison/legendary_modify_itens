package www.legendarycommunity.com.br.legendary_modify_itens.itens;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Ouroboros implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        Material itemMaterial = Material.PAPER;
        String itemName = "Item Mundial OuroBoros";

        itemName = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', itemName));
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType() != itemMaterial) continue;

            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                String itemDisplayName = ChatColor.stripColor(meta.getDisplayName());
                if (itemDisplayName.equalsIgnoreCase(itemName)) {
                    // Cancela o evento de dano, tornando o jogador invulnerável
                    event.setCancelled(true);
                    return;
                }
            }
        }
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK &&
                event.getCause() != EntityDamageEvent.DamageCause.FALL &&
                event.getCause() != EntityDamageEvent.DamageCause.MAGIC &&
                event.getCause() != EntityDamageEvent.DamageCause.LAVA &&
                event.getCause() != EntityDamageEvent.DamageCause.DROWNING &&
                event.getCause() != EntityDamageEvent.DamageCause.STARVATION) {
            return;
        }

    }
}
