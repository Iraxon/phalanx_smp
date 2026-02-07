package com.github.iraxon.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.HumanoidModel;

import com.github.iraxon.entity.DeepslateGolemEntity;

public class DeepslateGolemRenderer extends HumanoidMobRenderer<DeepslateGolemEntity, HumanoidModel<DeepslateGolemEntity>> {
	public DeepslateGolemRenderer(EntityRendererProvider.Context context) {
		super(context, new HumanoidModel<DeepslateGolemEntity>(context.bakeLayer(ModelLayers.PLAYER)), 0.7f);
		this.addLayer(new HumanoidArmorLayer(this, new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
	}

	@Override
	public ResourceLocation getTextureLocation(DeepslateGolemEntity entity) {
		return new ResourceLocation("phalanx_smp:textures/entities/mountain_raider.png");
	}
}