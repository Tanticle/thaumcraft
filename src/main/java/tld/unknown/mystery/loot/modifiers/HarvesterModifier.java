package tld.unknown.mystery.loot.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import tld.unknown.mystery.api.InfusionEnchantments;
import tld.unknown.mystery.data.aspects.AspectList;
import tld.unknown.mystery.items.components.AspectHolderComponent;
import tld.unknown.mystery.registries.ConfigItemComponents;
import tld.unknown.mystery.registries.ConfigItems;

public class HarvesterModifier extends LootModifier {

    protected HarvesterModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if(context.hasParam(LootContextParams.BLOCK_STATE))
            return generatedLoot;
        int level = ((Player)context.getParam(LootContextParams.KILLER_ENTITY)).getMainHandItem()
                .get(ConfigItemComponents.INFUSION_ENCHANTMENT.value()).enchantments()
                .get(InfusionEnchantments.HARVESTER);
        Entity victim = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        AspectList list = DataRegistries.ASPECT_REGISTRY.getAspects(victim.getType());
        if(context.getLevel().getRandom().nextInt(5) > level || list.isEmpty())
            return generatedLoot;
        for (int i = 0; i < list.getAspects().size(); i += 1 + context.getRandom().nextInt(2))
            generatedLoot.add(ConfigItems.VIS_CRYSTAL.value().create(new AspectHolderComponent(list.aspectsPresent().get(i))));
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    public static final MapCodec<HarvesterModifier> CODEC = RecordCodecBuilder.mapCodec(i -> LootModifier.codecStart(i).apply(i, HarvesterModifier::new));

}
