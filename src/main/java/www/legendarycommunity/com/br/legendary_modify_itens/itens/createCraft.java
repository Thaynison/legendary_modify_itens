package www.legendarycommunity.com.br.legendary_modify_itens.itens;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

public class createCraft {

    private final JavaPlugin plugin;
    private FileConfiguration craftConfig;

    public createCraft(JavaPlugin plugin) {
        this.plugin = plugin;
        carregarcraftConfig();
        registrarCrafts();
    }

    // Carrega o arquivo crafts.yml
    private void carregarcraftConfig() {
        File file = new File(plugin.getDataFolder(), "crafts.yml");
        if (!file.exists()) {
            plugin.saveResource("crafts.yml", false);
        }
        craftConfig = YamlConfiguration.loadConfiguration(file);
    }

    private void registrarCrafts() {
        if (craftConfig == null) return;

        for (String tableKey : craftConfig.getConfigurationSection("CraftTablet").getKeys(false)) {
            for (String itemKey : craftConfig.getConfigurationSection("CraftTablet." + tableKey).getKeys(false)) {
                String path = "CraftTablet." + tableKey + "." + itemKey;

                String[] pattern = craftConfig.getStringList(path + ".Pattern").toArray(new String[0]);

                // Verificar se cada linha do pattern tem entre 1 e 3 caracteres
                if (pattern.length != 3 || !Arrays.stream(pattern).allMatch(line -> line.length() >= 1 && line.length() <= 3)) {
                    plugin.getLogger().warning("Padrão inválido na receita: " + tableKey + "_" + itemKey);
                    continue;
                }

                Map<String, Object> ingredients = craftConfig.getConfigurationSection(path + ".Ingredients").getValues(false);
                String resultMaterial = craftConfig.getString(path + ".Result.Material");
                String resultName = craftConfig.getString(path + ".Result.Rename");

                // Criação do ItemStack resultado
                ItemStack result = new ItemStack(Material.valueOf(resultMaterial));
                ItemMeta resultMeta = result.getItemMeta();
                if (resultMeta != null && resultName != null) {
                    // Adiciona a cor dourada (&6) ao nome do item
                    resultMeta.setDisplayName(ChatColor.GOLD + resultName);
                    result.setItemMeta(resultMeta);
                }

                // Criação da receita com NamespacedKey
                NamespacedKey key = new NamespacedKey(plugin, tableKey + "_" + itemKey);
                ShapedRecipe recipe = new ShapedRecipe(key, result);
                recipe.shape(pattern);

                // Adiciona os ingredientes à receita
                for (String ingredientKey : ingredients.keySet()) {
                    Map<String, Object> ingredientData = craftConfig.getConfigurationSection(path + ".Ingredients." + ingredientKey).getValues(false);
                    String material = (String) ingredientData.get("Material");
                    String rename = (String) ingredientData.get("Rename");

                    ItemStack ingredient = new ItemStack(Material.valueOf(material));
                    if (rename != null) {
                        ItemMeta meta = ingredient.getItemMeta();
                        if (meta != null) {
                            meta.setDisplayName(rename);
                            ingredient.setItemMeta(meta);
                        }
                    }

                    recipe.setIngredient(ingredientKey.charAt(0), ingredient.getType());
                }

                // Registro da receita
                Bukkit.addRecipe(recipe);
            }
        }
    }



}
