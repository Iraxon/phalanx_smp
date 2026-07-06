package com.github.iraxon.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import com.github.iraxon.entity.ArmyStandardEntity;
import com.github.iraxon.client.model.Modelarmystandard;

public class ArmyStandardRenderer extends MobRenderer<ArmyStandardEntity, Modelarmystandard<ArmyStandardEntity>> {
	private final ResourceLocation entityTexture = new ResourceLocation("phalanx_smp:textures/entities/army_standard_tex.png");

	public ArmyStandardRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelarmystandard<ArmyStandardEntity>(context.bakeLayer(Modelarmystandard.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(ArmyStandardEntity entity) {
		return entityTexture;
	}
}