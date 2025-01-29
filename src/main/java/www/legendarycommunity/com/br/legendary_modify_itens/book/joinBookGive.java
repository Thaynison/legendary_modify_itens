package www.legendarycommunity.com.br.legendary_modify_itens.book;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import www.legendarycommunity.com.br.legendary_modify_itens.Legendary_modify_itens;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class joinBookGive implements Listener {

    private final Legendary_modify_itens plugin;

    public joinBookGive(Legendary_modify_itens plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoinPlayer(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = plugin.getItensConfig();

        if (!config.contains("Books")) return;

        for (String key : Objects.requireNonNull(config.getConfigurationSection("Books")).getKeys(false)) {
            String path = "Books." + key;

            // Verifica se o item é um livro
            if (!config.getString(path + ".item").equalsIgnoreCase("Book")) continue;

            // Cria o livro
            ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
            BookMeta bookMeta = (BookMeta) book.getItemMeta();

            if (bookMeta == null) continue;

            // Define título, autor e páginas
            bookMeta.setTitle(config.getString(path + ".title", "Livro Sem Título"));
            bookMeta.setAuthor(config.getString(path + ".author", "Desconhecido"));
            bookMeta.setDisplayName(config.getString(path + ".nameDisplay", "Livro"));

            List<String> pages = config.getStringList(path + ".pags");
            if (!pages.isEmpty()) {
                for (int i = 0; i < pages.size(); i++) {
                    pages.set(i, pages.get(i).replace("\\n", "\n"));
                }
                bookMeta.setPages(pages);
            } else {
                bookMeta.addPage("Este livro está vazio!");
            }

            // Adiciona a lore fixa
            List<String> lore = new ArrayList<>();
            lore.add("§7- §fInformação da §f[§bLivro§f].");
            lore.add("");
            lore.add("§aDescrição:");
            lore.add("§a❙ §7Usado para informações no servidor");
            lore.add("");
            lore.add("§aEconomia:");
            lore.add("§a❙ §7$ 100");
            lore.add("");
            lore.add("§a(!) Esse §dITEM §aé §7comum §amais informações acesse nosso site!");
            bookMeta.setLore(lore);

            book.setItemMeta(bookMeta);

            // Entrega o livro ao jogador
            player.getInventory().addItem(book);
            break; // Para após entregar um livro
        }
    }

}
