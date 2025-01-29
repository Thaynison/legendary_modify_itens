package www.legendarycommunity.com.br.legendary_modify_itens.useitens;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import www.legendarycommunity.com.br.legendary_modify_itens.Legendary_modify_itens;

import java.util.Objects;

public class useCapas implements Listener {

    private final Legendary_modify_itens plugin;

    public useCapas(Legendary_modify_itens plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerUsePaperCapas(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || !item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;

        FileConfiguration config = plugin.getItensConfig();

        for (String key : Objects.requireNonNull(config.getConfigurationSection("Capes")).getKeys(false)) {
            String configuredItem = config.getString("Capes." + key + ".item");
            String configuredName = config.getString("Capes." + key + ".nameDisplay");

            if (!item.getType().toString().equalsIgnoreCase(configuredItem)) continue;

            String displayName = ChatColor.stripColor(meta.getDisplayName());
            if (displayName == null || !displayName.equalsIgnoreCase(ChatColor.stripColor(configuredName))) continue;

            // Identificar ação do clique
            if (event.getAction().toString().contains("RIGHT")) {
                String command = config.getString("Capes." + key + ".rightClick");
                if (command != null) {
                    executeCommand(player, command);
                }
            } else if (event.getAction().toString().contains("LEFT")) {
                String command = config.getString("Capes." + key + ".leftClick");
                if (command != null) {
                    executeCommand(player, command);
                }
            }

            break;
        }
    }

    private void executeCommand(Player player, String command) {
        if (command.contains("%player%")) {
            command = command.replace("%player%", player.getName());
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }
}
