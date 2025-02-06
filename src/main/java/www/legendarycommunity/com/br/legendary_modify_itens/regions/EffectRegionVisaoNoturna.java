package www.legendarycommunity.com.br.legendary_modify_itens.regions;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class EffectRegionVisaoNoturna implements Listener {

    private final Plugin plugin;
    private final Set<String> nightVisionPlayers = new HashSet<>();
    private final Set<String> blockedRegions = new HashSet<>();

    public EffectRegionVisaoNoturna(Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
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

        for (String regionName : blockedRegions) {
            if (isPlayerInRegion(player, regionName)) {
                if (!nightVisionPlayers.contains(player.getName())) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
                    //player.sendMessage(ChatColor.GREEN + "Você recebeu visão noturna nesta região!");
                    nightVisionPlayers.add(player.getName());
                }
                return;
            }
        }

        // Remover o efeito se o jogador sair das regiões
        if (nightVisionPlayers.contains(player.getName())) {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            //player.sendMessage(ChatColor.RED + "Você saiu da região e perdeu a visão noturna.");
            nightVisionPlayers.remove(player.getName());
        }
    }

    private boolean isPlayerInRegion(Player player, String regionName) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet regions = query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));

        for (ProtectedRegion region : regions) {
            if (region.getId().equalsIgnoreCase(regionName)) {
                return true;
            }
        }
        return false;
    }
}
