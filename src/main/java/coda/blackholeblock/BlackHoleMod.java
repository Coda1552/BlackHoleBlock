package coda.blackholeblock;

import coda.blackholeblock.client.BlackHoleBlockRenderer;
import coda.blackholeblock.registry.BHBlockEntities;
import coda.blackholeblock.registry.BHBlocks;
import coda.blackholeblock.registry.BHItems;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BlackHoleMod.MOD_ID)
public class BlackHoleMod {
    public static final String MOD_ID = "blackholeblock";

    public BlackHoleMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        BHBlocks.BLOCKS.register(bus);
        BHItems.ITEMS.register(bus);
        BHBlockEntities.BLOCK_ENTITIES.register(bus);

        bus.addListener(this::clientSetup);
    }

    @OnlyIn(Dist.CLIENT)
    private void clientSetup(final FMLClientSetupEvent event) {
        BlockEntityRenderers.register(BHBlockEntities.BLACK_HOLE.get(), BlackHoleBlockRenderer::new);
    }
}
