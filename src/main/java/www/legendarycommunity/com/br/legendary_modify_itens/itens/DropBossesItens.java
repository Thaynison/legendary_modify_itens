package www.legendarycommunity.com.br.legendary_modify_itens.itens;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DropBossesItens implements Listener {

    private final JavaPlugin plugin;
    private FileConfiguration itensConfig;

    public DropBossesItens(JavaPlugin plugin) {
        this.plugin = plugin;
        loadItensConfig();
    }

    private void loadItensConfig() {
        File file = new File(plugin.getDataFolder(), "itens.yml");

        if (!file.exists()) {
            plugin.getLogger().warning("Arquivo itens.yml não encontrado! Criando um novo...");
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Erro ao criar itens.yml: " + e.getMessage());
            }
        }

        itensConfig = YamlConfiguration.loadConfiguration(file);
    }

    @EventHandler
    public void onBossDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        Player killer = event.getEntity().getKiller();

        if (killer == null || entity.getCustomName() == null) return;

        // Verifica se a seção "Bosses" existe para evitar NullPointerException
        if (itensConfig.getConfigurationSection("Bosses") == null) {
            plugin.getLogger().warning("A seção 'Bosses' não foi encontrada no itens.yml!");
            return;
        }

        for (String key : itensConfig.getConfigurationSection("Bosses").getKeys(false)) {
            String mobType = itensConfig.getString("Bosses." + key + ".Mob");
            String nameDisplay = ChatColor.stripColor(itensConfig.getString("Bosses." + key + ".NameDisplay"));
            String dropMaterial = itensConfig.getString("Bosses." + key + ".DropItem");
            String nameItem = itensConfig.getString("Bosses." + key + ".NameItem");
            String descItem = itensConfig.getString("Bosses." + key + ".DescItem");
            String descItem2 = itensConfig.getString("Bosses." + key + ".DescItem2");

            if (mobType == null || nameDisplay == null || descItem == null || descItem2 == null || dropMaterial == null || nameItem == null) continue;

            // Verifica se é o mob correto e se o nome coincide (ignorando cores)
            if (entity.getType() == EntityType.valueOf(mobType) &&
                    ChatColor.stripColor(entity.getCustomName()).equalsIgnoreCase(nameDisplay)) {

                // Limpa os drops padrão
                event.getDrops().clear();

                // Criar o item a ser dropado
                Material material = Material.getMaterial(dropMaterial.toUpperCase());
                if (material == null) continue;

                ItemStack dropItem = new ItemStack(material);
                ItemMeta meta = dropItem.getItemMeta();

                if (meta != null) {
                    meta.setDisplayName(ChatColor.RED + nameItem); // Renomeia o item para vermelho

                    List<String> arrastarLore = new ArrayList<>();
                    arrastarLore.add("§7- §fInformações do §f[§c" + descItem2 + "§f].");
                    arrastarLore.add("");
                    arrastarLore.add("§aDescrição:");
                    arrastarLore.add("§a❙ §7" + descItem);
                    arrastarLore.add("");
                    arrastarLore.add("§aEconomia:");
                    arrastarLore.add("§a❙ §cProibido a comercialização do item!");
                    arrastarLore.add("");
                    arrastarLore.add("§a(!) Esse §7item §aé §dépico §amais informações acesse nosso site!");

                    meta.setLore(arrastarLore); // Define a lore no item
                    dropItem.setItemMeta(meta);
                }

                // Dropar o item customizado
                entity.getWorld().dropItemNaturally(entity.getLocation(), dropItem);

                return; // Evita processar múltiplos bosses
            }
        }
    }
}
