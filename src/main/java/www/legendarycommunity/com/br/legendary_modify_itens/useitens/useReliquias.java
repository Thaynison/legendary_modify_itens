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

public class useReliquias implements Listener {

    private final Legendary_modify_itens plugin;

    public useReliquias(Legendary_modify_itens plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onReliquias(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        // Verifica se o item tem meta válida
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName() || !meta.hasLore()) return;

        FileConfiguration config = plugin.getItensConfig();

        for (String key : config.getConfigurationSection("Reliquias").getKeys(false)) {
            String configuredItem = config.getString("Reliquias." + key + ".item");
            String configuredName = config.getString("Reliquias." + key + ".nameDisplay");
            String configuredCommand = config.getString("Reliquias." + key + ".consoleCommand");

            // Verifica se o item corresponde ao configurado
            if (!item.getType().toString().equalsIgnoreCase(configuredItem)) continue;

            String displayName = ChatColor.stripColor(meta.getDisplayName());
            if (displayName == null || !displayName.contains(ChatColor.stripColor(configuredName))) continue;

            // Executa o comando no console, substituindo %player% pelo nome do jogador
            String finalCommand = configuredCommand.replace("%player%", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand);

            // Remove 1 item da mão do jogador
            item.setAmount(item.getAmount() - 1);

            break; // Para o loop, já que o item foi processado
        }
    }
}

