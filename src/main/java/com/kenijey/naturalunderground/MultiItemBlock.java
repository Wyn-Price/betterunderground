package com.kenijey.naturalunderground;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

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
	
}
