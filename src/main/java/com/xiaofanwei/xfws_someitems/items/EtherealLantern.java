package com.xiaofanwei.xfws_someitems.items;

import com.xiaofanwei.xfws_someitems.items.curios.CurioItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import top.theillusivec4.curios.api.SlotContext;

import java.util.*;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import static net.minecraft.world.item.crafting.RecipeType.CAMPFIRE_COOKING;


//This inspiration and some code come from P3pp3rF1y's Reliquary Reincarnations mod
//此灵感及部分代码来自P3pp3rF1y的Reliquary Reincarnations模组

public class EtherealLantern extends CurioItem {
    private static final List<Direction> TORCH_SIDES = List.of(
            Direction.DOWN,
            Direction.NORTH,
            Direction.SOUTH,
            Direction.WEST,
            Direction.EAST);

    public EtherealLantern() {
        super(builder("ethereal_lantern", new Properties().durability(128).rarity(Rarity.EPIC)).addTooltips(2));
    }

    public int getRange() {
        return 8;
    }

    public void curioTick(SlotContext ctx, ItemStack stack) {
        if (ctx.entity().level().isClientSide || !(ctx.entity() instanceof Player player) || player.isSpectator() || stack.getDamageValue() >= 128)
            return;
        getPositions(player).anyMatch(pos -> tryPlace(stack, player, pos));
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess slotAccess) {
        if (other.getBurnTime(CAMPFIRE_COOKING) < 1 || stack.getDamageValue() == 0) return false;

        int bt = (int) (other.getBurnTime(CAMPFIRE_COOKING) / 50.0);
        if (action == ClickAction.SECONDARY) {
            stack.setDamageValue(stack.getDamageValue() - bt);
            other.shrink(1);
        } else {
            int total = bt * other.getCount();
            if (stack.getDamageValue() > total) {
                other.shrink(other.getCount());
                stack.setDamageValue(stack.getDamageValue() + total);
            } else {
                other.shrink(stack.getDamageValue() / bt);
                stack.setDamageValue(0);
            }
        }
        player.playSound(SoundEvents.BLAZE_SHOOT, 1F, 1F);
        return true;
    }

    private Stream<BlockPos> getPositions(Player player) {
        int r = getRange();
        return BlockPos.betweenClosedStream(player.blockPosition().offset(-r, -r / 2, -r),
                player.blockPosition().offset(r, r / 2, r));
    }

    private boolean tryPlace(ItemStack stack, Player player, BlockPos pos) {
        Level level = player.level();
        if (level.getBrightness(LightLayer.BLOCK, pos) > 4) return false;

        BlockState state = level.getBlockState(pos);
        if (invalidPlacement(level, pos, state)) return false;

        return TORCH_SIDES.stream().anyMatch(side -> {
            Block torchBlock = side.getAxis().isHorizontal() ? Blocks.WALL_TORCH : Blocks.TORCH;
            BlockPlaceContext placementContext = new BlockPlaceContext(
                    level, player, InteractionHand.MAIN_HAND,
                    ItemStack.EMPTY, new BlockHitResult(
                    Vec3.atCenterOf(pos), side, pos, false
            )
            );
            BlockState torchState = torchBlock.getStateForPlacement(placementContext);
            if (torchState == null) return false;
            if (torchBlock == Blocks.WALL_TORCH) {
                torchState = torchState.setValue(WallTorchBlock.FACING, side);
            }
            return torchState.canSurvive(level, pos)
                    && level.isUnobstructed(torchState, pos, CollisionContext.empty())
                    && !isViewBlocked(level, player, pos)
                    && place(stack, player, level, pos, torchState);
        });
    }

    private boolean invalidPlacement(Level level, BlockPos pos, BlockState state) {
        return state.getBlock() instanceof LiquidBlock
                || !state.getFluidState().isEmpty()
                || level.getBlockState(pos.below()).getBlock().hasDynamicShape()
                || (!state.isAir() && !state.canBeReplaced(new BlockPlaceContext(level, null,
                InteractionHand.MAIN_HAND, ItemStack.EMPTY, new BlockHitResult(
                Vec3.ZERO, Direction.UP, pos, false))));
    }

    private boolean isViewBlocked(Level level, Player player, BlockPos pos) {
        Vec3 eyes = new Vec3(player.getX(), player.getEyeY(), player.getZ());
        return DoubleStream.of(-0.2F, 0.2F).anyMatch(x ->
                DoubleStream.of(-0.2F, 0.2F).anyMatch(y ->
                        DoubleStream.of(-0.2F, 0.2F).anyMatch(z ->
                                level.clip(new ClipContext(eyes.add(x, y, z),
                                                Vec3.atCenterOf(pos).add(x, y, z),
                                                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player))
                                        .getType() == HitResult.Type.BLOCK)));
    }

    private boolean place(ItemStack stack, Player player, Level level, BlockPos pos, BlockState state) {
        if (!level.setBlock(pos, state, 3)) return false;
        state.getBlock().setPlacedBy(level, pos, state, player, stack);
        stack.setDamageValue(stack.getDamageValue() + 1);

        level.addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT,
                        0.5F + level.random.nextFloat()/2, 0.5F + level.random.nextFloat()/2, 0),
                pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 0, 0, 0);
        level.playSound(null, pos, state.getSoundType(level, pos, player).getStepSound(),
                SoundSource.BLOCKS, 0.8F, 1.0F);
        return true;
    }
}
