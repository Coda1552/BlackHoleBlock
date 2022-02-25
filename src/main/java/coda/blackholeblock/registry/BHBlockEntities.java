package coda.blackholeblock.registry;

import coda.blackholeblock.common.BlackHoleBlockEntity;
import coda.blackholeblock.BlackHoleMod;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BHBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, BlackHoleMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<BlackHoleBlockEntity>> BLACK_HOLE = BLOCK_ENTITIES.register("black_hole", () -> BlockEntityType.Builder.of(BlackHoleBlockEntity::new, BHBlocks.BLACK_HOLE_BLOCK.get()).build(null));
}