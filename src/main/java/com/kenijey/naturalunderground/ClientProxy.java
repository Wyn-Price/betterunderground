package com.kenijey.naturalunderground;

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
    private static final String PREFIX = "naturalunderground";
    @Override
    public void registerModelBakery() {
    	mb(NaturalUnderground.blockStoneStalactite, NaturalUnderground.stalacs, "stone_");
    	mb(NaturalUnderground.blockSandStalactite, NaturalUnderground.sandStalacs, "sandstone_");
    	mb(NaturalUnderground.blockDecorations, NaturalUnderground.icicles, "icicle_");
   		mb(NaturalUnderground.blockFlora, NaturalUnderground.caps, "flora_");
   		mb(NaturalUnderground.blockFossils, NaturalUnderground.fossils, "fossil_");
   		mb(NaturalUnderground.mossyDirt, NaturalUnderground.mossy, "mossy_");
    }

	private void mb(Block block, ArrayList<String> size, String Suffix)
	{
		
		ArrayList<ResourceLocation> ArrayNames = new ArrayList<ResourceLocation>();
    	Item item = Item.getItemFromBlock(block);
        for(int i = 0; i < size.size(); i++)
        	ArrayNames.add(new ResourceLocation(PREFIX, Suffix + i)); 
        	
        ResourceLocation[] names = new ResourceLocation[ArrayNames.size()];
        names = ArrayNames.toArray(names);
    	ModelBakery.registerItemVariants(Item.getItemFromBlock(NaturalUnderground.blockStoneStalactite), names);
    	
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
		   regItemMesher(NaturalUnderground.blockStoneStalactite, NaturalUnderground.stalacs, "stone_");
	   		regItemMesher(NaturalUnderground.blockSandStalactite, NaturalUnderground.sandStalacs, "sandstone_");
	   		regItemMesher(NaturalUnderground.blockDecorations, NaturalUnderground.icicles, "icicle_");
	   		regItemMesher(NaturalUnderground.blockFlora, NaturalUnderground.caps, "flora_");
	   		regItemMesher(NaturalUnderground.blockFossils, NaturalUnderground.fossils, "fossil_");
	   		regItemMesher(NaturalUnderground.mossyDirt, NaturalUnderground.mossy, "mossy_");
	}
      
	  @Override
     public void init() {
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor() {
            @Override
            public int colorMultiplier(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockPos blockPos, int i) {
                if(iBlockState != null){
                    if(NaturalUnderground.blockFlora.getMetaFromState(iBlockState) < 6)
                        return iBlockAccess != null ? BiomeColorHelper.getFoliageColorAtPos(iBlockAccess, blockPos) : ColorizerFoliage.getFoliageColorBasic();
                }
                return -1;
            }
        }, NaturalUnderground.blockFlora);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
            @Override
            public int colorMultiplier(ItemStack itemStack, int i) {
                if(itemStack.getMetadata() < 6)
                    return ColorizerFoliage.getFoliageColorBasic();
                return -1;
            }
        }, NaturalUnderground.blockFlora);
    	ModBlocks.invtab();
    }
}
