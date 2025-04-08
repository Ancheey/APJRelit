package net.ancheey.apjrelit.item.renderer.model;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.ancheey.apjrelit.item.renderer.IArmsModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class Tier1CasterGloveModel <T extends Entity> extends EntityModel<T>  implements IArmsModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart LeftGlove;
	private final ModelPart RightGlove;

	public Tier1CasterGloveModel() {
		ModelPart root = createBodyLayer().bakeRoot();
		this.LeftGlove = root.getChild("LeftGlove");
		this.RightGlove = root.getChild("RightGlove");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition LeftGlove = partdefinition.addOrReplaceChild("LeftGlove", CubeListBuilder.create(), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition cube_r1 = LeftGlove.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(21, 123).addBox(3.0F, 6.0F, 0.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

		PartDefinition cube_r2 = LeftGlove.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 120).addBox(-2.25F, 7.75F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

		PartDefinition RightGlove = partdefinition.addOrReplaceChild("RightGlove", CubeListBuilder.create().texOffs(79, 122).addBox(-3.5F, 8.0F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		//LeftGlove.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
		//RightGlove.xRot = -Mth.cos(limbSwing  *  0.6662F) * limbSwingAmount;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		//LeftGlove.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		//RightGlove.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	@Override
	public ModelPart getLeft() {
		return LeftGlove;
	}
	@Override
	public ModelPart getRight() {
		return RightGlove;
	}

	@Override
	public void renderLeftToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		LeftGlove.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void renderRightToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		RightGlove.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
