// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modelarmystandard<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "armystandard"), "main");
	private final ModelPart Poles;

	public Modelarmystandard(ModelPart root) {
		this.Poles = root.getChild("Poles");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Poles = partdefinition.addOrReplaceChild("Poles",
				CubeListBuilder.create().texOffs(58, 1)
						.addBox(-0.5F, -49.0F, -0.499F, 1.0F, 49.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 13)
						.addBox(-4.0F, -55.0F, 0.001F, 8.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		Poles.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}