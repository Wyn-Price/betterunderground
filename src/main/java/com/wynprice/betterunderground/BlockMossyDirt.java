package com.wynprice.betterunderground;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public class BlockMossyDirt extends Block {
	public BlockMossyDirt() {
		super(Material.GRASS);
        this.setUnlocalizedName("mossyDirt");
        this.setRegistryName("mossyDirt");
        this.setSoundType(SoundType.GROUND);
	}

	@Override
	public Item getItemDropped(IBlockState metadata, Random random, int par3) {
		int choise = Utils.weightedChoise(0.5f, 0.15f, 0.05f, 0.5f, 0, 0);
		Item result = null;
		if(choise == 1)
			result = Item.getItemFromBlock(Blocks.VINE);
		else
			result = Item.getItemFromBlock(Blocks.DIRT);
		return result;
	}

}
