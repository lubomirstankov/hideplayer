package bg.lubomir_stankov;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	String prefix, hidemsg, showmsg, perm, noplayer, delaymsg;
	public String title, enable, disable;
	int delay;
	private Inv inv;

	public void onEnable() {

		getConfig().options().copyDefaults(true);
		saveConfig();

		prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix"));
		title = ChatColor.translateAlternateColorCodes('&', getConfig().getString("GUItitle"));
		enable = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Enable-Status"));
		disable = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Disable-Status"));
		hidemsg = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Hide-Message"));
		showmsg = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Show-Message"));
		perm = ChatColor.translateAlternateColorCodes('&', getConfig().getString("No-Permission"));
		noplayer = ChatColor.translateAlternateColorCodes('&', getConfig().getString("No-Player"));
		delay = getConfig().getInt("Delay");
		delaymsg = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Delay-Message").replaceAll("%s%", delay + ""));
		delay *= 20;

		getServer().getPluginManager().registerEvents(this, this);
		System.out.println("=================================");
		System.out.println("Loading Hide Players.... "+ChatColor.GREEN+"Success");
		System.out.println("=================================");

		inv = new Inv(this);
		Bukkit.getServer().getPluginManager().registerEvents(inv, this);
	}

	ArrayList<String> a = new ArrayList<>();
	ArrayList<String> b = new ArrayList<>();

	public boolean onCommand(CommandSender sender, Command command, String label, String args[]) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(prefix + noplayer);
			return false;
		} else {
			Player p = (Player) sender;
			if (p.hasPermission("hide.me")) {
				if (command.getName().equalsIgnoreCase("hiding")) {
					inv.show(p);
				}
			} else {
				sender.sendMessage(prefix + perm);
			}

			return true;
		}
	}

	@EventHandler
	public void onPvpEvent(EntityDamageByEntityEvent e) {
		Entity d = e.getDamager();
		Entity p = e.getEntity();

		if (p instanceof Player && (d instanceof Player)) {
			if (!((Player) p).canSee((Player) d)) {
				e.setCancelled(true);
			}
		}
	}

	public String GetStatus(Player p) {
		if (b.contains(p.getUniqueId().toString())) {
			return ChatColor.GREEN + enable;
		} else {
			return ChatColor.RED + disable;
		}
	}

	public void hide(Player p) {
		for (Player players : Bukkit.getOnlinePlayers()) {
			if (players.equals(p)) {
				continue;
			}
			if (p.canSee(players)) {
				p.hidePlayer(players);
			}

		}
		p.sendMessage(prefix + hidemsg);
		a.add(p.getUniqueId().toString());
		b.add(p.getUniqueId().toString());
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				a.remove(p.getUniqueId().toString());
			}
		}, delay);
	}

	public void show(Player p) {
		for (Player players : Bukkit.getOnlinePlayers()) {
			if (players.equals(p)) {
				continue;
			}
			if (!p.canSee(players)) {
				p.showPlayer(players);
			}
		}
		a.add(p.getUniqueId().toString());
		b.remove(p.getUniqueId().toString());
		p.sendMessage(prefix + showmsg);
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				a.remove(p.getUniqueId().toString());
			}
		}, delay);
	}
	public boolean getStatus(Player p) {
		if (b.contains(p.getUniqueId().toString())) {
			return true;
		} else {
			return false;
		}
	}
	public boolean can(Player p) {
		if (a.contains(p.getUniqueId().toString())) {
			p.sendMessage(prefix + delaymsg);
			return false;
		} else {
			return true;
		}
	}
}