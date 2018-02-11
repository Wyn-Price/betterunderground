package com.wynprice.betterunderground;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class EventManager
{
    private final WorldGenMinable[] mines = 
    	{new WorldGenMinable(BetterUnderground.blockFossils.getDefaultState(), 4),
    	 new WorldGenMinable(BetterUnderground.blockFossils.getDefaultState(), 5), 
    	 new WorldGenMinable(BetterUnderground.blockFossils.getDefaultState(), 6),
    	 new WorldGenMinable(BetterUnderground.mossyDirt.getDefaultState(), 6),
    	};
	private final int chanceForNodeToSpawn;
	public EventManager(int chanceForNodeToSpawn)
	{
		this.chanceForNodeToSpawn = chanceForNodeToSpawn;
	}

	@SubscribeEvent
	public void generate(OreGenEvent.Post oreGen){
        if (!WorldGenBetterUnderGround.dimensionBlacklist.contains(oreGen.getWorld().provider.getDimension())) {
            this.addOreSpawn(oreGen.getRand().nextInt(mines.length), oreGen.getWorld(), oreGen.getRand(), oreGen.getPos());
        }
    }

    /**
     * Adds an Ore Spawn to Minecraft
     * @param mine the type of ore generator
     * @param world to spawn in
     * @param pos to spawn around
     * @param random object for retrieving random positions within the world to spawn the Block
     **/
    private void addOreSpawn(int mine, World world, Random random, BlockPos pos)
    {
        for(int x = 0; x < chanceForNodeToSpawn; x++)
        {
            mines[mine].generate(world, random, pos.add(random.nextInt(16), 1 + random.nextInt(89), random.nextInt(16)));
        }
    }
}
