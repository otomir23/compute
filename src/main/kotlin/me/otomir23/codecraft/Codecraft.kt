package me.otomir23.codecraft

import me.otomir23.codecraft.blocks.ComputerBlock
import me.otomir23.codecraft.items.RemoteTerminalItem
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qkl.library.items.itemGroupOf
import org.quiltmc.qkl.library.registry.registryScope
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Codecraft : ModInitializer {
    val LOGGER: Logger = LoggerFactory.getLogger("Codecraft")
    val ITEM_GROUP: ItemGroup by lazy {
        itemGroupOf(
            Identifier("codecraft", "general"),
            iconSupplier = { ComputerBlock.COMPUTER_BLOCK_ITEM.defaultStack }
        )
    }

    override fun onInitialize(mod: ModContainer) {
        registryScope(mod.metadata().id()) {
            ComputerBlock.COMPUTER_BLOCK withPath "computer" toRegistry Registry.BLOCK
            ComputerBlock.COMPUTER_BLOCK_ITEM withPath "computer" toRegistry Registry.ITEM

            RemoteTerminalItem.REMOTE_TERMINAL_ITEM withPath "remote_terminal" toRegistry Registry.ITEM
        }

        LOGGER.info("{} successfully loaded!", mod.metadata()?.name())
    }
}
