package www.legendarycommunity.com.br.legendary_modify_itens.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.Map;

public class BlockBreakListener implements Listener {

    private final JavaPlugin plugin;
    private final FileConfiguration oresConfig;

    public BlockBreakListener(JavaPlugin plugin) {
        this.plugin = plugin;

        // Carregar o arquivo ores.yml
        File file = new File(plugin.getDataFolder(), "ores.yml");
        if (!file.exists()) {
            plugin.saveResource("ores.yml", false);
        }
        oresConfig = YamlConfiguration.loadConfiguration(file);

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material blockType = event.getBlock().getType();
        ItemStack tool = player.getInventory().getItemInMainHand();

        // Verificar configuração
        for (String key : oresConfig.getConfigurationSection("Ores").getKeys(false)) {
            String configuredBlock = oresConfig.getString("Ores." + key + ".Minerio");
            if (configuredBlock != null && blockType == Material.valueOf(configuredBlock)) {

                // Obter drops configurados
                List<?> itemDrops = oresConfig.getList("Ores." + key + ".itemdrop");
                if (itemDrops != null) {
                    event.setDropItems(false); // Impedir drops padrões

                    for (Object item : itemDrops) {
                        if (item instanceof String) {
                            dropItem(event, Material.valueOf((String) item), tool, player);
                        } else if (item instanceof Map) {
                            Map<?, ?> itemMap = (Map<?, ?>) item;
                            String materialName = (String) itemMap.keySet().toArray()[0];
                            Material material = Material.valueOf(materialName);

                            Map<?, ?> attributes = (Map<?, ?>) itemMap.get(materialName);
                            String itemNameModify = (String) attributes.get("itemNameModify");

                            ItemStack customItem = new ItemStack(material);
                            if (itemNameModify != null) {
                                customItem.getItemMeta().setDisplayName(ChatColor.translateAlternateColorCodes('&', itemNameModify));
                            }

                            dropItem(event, customItem, tool, player);
                        }
                    }
                }
                break;
            }
        }
    }

    private void dropItem(BlockBreakEvent event, Material material, ItemStack tool, Player player) {
        ItemStack drop = new ItemStack(material);
        applyFortune(drop, tool);
        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
    }

    private void dropItem(BlockBreakEvent event, ItemStack itemStack, ItemStack tool, Player player) {
        applyFortune(itemStack, tool);
        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), itemStack);
    }

    private void applyFortune(ItemStack itemStack, ItemStack tool) {
        int fortuneLevel = tool.getEnchantmentLevel(Enchantment.FORTUNE);
        if (fortuneLevel > 0) {
            int extraAmount = (int) (Math.random() * (fortuneLevel + 1));
            itemStack.setAmount(itemStack.getAmount() + extraAmount);
        }
    }
}
