/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package com.github.iraxon.init;

import org.lwjgl.glfw.GLFW;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

import com.github.iraxon.network.IssueOrdersMessage;
import com.github.iraxon.PhalanxSmpMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PhalanxSmpModKeyMappings {
	public static final KeyMapping ISSUE_ORDERS = new KeyMapping("key.phalanx_smp.issue_orders", GLFW.GLFW_KEY_R, "key.categories.phalanx") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PhalanxSmpMod.PACKET_HANDLER.sendToServer(new IssueOrdersMessage(0, 0));
				IssueOrdersMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				ISSUE_ORDERS_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - ISSUE_ORDERS_LASTPRESS);
				PhalanxSmpMod.PACKET_HANDLER.sendToServer(new IssueOrdersMessage(1, dt));
				IssueOrdersMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	private static long ISSUE_ORDERS_LASTPRESS = 0;

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(ISSUE_ORDERS);
	}

	@Mod.EventBusSubscriber(Dist.CLIENT)
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(TickEvent.ClientTickEvent event) {
			if (Minecraft.getInstance().screen == null) {
				ISSUE_ORDERS.consumeClick();
			}
		}
	}
}