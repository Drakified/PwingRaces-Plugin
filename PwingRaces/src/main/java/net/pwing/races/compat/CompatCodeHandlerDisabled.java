package net.pwing.races.compat;

import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.pwing.races.PwingRaces;

public class CompatCodeHandlerDisabled implements ICompatCodeHandler {

	private PwingRaces plugin;

	public CompatCodeHandlerDisabled(PwingRaces plugin) {
		this.plugin = plugin;
	}

	@Override
	public Enchantment getEnchantment(String name) {
		return Enchantment.getByName(name);
	}

	@Override
	public int getDamage(ItemStack item) {
		return item.getDurability();
	}

	@Override
	public void setDamage(ItemStack item, int damage) {
		item.setDurability((short) damage);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setOwner(ItemStack item, String owner) {
		if (item.getItemMeta() instanceof SkullMeta) {
			SkullMeta meta = (SkullMeta) item.getItemMeta();
			meta.setOwner(owner);
			item.setItemMeta(meta);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setUnbreakable(ItemStack item, boolean unbreakable) {
		ItemMeta meta = item.getItemMeta();
		meta.spigot().setUnbreakable(true);
		item.setItemMeta(meta);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setColor(ItemStack item, Color color) {
		if (item.getItemMeta() instanceof LeatherArmorMeta) {
			LeatherArmorMeta im = (LeatherArmorMeta) item.getItemMeta();
			im.setColor(color);
			item.setItemMeta(im);
		}
	}

	// Not implemented until 1.14
	@Override
	public void setCustomModelData(ItemStack item, int data) {

	}

	@Override // some versions need handlers for this still
	public double getDefaultAttributeValue(Player player, String attribute) {
		return 0;
	}

	@Override
	public double getDamage(Arrow arrow) {
		return arrow.spigot().getDamage();
	}

	@Override
	public void setPickupStatus(Arrow arrow, String status) {

	}

	@Override
	public void setDamage(Arrow arrow, double damage) {
		arrow.spigot().setDamage(damage);
	}

	@Override
	public String getHeadURL(String player) {
		return null;
	}
}
