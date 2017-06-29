package com.wynprice.wildCaves;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.block.Block;
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
		ArrayList<String> classEs = new ArrayList<String>(Arrays.asList(block.getClass().getName().split("\\.")));
		MultiItemBlock item = null;
		switch (classEs.get(classEs.size() - 1)) {
		case "BlockFlora":
			item = new MultiItemBlock(block, getn("flora_", WildCaves.caps.size()));
			break;
		case "BlockDecorations":
			item = new MultiItemBlock(block, getn("icicle_", WildCaves.icicles.size()));
			break;
		case "BlockFossils":
			item = new MultiItemBlock(block, getn("fossil_", WildCaves.fossils.size()));
			break;
		case "BlockStoneStalactite":
			item = new MultiItemBlock(block, getn("stone_", WildCaves.stalacs.size()));
			break;
		case "BlockStalactite":
			item = new MultiItemBlock(block, getn("sandstone_", WildCaves.sandStalacs.size()));
			break;
		}
		//ItemBlock item = new ItemBlock(block);
		item.setRegistryName(block.getRegistryName());
		item.setMaxStackSize(StackSize);
		ForgeRegistries.ITEMS.register(item);
	}
	
	private static ArrayList<String> getn(String preName, int size)
	{
		ArrayList<String> names = new ArrayList<String>();
		for (int i = 0; i < size; i ++)
			names.add(preName + i);
		return names;
	}
	
	public static void invtab()
	{
		for(Block b : Arrays.asList(f,d,F,SS,SaS))
		{
			b.setCreativeTab(WildCaves.wildTab);
		}
			
	}

}
