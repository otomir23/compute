package me.otomir23.compute

import me.otomir23.compute.blocks.ComputerBlock
import me.otomir23.compute.items.RemoteTerminalItem
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.slf4j.Logger
import org.slf4j.LoggerFactory


object Compute : ModInitializer {
    val LOGGER: Logger = LoggerFactory.getLogger("Compute")
    val MOD_ID = "compute"
    val ITEM_GROUP: ItemGroup by lazy {
        FabricItemGroupBuilder.create(Identifier(MOD_ID, "tab"))
            .icon { ComputerBlock.COMPUTER_BLOCK_ITEM.defaultStack }
            .build()
    }

    override fun onInitialize() {
        Registry.register(Registry.BLOCK, Identifier(MOD_ID, "computer"), ComputerBlock.COMPUTER_BLOCK)
        Registry.register(Registry.ITEM, Identifier(MOD_ID, "computer"), ComputerBlock.COMPUTER_BLOCK_ITEM)
        Registry.register(Registry.ITEM, Identifier(MOD_ID, "remote_terminal"), RemoteTerminalItem.REMOTE_TERMINAL_ITEM)

        LOGGER.info("Compute successfully loaded! {}", Int.MAX_VALUE)
    }
}
