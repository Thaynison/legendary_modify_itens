package www.legendarycommunity.com.br.legendary_modify_itens.itens;

import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.user.SkillsUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Ouroboros implements Listener {

    private final JavaPlugin plugin;

    public Ouroboros(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        Material itemMaterial = Material.PAPER;
        String itemName = "Item Mundial OuroBoros";

        itemName = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', itemName));
        boolean hasItem = false;

        // Verifica se o jogador possui o item
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType() != itemMaterial) continue;

            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                String itemDisplayName = ChatColor.stripColor(meta.getDisplayName());
                if (itemDisplayName.equalsIgnoreCase(itemName)) {
                    hasItem = true;
                    break;
                }
            }
        }

        if (hasItem) {
            // Verifica se o jogador possui mana suficiente para cancelar o dano
            if (Bukkit.getPluginManager().getPlugin("AuraSkills") == null) {
                return;
            }
            AuraSkillsApi auraSkills = AuraSkillsApi.get();
            SkillsUser skillsUser = auraSkills.getUser(player.getUniqueId());
            if (skillsUser == null) {
                return;
            }

            double manaAtual = skillsUser.getMana();
            if (manaAtual >= 100) { // Verifica se o jogador tem mana suficiente
                event.setCancelled(true); // Cancela o dano
                // Inicia o consumo de mana a cada 5 segundos
                startManaConsumption(player, skillsUser);
            } else {
                event.setCancelled(false); // Permite o dano caso o jogador não tenha mana suficiente
            }
        }
    }

    private void startManaConsumption(Player player, SkillsUser skillsUser) {
        new BukkitRunnable() {
            @Override
            public void run() {
                double manaAtual = skillsUser.getMana();
                if (manaAtual < 100) { // Se o jogador ficar sem mana, o agendador é cancelado
                    cancel();
                    return;
                }
                skillsUser.setMana(manaAtual - 100); // Consome 100 de mana
            }
        }.runTaskTimer(plugin, 0L, 100L * 5); // Passando o plugin corretamente
    }
}
