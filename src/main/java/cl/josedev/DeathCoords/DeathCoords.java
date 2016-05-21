package cl.josedev.DeathCoords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class DeathCoords extends JavaPlugin {

	public Map<UUID, Location> lastDeath = new HashMap<UUID, Location>();
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new DeathListener(this), this);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("guia")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Only players can use this command!");
				return true;
			}
			Player player = (Player) sender;
			
			if (!lastDeath.containsKey(player.getUniqueId())) {
				player.sendMessage(ChatColor.RED + "No tienes ubicacion de muerte!");
				return true;
			}
			
			// Player doesn't have full inventory
			if (player.getInventory().firstEmpty() > -1) {
				Location loc = lastDeath.get(player.getUniqueId());
				ItemStack compass = new ItemStack(Material.COMPASS);
				ItemMeta meta = compass.getItemMeta();
				List<String> lore = new ArrayList<String>();
				lore.add("Mundo: " + loc.getWorld().getName());
				lore.add("X: " + loc.getBlockX());
				lore.add("Y: " + loc.getBlockY());
				lore.add("Z: " + loc.getBlockZ());
				
				meta.setDisplayName("Ubicacion de tu muerte");
				meta.setLore(lore);
				compass.setItemMeta(meta);
				
				player.setCompassTarget(loc);
				player.getInventory().addItem(compass);
				player.sendMessage(ChatColor.GREEN + "Se te ha entregado una brujula indicando tu ubicacion de muerte.");
				player.sendMessage(ChatColor.GREEN + "Esta desaparecera cuando llegues al lugar.");
			} else {
				player.sendMessage(ChatColor.RED + "No tienes suficiente espacio en tu inventario!");
			}
			
			return true;
		}
		
		return false;
	}
}
