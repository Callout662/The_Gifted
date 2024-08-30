package com.AstianBk.the_gifted.common.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Invoker("tryCheckInsideBlocks")
    void tryCheck();

    @Invoker("setSharedFlag")
    void set(int p_20116_, boolean p_20117_);

    @Invoker("getBlockPosBelowThatAffectsMyMovement")
    BlockPos getBlockPos();

}
