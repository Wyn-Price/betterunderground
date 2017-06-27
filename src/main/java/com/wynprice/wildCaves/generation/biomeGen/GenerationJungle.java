package com.wynprice.wildCaves.generation.biomeGen;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

import com.wynprice.wildCaves.Utils;
import com.wynprice.wildCaves.WorldGenWildCaves;
import com.wynprice.wildCaves.generation.structureGen.DecorationHelper;
import com.wynprice.wildCaves.generation.structureGen.GenerateStoneStalactite;

public final class GenerationJungle extends WorldGenerator {
	public GenerationJungle() {
	}

	@Override
	public boolean generate(World world, Random random, BlockPos pos) {
		switch (Utils.weightedChoise(WorldGenWildCaves.probabilityGlowcapsHumid, WorldGenWildCaves.probabilityVinesJungle, WorldGenWildCaves.probabilitySpiderWeb, WorldGenWildCaves.probabilitySkulls,
				WorldGenWildCaves.probabilityStalactite, 0)) {
		case 1:
			DecorationHelper.generateGlowcaps(world, random, pos);
            return true;
		case 2:
			DecorationHelper.generateVines(world, random, pos);
            return true;
		case 3:
			world.setBlockState(pos, Blocks.WEB.getDefaultState(), 2);
            return true;
		case 4:
			DecorationHelper.generateSkulls(world, random, pos, Utils.getNumEmptyBlocks(world, pos));
            return true;
		default:
            new GenerateStoneStalactite().generate(world, random, pos, Utils.getNumEmptyBlocks(world, pos), WorldGenWildCaves.maxLength);
            return true;
		}
	}
}
