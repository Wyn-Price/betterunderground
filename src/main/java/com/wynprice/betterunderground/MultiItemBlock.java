package com.wynprice.betterunderground;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class MultiItemBlock extends ItemBlock {
	private final ArrayList<String> subNames;


	public MultiItemBlock(Block block, ArrayList<String> arrayList) {
		super(block);
		this.subNames = arrayList;
		setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int damage) {
		if (damage >= subNames.size())
			damage = 0;
		return damage;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return subNames.get(itemstack.getMetadata());
	}
	
}
