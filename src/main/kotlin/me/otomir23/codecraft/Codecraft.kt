package me.otomir23.codecraft

import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Codecraft : ModInitializer {
    val LOGGER: Logger = LoggerFactory.getLogger("Codecraft")
    const val MOD_ID: String = "codecraft"

    override fun onInitialize(mod: ModContainer) {
        LOGGER.info("{} successfully loaded!", mod.metadata()?.name())
    }
}
