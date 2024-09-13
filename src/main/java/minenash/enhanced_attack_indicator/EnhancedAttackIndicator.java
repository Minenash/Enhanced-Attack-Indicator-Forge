package minenash.enhanced_attack_indicator;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;

import java.util.List;

@Mod(EnhancedAttackIndicator.MODID)
public class EnhancedAttackIndicator {
    public static final String MODID = "enhanced_attack_indicator";
    private static final Logger LOGGER = LogUtils.getLogger();


    public EnhancedAttackIndicator(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    public static float getProgress(float weaponProgress) {


        LocalPlayer player = Minecraft.getInstance().player;
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        if (Config.weaponCoolDownImportance == Config.WeaponCoolDownImportance.FIRST && weaponProgress < 1.0F)
            return weaponCooldown(mainHand.getItem(), weaponProgress);

        if (Config.showSleep) {
            int sleep = player.getSleepTimer();
            if (sleep > 0 && sleep <= 100)
                return sleep == 100 ? 2.0F : sleep / 100.0F;
        }

        if (Config.showBlockBreaking) {
            float breakingProgress = Minecraft.getInstance().gameMode.getDestroyStage();

            if (breakingProgress > 0)
                return breakingProgress / 10;
        }

        if (Config.showRangeWeaponDraw) {
            ItemStack stack = player.getUseItem();
            Item item = stack.getItem();

            if (item == Items.BOW) {
                float progress = BowItem.getPowerForTime(72000 - player.getUseItemRemainingTicks());
                return progress == 1.0F ? 2.0F : progress;
            }
            if (item == Items.CROSSBOW) {
                float progress = (stack.getUseDuration(player) - player.getUseItemRemainingTicks()) / (float) CrossbowItem.getChargeDuration(stack, player);
                return progress >= 1.0F ? 2.0F : progress;
            }
            if (item == Items.TRIDENT) {
                float progress = (stack.getUseDuration(player) - player.getUseItemRemainingTicks()) / 10.0F;
                return progress >= 1.0F ? 2.0F : progress;
            }
        }

        if (Config.showFoodAndPotions) {
            ItemStack stack = player.getUseItem();
            Item item = stack.getItem();
            if (item.components().has(DataComponents.FOOD) || item == Items.POTION) {
                float itemCooldown = (float) player.getUseItemRemainingTicks() / stack.getUseDuration(player);
                return itemCooldown == 0.0F ? 1.0F : itemCooldown;
            }
        }

        if (Config.showItemContainerFullness) {
            ItemStack stack = player.getMainHandItem();
            var container = stack.get(DataComponents.CONTAINER);
            if (container != null) {
                List<ItemStack> items = container.stream().toList();
                int total = 0;
                int maxTotal = 64 * (27-items.size());
                for (ItemStack item : items) {
                    total += item.getCount();
                    maxTotal += item.getMaxStackSize();
                }
                float result = (float) total / maxTotal;
                return result == 1.0F ? 2.0F : result;
            }
            var bundle = stack.get(DataComponents.BUNDLE_CONTENTS);
            if (bundle != null) {
                int total = 0;
                for (ItemStack item : bundle.items())
                    total += item.getCount();
                return total / 64F;
            }
        }

        if (Config.weaponCoolDownImportance == Config.WeaponCoolDownImportance.MIDDLE && weaponProgress < 1.0F)
            return weaponCooldown(mainHand.getItem(), weaponProgress);

        if (Config.showItemCooldowns) {
            float cooldown = player.getCooldowns().getCooldownPercent(offHand.getItem(), 0);
            if (cooldown != 0.0F)
                return cooldown;

            cooldown = player.getCooldowns().getCooldownPercent(mainHand.getItem(), 0);
            if (cooldown != 0.0F)
                return cooldown;
        }

        if (Config.showRangeWeaponDraw && (mainHand.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(mainHand)
                || offHand.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(mainHand)))
            return 2.0F;

        if (Config.weaponCoolDownImportance == Config.WeaponCoolDownImportance.LAST)
            return weaponCooldown(mainHand.getItem(), weaponProgress);

        return 1.0F;

    }

    private static float weaponCooldown(Item item, float weaponProgress) {
        if (Config.disablePickaxesAndShovels && (item.getDescriptionId().contains("pickaxe") || item.getDescriptionId().contains("shovel")))
            return 1.0F;
        if (Config.disableAxes && item.getDescriptionId().contains("axe") && !item.getDescriptionId().contains("pickaxe"))
            return 1.0F;
        return weaponProgress;

    }


}
