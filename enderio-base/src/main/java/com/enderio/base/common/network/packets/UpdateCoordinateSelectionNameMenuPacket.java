package com.enderio.base.common.network.packets;

import com.enderio.base.common.menu.CoordinateMenu;
import com.enderio.base.common.network.ClientToServerMenuPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

public class UpdateCoordinateSelectionNameMenuPacket extends ClientToServerMenuPacket<CoordinateMenu> {

    private final String name;

    public UpdateCoordinateSelectionNameMenuPacket(int containerID, String name) {
        super(CoordinateMenu.class, containerID);
        this.name = name;
    }

    protected UpdateCoordinateSelectionNameMenuPacket(FriendlyByteBuf buf) {
        super(CoordinateMenu.class, buf);
        name = buf.readUtf(50);
    }

    @Override
    protected void write(FriendlyByteBuf writeInto) {
        super.write(writeInto);
        writeInto.writeUtf(name, 50);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        getMenu(context).updateName(name, context.getSender());
    }

    public static class Handler extends ClientToServerMenuPacket.Handler<UpdateCoordinateSelectionNameMenuPacket> {

        @Override
        public UpdateCoordinateSelectionNameMenuPacket of(FriendlyByteBuf buf) {
            return new UpdateCoordinateSelectionNameMenuPacket(buf);
        }
    }
}
