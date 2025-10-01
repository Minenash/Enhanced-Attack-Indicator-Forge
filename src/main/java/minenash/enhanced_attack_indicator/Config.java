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
public class Config {
    public enum WeaponCoolDownImportance implements TranslatableEnum { FIRST, MIDDLE, LAST;
        @Override
        public @NotNull Component getTranslatedName() {
            return Component.translatable("enhanced_attack_indicator.configuration.enum.WeaponCoolDownImportance." + name());
        }
    }

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.EnumValue<WeaponCoolDownImportance> WEAPON_COOL_DOWN_IMPORTANCE = BUILDER.defineEnum("weaponCoolDownImportance", WeaponCoolDownImportance.MIDDLE);
    public static final ModConfigSpec.BooleanValue DISABLE_PICKAXES_AND_SHOVELS = BUILDER.define("disablePickaxesAndShovels", true);
    public static final ModConfigSpec.BooleanValue DISABLE_AXES = BUILDER.define("disableAxes", false);
    public static final ModConfigSpec.BooleanValue SHOW_BLOCK_BREAKING = BUILDER.define("showBlockBreaking", true);
    public static final ModConfigSpec.BooleanValue SHOW_RANGE_WEAPON_DRAW = BUILDER.define("showRangeWeaponDraw", true);
    public static final ModConfigSpec.BooleanValue SHOW_ITEM_COOLDOWNS = BUILDER.define("showItemCooldowns", true);
    public static final ModConfigSpec.BooleanValue SHOW_FOOD_AND_POTIONS = BUILDER.define("showFoodAndPotions", true);
    public static final ModConfigSpec.BooleanValue SHOW_SLEEP = BUILDER.define("showSleep", true);
    public static final ModConfigSpec.BooleanValue SHOW_ITEM_CONTAINER_FULLNESS = BUILDER.define("showItemContainerFullness", true);

    static final ModConfigSpec SPEC = BUILDER.build();

}
