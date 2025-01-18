package www.legendarycommunity.com.br.legendary_modify_itens;

import org.bukkit.plugin.java.JavaPlugin;
import www.legendarycommunity.com.br.legendary_modify_itens.itens.itemMagnetismo;
import www.legendarycommunity.com.br.legendary_modify_itens.itens.oresModificados;

public final class Legendary_modify_itens extends JavaPlugin {

    private itemMagnetismo itemMagnetismo;

    @Override
    public void onEnable() {
        itemMagnetismo = new itemMagnetismo(this);
        itemMagnetismo.startMagnetismCheck();
        new oresModificados(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
