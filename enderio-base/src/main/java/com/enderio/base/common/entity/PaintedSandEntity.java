package com.enderio.base.common.entity;

import com.enderio.base.EIOEntities;
import com.enderio.base.common.util.PaintUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fmllegacy.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

public class PaintedSandEntity extends FallingBlockEntity implements IEntityAdditionalSpawnData {

    public PaintedSandEntity(EntityType<? extends FallingBlockEntity> p_31950_, Level level) {
        super(p_31950_, level);
    }

    public PaintedSandEntity(Level p_31953_, double p_31954_, double p_31955_, double p_31956_, BlockState p_31957_) {
        super(p_31953_, p_31954_, p_31955_, p_31956_, p_31957_);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public EntityType<?> getType() {
        return EIOEntities.PAINTED_SAND.get();
    }

    public Block getPaint() {
        if (blockData != null) {
            return PaintUtils.getBlockFromRL(blockData.getString("paint"));
        }
        return null;
    }
    public void setPaint(Block block) {
        if (blockData == null)
            blockData = new CompoundTag();
        blockData.putString("paint", block.getRegistryName().toString());
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        Block block = getPaint();
        buffer.writeResourceLocation(block != null ? block.getRegistryName() : new ResourceLocation(""));
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        ResourceLocation rl = additionalData.readResourceLocation();
        Block block = ForgeRegistries.BLOCKS.getValue(rl);
        if (block != null)
            setPaint(block);
    }
}
