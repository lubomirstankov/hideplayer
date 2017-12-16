package bg.lubomir_stankov;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class Inv implements Listener {
	Main main;

	private Inventory inv;
	String ins;

	public Inv(Main m) {
		main = m;
		ins = main.title;
		inv = Bukkit.getServer().createInventory(null, 9, ins);

		for (int i = 0; i < 9; i++) {
			inv.setItem(i, Item3());
		}

	}

	protected ItemStack Item3() {
		ItemStack item1 = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		ItemMeta md = item1.getItemMeta();
		md.setDisplayName("");
		item1.setItemMeta(md);
		item1.setDurability((short) 7);
		return item1;
	}

	protected ItemStack Item4(Player p) {
		ItemStack item1 = null;
		if(main.getStatus(p)) {
			item1 = new ItemStack(Material.MAGMA_CREAM);
		} else {
			item1 = new ItemStack(Material.SLIME_BALL);
		}
		ItemMeta md = item1.getItemMeta();
		md.setDisplayName(ChatColor.GREEN + "STATUS: " + main.GetStatus(p));
		item1.setItemMeta(md);
		return item1;
	}

	public void show(Player p) {
		inv.setItem(4, Item4(p));
		p.openInventory(inv);
	}

	@EventHandler
	public void click(InventoryClickEvent e) {
		String in = e.getInventory().getName();
		Player p = (Player) e.getWhoClicked();
		if (!in.equals(ins))
			return;
		e.setCancelled(true);
		if (e.getAction().equals(InventoryAction.NOTHING))
			return;
		if (e.getCurrentItem().equals(Item4(p)) && main.can(p)) {
			if (main.getStatus(p)) {
				main.show(p);
			} else {
				main.hide(p);
			}
			inv.setItem(4, Item4(p));
		}
	}
}
