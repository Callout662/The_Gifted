package com.AstianBk.the_gifted.server.powers;

import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.*;
import net.minecraftforge.entity.PartEntity;

import java.util.ArrayList;
import java.util.List;

public class LaserPower extends Power{
    public Vec3 start=Vec3.ZERO;
    public Vec3 end=Vec3.ZERO;
    public int progressMine=0;
    public BlockPos lastBlockPos=BlockPos.ZERO;
    public LaserPower() {
        super("laser", 300, 100, 1, ElementPower.FIRE, true, false, true,false);
    }

    @Override
    public void effectPowerForTick(Player player) {
        super.effectPowerForTick(player);
        var hitResult = raycastForEntity(player.level(), player, 200, true, 0.15f);
        if(hitResult.getType()== HitResult.Type.ENTITY){
            Entity entity =((EntityHitResult)hitResult).getEntity();
            if(entity.hurt(entity.damageSources().playerAttack(player),1.0F+1.0F*this.level)){
                entity.setSecondsOnFire(3);
            }
            this.start=player.getEyePosition();
            this.end=hitResult.getLocation();
        }else if(hitResult.getType()==HitResult.Type.BLOCK){
            Level level = player.level();
            BlockPos blockpos = ((BlockHitResult)hitResult).getBlockPos();
            BlockState blockstate = level.getBlockState(blockpos);
            BlockState oldBlockState =level.getBlockState(this.lastBlockPos);
            if(!player.level().isClientSide){
                if (blockstate.isFlammable(player.level(),blockpos,((BlockHitResult) hitResult).getDirection()) && !CampfireBlock.canLight(blockstate) && !CandleBlock.canLight(blockstate) && !CandleCakeBlock.canLight(blockstate)) {
                    BlockPos blockpos1 = blockpos.relative(((BlockHitResult) hitResult).getDirection());
                    if (BaseFireBlock.canBePlacedAt(level, blockpos1, ((BlockHitResult) hitResult).getDirection())) {
                        level.playSound(player, blockpos1, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                        BlockState blockstate1 = BaseFireBlock.getState(level, blockpos1);
                        level.setBlock(blockpos1, blockstate1, 11);
                        level.gameEvent(player, GameEvent.BLOCK_PLACE, blockpos);
                    }
                }
                if(oldBlockState==blockstate){
                    int hardness = (int) (Math.max(blockstate.getDestroySpeed(player.level(), blockpos), 0.2F) * 15F);
                    int i = (int) ((float) this.progressMine / hardness * 10.0F);

                    if(hardness<75){
                        player.level().destroyBlockProgress(player.getId(),blockpos,i);

                        if (this.progressMine++ > hardness) {
                            player.level().destroyBlock(blockpos, true);
                            this.progressMine = 0;
                        }
                    }
                }else {
                    this.lastBlockPos=blockpos;
                    this.progressMine=0;
                }
            }

        }
    }

    @Override
    public void stopPower(PowerPlayerCapability player) {
        super.stopPower(player);
        this.progressMine=0;
        this.lastBlockPos=BlockPos.ZERO;
    }

    public static HitResult raycastForEntity(Level level, Entity originEntity, float distance, boolean checkForBlocks, float bbInflation) {
        Vec3 start = originEntity.getEyePosition();
        Vec3 end = originEntity.getLookAngle().normalize().scale(distance).add(start);

        return internalRaycastForEntity(level, originEntity, start, end, checkForBlocks, bbInflation);
    }

    private static HitResult internalRaycastForEntity(Level level, Entity originEntity, Vec3 start, Vec3 end, boolean checkForBlocks, float bbInflation) {
        BlockHitResult blockHitResult = null;
        if (checkForBlocks) {
            blockHitResult = level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, originEntity));
            end = blockHitResult.getLocation();
        }
        AABB range = originEntity.getBoundingBox().expandTowards(end.subtract(start));



        List<HitResult> hits = new ArrayList<>();
        List<? extends Entity> entities = level.getEntities(originEntity, range, Entity::isAlive);
        for (Entity target : entities) {
            HitResult hit = checkEntityIntersecting(target, start, end, bbInflation);
            if (hit.getType() != HitResult.Type.MISS)
                hits.add(hit);
        }

        if (!hits.isEmpty()) {
            hits.sort((o1, o2) -> o1.getLocation().distanceToSqr(start) < o2.getLocation().distanceToSqr(start) ? -1 : 1);
            return hits.get(0);
        } else if (checkForBlocks) {
            return blockHitResult;
        }
        return BlockHitResult.miss(end, Direction.UP, BlockPos.containing(end));
    }
    public static HitResult checkEntityIntersecting(Entity entity, Vec3 start, Vec3 end, float bbInflation) {
        Vec3 hitPos = null;
        if (entity.isMultipartEntity()) {
            for (PartEntity p : entity.getParts()) {
                var hit = p.getBoundingBox().inflate(bbInflation).clip(start, end).orElse(null);
                if (hit != null) {
                    hitPos = hit;
                    break;
                }
            }
        } else {
            hitPos = entity.getBoundingBox().inflate(bbInflation).clip(start, end).orElse(null);
        }
        if (hitPos != null)
            return new EntityHitResult(entity, hitPos);
        else
            return BlockHitResult.miss(end, Direction.UP, BlockPos.containing(end));

    }



}
