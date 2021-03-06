/**
 * 
 */
package jordan.sicherman.nms.v1_8_R1.mobs;

import jordan.sicherman.MyZ;
import jordan.sicherman.nms.v1_8_R1.mobs.pathfinders.CustomPathfinderGoalHurtByTarget;
import jordan.sicherman.nms.v1_8_R1.mobs.pathfinders.CustomPathfinderGoalLookAtPlayer;
import jordan.sicherman.nms.v1_8_R1.mobs.pathfinders.CustomPathfinderGoalMeleeAttack;
import jordan.sicherman.nms.v1_8_R1.mobs.pathfinders.CustomPathfinderGoalMoveToLocation;
import jordan.sicherman.nms.v1_8_R1.mobs.pathfinders.CustomPathfinderGoalNearestAttackableTarget;
import jordan.sicherman.nms.v1_8_R1.mobs.pathfinders.CustomPathfinderGoalRandomLookaround;
import jordan.sicherman.nms.v1_8_R1.mobs.pathfinders.CustomPathfinderGoalRandomStroll;
import jordan.sicherman.utilities.configuration.ConfigEntries;
import net.minecraft.server.v1_8_R1.Enchantment;
import net.minecraft.server.v1_8_R1.EntityCreature;
import net.minecraft.server.v1_8_R1.EntityGiantZombie;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.GenericAttributes;
import net.minecraft.server.v1_8_R1.Item;
import net.minecraft.server.v1_8_R1.ItemStack;
import net.minecraft.server.v1_8_R1.Items;
import net.minecraft.server.v1_8_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R1.World;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Jordan
 * 
 */
public class CustomEntityGiantZombie extends EntityGiantZombie implements SmartEntity {

	private Location smartTarget;

	@Override
	public void setSmartTarget(Location inLoc, long duration) {
		smartTarget = inLoc;

		new BukkitRunnable() {
			@Override
			public void run() {
				smartTarget = null;
			}
		}.runTaskLater(MyZ.instance, duration);
	}

	@Override
	public Location getSmartTarget() {
		return smartTarget;
	}

	@Override
	public EntityCreature getEntity() {
		return this;
	}

	@Override
	protected float bD() {
		return (float) (super.bD() * ConfigEntries.GIANT_JUMP_MULTIPLIER.<Double> getValue());
	}

	@SuppressWarnings("unchecked")
	public CustomEntityGiantZombie(World world) {
		super(world);

		goalSelector.a(0, new PathfinderGoalFloat(this));
		goalSelector.a(2, new CustomPathfinderGoalMeleeAttack(this, EntityHuman.class,
				ConfigEntries.GIANT_SPEED_TARGET.<Double> getValue(), false));
		goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
		goalSelector.a(7, new CustomPathfinderGoalRandomStroll(this, 1.0D));
		goalSelector.a(4, new CustomPathfinderGoalMoveToLocation(this, 1.2D));
		goalSelector.a(8, new CustomPathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		goalSelector.a(8, new CustomPathfinderGoalLookAtPlayer(this, CustomEntityGuard.class, 8.0F));
		goalSelector.a(8, new CustomPathfinderGoalRandomLookaround(this));
		goalSelector.a(4,
				new CustomPathfinderGoalMeleeAttack(this, CustomEntityGuard.class, ConfigEntries.GIANT_SPEED_TARGET.<Double> getValue(),
						true));
		targetSelector.a(1, new CustomPathfinderGoalHurtByTarget(this, true, new Class[] { EntityHuman.class, CustomEntityGuard.class }));
		targetSelector.a(2, new CustomPathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
		targetSelector.a(2, new CustomPathfinderGoalNearestAttackableTarget(this, CustomEntityGuard.class, false));
	}

	@Override
	public boolean canSpawn() {
		return world.a(getBoundingBox(), this) && world.getCubes(this, getBoundingBox()).isEmpty()
				&& !world.containsLiquid(getBoundingBox());
	}

	@Override
	protected void aW() {
		super.aW();
		getAttributeInstance(GenericAttributes.maxHealth).setValue(ConfigEntries.GIANT_HEALTH.<Double> getValue());
		getAttributeInstance(GenericAttributes.c).setValue(ConfigEntries.GIANT_KNOCKBACK_RESIST.<Double> getValue());
		getAttributeInstance(GenericAttributes.d).setValue(ConfigEntries.GIANT_SPEED.<Double> getValue());
		getAttributeInstance(GenericAttributes.e).setValue(ConfigEntries.GIANT_DAMAGE.<Double> getValue());
	}

	@Override
	protected Item getLoot() {
		return null;
	}

	@Override
	protected void dropDeathLoot(boolean killedByPlayer, int enchantmentLevel) {
		int amount = random.nextInt(10 + enchantmentLevel * enchantmentLevel) + 1;
		for (int dropped = 0; dropped <= amount; dropped++) {
			a(Items.ROTTEN_FLESH, 1);
		}

		amount = random.nextInt(5);
		for (int dropped = 0; dropped <= amount; dropped++) {
			ItemStack drop = new ItemStack(Items.BOW, 1);
			drop.addEnchantment(Enchantment.ARROW_INFINITE, 0);
			switch (random.nextInt(3)) {
			case 0:
				drop = new ItemStack(Items.DIAMOND_SWORD, 1, Items.DIAMOND_SWORD.getMaxDurability() / 2);
				break;
			case 1:
				drop = new ItemStack(Items.GOLDEN_APPLE);
				break;
			}
			a(drop, 0.0F);
		}
	}

	@Override
	protected void getRareDrop() {
	}
}
