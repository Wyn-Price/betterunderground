package com.wynprice.betterunderground;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends ServerProxy{
    private static final String PREFIX = "betterunderground";
    @Override
    public void registerModelBakery() {
    	mb(BetterUnderground.blockStoneStalactite, BetterUnderground.stalacs, "stone_");
    	mb(BetterUnderground.blockSandStalactite, BetterUnderground.sandStalacs, "sandstone_");
    	mb(BetterUnderground.blockDecorations, BetterUnderground.icicles, "icicle_");
   		mb(BetterUnderground.blockFlora, BetterUnderground.caps, "flora_");
   		mb(BetterUnderground.blockFossils, BetterUnderground.fossils, "fossil_");
   		mb(BetterUnderground.mossyDirt, BetterUnderground.mossy, "mossy_");
    }

	private void mb(Block block, ArrayList<String> size, String Suffix)
	{
		
		ArrayList<ResourceLocation> ArrayNames = new ArrayList<ResourceLocation>();
    	Item item = Item.getItemFromBlock(block);
        for(int i = 0; i < size.size(); i++)
        	ArrayNames.add(new ResourceLocation(PREFIX, Suffix + i)); 
        	
        ResourceLocation[] names = new ResourceLocation[ArrayNames.size()];
        names = ArrayNames.toArray(names);
    	ModelBakery.registerItemVariants(Item.getItemFromBlock(BetterUnderground.blockStoneStalactite), names);
    	
	}
	
	
    private void regItemMesher(Block block, ArrayList<String> size, String Suffix)
    {
    	Item item = Item.getItemFromBlock(block);
        for(int i = 0; i < size.size(); i++) {
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(new ResourceLocation(PREFIX, Suffix + Integer.toString(i)), "inventory"));
        }
    }     

    
	   @Override
	public void registerRenders() {
		   regItemMesher(BetterUnderground.blockStoneStalactite, BetterUnderground.stalacs, "stone_");
	   		regItemMesher(BetterUnderground.blockSandStalactite, BetterUnderground.sandStalacs, "sandstone_");
	   		regItemMesher(BetterUnderground.blockDecorations, BetterUnderground.icicles, "icicle_");
	   		regItemMesher(BetterUnderground.blockFlora, BetterUnderground.caps, "flora_");
	   		regItemMesher(BetterUnderground.blockFossils, BetterUnderground.fossils, "fossil_");
	   		regItemMesher(BetterUnderground.mossyDirt, BetterUnderground.mossy, "mossy_");
	}
      
	  @Override
     public void init() {
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor() {
            @Override
            public int colorMultiplier(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockPos blockPos, int i) {
                if(iBlockState != null){
                    if(BetterUnderground.blockFlora.getMetaFromState(iBlockState) < 6)
                        return iBlockAccess != null ? BiomeColorHelper.getFoliageColorAtPos(iBlockAccess, blockPos) : ColorizerFoliage.getFoliageColorBasic();
                }
                return -1;
            }
        }, BetterUnderground.blockFlora);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
            @Override
            public int colorMultiplier(ItemStack itemStack, int i) {
                if(itemStack.getMetadata() < 6)
                    return ColorizerFoliage.getFoliageColorBasic();
                return -1;
            }
        }, BetterUnderground.blockFlora);
    	ModBlocks.invtab();
    }
}
