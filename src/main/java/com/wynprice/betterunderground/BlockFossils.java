package com.wynprice.betterunderground;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockFossils extends Block {
	public BlockFossils() {
		super(Material.ROCK);
		this.setHardness(1F);
        this.setUnlocalizedName("fossilsBlock");
        this.setRegistryName("Blockfossils");
		this.setSoundType(SoundType.STONE);
	}

	@Override
	public Item getItemDropped(IBlockState metadata, Random random, int par3) {
		int choise = Utils.weightedChoise(0.5f, 0.15f, 0.05f, 0.5f, 0, 0);
		Item result = null;
		if(choise == 1)
			result = Items.BONE;
		else if(choise == 2 && BetterUnderground.bonePileArrowDrop)
			result = Items.ARROW;
		else if(choise == 3)
			result = Items.SKULL;
		else
			result = Item.getItemFromBlock(Blocks.COBBLESTONE);
		return result;
	}

}
