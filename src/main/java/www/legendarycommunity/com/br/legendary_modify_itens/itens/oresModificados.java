package www.legendarycommunity.com.br.legendary_modify_itens.itens;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class oresModificados implements Listener {

    private final JavaPlugin plugin;
    private FileConfiguration oresConfig;
    private final Map<Material, OreData> oresMap = new HashMap<>();
    private final Random random = new Random();

    public oresModificados(JavaPlugin plugin) {
        this.plugin = plugin;
        carregaroresConfig();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private void carregaroresConfig() {
        File file = new File(plugin.getDataFolder(), "ores.yml");
        if (!file.exists()) {
            plugin.saveResource("ores.yml", false);
        }
        oresConfig = YamlConfiguration.loadConfiguration(file);

        for (String key : oresConfig.getConfigurationSection("Ores").getKeys(false)) {
            String path = "Ores." + key + ".";
            Material minerio = Material.getMaterial(oresConfig.getString(path + "Minerio").toUpperCase());
            Material itemdrop = Material.getMaterial(oresConfig.getString(path + "itemdrop").toUpperCase());
            String itemNameModify = oresConfig.getString(path + "itemNameModify");

            if (minerio != null && itemdrop != null) {
                oresMap.put(minerio, new OreData(itemdrop, itemNameModify));
            } else {
                plugin.getLogger().warning("Configuração inválida para " + key + " em ores.yml!");
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material brokenBlock = event.getBlock().getType();

        if (oresMap.containsKey(brokenBlock)) {
            OreData oreData = oresMap.get(brokenBlock);

            // Obter o nível do encantamento Fortuna
            ItemStack tool = player.getInventory().getItemInMainHand();
            int fortuneLevel = tool.getEnchantmentLevel(Enchantment.FORTUNE);

            // Calcular a quantidade de drops com base na Fortuna
            int dropQuantity = calculateFortuneDrops(fortuneLevel);

            // Dropar itens padrão
            for (int i = 0; i < dropQuantity; i++) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(oreData.getItemDrop()));
            }

            // Dropar itens com nome modificado
            for (int i = 0; i < dropQuantity; i++) {
                ItemStack customItem = new ItemStack(oreData.getItemDrop());
                ItemMeta meta = customItem.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(oreData.getItemNameModify());
                    customItem.setItemMeta(meta);
                }
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), customItem);
            }

            // Cancelar o drop padrão do bloco
            event.setDropItems(false);
        }
    }

    private int calculateFortuneDrops(int fortuneLevel) {
        if (fortuneLevel <= 0) {
            return 1; // Sem Fortuna, apenas 1 item
        }

        // Fortuna aumenta a quantidade com base no nível
        int maxDrops = fortuneLevel + 1; // Ex.: Fortuna I = 2, Fortuna II = 3, etc.
        return random.nextInt(maxDrops) + 1; // Número aleatório entre 1 e maxDrops
    }

    private static class OreData {
        private final Material itemDrop;
        private final String itemNameModify;

        public OreData(Material itemDrop, String itemNameModify) {
            this.itemDrop = itemDrop;
            this.itemNameModify = itemNameModify;
        }

        public Material getItemDrop() {
            return itemDrop;
        }

        public String getItemNameModify() {
            return itemNameModify;
        }
    }
}
