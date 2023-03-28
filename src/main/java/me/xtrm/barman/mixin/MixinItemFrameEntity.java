package me.xtrm.barman.mixin;

import me.xtrm.barman.Barman;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ItemFrameEntity.class)
public abstract class MixinItemFrameEntity extends Entity {
    public MixinItemFrameEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "dropHeldStack", at = @At("HEAD"), cancellable = true)
    private void dropHeldStack(Entity entity, boolean alwaysDrop, CallbackInfo ci) {
        if (Barman.CONFIG.killEndItemFrames) {
            if (Objects.equals(this.world.getDimensionEntry().getKey().get(), DimensionTypes.THE_END)) {
                ci.cancel();
            }
        }
    }
}
