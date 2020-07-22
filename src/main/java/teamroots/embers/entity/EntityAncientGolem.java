package teamroots.embers.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teamroots.embers.ConfigManager;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;
import teamroots.embers.SoundManager;
import teamroots.embers.api.projectile.EffectDamage;
import teamroots.embers.damage.DamageEmber;

public class EntityAncientGolem extends EntityMob {
    public long lastPickaxeHit;

	public EntityAncientGolem(World worldIn) {
		super(worldIn);
		setSize(0.6f,1.8f);
		this.experienceValue = 10;
	}
	
	@Override
    protected void entityInit(){
    	super.entityInit();
        this.isImmuneToFire = true;
    }

	protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 0.46D, true));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.46D));
        this.tasks.addTask(7, new EntityAIWander(this, 0.46D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.applyEntityAI();
    }
	
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(ConfigManager.ancientGolemKnockbackResistance);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
    }

    protected void applyEntityAI()
    {
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    @Override
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
        super.dropLoot(wasRecentlyHit, lootingModifier, source);

        if(world.getTotalWorldTime() - lastPickaxeHit < 400 || isPickaxeHit(source)) {
            dropItem(RegistryManager.golems_eye,1);
        }
    }

    @Override
    public void onUpdate(){
    	super.onUpdate();
    	this.rotationYaw = this.rotationYawHead;
    	if (!this.isDead && this.getHealth() > 0 && this.ticksExisted % 100 == 0 && this.getAttackTarget() != null){
    		if (!getEntityWorld().isRemote){
                playSound(SoundManager.FIREBALL,1.0f,1.0f);
                EffectDamage effect = new EffectDamage(4.0f, DamageEmber.EMBER_DAMAGE_SOURCE_FACTORY, 1, 1.0f);
    			EntityEmberProjectile proj = new EntityEmberProjectile(getEntityWorld());
    			proj.initCustom(posX, posY+1.6, posZ, getLookVec().x*0.5, getLookVec().y*0.5, getLookVec().z*0.5, 4.0f, this);
                proj.setEffect(effect);

    			getEntityWorld().spawnEntity(proj);
    		}
    	}
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        boolean result = super.attackEntityFrom(source, amount);
        if (result && isPickaxeHit(source))
            lastPickaxeHit = world.getTotalWorldTime();
        return result;
    }

    public boolean isPickaxeHit(DamageSource source) {
        Entity attacker = source.getImmediateSource();
        boolean isNormalAttack = source.damageType.equals("player") || source.damageType.equals("mob");
        if (isNormalAttack && attacker instanceof EntityLivingBase) {
            ItemStack weapon = ((EntityLivingBase) attacker).getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
            if (weapon.getItem().getToolClasses(weapon).contains("pickaxe"))
                return true;
        }
        return false;
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (super.attackEntityAsMob(entityIn))
        {
            playSound(SoundManager.ANCIENT_GOLEM_PUNCH,1.0f,1.0f);
            return true;
        }
        else
            return false;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundManager.ANCIENT_GOLEM_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundManager.ANCIENT_GOLEM_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        super.playStepSound(pos, blockIn);
        this.playSound(SoundManager.ANCIENT_GOLEM_STEP,1.0f,1.0f);
    }

    @Override
	public ResourceLocation getLootTable(){
		return new ResourceLocation(Embers.MODID,"entity/ancient_golem");
	}

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setLong("lastPickaxeHit",lastPickaxeHit);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        lastPickaxeHit = compound.getLong("lastPickaxeHit");
    }
}
