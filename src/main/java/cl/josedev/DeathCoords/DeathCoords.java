package cl.josedev.DeathCoords;

import org.bukkit.plugin.java.JavaPlugin;

public class DeathCoords extends JavaPlugin {

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new DeathListener(this), this);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	
}
