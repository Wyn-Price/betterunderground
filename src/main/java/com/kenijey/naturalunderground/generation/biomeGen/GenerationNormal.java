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

public final class GenerationNormal extends WorldGenerator {
	public GenerationNormal() {
	}

	@Override
	public boolean generate(World world, Random random, BlockPos pos) {
		switch (Utils.weightedChoise(WorldGenNaturalUnderGround.probabilityVines, WorldGenNaturalUnderGround.probabilitySpiderWeb, WorldGenNaturalUnderGround.probabilityStalactite, WorldGenNaturalUnderGround.probabilityGlowcaps,
				WorldGenNaturalUnderGround.probabilitySkulls, 0)) {
		case 1:
			DecorationHelper.generateVines(world, random, pos);
            return true;
		case 2:
			world.setBlockState(pos, Blocks.WEB.getDefaultState(), 2);
            return true;
		case 3:
            GenerateStoneStalactite g = new GenerateStoneStalactite();
            g.generate(world, random, pos, Utils.getNumEmptyBlocks(world, pos), WorldGenNaturalUnderGround.maxLength);
            return true;
		case 4:
			DecorationHelper.generateGlowcaps(world, random, pos);
            return true;
		case 5:
			DecorationHelper.generateSkulls(world, random, pos, Utils.getNumEmptyBlocks(world, pos));
            return true;
		}
        return false;
	}
}
