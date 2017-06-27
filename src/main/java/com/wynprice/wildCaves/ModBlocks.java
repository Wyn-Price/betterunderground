package com.wynprice.wildCaves;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModBlocks 
{
	private static Block f, d ,F, SS, SaS;
	
	public static void init()
	{
		f = new BlockFlora();
		d = new BlockDecorations();
		F = new BlockFossils();
		SS = new BlockStoneStalactite();
		SaS = new BlockStalactite(Item.getItemFromBlock(Blocks.SANDSTONE)).setUnlocalizedName("sandstoneStalactiteBlock").setRegistryName("BlockSandstoneStalactite");
	}
	
	public static void reg()
	{

		regBlock(f,64);
		regBlock(d,64);
		regBlock(F, 64);
		regBlock(SS, 64);
		regBlock(SaS, 64);
		WildCaves.blockFlora = f;
		WildCaves.blockDecorations = d;
		WildCaves.blockFossils = F;
		WildCaves.blockStoneStalactite = SS;
		WildCaves.blockSandStalactite = SaS;
	}
	
	private static void regBlock(Block block, int StackSize)
	{
		ForgeRegistries.BLOCKS.register(block);
		ItemBlock item = new ItemBlock(block);
		item.setRegistryName(block.getRegistryName());
		item.setMaxStackSize(StackSize);
		ForgeRegistries.ITEMS.register(item);
	}
	
	public static void invtab()
	{
		for(Block b : Arrays.asList(f,d,F,SS,SaS))
			b.setCreativeTab(WildCaves.wildTab);
	}

}
