package me.otomir23.compute.blocks

import net.minecraft.block.MapColor
import net.minecraft.block.piston.PistonBehavior
import org.quiltmc.qkl.library.blocks.materialOf

val computerMaterial = materialOf(
    color = MapColor.BLACK,
    pistonBehavior = PistonBehavior.BLOCK,
)
