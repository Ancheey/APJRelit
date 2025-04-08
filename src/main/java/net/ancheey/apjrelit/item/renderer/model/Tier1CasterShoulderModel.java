package net.ancheey.apjrelit.item.renderer.model;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.ancheey.apjrelit.item.renderer.IArmsModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class Tier1CasterShoulderModel<T extends Entity> extends EntityModel<T> implements IArmsModel {
	private final ModelPart LeftShoulder;
	private final ModelPart RightShoulder;

	public Tier1CasterShoulderModel() {
		ModelPart root = createBodyLayer().bakeRoot();
		this.LeftShoulder = root.getChild("LeftShoulder");
		this.RightShoulder = root.getChild("RightShoulder");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition LeftShoulder = partdefinition.addOrReplaceChild("LeftShoulder", CubeListBuilder.create(), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition cube_r1 = LeftShoulder.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(29, 6).addBox(2.0F, -3.25F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(25, 5).addBox(4.0F, -3.75F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition cube_r2 = LeftShoulder.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(32, 35).addBox(-1.0F, -3.0F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition RightShoulder = partdefinition.addOrReplaceChild("RightShoulder", CubeListBuilder.create().texOffs(33, 12).addBox(-3.0F, -4.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition cube_r3 = RightShoulder.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(12, 0).addBox(-1.0F, -3.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition cube_r4 = RightShoulder.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(19, 0).addBox(-5.0F, -5.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition cube_r5 = RightShoulder.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(32, 35).addBox(-5.0F, -3.0F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	//LeftShoulder.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount * 0.5f;
	//RightShoulder.xRot = -Mth.cos(limbSwing  *  0.6662F) * limbSwingAmount * 0.5f;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		//LeftShoulder.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		//RightShoulder.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart getLeft() {
		return LeftShoulder;
	}
	@Override
	public ModelPart getRight() {
		return RightShoulder;
	}

	@Override
	public void renderLeftToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

		LeftShoulder.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void renderRightToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		RightShoulder.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
