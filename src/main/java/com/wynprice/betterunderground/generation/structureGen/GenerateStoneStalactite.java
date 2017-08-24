package com.wynprice.betterunderground.generation.structureGen;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

import com.wynprice.betterunderground.BetterUnderground;
import com.wynprice.betterunderground.Utils;
import com.wynprice.betterunderground.WorldGenBetterUnderGround;

public class GenerateStoneStalactite {
    public Block blockId;
    public GenerateStoneStalactite(){
    	init(BetterUnderground.blockStoneStalactite);
    	}
    public GenerateStoneStalactite(Block toGen){
    	init(toGen);
    }
    
    private void init(Block toGen)
    {
    	blockId = toGen;
    }

	public void generate(World world, Random random, BlockPos pos, int distance, int maxLength) {
		boolean stalagmiteGenerated = false;
		if (distance <= 1) {
			//x,y,z,blockID, metadata, no update
            if (!world.isAirBlock(pos.up())) {
			    world.setBlockState(pos, blockId.getStateFromMeta(3), 2);
            }
		} else {
			int j = 0; // blocks placed
			BlockPos topY = new BlockPos(pos);
			BlockPos botY = pos.down(distance - 1);
			int aux;
			//stalactite base
			if (!world.isAirBlock(topY.up()) && world.isAirBlock(topY.add(0, -1, 0))) {
                generateStalactiteBase(world, random, topY);
				j++;
			}
			// stalagmite base
			if (!world.getBlockState(botY).getMaterial().isLiquid() && WorldGenBetterUnderGround.isWhiteListed(world.getBlockState(botY.down()).getBlock())) {
				aux = Utils.randomChoise(-1, 8, 9, 10);
				if (aux != -1) {
					j++;
					stalagmiteGenerated = generateStalagmiteBase(world, random, botY, aux);
				}
			}
			if (j==2) {
                int k = 0; // counter
                int topMetadata, bottomMetadata;
				while (k < maxLength && topY.getY() >= botY.getY() && j < distance && !world.getBlockState(topY.down()).getMaterial().isLiquid()) {
					k++;
					IBlockState state = world.getBlockState(topY);
					topMetadata = state.getBlock().getMetaFromState(state);
					state = world.getBlockState(botY);
					bottomMetadata = state.getBlock().getMetaFromState(state);
					topY = topY.down();
					botY = botY.up();
					// Expand downwards
					if (world.isAirBlock(topY) && topMetadata > 2 && topMetadata < 6) {
						aux = random.nextInt(5);
						if (aux != 4)
							world.setBlockState(topY, blockId.getStateFromMeta(Utils.randomChoise(4, 5, 7, 11)), 2);
						else
							world.setBlockState(topY, blockId.getStateFromMeta(Utils.randomChoise(7, 11)), 2);
						j++;
					}
					// Expand upwards
					if (world.isAirBlock(botY) && (bottomMetadata > 3 && bottomMetadata < 5 || bottomMetadata == 8) && j < distance && stalagmiteGenerated) {
						aux = random.nextInt(5);
						if (aux != 4)
							world.setBlockState(botY, blockId.getStateFromMeta(Utils.randomChoise(4, 5, 6, 12)), 2);
						else
							world.setBlockState(botY, blockId.getStateFromMeta(Utils.randomChoise(12, 6)), 2);
						j++;
					}
				}
			}
		}
	}

    protected boolean generateStalagmiteBase(World world, Random random, BlockPos botY, int aux) {
    	if(world.isAirBlock(botY.down()))
    		return false;
        world.setBlockState(botY, blockId.getStateFromMeta(aux), 2);
        return true;
    }

    protected void generateStalactiteBase(World world, Random random, BlockPos topY) {
        world.setBlockState(topY, blockId.getStateFromMeta(Utils.randomChoise(1, 2, 3, 3)), 2);
    }
}
