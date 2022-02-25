package coda.blackholeblock.client;

import coda.blackholeblock.common.BlackHoleBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BlackHoleBlockRenderer implements BlockEntityRenderer<BlackHoleBlockEntity> {

   public BlackHoleBlockRenderer(BlockEntityRendererProvider.Context p_173554_) {
   }

   @Override
   public void render(BlackHoleBlockEntity be, float p_112308_, PoseStack stack, MultiBufferSource source, int p_112311_, int p_112312_) {
      BlockState state = Blocks.STONE.defaultBlockState();
      BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
      Level world = be.getLevel();
      VertexConsumer vertexConsumer = source.getBuffer(RenderType.endPortal());
      dispatcher.getModelRenderer().tesselateBlock(world, dispatcher.getBlockModel(state), state, be.getBlockPos(), stack, vertexConsumer, false, world.random, state.getSeed(be.getBlockPos()), OverlayTexture.NO_OVERLAY);

   }
}