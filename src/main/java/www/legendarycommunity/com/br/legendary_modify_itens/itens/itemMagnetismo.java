package www.legendarycommunity.com.br.legendary_modify_itens.itens;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;

public class itemMagnetismo {

    private final JavaPlugin plugin;
    private FileConfiguration magnetismoConfig;

    public itemMagnetismo(JavaPlugin plugin) {
        this.plugin = plugin;
        carregarMagnetismoConfig();
    }

    // Carrega o arquivo magnetismo.yml
    private void carregarMagnetismoConfig() {
        File file = new File(plugin.getDataFolder(), "magnetismo.yml");
        if (!file.exists()) {
            plugin.saveResource("magnetismo.yml", false);
        }
        magnetismoConfig = YamlConfiguration.loadConfiguration(file);
    }

    // Inicia a verificação a cada segundo
    public void startMagnetismCheck() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Verifica todos os jogadores no servidor
                for (Player player : Bukkit.getOnlinePlayers()) {
                    // Verifica o item de magnetismo que o jogador possui
                    for (String chave : magnetismoConfig.getConfigurationSection("Itens").getKeys(false)) {
                        String nomePapel = magnetismoConfig.getString("Itens." + chave + ".name");
                        String item = magnetismoConfig.getString("Itens." + chave + ".item");
                        int raio = magnetismoConfig.getInt("Itens." + chave + ".raio");

                        if (temPapelComNome(player, nomePapel, item)) {
                            puxarItens(player, raio);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // 20L = 1 segundo
    }

    // Verifica se o jogador tem um papel com o nome exato
    private boolean temPapelComNome(Player player, String nomePapel, String item) {
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null && itemStack.hasItemMeta()) {
                ItemMeta meta = itemStack.getItemMeta();
                if (meta != null && meta.hasDisplayName()) {
                    // Remove as cores do nome do item
                    String displayName = ChatColor.stripColor(meta.getDisplayName());
                    if (displayName.equalsIgnoreCase(ChatColor.stripColor(nomePapel)) && itemStack.getType().toString().equalsIgnoreCase(item)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Puxa todos os itens ao redor do jogador dentro de um raio especificado
    private void puxarItens(Player player, int raio) {
        player.getWorld().getNearbyEntities(player.getLocation(), raio, raio, raio).forEach(entity -> {
            if (entity instanceof org.bukkit.entity.Item) {
                org.bukkit.entity.Item itemEntity = (org.bukkit.entity.Item) entity;

                // Verifica se o item está dentro do raio do jogador
                if (player.getLocation().distance(itemEntity.getLocation()) <= raio) {
                    ItemStack itemStack = itemEntity.getItemStack();

                    // Verifica se o inventário do jogador está cheio
                    if (player.getInventory().firstEmpty() == -1) {
                        // Inventário cheio, deixa o item no chão perto do jogador
                        player.getWorld().dropItem(player.getLocation(), itemStack);
                    } else {
                        // Adiciona o item ao inventário
                        player.getInventory().addItem(itemStack);
                    }

                    // Remove o item do mundo
                    itemEntity.remove();
                }
            }
        });
    }
}
