package io.github.brandonitaly.toolattributetooltips;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;

//? if fabric {
import net.fabricmc.api.ModInitializer;
//?} else if neoforge {
/*import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;
*///?}

//? if neoforge {
/*@Mod(ToolAttributeTooltips.MOD_ID)
*///?}
public class ToolAttributeTooltips /*? if fabric {*/ implements ModInitializer /*?}*/ {
    public static final String MOD_ID = "toolattributetooltips";

    private static final List<Map.Entry<TagKey<Item>, BlockState>> DESTROY_SPEED_PROBES = List.of(
        Map.entry(ItemTags.PICKAXES, Blocks.STONE.defaultBlockState()),
        Map.entry(ItemTags.AXES, Blocks.OAK_LOG.defaultBlockState()),
        Map.entry(ItemTags.SHOVELS, Blocks.DIRT.defaultBlockState()),
        Map.entry(ItemTags.HOES, Blocks.NETHER_WART_BLOCK.defaultBlockState())
    );

    public static final Holder<Attribute> CHOP_EFFICIENCY = Holder.direct(
        new Attribute("attribute.name.chop_efficiency", 0.0) {});
    public static final Holder<Attribute> DIG_EFFICIENCY = Holder.direct(
        new Attribute("attribute.name.dig_efficiency", 0.0) {});

    //? if fabric {
    @Override
    public void onInitialize() {
    }
    //?} else if neoforge {
    /*public ToolAttributeTooltips(IEventBus modEventBus) {
    }
    *///?}

    public static Float getMiningSpeed(ItemStack stack) {
        if (stack == null || stack.isEmpty() || stack.is(ItemTags.SWORDS)) return null;

        for (var probe : DESTROY_SPEED_PROBES) {
            float speed = stack.getDestroySpeed(probe.getValue());
            if (speed > 1.0f) {
                return speed;
            }
        }
        return null;
    }

    public static Holder<Attribute> getMiningAttribute(ItemStack stack) {
        if (stack == null || stack.isEmpty() || stack.is(ItemTags.SWORDS)) return null;

        for (int i = 0; i < DESTROY_SPEED_PROBES.size(); i++) {
            if (stack.getDestroySpeed(DESTROY_SPEED_PROBES.get(i).getValue()) > 1.0f) {
                return i == 1 ? CHOP_EFFICIENCY : i == 2 ? DIG_EFFICIENCY : Attributes.MINING_EFFICIENCY;
            }
        }
        return null;
    }

    public static AttributeModifier getCombinedMiningModifier(ItemStack stack, float baseSpeed) {
        AttributeInstance efficiency = new AttributeInstance(Attributes.MINING_EFFICIENCY, ignored -> {});
        stack.forEachModifier(EquipmentSlot.MAINHAND, (attribute, modifier) -> {
            if (attribute.equals(Attributes.MINING_EFFICIENCY)) {
                efficiency.addOrUpdateTransientModifier(modifier);
            }
        });
        return new AttributeModifier(
            Identifier.fromNamespaceAndPath(ToolAttributeTooltips.MOD_ID, "combined_mining_efficiency"),
            baseSpeed + efficiency.getValue(), AttributeModifier.Operation.ADD_VALUE);
    }

    public static AttributeModifier getCombinedAttackDamageModifier(ItemStack stack) {
        AttributeInstance damage = new AttributeInstance(Attributes.ATTACK_DAMAGE, ignored -> {});
        damage.setBaseValue(Player.createAttributes().build().getBaseValue(Attributes.ATTACK_DAMAGE));
        stack.forEachModifier(EquipmentSlot.MAINHAND, (attribute, modifier) -> {
            if (attribute.equals(Attributes.ATTACK_DAMAGE)) {
                damage.addOrUpdateTransientModifier(modifier);
            }
        });
        if (damage.getModifiers().isEmpty()) return null;

        float totalDamage = (float) damage.getValue();
        RandomSource random = RandomSource.create(0L);
        for (var entry : stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).entrySet()) {
            var enchantment = entry.getKey().value();
            if (!enchantment.matchingSlot(EquipmentSlot.MAINHAND)) continue;
            for (var effect : enchantment.getEffects(EnchantmentEffectComponents.DAMAGE)) {
                if (effect.requirements().isEmpty()) {
                    totalDamage = effect.effect().process(entry.getIntValue(), random, totalDamage);
                }
            }
        }
        return new AttributeModifier(
            Identifier.fromNamespaceAndPath(MOD_ID, "combined_attack_damage"),
            totalDamage, AttributeModifier.Operation.ADD_VALUE);
    }
}
