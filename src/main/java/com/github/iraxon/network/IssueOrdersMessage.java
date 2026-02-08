package com.github.iraxon.network;

import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

import com.github.iraxon.PhalanxSmpMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class IssueOrdersMessage {
	int type, pressedms;

	public IssueOrdersMessage(int type, int pressedms) {
		this.type = type;
		this.pressedms = pressedms;
	}

	public IssueOrdersMessage(FriendlyByteBuf buffer) {
		this.type = buffer.readInt();
		this.pressedms = buffer.readInt();
	}

	public static void buffer(IssueOrdersMessage message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.type);
		buffer.writeInt(message.pressedms);
	}

	public static void handler(IssueOrdersMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
		});
		context.setPacketHandled(true);
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		PhalanxSmpMod.addNetworkMessage(IssueOrdersMessage.class, IssueOrdersMessage::buffer, IssueOrdersMessage::new, IssueOrdersMessage::handler);
	}
}