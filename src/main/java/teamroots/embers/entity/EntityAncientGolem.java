package teamroots.embers.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import teamroots.embers.RegistryManager;

public class EntityAncientGolem extends EntityMob {

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
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
    }

    protected void applyEntityAI()
    {
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }
    
    @Override
    public void onUpdate(){
    	super.onUpdate();
    	if (this.ticksExisted % 100 == 0 && this.getAttackTarget() != null){
    		if (!getEntityWorld().isRemote){
    			EntityEmberProjectile proj = new EntityEmberProjectile(getEntityWorld());
    			proj.initCustom(posX, posY+1.6, posZ, getLookVec().xCoord*0.5, getLookVec().yCoord*0.5, getLookVec().zCoord*0.5, 4.0f, this.getUniqueID());
    			getEntityWorld().spawnEntity(proj);
    		}
    	}
    }
    
    @Override
    public void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source){
    	super.dropLoot(wasRecentlyHit,lootingModifier,source);
    	if (!getEntityWorld().isRemote){
    		if (source.getEntity() instanceof EntityPlayer && !(source.getEntity() instanceof FakePlayer)){
    			if (((EntityPlayer)source.getEntity()).getHeldItemMainhand() != ItemStack.EMPTY){
    				if (((EntityPlayer)source.getEntity()).getHeldItemMainhand().getItem() instanceof ItemPickaxe){
    	    			getEntityWorld().spawnEntity(new EntityItem(getEntityWorld(),posX,posY+0.5,posZ,new ItemStack(RegistryManager.golems_eye,1)));
    				}
    			}
    		}
	    	for (int i = 0; i < 3+lootingModifier; i ++){
	    		if (rand.nextInt(2) == 0){
	    			getEntityWorld().spawnEntity(new EntityItem(getEntityWorld(),posX,posY+0.5,posZ,new ItemStack(RegistryManager.core_stone,1)));
	    		}
	    	}
    	}
    }

}
