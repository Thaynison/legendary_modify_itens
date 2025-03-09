package www.legendarycommunity.com.br.legendary_modify_itens.itens;

import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.user.SkillsUser;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class Armageddon implements Listener {

    private final JavaPlugin plugin;
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    public Armageddon(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.PAPER) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;

        String itemDisplayName = ChatColor.stripColor(meta.getDisplayName());
        String expectedName = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', "Item Mundial Armageddon"));

        if (!itemDisplayName.equalsIgnoreCase(expectedName)) return;

        // Verifica cooldown
        UUID playerId = player.getUniqueId();
        if (cooldowns.containsKey(playerId)) {
            long tempoRestante = (cooldowns.get(playerId) - System.currentTimeMillis()) / 1000;
            if (tempoRestante > 0) {
                player.sendMessage(ChatColor.RED + "Aguarde " + tempoRestante + " segundos para usar novamente.");
                return;
            }
        }

        // Verifica se o jogador tem mana suficiente para invocar os zumbis
        AuraSkillsApi auraSkills = AuraSkillsApi.get();
        SkillsUser skillsUser = auraSkills.getUser(player.getUniqueId());
        if (skillsUser == null) return;

        double manaAtual = skillsUser.getMana();
        if (manaAtual < 100) { // Se o jogador não tiver mana suficiente
            player.sendMessage(ChatColor.RED + "Você não tem mana suficiente para invocar os zumbis.");
            return; // Cancela a invocação
        }

        // Consome 100 de mana
        skillsUser.setMana(manaAtual - 100);

        // Define novo cooldown
        cooldowns.put(playerId, System.currentTimeMillis() + (10 * 1000));

        World world = player.getWorld();
        for (int i = 0; i < 10; i++) {
            double x = player.getLocation().getX() + (Math.random() * 10 - 5);
            double y = player.getLocation().getY();
            double z = player.getLocation().getZ() + (Math.random() * 10 - 5);

            world.spawn(player.getLocation().clone().add(x - player.getLocation().getX(), 0, z - player.getLocation().getZ()), Zombie.class);
        }

        player.sendMessage(ChatColor.GREEN + "Você invocou 10 zumbis!");

        new BukkitRunnable() {
            @Override
            public void run() {
                cooldowns.remove(playerId);
            }
        }.runTaskLater(plugin, 200L);
    }
}
