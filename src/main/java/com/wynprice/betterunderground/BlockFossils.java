package com.wynprice.betterunderground;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import java.util.Random;

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
		switch (choise) {
		case 1:
			result = Items.BONE;
			break;
		case 2:
			result = Items.ARROW;
			break;
		case 3:
			result = Items.SKULL;
			break;
		case 4:
			result = Item.getItemFromBlock(Blocks.COBBLESTONE);
		}
		return result;
	}

}
