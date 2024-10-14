package com.AstianBk.the_gifted.common.mixins;

import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import com.AstianBk.the_gifted.server.manager.LimbsPartRegeneration;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

import static com.AstianBk.the_gifted.client.renderers.RenderUtil.getModelPartForLimbs;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin<T extends LivingEntity> {
    @Shadow @Final public ModelPart head;

    @Shadow @Final public ModelPart rightArm;

    @Shadow @Final public ModelPart leftArm;

    @Shadow @Final public ModelPart rightLeg;

    @Shadow @Final public ModelPart leftLeg;

    @Shadow @Final public ModelPart body;

    @Shadow @Final public ModelPart hat;

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V",at = @At("TAIL"))
    public void regeneration$Layer(T p_102866_, float p_102867_, float p_102868_, float p_102869_, float p_102870_, float p_102871_, CallbackInfo ci){
        if (((Object)this) instanceof PlayerModel<?> model){
            PowerPlayerCapability cap=PowerPlayerCapability.get((Player) p_102866_);
            if(cap!=null){
                LimbsPartRegeneration regeneration=cap.getLimbsPartRegeneration();
                if(regeneration!=null){
                    regeneration.getLimbs().forEach(e->{
                        List<ModelPart> part=getModelPartForLimbs(e,model);
                        if(part!=null){
                            part.forEach(k->k.visible=false);
                        }
                    });
                }
            }

        }
    }

}
