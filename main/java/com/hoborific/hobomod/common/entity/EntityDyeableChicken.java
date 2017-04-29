package com.hoborific.hobomod.common.entity;

import com.google.common.collect.Maps;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Map;

import static net.minecraft.entity.passive.EntitySheep.getRandomSheepColor;

public class EntityDyeableChicken extends EntityChicken{
    public EntityDyeableChicken(World worldIn){
        super(worldIn);
    }
    private static final Map<EnumDyeColor, float[]> DYE_TO_RGB = Maps.newEnumMap(EnumDyeColor.class);
    private static final DataParameter<Byte> DYE_COLOR = EntityDataManager.createKey(EntityDyeableChicken.class, DataSerializers.BYTE);
    static
    {
        DYE_TO_RGB.put(EnumDyeColor.WHITE, new float[] {1.0F, 1.0F, 1.0F});
        DYE_TO_RGB.put(EnumDyeColor.ORANGE, new float[] {0.85F, 0.5F, 0.2F});
        DYE_TO_RGB.put(EnumDyeColor.MAGENTA, new float[] {0.7F, 0.3F, 0.85F});
        DYE_TO_RGB.put(EnumDyeColor.LIGHT_BLUE, new float[] {0.4F, 0.6F, 0.85F});
        DYE_TO_RGB.put(EnumDyeColor.YELLOW, new float[] {0.9F, 0.9F, 0.2F});
        DYE_TO_RGB.put(EnumDyeColor.LIME, new float[] {0.5F, 0.8F, 0.1F});
        DYE_TO_RGB.put(EnumDyeColor.PINK, new float[] {0.95F, 0.5F, 0.65F});
        DYE_TO_RGB.put(EnumDyeColor.GRAY, new float[] {0.3F, 0.3F, 0.3F});
        DYE_TO_RGB.put(EnumDyeColor.SILVER, new float[] {0.6F, 0.6F, 0.6F});
        DYE_TO_RGB.put(EnumDyeColor.CYAN, new float[] {0.3F, 0.5F, 0.6F});
        DYE_TO_RGB.put(EnumDyeColor.PURPLE, new float[] {0.5F, 0.25F, 0.7F});
        DYE_TO_RGB.put(EnumDyeColor.BLUE, new float[] {0.2F, 0.3F, 0.7F});
        DYE_TO_RGB.put(EnumDyeColor.BROWN, new float[] {0.4F, 0.3F, 0.2F});
        DYE_TO_RGB.put(EnumDyeColor.GREEN, new float[] {0.4F, 0.5F, 0.2F});
        DYE_TO_RGB.put(EnumDyeColor.RED, new float[] {0.6F, 0.2F, 0.2F});
        DYE_TO_RGB.put(EnumDyeColor.BLACK, new float[] {0.1F, 0.1F, 0.1F});
    }


    public boolean processInteract(EntityPlayer player, EnumHand hand){
        EntityPlayer p = player;
        ItemStack itemstack = p.getHeldItem(hand);
        if(itemstack.getItem() instanceof ItemDye){
            EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(itemstack.getMetadata());

            this.setFeatherColor(enumdyecolor);
            itemstack.func_190918_g(1);
        }
        return super.processInteract(player, hand);
    }

    public static float[] getDyeRgb(EnumDyeColor dyeColor)
    {
        return DYE_TO_RGB.get(dyeColor);
    }
    public EnumDyeColor getFeatherColor()
    {
        return EnumDyeColor.byMetadata(this.dataManager.get(DYE_COLOR) & 15);
    }

    public void setFeatherColor(EnumDyeColor color)
    {
        byte b0 = this.dataManager.get(DYE_COLOR);
        this.dataManager.set(DYE_COLOR, (byte) (b0 & 240 | color.getMetadata() & 15));
    }
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(DYE_COLOR, (byte) 0);
    }

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setFeatherColor(getRandomSheepColor(this.worldObj.rand));
        return livingdata;
    }
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setByte("Color", (byte)this.getFeatherColor().getMetadata());
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setFeatherColor(EnumDyeColor.byMetadata(compound.getByte("Color")));
    }
}

