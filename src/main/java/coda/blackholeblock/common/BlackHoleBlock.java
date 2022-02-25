package coda.blackholeblock.common;

import coda.blackholeblock.registry.BHBlocks;
import coda.blackholeblock.registry.BHTags;
import com.mojang.math.Vector3f;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.VanillaInventoryCodeHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BlackHoleBlock extends BaseEntityBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public BlackHoleBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.getStateDefinition().any().setValue(POWERED, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        ItemStack stack = player.getItemInHand(hand);
        Vec3[] COLORS = Util.make(new Vec3[16], (p_154319_) -> {
            for(int i = 0; i <= 15; ++i) {
                float f = (float)i / 10.0F;
                float f1 = f * 0.5F + (f > 0.0F ? 0.7F : 0.5F);
                float f2 = Mth.clamp(0.7F - 0.2F, 0.0F, 2.5F);
                float f3 = Mth.clamp(0.6F + 0.2F, 0.9F, 1.9F);
                p_154319_[i] = new Vec3(f1, f2, f3);
            }
        });

        if (!stack.isEmpty() && !state.getValue(POWERED) && !stack.is(BHTags.BLACKLIST)) {
            stack.shrink(1);
            press(state, world, pos);
            return InteractionResult.CONSUME;
        }
        else if (player.isShiftKeyDown()) {
            ItemEntity entity = EntityType.ITEM.create(world);

            entity.setItem(new ItemStack(BHBlocks.BLACK_HOLE_BLOCK.get().asItem()));
            entity.moveTo(pos, 0F, 0F);
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
            for (int i = 0; i < 16; i++) {
                world.addParticle(new DustParticleOptions(new Vector3f(COLORS[i]), 1.0F), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, Direction.getRandom(world.random).getStepX(), Direction.getRandom(world.random).getStepY(), Direction.getRandom(world.random).getStepZ());
            }

            world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENDER_EYE_DEATH, SoundSource.BLOCKS, 0.4F, 1.0F, false);
            world.addFreshEntity(entity);
        }

        return super.use(state, world, pos, player, hand, result);
    }

    private void updateNeighbours(BlockState state, Level level, BlockPos pos) {
        level.updateNeighborsAt(pos, this);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_51101_) {
        p_51101_.add(POWERED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_152198_, BlockState p_152199_) {
        return new BlackHoleBlockEntity(p_152198_, p_152199_);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, Random rand) {
        for (Direction directions : Direction.values()) {
            BlockState state0 = level.getBlockState(pos.relative(directions));
            if (level.getBlockEntity(pos) instanceof HopperBlockEntity hopper && state0.is(Blocks.HOPPER)) {
                for (int i = 0; i < hopper.getContainerSize(); ++i) {
                    if (!hopper.getItem(i).isEmpty()) {
                        hopper.getItem(i).shrink(1);
                    }
                }
            }
        }

        BlockState state0 = level.getBlockState(pos.relative(Direction.NORTH));
        if (level.getBlockEntity(pos) instanceof HopperBlockEntity hopper) {
            for (int i = 0; i < hopper.getContainerSize(); ++i) {
                if (!hopper.getItem(i).isEmpty()) {
                    hopper.getItem(i).shrink(1);
                }
            }
        }

        /*if (state0.is(Blocks.HOPPER) && state0.hasBlockEntity() && level.getBlockEntity(pos) instanceof HopperBlockEntity hopper) {
            VanillaInventoryCodeHooks.insertHook(hopper);
        }*/

        if (state.getValue(POWERED)) {
            level.setBlock(pos, state.setValue(POWERED, false), 3);

        }
        level.scheduleTick(new BlockPos(pos), this, 20);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        }
        return (lvl, pos, blockState, t) -> {
            if (t instanceof BlackHoleBlockEntity tile) {
                tile.tickServer();
            }
        };
    }

    public void press(BlockState state, Level world, BlockPos pos) {
        world.setBlock(pos, state.setValue(POWERED, true), 3);
        world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 0.4F, 1.0F, false);
        updateNeighbours(state, world, pos);
        world.scheduleTick(pos, this, 20);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction direction) {
        return state.isSignalSource() && direction != null;
    }

    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    public int getDirectSignal(BlockState p_51109_, BlockGetter level, BlockPos pos, Direction direction) {
        return 15;
    }

    public boolean isSignalSource(BlockState pos) {
        return true;
    }

}
