package www.legendarycommunity.com.br.legendary_modify_itens;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import www.legendarycommunity.com.br.legendary_modify_itens.book.joinBookGive;
import www.legendarycommunity.com.br.legendary_modify_itens.guis.emotesGui;
import www.legendarycommunity.com.br.legendary_modify_itens.itens.*;
import www.legendarycommunity.com.br.legendary_modify_itens.regions.flyRegionBlock;
import www.legendarycommunity.com.br.legendary_modify_itens.useitens.useBottle;
import www.legendarycommunity.com.br.legendary_modify_itens.useitens.useCapas;
import www.legendarycommunity.com.br.legendary_modify_itens.useitens.useMoneyPaper;
import www.legendarycommunity.com.br.legendary_modify_itens.useitens.useReliquias;

import java.io.File;

public final class Legendary_modify_itens extends JavaPlugin {

    private itemMagnetismo itemMagnetismo;
    private File itensFile;
    private FileConfiguration itensConfig;
    private File RegionsFile;
    private FileConfiguration RegionsConfig;

    @Override
    public void onEnable() {
        saveDefaultItensConfig();
        saveDefaultRegionsConfig();

        itemMagnetismo = new itemMagnetismo(this);
        itemMagnetismo.startMagnetismCheck();
        new BlockBreakListener(this);

        getServer().getPluginManager().registerEvents(new useMoneyPaper(this), this);
        getServer().getPluginManager().registerEvents(new useBottle(this), this);
        getServer().getPluginManager().registerEvents(new useReliquias(this), this);
        getServer().getPluginManager().registerEvents(new useCapas(this), this);
        getServer().getPluginManager().registerEvents(new joinBookGive(this), this);
        getServer().getPluginManager().registerEvents(new flyRegionBlock(this), this);
        getServer().getPluginManager().registerEvents(new CryingBreakListener(this), this);

        this.getCommand("emotes").setExecutor(new emotesGui());
        getServer().getPluginManager().registerEvents(new emotesGui(), this);

        getLogger().info("Legendary Modify Items foi habilitado com sucesso!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Legendary Modify Items foi desativado.");
    }

    public FileConfiguration getItensConfig() {
        return itensConfig;
    }

    private void saveDefaultItensConfig() {
        itensFile = new File(getDataFolder(), "itens.yml");
        if (!itensFile.exists()) {
            saveResource("itens.yml", false);
        }
        itensConfig = YamlConfiguration.loadConfiguration(itensFile);
    }

    private void saveDefaultRegionsConfig() {
        RegionsFile = new File(getDataFolder(), "regions.yml");
        if (!RegionsFile.exists()) {
            saveResource("regions.yml", false);
        }
        RegionsConfig = YamlConfiguration.loadConfiguration(RegionsFile);
    }

}
