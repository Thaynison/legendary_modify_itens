package www.legendarycommunity.com.br.legendary_modify_itens.guis;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class emotesGui implements CommandExecutor, Listener {


    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("emotes")) {

            if (sender instanceof Player) {
                Player player = (Player) sender;

                Inventory EmotesGUI = Bukkit.createInventory(null, 27, ChatColor.RED + "Legendary Emotes");

                EmotesGUI.setItem(0, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1));
                EmotesGUI.setItem(1, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1));
                EmotesGUI.setItem(2, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1));
                EmotesGUI.setItem(3, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1));
                EmotesGUI.setItem(4, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1));
                EmotesGUI.setItem(5, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1));
                EmotesGUI.setItem(6, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1));
                EmotesGUI.setItem(7, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1));
                EmotesGUI.setItem(8, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1));
                EmotesGUI.setItem(9, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1));

                ItemStack sentarBlock = new ItemStack(Material.PAPER);
                ItemMeta sentarMeta = sentarBlock.getItemMeta();
                sentarMeta.setDisplayName(ChatColor.YELLOW + "Sentar no Bloco");
                List<String> sentarLore = new ArrayList<>();
                sentarLore.add("§7- §fInformação do §f[§bEmote§f].");
                sentarLore.add("");
                sentarLore.add("§aDescrição:");
                sentarLore.add("§a❙ §7Esquerdo (Botão de bater) - §bSentar§7.");
                sentarLore.add("§a❙ §7Direito (Botão de usar) - §cLevantar§7.");
                sentarLore.add("");
                sentarLore.add("§a(!) Esse §eEmote §aé §7comum §amais informações acesse nosso site!");
                sentarMeta.setLore(sentarLore);
                sentarBlock.setItemMeta(sentarMeta);
                EmotesGUI.setItem(10, sentarBlock);

                EmotesGUI.setItem(11, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1));

                ItemStack deitarBlock = new ItemStack(Material.PAPER);
                ItemMeta deitarMeta = deitarBlock.getItemMeta();
                deitarMeta.setDisplayName(ChatColor.YELLOW + "Deitar no Bloco");
                List<String> deitarLore = new ArrayList<>();
                deitarLore.add("§7- §fInformação do §f[§bEmote§f].");
                deitarLore.add("");
                deitarLore.add("§aDescrição:");
                deitarLore.add("§a❙ §7Esquerdo (Botão de bater) - §bDeitar§7.");
                deitarLore.add("§a❙ §7Direito (Botão de usar) - §cLevantar§7.");
                deitarLore.add("");
                deitarLore.add("§a(!) Esse §eEmote §aé §7comum §amais informações acesse nosso site!");
                deitarMeta.setLore(deitarLore);
                deitarBlock.setItemMeta(deitarMeta);
                EmotesGUI.setItem(12, deitarBlock);

                EmotesGUI.setItem(13, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1));

                ItemStack barrigaBlock = new ItemStack(Material.PAPER);
                ItemMeta barrigaMeta = barrigaBlock.getItemMeta();
                barrigaMeta.setDisplayName(ChatColor.YELLOW + "Barriga no Bloco");
                List<String> barrigaLore = new ArrayList<>();
                barrigaLore.add("§7- §fInformação do §f[§bEmote§f].");
                barrigaLore.add("");
                barrigaLore.add("§aDescrição:");
                barrigaLore.add("§a❙ §7Esquerdo (Botão de bater) - §bDe Barriga para baixo§7.");
                barrigaLore.add("§a❙ §7Direito (Botão de usar) - §cLevantar§7.");
                barrigaLore.add("");
                barrigaLore.add("§a(!) Esse §eEmote §aé §7comum §amais informações acesse nosso site!");
                barrigaMeta.setLore(barrigaLore);
                barrigaBlock.setItemMeta(barrigaMeta);
                EmotesGUI.setItem(14, barrigaBlock);

                EmotesGUI.setItem(15, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1));

                ItemStack arrastarBlock = new ItemStack(Material.PAPER);
                ItemMeta arrastarMeta = arrastarBlock.getItemMeta();
                arrastarMeta.setDisplayName(ChatColor.YELLOW + "Se Arrastar no Bloco");
                List<String> arrastarLore = new ArrayList<>();
                arrastarLore.add("§7- §fInformação do §f[§bEmote§f].");
                arrastarLore.add("");
                arrastarLore.add("§aDescrição:");
                arrastarLore.add("§a❙ §7Esquerdo (Botão de bater) - §bSe arrastar§7.");
                arrastarLore.add("§a❙ §7Direito (Botão de usar) - §cLevantar§7.");
                arrastarLore.add("");
                arrastarLore.add("§a(!) Esse §eEmote §aé §7comum §amais informações acesse nosso site!");
                arrastarMeta.setLore(arrastarLore);
                arrastarBlock.setItemMeta(arrastarMeta);
                EmotesGUI.setItem(16, arrastarBlock);

                EmotesGUI.setItem(17, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1));
                EmotesGUI.setItem(18, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1));
                EmotesGUI.setItem(19, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1));
                EmotesGUI.setItem(20, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1));
                EmotesGUI.setItem(21, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1));
                EmotesGUI.setItem(22, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1));
                EmotesGUI.setItem(23, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1));
                EmotesGUI.setItem(24, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1));
                EmotesGUI.setItem(25, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1));
                EmotesGUI.setItem(26, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1));

                player.openInventory(EmotesGUI);
            } else {
                sender.sendMessage(ChatColor.RED + "Apenas jogadores podem executar este comando.");
            }
            return true;
        }
        return false;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (ChatColor.stripColor(event.getView().getTitle()).equals("Legendary Emotes")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            int slot = event.getSlot();
            // player.sendMessage("Você clicou no slot: " + slot);

            if (slot == 10) {
                if (event.getClick().isRightClick()) {
                    player.performCommand("sit");
                } else if (event.getClick().isLeftClick()) {
                    player.performCommand("gsit");
                }
            } else if (slot == 12) {
                if (event.getClick().isRightClick()) {
                    player.performCommand("lay");
                } else if (event.getClick().isLeftClick()) {
                    player.performCommand("glay");
                }
            } else if (slot == 14) {
                if (event.getClick().isRightClick()) {
                    player.performCommand("bellyflop");
                } else if (event.getClick().isLeftClick()) {
                    player.performCommand("gbellyflop");
                }
            } else if (slot == 16) {
                if (event.getClick().isRightClick()) {
                    player.performCommand("crawl");
                } else if (event.getClick().isLeftClick()) {
                    player.performCommand("gcrawl");
                }
            }
        }
    }


}
