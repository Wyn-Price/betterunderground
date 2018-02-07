package com.wynprice.betterunderground;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModBlocks 
{
	public static Block f, d ,F, md, SS, SaS;
	
	public static void init()
	{
		f = new BlockFlora();
		d = new BlockDecorations();
		F = new BlockFossils();
		md = new BlockMossyDirt();
		SS = new BlockStoneStalactite();
		SaS = new BlockStalactite(Item.getItemFromBlock(Blocks.SANDSTONE)).setUnlocalizedName("sandstoneStalactiteBlock").setRegistryName("BlockSandstoneStalactite");
	}
	
	public static void reg()
	{

		regBlock(f, 64);
		regBlock(d, 64);
		regBlock(F, 64);
		regBlock(md, 64);
		regBlock(SS, 64);
		regBlock(SaS, 64);
		BetterUnderground.blockFlora = f;
		BetterUnderground.blockDecorations = d;
		BetterUnderground.blockFossils = F;
		BetterUnderground.mossyDirt = md;
		BetterUnderground.blockStoneStalactite = SS;
		BetterUnderground.blockSandStalactite = SaS;
	}
	
	private static void regBlock(Block block, int StackSize)
	{
		ForgeRegistries.BLOCKS.register(block);
		ArrayList<String> classEs = new ArrayList<String>(Arrays.asList(block.getClass().getName().split("\\.")));
		MultiItemBlock item = null;
		switch (classEs.get(classEs.size() - 1)) {
		case "BlockFlora":
			item = new MultiItemBlock(block, getn("flora_", BetterUnderground.caps.size()));
			break;
		case "BlockDecorations":
			item = new MultiItemBlock(block, getn("icicle_", BetterUnderground.icicles.size()));
			break;
		case "BlockFossils":
			item = new MultiItemBlock(block, getn("fossil_", BetterUnderground.fossils.size()));
			break;
		case "BlockMossyDirt":
			item = new MultiItemBlock(block, getn(null, BetterUnderground.mossy.size()));
			break;
		case "BlockStoneStalactite":
			item = new MultiItemBlock(block, getn("stone_", BetterUnderground.stalacs.size()));
			break;
		case "BlockStalactite":
			item = new MultiItemBlock(block, getn("sandstone_", BetterUnderground.sandStalacs.size()));
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
		for(Block b : Arrays.asList(f,d,F,md,SS,SaS))
		{
			b.setCreativeTab(BetterUnderground.tab);
		}
			
	}

}
