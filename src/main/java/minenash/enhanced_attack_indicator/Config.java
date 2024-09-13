package minenash.enhanced_attack_indicator;

import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.TranslatableEnum;
import org.jetbrains.annotations.NotNull;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = EnhancedAttackIndicator.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    public enum WeaponCoolDownImportance implements TranslatableEnum { FIRST, MIDDLE, LAST;
        @Override
        public @NotNull Component getTranslatedName() {
            return Component.translatable("enhanced_attack_indicator.configuration.enum.WeaponCoolDownImportance." + name());
        }
    }

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.EnumValue<WeaponCoolDownImportance> WEAPON_COOL_DOWN_IMPORTANCE = BUILDER.defineEnum("weaponCoolDownImportance", WeaponCoolDownImportance.MIDDLE);
    private static final ModConfigSpec.BooleanValue DISABLE_PICKAXES_AND_SHOVELS = BUILDER.define("disablePickaxesAndShovels", true);
    private static final ModConfigSpec.BooleanValue DISABLE_AXES = BUILDER.define("disableAxes", true);
    private static final ModConfigSpec.BooleanValue SHOW_BLOCK_BREAKING = BUILDER.define("showBlockBreaking", true);
    private static final ModConfigSpec.BooleanValue SHOW_RANGE_WEAPON_DRAW = BUILDER.define("showRangeWeaponDraw", true);
    private static final ModConfigSpec.BooleanValue SHOW_ITEM_COOLDOWNS = BUILDER.define("showItemCooldowns", true);
    private static final ModConfigSpec.BooleanValue SHOW_FOOD_AND_POTIONS = BUILDER.define("showFoodAndPotions", true);
    private static final ModConfigSpec.BooleanValue SHOW_SLEEP = BUILDER.define("showSleep", true);
    private static final ModConfigSpec.BooleanValue SHOW_ITEM_CONTAINER_FULLNESS = BUILDER.define("showItemContainerFullness", true);

    static final ModConfigSpec SPEC = BUILDER.build();


    public static WeaponCoolDownImportance weaponCoolDownImportance = WeaponCoolDownImportance.MIDDLE;
    public static boolean disablePickaxesAndShovels = true;
    public static boolean disableAxes = true;

    public static boolean showBlockBreaking = true;
    public static boolean showRangeWeaponDraw = true;
    public static boolean showItemCooldowns = true;
    public static boolean showFoodAndPotions = true;
    public static boolean showSleep = true;
    public static boolean showItemContainerFullness = true;


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        weaponCoolDownImportance = WEAPON_COOL_DOWN_IMPORTANCE.get();
        disablePickaxesAndShovels = DISABLE_PICKAXES_AND_SHOVELS.get();
        disableAxes = DISABLE_AXES.get();
        showBlockBreaking = SHOW_BLOCK_BREAKING.get();
        showRangeWeaponDraw = SHOW_RANGE_WEAPON_DRAW.get();
        showItemCooldowns = SHOW_ITEM_COOLDOWNS.get();
        showFoodAndPotions = SHOW_FOOD_AND_POTIONS.get();
        showSleep = SHOW_SLEEP.get();
        showItemContainerFullness = SHOW_ITEM_CONTAINER_FULLNESS.get();
    }
}
