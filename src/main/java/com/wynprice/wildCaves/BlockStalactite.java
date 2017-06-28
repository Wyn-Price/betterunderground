package com.wynprice.wildCaves;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.wynprice.wildCaves.generation.structureGen.GenerateStoneStalactite;

public class BlockStalactite extends Block {
    private final Item droppedItem;
	private PropertyInteger ALL_TYPE;

	public BlockStalactite(Item drop) {
		super(Material.ROCK);
        this.droppedItem = drop;
		this.setHardness(0.8F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(ALL_TYPE, 0));
	}

	public int getNumOfStructures(){
		return WildCaves.sandStalacs.size();
	}

	public boolean isUp(IBlockState state){
		return getMetaFromState(state) < 4;
	}


	@Override
	protected BlockStateContainer createBlockState(){
		if(ALL_TYPE == null) {
			ALL_TYPE = PropertyInteger.create("type", 0, getNumOfStructures() - 1);
		}
		return new BlockStateContainer(this, ALL_TYPE);
	}

	@Override
	public int getMetaFromState(IBlockState state){
		return state.getValue(ALL_TYPE);
	}

	@Override
	public IBlockState getStateFromMeta(int meta){
		return this.getDefaultState().withProperty(ALL_TYPE, meta);
	}

    @Override
    public Item getItemDropped(IBlockState metadata, Random random, int par3) {
        return droppedItem;
    }

