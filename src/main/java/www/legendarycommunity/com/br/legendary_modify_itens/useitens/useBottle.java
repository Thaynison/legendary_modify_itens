package www.legendarycommunity.com.br.legendary_modify_itens.useitens;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import www.legendarycommunity.com.br.legendary_modify_itens.Legendary_modify_itens;

public class useBottle implements Listener {

    private final Legendary_modify_itens plugin;

    public useBottle(Legendary_modify_itens plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBottle(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || !item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName() || !meta.hasLore()) return;

        FileConfiguration config = plugin.getItensConfig();

        for (String key : config.getConfigurationSection("Bottles").getKeys(false)) {
            String configuredItem = config.getString("Bottles." + key + ".item");
            String configuredName = config.getString("Bottles." + key + ".nameDisplay");
            String configuredEffect = config.getString("Bottles." + key + ".effectName");
            String configuredWorld = config.getString("Bottles." + key + ".world");
            int time = config.getInt("Bottles." + key + ".effectTime") * 20; // Convertendo para ticks
            int amplifier = config.getInt("Bottles." + key + ".effectAmplifier");

            if (!item.getType().toString().equalsIgnoreCase(configuredItem)) continue;

            String displayName = ChatColor.stripColor(meta.getDisplayName());
            if (displayName == null || !displayName.contains(ChatColor.stripColor(configuredName))) continue;

            // Verificar se o jogador está no mundo configurado
            World playerWorld = player.getWorld();
            if (!playerWorld.getName().equalsIgnoreCase(configuredWorld)) {
                                return;
            }

            // Aplicar o efeito ao jogador
            PotionEffectType effectType = PotionEffectType.getByName(configuredEffect);
            if (effectType != null) {
                player.addPotionEffect(new PotionEffect(effectType, time, amplifier));
            } else {
                                return;
            }

            item.setAmount(item.getAmount() - 1);
            break;
        }
    }
}
