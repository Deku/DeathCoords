package cl.josedev.DeathCoords;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;


public class DeathListener implements Listener {

	DeathCoords plugin;
	
	public DeathListener(DeathCoords pl) {
		this.plugin = pl;
	}
	
	@EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
	public void onPlayerDeath(PlayerDeathEvent event) {
		try {
			Player player = event.getEntity();
			
			if (player.isOnline()) {
				Location loc = event.getEntity().getLocation();
				plugin.lastDeath.put(player.getUniqueId(), loc);
				String message = ">> " + player.getName()
									+ ", moriste en: "
									+ "[Mundo] " + loc.getWorld().getName()
									+ " - [X] " + loc.getBlockX()
									+ " - [Y] " + loc.getBlockY()
									+ " - [Z] "+loc.getBlockZ();
				
				player.sendMessage(ChatColor.AQUA + message);
			}
		}
		catch (Exception ex) {
			this.plugin.getLogger().warning("Couldn't handle death event. Error: " + ex.getMessage());
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
	public void onPlayerReachedDeathPoint(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		// Bodies of dead players trigger this event when they are falling
		if (player.isDead()) {
			return;
		}
		
		if (plugin.lastDeath.containsKey(player.getUniqueId()))
		{
			Location deathPoint = plugin.lastDeath.get(player.getUniqueId());
			Location loc = event.getTo();
			
			if (loc.distance(deathPoint) <= 5.0) {
				plugin.lastDeath.remove(player.getUniqueId());
				deleteCompass(player);
				player.setCompassTarget(new Location(player.getWorld(), 0, 0, 0));
			}
		}
	}
	
	@EventHandler
	public void onPlayerDisconnect(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		if (plugin.lastDeath.containsKey(player.getUniqueId())) {
			plugin.lastDeath.remove(player.getUniqueId());
		}
		
		deleteCompass(player);
	}
	
	private void deleteCompass(Player player) {
		// Remove guide compass
		for (ItemStack item : player.getInventory().getContents()) {
			if (item != null) {
				if (isDCCompass(item)) {
					player.getInventory().remove(item);
				}
			}
		}
		
		// Remove from enderchest
		for (ItemStack item : player.getEnderChest().getContents()) {
			if (item != null) {
				if (isDCCompass(item)) {
					player.getEnderChest().remove(item);
				}
			}
		}
	}
	
	private boolean isDCCompass(ItemStack item) {
		if (item.getType().equals(Material.COMPASS)) {
			String name = item.getItemMeta().getDisplayName();
			
			return name.equals("Ubicacion de tu muerte") && item.getItemMeta().getLore().size() > 0;
		}
		
		return false;
	}
}
