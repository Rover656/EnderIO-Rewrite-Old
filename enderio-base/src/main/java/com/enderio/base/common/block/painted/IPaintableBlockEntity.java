package com.enderio.base.common.block.painted;

import net.minecraft.world.level.block.Block;

public interface IPaintableBlockEntity {
    Block getPaint();
    default Block[] getPaints() {
        return new Block[]{getPaint()};
    }
}
