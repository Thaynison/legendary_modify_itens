package www.legendarycommunity.com.br.legendary_modify_itens.itens;

import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.skill.Skills;
import dev.aurelium.auraskills.api.stat.Stats;
import dev.aurelium.auraskills.api.user.SkillsUser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Random;

public class CryingBreakListener implements Listener {

    private final JavaPlugin plugin;
    private final Random random = new Random();

    public CryingBreakListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material brokenBlock = event.getBlock().getType();

        if (Bukkit.getPluginManager().getPlugin("AuraSkills") == null) {
            return;
        }
        AuraSkillsApi auraSkills = AuraSkillsApi.get();

        // Carregar ores.yml corretamente
        File file = new File(plugin.getDataFolder(), "ores.yml");
        if (!file.exists()) return;

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        String path = "Nidavellir.FragmentoStar";
        if (config.contains(path + ".Block")) {
            Material configuredBlock = Material.matchMaterial(config.getString(path + ".Block"));
            if (configuredBlock == brokenBlock) {
                event.setDropItems(false);

                // Obtém o nível da skill LUCK do jogador
                SkillsUser skillsUser = auraSkills.getUserManager().getUser(player.getUniqueId());
                if (skillsUser == null) return;

                double luckLevel = skillsUser.getStatLevel(Stats.LUCK);

                // Aumenta a chance com base na skill LUCK
                double baseChance = 0.0001; // Chance base de 0,01%
                double luckBonus = luckLevel * 0.002; // Cada nível de LUCK aumenta a chance em 0,02%
                double finalChance = Math.min(baseChance + luckBonus, 0.005); // Limita a chance máxima a 5%

                if (random.nextDouble() > finalChance) return;

                // Obtém o item drop configurado
                String itemDropPath = path + ".itemdrop";
                if (config.contains(itemDropPath)) {
                    for (String key : config.getConfigurationSection(itemDropPath).getKeys(false)) {
                        Material dropMaterial = Material.matchMaterial(key);
                        if (dropMaterial != null) {
                            ItemStack fragmento = new ItemStack(dropMaterial, 1);
                            ItemMeta meta = fragmento.getItemMeta();
                            if (meta != null && config.contains(itemDropPath + "." + key + ".itemNameModify")) {
                                String displayName = config.getString(itemDropPath + "." + key + ".itemNameModify")
                                        .replace("&", "§");
                                meta.setDisplayName(displayName);
                                fragmento.setItemMeta(meta);
                            }

                            // Adiciona ao inventário do jogador, se possível
                            if (player.getInventory().firstEmpty() != -1) {
                                player.getInventory().addItem(fragmento);
                            }
                        }
                    }
                }
            }
        }
    }
}