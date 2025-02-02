package www.legendarycommunity.com.br.legendary_modify_itens.regions;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class flyRegionBlock implements Listener {

    private final Plugin plugin;
    private final Set<String> blockedRegions = new HashSet<>();

    public flyRegionBlock(JavaPlugin plugin) {
        this.plugin = plugin;
        loadRegions();
    }

    private void loadRegions() {
        File file = new File(plugin.getDataFolder(), "regions.yml");
        if (!file.exists()) {
            plugin.getLogger().warning("Arquivo regions.yml não encontrado!");
            return;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!config.contains("Regions")) {
            plugin.getLogger().warning("Nenhuma região configurada em regions.yml!");
            return;
        }

        for (String key : config.getConfigurationSection("Regions").getKeys(false)) {
            String regionName = config.getString("Regions." + key);
            if (regionName != null) {
                blockedRegions.add(regionName);
            }
        }

        plugin.getLogger().info("Regiões carregadas: " + blockedRegions);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Se o jogador tiver a permissão de staff, ele pode continuar voando
        if (player.hasPermission("staff.fly.perm")) return;

        if (!player.isFlying()) return; // Se o player não estiver voando, ignorar.

        // Obtendo o RegionContainer corretamente
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet regions = query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));

        boolean isInBlockedRegion = false;

        for (ProtectedRegion region : regions) {
            if (blockedRegions.contains(region.getId())) {
                isInBlockedRegion = true;
                break;
            }
        }

        if (isInBlockedRegion) {
            player.setFlying(false);
            player.setAllowFlight(false);
            player.sendMessage("§cVocê entrou em uma região onde o fly é desativado!");

            // Remover os efeitos de regeneração e resistência
            player.removePotionEffect(PotionEffectType.REGENERATION);
            player.removePotionEffect(PotionEffectType.RESISTANCE);
            player.removePotionEffect(PotionEffectType.STRENGTH);
        }
    }
}
