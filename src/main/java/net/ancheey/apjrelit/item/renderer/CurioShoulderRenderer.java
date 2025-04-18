package net.ancheey.apjrelit.item.renderer;

import net.minecraft.client.model.HumanoidModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.util.RenderUtils;

import java.util.ArrayList;
import java.util.List;

public class CurioShoulderRenderer extends GeoCurioRenderer {
	protected CurioShoulderRenderer(String modelName) {
		super(modelName);
		leftShoulder = bakedModel.getBone("armorLeftShoulder").orElse(null);
		rightShoulder = bakedModel.getBone("armorRightShoulder").orElse(null);
	}
	GeoBone leftShoulder;
	GeoBone rightShoulder;
	@Override
	public void applyBoneTransformations(HumanoidModel<?> parent, float limbSwing) {
		var leftArm = parent.leftArm;
		RenderUtils.matchModelPartRot(leftArm,leftShoulder);

		var rightArm = parent.rightArm;
		RenderUtils.matchModelPartRot(rightArm,rightShoulder);
	}
	@Override
	public List<GeoBone> getRelevantBones() {
		var list = new ArrayList<GeoBone>();
		list.add(leftShoulder);
		list.add(rightShoulder);

		return list;
	}
}
