package com.wynprice.betterunderground;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = "betterunderground", name = "Better Underground", version = "1.0.6", useMetadata = true)
public final class BetterUnderground {
    @SidedProxy(clientSide = "com.wynprice.betterunderground.ClientProxy", serverSide = "com.wynprice.betterunderground.ServerProxy")
    public static ServerProxy proxy;

    public static final ArrayList<String> stalacs = new ArrayList<String>(Arrays.asList("stalactite1", "stalactite2", "stalactite3", "stalactite4", "stalactiteConnection1", "stalactiteConnection2", "stalactiteConnection3",
            "stalactiteConnection4", "stalactiteEnd", "stalacmiteEnd", "stalacmite1", "stalacmite2", "stalacmite3"));
    public static final ArrayList<String> sandStalacs = new ArrayList<String>(Arrays.asList("sandstoneStalactite1", "sandstoneStalactite2", "sandstoneStalactite3", "sandstoneStalactite4", "sandstoneStalactiteConnection1",
            "sandstoneStalactiteConnection2", "sandstoneStalactiteConnection3", "sandstoneStalactiteConnection4", "sandstoneStalactiteEnd", "sandstoneStalacmiteEnd", "sandstoneStalacmite1",
            "sandstoneStalacmite2", "sandstoneStalacmite3"));
    public static final ArrayList<String> icicles = new ArrayList<String>(Arrays.asList("icicle1", "icicle2", "icicle3"));
    public static final ArrayList<String> caps = new ArrayList<String>(Arrays.asList("glowcap1", "glowcap2", "glowcap3", "gloweed1", "glowcap4top", "glowcap4bottom", "bluecap1", "bluecap2", "bluecap3", "bluecap4"));
	public static final ArrayList<String> fossils = new ArrayList<String>(Arrays.asList("fossil1"));
    public static Block blockFlora, blockDecorations, blockFossils;
	public static Block blockStoneStalactite, blockSandStalactite;
	public static boolean solidStalactites, damageWhenFallenOn, bonePileArrowDrop;
	private static Configuration config;
    private static int chestSkull = 50;
    

	@EventHandler
	public void load(FMLInitializationEvent event) {
		proxy.init();
		WorldGenBetterUnderGround gen = new WorldGenBetterUnderGround(config);
		if (gen.maxLength > 0)
            MinecraftForge.EVENT_BUS.register(gen);
        if(chestSkull > 0)
            MinecraftForge.EVENT_BUS.register(this);
	}
	
	public static CreativeTabs tab;
	
    @EventHandler
	public void preInit(FMLPreInitializationEvent event) {
    	ModBlocks.init();
    	ModBlocks.reg();
		config = new Configuration(event.getSuggestedConfigurationFile());
        solidStalactites = config.getBoolean("Solid stalactites/stalgmites", Configuration.CATEGORY_GENERAL, false, "Whether stalactites can be collided with.");
        damageWhenFallenOn = config.getBoolean("Stalgmites damage entities when fallen on", Configuration.CATEGORY_GENERAL, false, "Whether living beings would be damaged when falling on the block.");
        bonePileArrowDrop = config.getBoolean("Fossils have an arrow drop", Configuration.CATEGORY_GENERAL, true, "When Fossils are broken, should their be a chance of an arrow being dropped");
        int floraLightLevel = config.getInt("Flora light level", Configuration.CATEGORY_GENERAL, 5, 0, 15, "How much light is emitted by the mushrooms.");

        tab = new CreativeTabs("betterunderground") {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(ModBlocks.SS, 1, 0);
            }
        };
        int chanceForNodeToSpawn = config.get(Configuration.CATEGORY_GENERAL, "Chance for a fossil node to generate", 5).getInt();
        if(chanceForNodeToSpawn > 0) {
            MinecraftForge.ORE_GEN_BUS.register(new EventManager(chanceForNodeToSpawn));
        }
        
        chestSkull = config.get(Configuration.CATEGORY_GENERAL, "Chance for a skull to be added in chests", chestSkull).getInt();

        if(event.getSourceFile().getName().endsWith(".jar")){
            proxy.MUD();
        }
        proxy.registerRenders();
	}

    @SubscribeEvent
    public void onLootLoad(LootTableLoadEvent loading){
        if(loading.getName() == LootTableList.CHESTS_SIMPLE_DUNGEON || loading.getName() == LootTableList.CHESTS_ABANDONED_MINESHAFT || loading.getName() == LootTableList.CHESTS_STRONGHOLD_CORRIDOR) {
            loading.getTable().addPool(new LootPool(new LootEntry[]{new LootEntryItem(Items.SKULL, 1, 0, new LootFunction[]{new SetMetadata(null, new RandomValueRange(0, 4))}, new LootCondition[0], "skull")}, new LootCondition[]{new RandomChance(1 / (float)chestSkull)}, new RandomValueRange(1, 1), new RandomValueRange(0, 0), "skulls"));
        }
    }


}
