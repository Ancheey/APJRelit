package net.ancheey.apjrelit.item.renderer;

import net.minecraft.client.model.HumanoidModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.util.RenderUtils;

import java.util.ArrayList;
import java.util.List;

public class CurioHandsRenderer extends GeoCurioRenderer {
	protected CurioHandsRenderer(String modelName) {
		super(modelName);
		leftGlove = bakedModel.getBone("armorLeftGlove").orElse(null);
		rightGlove = bakedModel.getBone("armorRightGlove").orElse(null);
	}
	GeoBone leftGlove;
	GeoBone rightGlove;
	@Override
	public void applyBoneTransformations(HumanoidModel<?> parent) {
		var leftArm = parent.leftArm;
		RenderUtils.matchModelPartRot(leftArm,leftGlove);

		var rightArm = parent.rightArm;
		RenderUtils.matchModelPartRot(rightArm,rightGlove);
	}

	@Override
	public List<GeoBone> getRelevantBones() {
		var list = new ArrayList<GeoBone>();
		list.add(leftGlove);
		list.add(rightGlove);

		return list;
	}
}
