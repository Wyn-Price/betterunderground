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

public final class GenerationHumid extends WorldGenerator {
	public GenerationHumid() {
	}

	@Override
	public boolean generate(World world, Random random, BlockPos pos) {
		switch (Utils.weightedChoise(WorldGenWildCaves.probabilityGlowcapsHumid, WorldGenWildCaves.probabilityWet, WorldGenWildCaves.probabilityVines, WorldGenWildCaves.probabilitySpiderWeb,
				WorldGenWildCaves.probabilitySkulls, WorldGenWildCaves.probabilityStalactite)) {
		case 1:
			DecorationHelper.generateGlowcaps(world, random, pos);
			return true;
		case 2:
			DecorationHelper.generateFloodedCaves(world, random, pos);
			return true;
		case 3:
			DecorationHelper.generateVines(world, random, pos);
            return true;
		case 4:
			world.setBlockState(pos.down(Utils.getNumEmptyBlocks(world, pos) - 1), Blocks.WEB.getDefaultState(), 2);
            return true;
		case 5:
			DecorationHelper.generateSkulls(world, random, pos, Utils.getNumEmptyBlocks(world, pos));
            return true;
		default:
            new GenerateStoneStalactite().generate(world, random, pos, Utils.getNumEmptyBlocks(world, pos), WorldGenWildCaves.maxLength);
            return true;
		}
	}
}
