package potionstudios.byg.common.entity.pumpkinwarden;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.units.qual.A;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class PumpkinWarden extends PathfinderMob implements IAnimatable {

    private final AnimationFactory factory = new AnimationFactory(this);
    private BlockPos jukebox;
    private boolean party;
    private static final EntityDataAccessor<Boolean> HIDING = SynchedEntityData.defineId(PumpkinWarden.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> TIMER = SynchedEntityData.defineId(PumpkinWarden.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Optional<BlockState>> DATA_CARRY_STATE = SynchedEntityData.defineId(PumpkinWarden.class, EntityDataSerializers.BLOCK_STATE);


    public PumpkinWarden(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
    }


    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_CARRY_STATE, Optional.empty());
        this.entityData.define(HIDING, false);
        this.entityData.define(TIMER, 50);
        super.defineSynchedData();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.4D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.2D, Ingredient.of(Items.PUMPKIN_PIE), false));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 2.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(10, new PumpkinWardenLeaveBlockGoal(this, 1, 30 ,5));
        this.goalSelector.addGoal(11, new PumpkinWarden.PumpkinWardenTakeBlockGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        super.registerGoals();
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        AnimationController controller = event.getController();
        controller.transitionLengthTicks = 0;
        if (this.isHiding()) {
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.pumpkinwarden.hide", false));
            return PlayState.CONTINUE;
        }
        if (this.getCarriedBlock() != null) {
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.pumpkinwarden.hold", false));
            return PlayState.CONTINUE;
        }
        else if (event.isMoving()) {
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.pumpkinwarden.walking", true));
            return PlayState.CONTINUE;
        }
        else if (this.party) {
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.pumpkinwarden.wave", true));
            return PlayState.CONTINUE;
        } else if (this.getCarriedBlock() != null) {
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.pumpkinwarden.hide", false));
            return PlayState.CONTINUE;
        } else {
            return PlayState.STOP;
        }
    }



    @Override
    public void setRecordPlayingNearby(BlockPos pPos, boolean pIsPartying) {
        this.jukebox = pPos;
        this.party = pIsPartying;
    }

    public void aiStep() {
        super.aiStep();
        System.out.println(isHiding());
        System.out.println(getTimer());
        if (this.jukebox == null || !this.jukebox.closerToCenterThan(this.position(), 10D) || !this.level.getBlockState(this.jukebox).is(Blocks.JUKEBOX)) {
            this.party = false;
            this.jukebox = null;
        }
        if (this.getLastHurtByMob() != null){
            this.setTimer(100);
            this.setLastHurtByMob(null);
        }
        if (this.getTimer() >= 1){
            this.setTimer(this.getTimer() - 1);
            this.setHiding(true);
        }
        if (this.getTimer() == 0){
            this.setHiding(false);
        }

        if (this.isHiding()){
            this.setDeltaMovement(0, 0, 0);
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    public boolean isHiding() {
        return entityData.get(HIDING);
    }

    public void setHiding(boolean flag) {
        entityData.set(HIDING, flag);
    }

    public int getTimer() {
        return entityData.get(TIMER);
    }

    public void setTimer(int flag) {
        entityData.set(TIMER, flag);
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    public void setCarriedBlock(@Nullable BlockState pState) {
        this.entityData.set(DATA_CARRY_STATE, Optional.ofNullable(pState));
    }

    @Nullable
    public BlockState getCarriedBlock() {
        return this.entityData.get(DATA_CARRY_STATE).orElse(null);
    }
    static class PumpkinWardenTakeBlockGoal extends Goal {
        private final PumpkinWarden warden;

        public PumpkinWardenTakeBlockGoal(PumpkinWarden p) {
            this.warden = p;
        }
        public boolean canUse() {
            if (this.warden.getCarriedBlock() != null) {
                return false;
            } else {
                return this.warden.getRandom().nextInt(reducedTickDelay(20)) == 0;
            }
        }
        public void tick() {
            RandomSource randomsource = this.warden.getRandom();
            Level level = this.warden.level;
            int i = Mth.floor(this.warden.getX() - 2.0D + randomsource.nextDouble() * 4.0D);
            int j = Mth.floor(this.warden.getY() + randomsource.nextDouble() * 3.0D);
            int k = Mth.floor(this.warden.getZ() - 2.0D + randomsource.nextDouble() * 4.0D);
            BlockPos blockpos = new BlockPos(i, j, k);
            BlockState blockstate = level.getBlockState(blockpos);
            Vec3 vec3 = new Vec3((double)this.warden.getBlockX() + 0.5D, (double)j + 0.5D, (double)this.warden.getBlockZ() + 0.5D);
            Vec3 vec31 = new Vec3((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D);
            BlockHitResult blockhitresult = level.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this.warden));
            boolean flag = blockhitresult.getBlockPos().equals(blockpos);
            if (blockstate.getBlock() instanceof StemGrownBlock && flag) {
                level.removeBlock(blockpos, false);
                level.gameEvent(GameEvent.BLOCK_DESTROY, blockpos, GameEvent.Context.of(this.warden, blockstate));
                this.warden.setCarriedBlock(blockstate.getBlock().defaultBlockState());
            }
        }
    }
    static class PumpkinWardenLeaveBlockGoal extends MoveToBlockGoal {
        public PumpkinWarden warden;

        public PumpkinWardenLeaveBlockGoal(PumpkinWarden warden, double speed, int range, int y) {
            super(warden, speed, range, y);
            this.warden = warden;
        }
        public boolean canUse() {
            return this.warden.getCarriedBlock() != null;
        }

        public void tick() {
            super.tick();
            if (this.isReachedTarget()){
                BehaviorUtils.throwItem(this.warden, this.warden.getCarriedBlock().getBlock().asItem().getDefaultInstance(), new Vec3(this.blockPos.getX(), this.blockPos.getY(), this.blockPos.getZ()));
                this.warden.setCarriedBlock(null);
            }
        }
        @Override
        protected boolean isValidTarget(LevelReader var1, BlockPos var2) {
            BlockState pos = var1.getBlockState(var2);
            return (pos.is(Blocks.CARVED_PUMPKIN));
        }
    }
}