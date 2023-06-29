package me.otomir23.compute.items

import me.otomir23.compute.Compute
import me.otomir23.compute.blocks.ComputerBlock
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.text.Text
import net.minecraft.util.ActionResult

class RemoteTerminalItem : Item(
    FabricItemSettings()
        .maxCount(1)
        .group(Compute.ITEM_GROUP)
) {
    companion object {
        val REMOTE_TERMINAL_ITEM = RemoteTerminalItem()
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val blockPos = context.blockPos
        val world = context.world
        if (!world.getBlockState(blockPos).isOf(ComputerBlock.COMPUTER_BLOCK)) {
            return super.useOnBlock(context)
        }
        val blockState = world.getBlockState(blockPos)

        context.player?.sendMessage(Text.literal("Computer is ${if (blockState.get(ComputerBlock.ON)) "on" else "off"}"), false)

        return ActionResult.SUCCESS
    }
}
