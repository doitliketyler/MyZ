/**
 * 
 */
package jordan.sicherman.items;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jordan.sicherman.locales.LocaleMessage;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 * @author Jordan
 * 
 */
public enum ItemTag {
	MURKY_WATER(Material.POTION, new TagMeta(0, LocaleMessage.MURKY_WATER, null, null), TagDefinition.TITLE), SALTY_WATER(Material.POTION,
			new TagMeta(0, LocaleMessage.SALT_WATER, null, null), TagDefinition.TITLE), WAND(Material.BLAZE_ROD, new TagMeta(0,
			ChatColor.BLUE + "MyZ Wand", ChatColor.GOLD + "Drop to stop managing", null), TagDefinition.TITLE, TagDefinition.LORE), STARTER_CHESTPLATE(
			Material.LEATHER_CHESTPLATE, new TagMeta(0, LocaleMessage.STARTER_TUNIC_DISPLAY, LocaleMessage.STARTER_TUNIC_LORE, null),
			TagDefinition.TITLE), STARTER_SWORD(Material.WOOD_SWORD, new TagMeta(0, LocaleMessage.STARTER_SWORD_DISPLAY,
			LocaleMessage.STARTER_SWORD_LORE, null), TagDefinition.TITLE), CHAIN(Material.IRON_FENCE, new TagMeta(0, LocaleMessage.CHAIN,
			null, null), TagDefinition.TITLE), GRAPPLE(Material.FISHING_ROD, new TagMeta(0, LocaleMessage.GRAPPLE_DISPLAY,
			LocaleMessage.GRAPPLE_LORE, null), TagDefinition.TITLE), COLD_WATER(Material.POTION, new TagMeta(0, LocaleMessage.COLD_WATER,
			null, null), TagDefinition.TITLE), WARM_WATER(Material.POTION, new TagMeta(0, LocaleMessage.WARM_WATER, null, null),
			TagDefinition.TITLE);

	private static enum TagDefinition {
		DURABILITY, TITLE, LORE, ENCHANTS;
	}

	private final short data;
	private final Material material;
	private final String displayName;
	private final TagDefinition[] definition;
	private final Map<Enchantment, Integer> enchants;
	private final List<String> lore;

	private ItemTag(Material material, TagMeta meta, TagDefinition... definition) {
		this.material = material;
		this.definition = definition;

		enchants = meta.getEnchants();
		data = meta.getData();
		displayName = meta.getDisplayName();
		if (meta.getLore() != null && !meta.getLore().isEmpty()) {
			String[] cLore = meta.getLore().split("~");
			String[] loreBuilder = new String[cLore.length];
			for (int i = 0; i < cLore.length; i++) {
				loreBuilder[i] = ChatColor.RESET + cLore[i];
			}
			lore = Arrays.asList(loreBuilder);
		} else {
			lore = null;
		}
	}

	public short getDurability() {
		return data;
	}

	public Material getType() {
		return material;
	}

	public String getDisplayName() {
		return displayName;
	}

	public TagDefinition[] getDefinition() {
		return definition;
	}

	public Map<Enchantment, Integer> getEnchantments() {
		return enchants;
	}

	public List<String> getLore() {
		return lore;
	}

	public boolean matches(ItemStack item) {
		if (item == null || item.getType() != getType()) { return false; }

		for (TagDefinition def : getDefinition()) {
			if (!matches(item, def)) { return false; }
		}

		return true;
	}

	private boolean matches(ItemStack item, TagDefinition definition) {
		switch (definition) {
		case DURABILITY:
			return item.getDurability() == getDurability();
		case ENCHANTS:
			return !item.hasItemMeta() && getEnchantments() == null || getEnchantments().equals(item.getItemMeta().getEnchants());
		case LORE:
			return !item.hasItemMeta() && getLore() == null || getLore().equals(item.getItemMeta().getLore());
		case TITLE:
			return !item.hasItemMeta() && getDisplayName() == null
					|| (ChatColor.RESET + getDisplayName()).equals(item.getItemMeta().getDisplayName());
		}
		return true;
	}

	private static class TagMeta {

		private final short data;
		private final String name, lore;
		private final Map<Enchantment, Integer> enchants;

		private TagMeta(int data, LocaleMessage name, LocaleMessage lore, Map<Enchantment, Integer> enchants) {
			this(data, name != null ? name.toString() : null, lore != null ? lore.toString() : null, enchants);
		}

		private TagMeta(int data, String name, String lore, Map<Enchantment, Integer> enchants) {
			this.data = (short) data;
			this.name = name;
			this.lore = lore;
			this.enchants = enchants;
		}

		public String getDisplayName() {
			return name;
		}

		public String getLore() {
			return lore;
		}

		public Map<Enchantment, Integer> getEnchants() {
			return enchants;
		}

		public short getData() {
			return data;
		}
	}

	public static ItemTag fromString(String string) {
		for (ItemTag tag : values()) {
			if (tag.toString().toLowerCase().equals(string)) { return tag; }
		}
		return null;
	}
}
