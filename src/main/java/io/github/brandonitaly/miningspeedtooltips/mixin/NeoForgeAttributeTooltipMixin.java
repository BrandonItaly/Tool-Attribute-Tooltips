//? if neoforge && >1.21.8 {
/*package io.github.brandonitaly.toolattributetooltips.mixin;

import io.github.brandonitaly.toolattributetooltips.ToolAttributeTooltips;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.AttributeUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = AttributeUtil.class, remap = false)
public class NeoForgeAttributeTooltipMixin {
    @Redirect(method = "applyModifierTooltips", at = @At(value = "INVOKE", target =
        "Lnet/neoforged/neoforge/common/util/AttributeUtil;getSortedModifiers(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlotGroup;)Lcom/google/common/collect/Multimap;"))
    private static Multimap<Holder<Attribute>, AttributeModifier> toolattributetooltips$combinedStats(
        ItemStack stack, EquipmentSlotGroup group) {
        Multimap<Holder<Attribute>, AttributeModifier> modifiers = AttributeUtil.getSortedModifiers(stack, group);
        Float baseSpeed = ToolAttributeTooltips.getMiningSpeed(stack);
        Holder<Attribute> miningAttribute = ToolAttributeTooltips.getMiningAttribute(stack);
        AttributeModifier attackDamage = ToolAttributeTooltips.getCombinedAttackDamageModifier(stack);
        if (attackDamage != null && group.test(EquipmentSlot.MAINHAND)) {
            modifiers.removeAll(Attributes.ATTACK_DAMAGE);
            if (group == EquipmentSlotGroup.MAINHAND) {
                modifiers.put(Attributes.ATTACK_DAMAGE, attackDamage);
            }
        }
        if (miningAttribute != null && group.test(EquipmentSlot.MAINHAND)) {
            modifiers.removeAll(Attributes.MINING_EFFICIENCY);
            if (group == EquipmentSlotGroup.MAINHAND) {
                modifiers.put(miningAttribute,
                    ToolAttributeTooltips.getCombinedMiningModifier(stack, baseSpeed));
            }
        }
        return modifiers;
    }
}
*///?}
