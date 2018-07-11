package com.kenijey.naturalunderground.generation.biomeGen;

import java.util.Random;

import com.kenijey.naturalunderground.Utils;
import com.kenijey.naturalunderground.WorldGenNaturalUnderGround;
import com.kenijey.naturalunderground.generation.structureGen.DecorationHelper;
import com.kenijey.naturalunderground.generation.structureGen.GenerateStoneStalactite;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public final class GenerationHumid extends WorldGenerator {
	public GenerationHumid() {
	}

	@Override
	public boolean generate(World world, Random random, BlockPos pos) {
		switch (Utils.weightedChoise(WorldGenNaturalUnderGround.probabilityGlowcapsHumid, WorldGenNaturalUnderGround.probabilityWet, WorldGenNaturalUnderGround.probabilityVines, WorldGenNaturalUnderGround.probabilitySpiderWeb,
				WorldGenNaturalUnderGround.probabilitySkulls, WorldGenNaturalUnderGround.probabilityStalactite)) {
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
            new GenerateStoneStalactite().generate(world, random, pos, Utils.getNumEmptyBlocks(world, pos), WorldGenNaturalUnderGround.maxLength);
            return true;
		}
	}
}
