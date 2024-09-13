package minenash.enhanced_attack_indicator.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import minenash.enhanced_attack_indicator.EnhancedAttackIndicator;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    @Unique boolean renderFullness = false;

    @Redirect(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F"))
    private float setBarProgress(LocalPlayer player, float baseTime) {
        float progress = EnhancedAttackIndicator.getProgress(player.getAttackStrengthScale(baseTime));

        if (progress == 2.0F)
            renderFullness = true;

        return progress == 2.0F ? 1.0F : progress;
    }

    @Redirect(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isAlive()Z"))
    private boolean dontShowPlus(Entity _entity) {
        return false;
    }

    private static final ResourceLocation CROSSHAIR_ATTACK_INDICATOR_FULL_TEXTURE = ResourceLocation.parse("hud/crosshair_attack_indicator_full");
    @Inject(method = "renderCrosshair", at = @At(value = "TAIL"))
    private void showPlus(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (renderFullness) {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            int j = guiGraphics.guiHeight() / 2 - 7 + 16;
            int k = guiGraphics.guiWidth() / 2 - 8;
            guiGraphics.blitSprite(CROSSHAIR_ATTACK_INDICATOR_FULL_TEXTURE, k, j, 16, 16);
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
            renderFullness = false;
        }
    }

    @Redirect(method = "renderItemHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F"))
    private float setHotBarProgress(LocalPlayer player, float baseTime) {
        float progress = EnhancedAttackIndicator.getProgress(player.getAttackStrengthScale(baseTime));
        return progress == 2.0F ? 0.99F : progress;
    }
}
