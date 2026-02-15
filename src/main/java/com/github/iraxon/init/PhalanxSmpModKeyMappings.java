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

import com.github.iraxon.network.IssueOrdersUpInputMessage;
import com.github.iraxon.network.IssueOrdersDownInputMessage;
import com.github.iraxon.network.IssueOrdersConfirmMessage;
import com.github.iraxon.network.IssueOrdersCancelMessage;
import com.github.iraxon.PhalanxSmpMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PhalanxSmpModKeyMappings {
	public static final KeyMapping ISSUE_ORDERS_CANCEL = new KeyMapping("key.phalanx_smp.issue_orders_cancel", GLFW.GLFW_KEY_LEFT, "key.categories.phalanx") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PhalanxSmpMod.PACKET_HANDLER.sendToServer(new IssueOrdersCancelMessage(0, 0));
				IssueOrdersCancelMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping ISSUE_ORDERS_UP_INPUT = new KeyMapping("key.phalanx_smp.issue_orders_up_input", GLFW.GLFW_KEY_UP, "key.categories.phalanx") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PhalanxSmpMod.PACKET_HANDLER.sendToServer(new IssueOrdersUpInputMessage(0, 0));
				IssueOrdersUpInputMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping ISSUE_ORDERS_DOWN_INPUT = new KeyMapping("key.phalanx_smp.issue_orders_down_input", GLFW.GLFW_KEY_DOWN, "key.categories.phalanx") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PhalanxSmpMod.PACKET_HANDLER.sendToServer(new IssueOrdersDownInputMessage(0, 0));
				IssueOrdersDownInputMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping ISSUE_ORDERS_CONFIRM = new KeyMapping("key.phalanx_smp.issue_orders_confirm", GLFW.GLFW_KEY_RIGHT, "key.categories.phalanx") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PhalanxSmpMod.PACKET_HANDLER.sendToServer(new IssueOrdersConfirmMessage(0, 0));
				IssueOrdersConfirmMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(ISSUE_ORDERS_CANCEL);
		event.register(ISSUE_ORDERS_UP_INPUT);
		event.register(ISSUE_ORDERS_DOWN_INPUT);
		event.register(ISSUE_ORDERS_CONFIRM);
	}

	@Mod.EventBusSubscriber(Dist.CLIENT)
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(TickEvent.ClientTickEvent event) {
			if (Minecraft.getInstance().screen == null) {
				ISSUE_ORDERS_CANCEL.consumeClick();
				ISSUE_ORDERS_UP_INPUT.consumeClick();
				ISSUE_ORDERS_DOWN_INPUT.consumeClick();
				ISSUE_ORDERS_CONFIRM.consumeClick();
			}
		}
	}
}