package me.xtrm.barman.mixin;

import me.xtrm.barman.Barman;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(EnderEyeItem.class)
public class MixinEnderEyeItem {
    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (!Barman.CONFIG.endEnabled) {
            PlayerEntity player = context.getPlayer();
            Objects.requireNonNull(player);
            player.sendMessage(Text.literal("On a dit quoi ?").formatted(Formatting.RED));
            player.damage(DamageSource.CACTUS, 5);
            cir.setReturnValue(ActionResult.FAIL);
        }
    }
}
