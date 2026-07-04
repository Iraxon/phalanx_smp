package com.github.iraxon.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.HumanoidModel;

import com.github.iraxon.entity.ArmyStandardEntity;

public class ArmyStandardRenderer extends HumanoidMobRenderer<ArmyStandardEntity, HumanoidModel<ArmyStandardEntity>> {
	private final ResourceLocation entityTexture = new ResourceLocation("phalanx_smp:textures/entities/deepslate_golem.png");

	public ArmyStandardRenderer(EntityRendererProvider.Context context) {
		super(context, new HumanoidModel<ArmyStandardEntity>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
		this.addLayer(new HumanoidArmorLayer(this, new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
	}

	@Override
	public ResourceLocation getTextureLocation(ArmyStandardEntity entity) {
		return entityTexture;
	}
}