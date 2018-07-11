package com.kenijey.naturalunderground;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.kenijey.naturalunderground.generation.biomeGen.GenerationArid;
import com.kenijey.naturalunderground.generation.biomeGen.GenerationFrozen;
import com.kenijey.naturalunderground.generation.biomeGen.GenerationHumid;
import com.kenijey.naturalunderground.generation.biomeGen.GenerationJungle;
import com.kenijey.naturalunderground.generation.biomeGen.GenerationNormal;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.TempCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class WorldGenNaturalUnderGround {
	public static float probabilityVinesJungle;
	public static float probabilityVines;
	public static float probabilityIcicle;
	public static float probabilityWet;
	public static float probabilityDry;
	public static float probabilityGlowcapsHumid;
	public static float probabilityGlowcaps;
	public static float probabilityIceshrooms;
	public static float probabilityStalactite;
	public static float probabilitySpiderWeb;
	public static float probabilitySandStalactites;
	public static float probabilitySkulls;
	public static int maxGenHeight;
	public static int maxLength;
	private static int timesPerChunck = 55;
	public static int maxGenHeightGlowcapNormal;
	public static List<Integer> dimensionBlacklist = new ArrayList<Integer>();
	private static List<Block> blockWhiteList = new ArrayList<Block>();
	private static final GenerationJungle jungleGen = new GenerationJungle();
	private static final GenerationHumid wetGen = new GenerationHumid();
	private static final GenerationArid aridGen = new GenerationArid();
	private static final GenerationNormal normalGen = new GenerationNormal();
	private static final GenerationFrozen frozenGen = new GenerationFrozen();

	public WorldGenNaturalUnderGround(Configuration config) {
		setConfig(config);
	}

    public static boolean isWhiteListed(Block block){
        return blockWhiteList.contains(block);
    }

    @SubscribeEvent
    public void decorate(DecorateBiomeEvent.Post decorationEvent){
        generate(decorationEvent.getRand(), decorationEvent.getPos().add(8, 0, 8), decorationEvent.getWorld());
    }

	public void generate(Random random, BlockPos pos, World world) {
		if (!dimensionBlacklist.contains(world.provider.getDimension())) {
            BlockPos coord;
            //int dist;// distance
            Biome biome;
			for (int i = 0; i < timesPerChunck; i++) {
                coord = pos.add(random.nextInt(16), 0, random.nextInt(16));
                coord = new BlockPos(coord.getX(), Math.min(world.getHeight(coord).getY()-1, random.nextInt(maxGenHeight)), coord.getZ());
				// search for the first available spot
				while (coord.getY() > 10 && (!blockWhiteList.contains(world.getBlockState(coord.up()).getBlock()) || !world.isAirBlock(coord))) {
					coord = coord.down();
				}
				// found a spot
				if (coord.getY() > 10) {
					// getting the biome
					biome = world.getBiome(coord);
					//dist = Utils.getNumEmptyBlocks(world, Xcoord, Ycoord, Zcoord);
					if (biome.getTempCategory().equals(TempCategory.COLD))
						frozenGen.generate(world, random, coord);
					else if (biome.getDefaultTemperature() > 1.5f && biome.getRainfall() < 0.1f)
						aridGen.generate(world, random, coord);
					else if (biome.getTempCategory().equals(TempCategory.WARM))
						jungleGen.generate(world, random, coord);
					else if (biome.isHighHumidity() || biome.getTempCategory().equals(TempCategory.OCEAN))
						wetGen.generate(world, random, coord);
					else
						normalGen.generate(world, random, coord);
				}
			}
		}
	}

	private static void setConfig(Configuration config) {
        // --generation permissions------
        String category = "Permissions";
        boolean sandstoneStalactites = config.get(category, "Generate Sandstone stalactites on arid biomes", true).getBoolean(true);
        boolean flora = config.get(category, "Generate flora on caves", true).getBoolean(true);
        boolean stalactites = config.get(category, "Generate stalactites on caves", true).getBoolean(true);
        String[] list = config.get(category, "Dimension Blacklist", "-1,1", "Worlds where generation won't occur, by dimension ids. Use [id1;id2] to add a range of id, prefix with - to exclude.").getString().split(",");
        for(String text:list){
            if(text!=null && !text.isEmpty()){
                boolean done = false;
                if(text.contains("[") && text.contains("]")){
                    String[] results = text.substring(text.indexOf("[")+1, text.indexOf("]")).split(";");
                    if(results.length==2){
                        try {
                            int a = Integer.parseInt(results[0]);
                            int b = Integer.parseInt(results[1]);
                            boolean remove = text.startsWith("-");
                            for(int x = a; x <=b; x++){
                                if(remove)
                                    dimensionBlacklist.remove(x);
                                else
                                    dimensionBlacklist.add(x);
                            }
                            done = true;
                        }catch (Exception ignored){

                        }
                    }
                }
                if(!done) {
                    try {
                        dimensionBlacklist.add(Integer.parseInt(text.trim()));
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        list = config.get(category, "Block white list", "stone,grass,dirt,cobblestone,gravel,gold_ore,iron_ore,coal_ore,lapis_ore,sandstone,diamond_ore,redstone_ore,lit_redstone_ore,ice,snow,clay,monster_egg,emerald_ore").getString().split(",");
        Block block;
        for (String txt : list) {
            try {
                block = Block.getBlockFromName(txt.trim());
                if(block != null && block.getMaterial(null) != Material.AIR){
                    blockWhiteList.add(block);
                }
            } catch (Throwable n) {
            }
        }
        // --Biome specific ratios------
        category = "Biome specific";
        probabilityVinesJungle = (float) config.get(category, "Probability of vines on jungle caves", 1.0).getDouble(1.0);
        probabilityIcicle = (float) config.get(category, "Probability of icicles on frozen caves", 2.0).getDouble(2.0);
        try{
            block = Block.getBlockFromName(config.get(category, "Block to generate in frozen caves", "ice").getString().trim());
            if(block!=null && block.getMapColor(null, null, null) == MapColor.ICE){
                Utils.frozen = block;
            }
        }catch (Throwable n){
        }
        probabilityWet = (float) config.get(category, "Probability of more water fountains on wet caves", 0.3).getDouble(0.3);
        probabilityDry = (float) config.get(category, "Probability of less generation arid caves", 0.5).getDouble(0.5);
        probabilityGlowcapsHumid = (float) config.get(category, "Probability of Glowing mushrooms on humid/jungle caves", 0.3).getDouble(0.3);
        probabilityIceshrooms = (float) config.get(category, "Probability of Glowing Ice mushrooms on frozen caves", 1.0).getDouble(0.3);
        probabilitySandStalactites = (float) config.get(category, "Probability of sandstone stalactites on arid caves", 4).getDouble(4);
        // --General ratios------
        category = "Non biome specific";
        probabilityVines = (float) config.get(category, "Probability of vines on caves", 0.5).getDouble(0.5);
        probabilityGlowcaps = (float) config.get(category, "Probability of glowing mushrooms on caves", 0.5).getDouble(0.5);
        probabilityStalactite = (float) config.get(category, "Probability of Stalactites/stalagmites",5).getDouble(5);
        probabilitySpiderWeb = (float) config.get(category, "Probability of spider webs", 0.2).getDouble(0.2);
        maxGenHeightGlowcapNormal = config.get(category, "Max height at which to generate glowcaps on normal biomes", 33).getInt();
        probabilitySkulls = (float) config.get(category, "Probability of skulls", 0.001).getDouble(0.001);
        if(!sandstoneStalactites){
            probabilitySandStalactites = 0;
        }
        if(!flora){
            probabilityGlowcaps = 0;
            probabilityVinesJungle = 0;
            probabilityGlowcapsHumid = 0;
            probabilityIceshrooms = 0;
            probabilityVines = 0;
            probabilityGlowcaps = 0;
        }
        if(!stalactites){
            probabilityStalactite = 0;
            probabilitySandStalactites = 0;
        }
        // --other------
        category = Configuration.CATEGORY_GENERAL;
        timesPerChunck = config.get(category, "times to attempt generating per chunk", 40).getInt();
        maxGenHeight = config.get(category, "Max height at which to generate", 80).getInt();
        maxLength = config.get(category, "Max length of structure generation", 8).getInt();
        if(config.hasChanged()){
            config.save();
        }
	}
}
