package www.legendarycommunity.com.br.legendary_modify_itens.useitens;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import www.legendarycommunity.com.br.legendary_modify_itens.Legendary_modify_itens;

import java.util.List;
import java.util.Objects;

public class useMoneyPaper implements Listener {

    private final Legendary_modify_itens plugin;
    private static Economy economy = null;

    public useMoneyPaper(Legendary_modify_itens plugin) {
        this.plugin = plugin;
        setupEconomy();
    }

    private void setupEconomy() {
        if (economy == null) {
            economy = Bukkit.getServer().getServicesManager().getRegistration(Economy.class).getProvider();
        }
    }

    @EventHandler
    public void onPlayerUsePaper(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || !item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName() || !meta.hasLore()) return;

        FileConfiguration config = plugin.getItensConfig();
        boolean matched = false;

        for (String key : Objects.requireNonNull(config.getConfigurationSection("Itens")).getKeys(false)) {
            String configuredItem = config.getString("Itens." + key + ".item");
            String configuredName = config.getString("Itens." + key + ".nameDisplay");
            String configuredLore = config.getString("Itens." + key + ".loreDisplay");
            double value = config.getDouble("Itens." + key + ".valueGive");

            // Verifica o material
            if (!item.getType().toString().equalsIgnoreCase(configuredItem)) continue;

            // Verifica se o nome contém a parte relevante, ignorando cores
            String displayName = ChatColor.stripColor(meta.getDisplayName());
            if (displayName == null || !displayName.contains(ChatColor.stripColor(configuredName))) continue;

            // Verifica se alguma linha da lore contém a parte relevante, ignorando cores
            List<String> lores = meta.getLore();
            boolean loreMatches = lores.stream()
                    .map(ChatColor::stripColor)
                    .anyMatch(lore -> lore.contains(ChatColor.stripColor(configuredLore)));

            if (!loreMatches) continue;

            // Dá o valor configurado ao jogador e remove o item
            economy.depositPlayer(player, value);
            // player.sendMessage(ChatColor.GREEN + "Você recebeu $" + value + "!");
            item.setAmount(item.getAmount() - 1);
            matched = true;
            break;
        }

        if (!matched) {

        }
    }

}
