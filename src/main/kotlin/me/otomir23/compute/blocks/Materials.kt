package me.otomir23.compute.blocks

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricMaterialBuilder
import net.minecraft.block.MapColor
import net.minecraft.block.Material
import net.minecraft.block.piston.PistonBehavior

val computerMaterial: Material = FabricMaterialBuilder(MapColor.BLACK).pistonBehavior(PistonBehavior.BLOCK).build()
