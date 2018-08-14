package teamroots.embers.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teamroots.embers.Embers;
import teamroots.embers.SoundManager;

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
    	this.rotationYaw = this.rotationYawHead;
    	if (this.ticksExisted % 100 == 0 && this.getAttackTarget() != null){
    		if (!getEntityWorld().isRemote){
                playSound(SoundManager.FIREBALL,1.0f,1.0f);
    			EntityEmberProjectile proj = new EntityEmberProjectile(getEntityWorld());
    			proj.initCustom(posX, posY+1.6, posZ, getLookVec().x*0.5, getLookVec().y*0.5, getLookVec().z*0.5, 4.0f, this.getUniqueID());
    			getEntityWorld().spawnEntity(proj);
    		}
    	}
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
}