    @Override
    public int quantityDropped(Random rand) {
        return rand.nextInt(3) - 1;
    }

	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
		boolean result = false;
		int metadata = getMetaFromState(state);
		if ((metadata != 0 && metadata < 4) || metadata == 7 || metadata == 11)
			result = connected(world, pos, true);
		else if (metadata == 6 || (metadata > 7 && metadata < 11) || metadata == 12)
			result = connected(world, pos, false);
		else if (metadata == 0 || metadata == 4 || metadata == 5)
			result = connected(world, pos, true) || connected(world, pos, false);
		return result;
	}

	@Override
	protected boolean canSilkHarvest() {
		return true;
	}

	//aux funtion for canblockStay
	public boolean connected(World world, BlockPos pos, boolean searchUp) {
		int increment;
		int i;
		if (searchUp)
			increment = 1;
		else
			increment = -1;
		i = increment;
		while (world.getBlockState(pos.up(i)).getBlock() == WildCaves.blockStoneStalactite || world.getBlockState(pos.up(i)).getBlock() == WildCaves.blockSandStalactite)
			i = i + increment;
		return world.getBlockState(pos.up(i)).isNormalCube();
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		for (int i = 0; i < getNumOfStructures(); ++i) {
			items.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
		double x = pos.getX();
		double y = pos.getY();
		double z = pos.getZ();
		Boolean hitOtherBlock = false;
		double finalY = 0d;
		int times = -1;
		if(!this.canBlockStay(world, pos, state))
		{
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
			EntityPlayer p = (EntityPlayer)par5EntityLivingBase;
			par6ItemStack.setCount(par6ItemStack.getCount() + 1);
			p.inventory.setInventorySlotContents(p.inventory.currentItem, par6ItemStack);
			return;
		}
		if(world.getBlockState(pos.add(0, 1, 0)).getBlock() == (Block)this && world.getBlockState(pos.add(0, -1, 0)).getBlock() == (Block)this)
			{
				world.setBlockState(pos, getStateFromMeta(Utils.randomChoise(4,5)), 2);	
				if(Arrays.asList(getStateFromMeta(6), getStateFromMeta(7)).contains((world.getBlockState(pos.add(0, 1, 0)))))
					world.setBlockState(pos.add(0, 1, 0), getStateFromMeta(Utils.randomChoise(4,5)), 2);
				if(Arrays.asList(getStateFromMeta(6), getStateFromMeta(7)).contains((world.getBlockState(pos.add(0, -1, 0)))))
					world.setBlockState(pos.add(0, -1, 0), getStateFromMeta(Utils.randomChoise(4,5)), 2);
				if(Arrays.asList(getStateFromMeta(1), getStateFromMeta(2)).contains((world.getBlockState(pos.add(0, 1, 0)))))
					world.setBlockState(pos.add(0, 1, 0), getStateFromMeta(3), 2);
				if(Arrays.asList(getStateFromMeta(9), getStateFromMeta(10)).contains((world.getBlockState(pos.add(0, -1, 0)))))
					world.setBlockState(pos.add(0, -1, 0), getStateFromMeta(8), 2);
				return;
			}
		if(world.isAirBlock(new BlockPos(x, y-1, z)) || world.getBlockState(new BlockPos(x, y+1, z)).getBlock() == this)
		{
			for(int i = (int) y; !hitOtherBlock; i++)
			{
				if(world.getBlockState(new BlockPos(x, i ,z)).getBlock() !=  (Block)this)
				{
					hitOtherBlock = true;
					finalY = i - 1;
				}
				times++;
			}
			BlockPos startup = new BlockPos(x, finalY, z);
			GenerateDown(world, new Random(), startup, times, WorldGenWildCaves.maxLength, par5EntityLivingBase, par6ItemStack);
		}
		if(world.isAirBlock(pos.add(0, 1, 0)) || world.getBlockState(pos.add(0, -1, 0)).getBlock() == this)
		{
			for(int i = (int) y; !hitOtherBlock; i--)
			{
				if(world.getBlockState(new BlockPos(x, i ,z)).getBlock() !=  (Block)this)
				{
					hitOtherBlock = true;
					finalY = i + 1;
				}
				times++;
			}
			BlockPos startup = new BlockPos(x, finalY, z);
			GenerateUp(world, new Random(), startup, times, WorldGenWildCaves.maxLength, par5EntityLivingBase, par6ItemStack);
		}
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) 
	{
		if(state != getStateFromMeta(0))
		{
			int up = -1;
			for(int i = 0; i < WorldGenWildCaves.maxLength; i++)
			{
				up = world.getBlockState(new BlockPos(pos.getX(), pos.getY() - i , pos.getZ())).getBlock() == (Block) this ? pos.getY() + i : up;
			}
			if(world.getBlockState(new BlockPos(pos.getX(), up + 1, pos.getZ())).getBlock() == Blocks.AIR)
			{
				destroyUpwards(world, pos, state, up - pos.getY());
				return;
			}
			int down = -1;
			for(int i = 0; i < WorldGenWildCaves.maxLength; i++)
			{
				down = world.getBlockState(pos.add(0, i, 0)).getBlock() == (Block) this ? i + pos.getY() : down;
			}
			if(world.getBlockState(new BlockPos(pos.getX(), down - 1, pos.getZ())).getBlock() == Blocks.AIR)
			{
				destroyDownwards(world, pos, state, pos.getY() - down);
				return;
			}
			if(world.getBlockState(new BlockPos(pos.getX(), down, pos.getZ())) != getStateFromMeta(8))
				world.setBlockState(new BlockPos(pos.getX(), down, pos.getZ()), getStateFromMeta(8));
			if(world.getBlockState(new BlockPos(pos.getX(), down - 1, pos.getZ())).getBlock() != (Block)this && pos.getY() - down == 1)
			{
				world.setBlockState(new BlockPos(pos.getX(), down, pos.getZ()), getStateFromMeta(Utils.randomChoise(9,10)), 2);
				if(world.getBlockState(pos.add(0, 1, 0)).getBlock() == (Block)this)
					world.setBlockState(pos.add(0, 1, 0), getStateFromMeta(7));
				return;
			}
			IBlockState upState;
			if(world.getBlockState(pos.add(0, 1, 0)).getBlock() != (Block)this)
				upState = world.getBlockState(pos.add(0, 1, 0));
			else if(world.getBlockState(pos.add(0, 1, 0)) != getStateFromMeta(3))
				upState = getStateFromMeta(7);
			else
				upState = getStateFromMeta(Utils.randomChoise(1,2));
			IBlockState downState = world.getBlockState(pos.add(0, -1, 0)).getBlock() != (Block)this ? world.getBlockState(pos.add(0, -1, 0)) : getStateFromMeta(6);  
			destroyUpdate(world, pos, upState, downState);
		}
	}
	
	private void GenerateDown(World world, Random random, BlockPos pos, int distance, int maxLength, EntityLivingBase player, ItemStack itemStack)
	{
		double x = pos.getX();
		double y = pos.getY();
		double z = pos.getZ();
	
		for(int i = 0; i < distance; i++)
		{
			pos = new BlockPos(x, y - i, z);
			if(i >= maxLength)
			{
				world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
				pos = new BlockPos(x, y - i + 1, z);
				world.setBlockState(pos, getStateFromMeta(Arrays.asList(Blocks.AIR, (Block)this).contains(world.getBlockState(new BlockPos(x, y - i - 1, z)).getBlock())? Utils.randomChoise(7,11) : 8), 2);
				EntityPlayer p = (EntityPlayer)player;
				itemStack.setCount(itemStack.getCount() + 1);
				p.inventory.setInventorySlotContents(p.inventory.currentItem, itemStack);
				return;
			}
			if(distance == 1)
				world.setBlockState(pos, getStateFromMeta(Utils.randomChoise(1,2)), 2);
			else if(i==0)
				world.setBlockState(pos, getStateFromMeta(3), 2);
			else if(i==distance - 1)
				world.setBlockState(pos, getStateFromMeta(Arrays.asList(Blocks.AIR, (Block)this).contains(world.getBlockState(new BlockPos(x, y - i - 1, z)).getBlock())? Utils.randomChoise(7,11) : 8), 2);	
			else if(i==distance - 2 && !Arrays.asList(getStateFromMeta(5), getStateFromMeta(4)).contains(world.getBlockState(pos)))
				world.setBlockState(pos, getStateFromMeta(Utils.randomChoise(4,5)), 2);		
		}
	}
	
	private void GenerateUp(World world, Random random, BlockPos pos , int distance, int maxLength, EntityLivingBase player, ItemStack itemStack)
	{
		
		double x = pos.getX();
		double y = pos.getY();
		double z = pos.getZ();;
		for(int i = 0; i < distance; i++)
		{
			pos = new BlockPos(x, y + i, z);
			if(i >= maxLength)
			{
				world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
				pos = pos.add(0, -1, 0);
				world.setBlockState(pos, getStateFromMeta(Arrays.asList(Blocks.AIR, (Block)this).contains(world.getBlockState(new BlockPos(x, y - i - 1, z)).getBlock())? Utils.randomChoise(6,12) : 8), 2);
				EntityPlayer p = (EntityPlayer)player;
				itemStack.setCount(itemStack.getCount() + 1);
				p.inventory.setInventorySlotContents(p.inventory.currentItem, itemStack);
				return;
			}
			if(distance == 1)
				world.setBlockState(pos, getStateFromMeta(Utils.randomChoise(9,10)), 2);
			else if(i==0)
				world.setBlockState(pos, getStateFromMeta(8), 2);
			else if(i==distance - 1)
				world.setBlockState(pos, getStateFromMeta(Arrays.asList(Blocks.AIR).contains(world.getBlockState(pos.add(0, 1, 0)).getBlock())? Utils.randomChoise(6,12) : 3), 2);	
			else if(i==distance - 2 && !Arrays.asList(getStateFromMeta(5), getStateFromMeta(4)).contains(world.getBlockState(pos)))
				world.setBlockState(pos, getStateFromMeta(Utils.randomChoise(4,5)), 2);		

		}
	}
	
	private void destroyUpwards(World world, BlockPos pos, IBlockState state,  int distance)
	{
		for(int i = 0; i <= distance; i ++)
		{
			BlockPos position = new BlockPos(pos.getX(), pos.getY() + i, pos.getZ());
			this.dropBlockAsItem(world, position, getDefaultState(), 0);
			world.setBlockToAir(position);
			BlockPos posDown = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());
			IBlockState upState;
			if(world.getBlockState(posDown).getBlock() != (Block)this)
				upState = world.getBlockState(posDown);
			else if(!Arrays.asList(getStateFromMeta(8)).contains(world.getBlockState(posDown)))
				upState = getStateFromMeta(12);
			else
				upState = getStateFromMeta(Utils.randomChoise(1,2));
			world.setBlockState(posDown, upState);
		}			
	}
	
	private void destroyDownwards(World world, BlockPos pos, IBlockState state,  int distance)
	{
		for(int i = 0; i <= distance; i ++)
		{
			BlockPos position = new BlockPos(pos.getX(), pos.getY() - i, pos.getZ());
			this.dropBlockAsItem(world, position, getDefaultState(), 0);
			world.setBlockToAir(position);
			BlockPos posUp = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
			IBlockState upState;
			if(world.getBlockState(posUp).getBlock() != (Block)this)
				upState = world.getBlockState(posUp);
			else if(!Arrays.asList(getStateFromMeta(1), getStateFromMeta(2), getStateFromMeta(3)).contains(world.getBlockState(posUp)))
				upState = getStateFromMeta(7);
			else
				upState = getStateFromMeta(Utils.randomChoise(1,2));
			world.setBlockState(posUp, upState);
		}			
	}
	
	private void destroyUpdate(World world, BlockPos pos, IBlockState stateUp, IBlockState stateDown)
	{
		BlockPos posUp = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
		BlockPos posDown = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());
		world.setBlockState(posUp, stateUp, 2);
		world.setBlockState(posDown, stateDown, 2);
	}

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (!this.canBlockStay(world, pos, state)){
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }
    
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
		if(entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode && ((EntityPlayer) entity).capabilities.isFlying){
            return;
        }
		entity.motionX *= 0.7D;
		entity.motionZ *= 0.7D;
	}

	@Override
	public void onFallenUpon(World world, BlockPos pos, Entity entity, float par6) {
		if (WildCaves.damageWhenFallenOn && entity.isEntityAlive()) {
			entity.attackEntityFrom(DamageSource.GENERIC, 5);
		}
	}

	@Override
	public void onLanded(World world, Entity entity) {
		if(WildCaves.solidStalactites)
			super.onLanded(world, entity);
	}

	
	@Override
	public void onNeighborChange(IBlockAccess worldIn, BlockPos pos, BlockPos neighbor) {
		World world = (World) worldIn;
		if (!world.isRemote && !this.canBlockStay(world, pos, world.getBlockState(pos))) {
			world.destroyBlock(pos, true);
		}
	}

	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isBlockNormalCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isPassable(IBlockAccess access, BlockPos pos) {
		return !WildCaves.solidStalactites || super.isPassable(access, pos);
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean p_185477_7_) {
		if(WildCaves.solidStalactites)
			addCollisionBoxToList(state, world, pos, entityBox, collidingBoxes, entityIn, p_185477_7_);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess par1IBlockAccess, BlockPos pos) {
		return Utils.getBox(getMetaFromState(state));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isTranslucent(IBlockState state) {
		return true;
	}
	
	
}
