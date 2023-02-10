package me.otomir23.codecraft.blocks

import me.otomir23.codecraft.Codecraft
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import org.quiltmc.qkl.library.blocks.blockSettingsOf
import org.quiltmc.qkl.library.items.itemSettingsOf

class ComputerBlock : Block(blockSettingsOf(
    material = computerMaterial,
    hardness = 1.5f,
    resistance = 6.0f,
    luminanceFunction = { state ->
        state.get(ON)?.let { if (it) 2 else 0 } ?: 0
    }
)) {
    companion object {
        val ON: BooleanProperty = BooleanProperty.of("on")
        val FACING: DirectionProperty = DirectionProperty.of("facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)

        val COMPUTER_BLOCK = ComputerBlock()
        val COMPUTER_BLOCK_ITEM = BlockItem(COMPUTER_BLOCK, itemSettingsOf(
            maxCount = 1,
            group = Codecraft.ITEM_GROUP
        ))
    }

    init {
        defaultState = defaultState.with(ON, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(ON, FACING)
    }

    override fun getPlacementState(ctx: ItemPlacementContext?): BlockState? {
        return defaultState.with(FACING, ctx?.playerFacing?.opposite)
    }

    @Deprecated("Deprecated in vanilla", ReplaceWith("BlockState::onUse"))
    override fun onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult): ActionResult {
        world.setBlockState(pos, state.with(ON, !state.get(ON)))
        return ActionResult.SUCCESS
    }

    @Deprecated("Deprecated in vanilla", ReplaceWith("BlockState::getOutlineShape"))
    override fun getOutlineShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        return createCuboidShape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0)
    }
}
