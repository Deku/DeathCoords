package cl.josedev.DeathCoords;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.md_5.bungee.api.ChatColor;

public class DeathListener implements Listener {

	DeathCoords plugin;
	
	public DeathListener(DeathCoords pl) {
		this.plugin = pl;
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		try {
			Player player = event.getEntity();
			
			if (player.isOnline()) {
				Location loc = event.getEntity().getLocation();
				String message = ">> " + player.getName()
									+ ", moriste en las coordenadas "
									+ "x: " + loc.getBlockX()
									+ ", y: " + loc.getBlockY()
									+ ", z: "+loc.getBlockZ();
				
				player.sendMessage(ChatColor.AQUA + message);
			}
		}
		catch (Exception ex) {
			this.plugin.getLogger().warning("Couldn't handle death event. Error: " + ex.getMessage());
		}
	}
}
