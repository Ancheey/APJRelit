package net.ancheey.apjrelit.item.renderer;

import net.minecraft.client.model.HumanoidModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.util.RenderUtils;

import java.util.ArrayList;
import java.util.List;

public class CurioBackRenderer extends GeoCurioRenderer {
	protected CurioBackRenderer(String modelName) {
		super(modelName);
		back = bakedModel.getBone("armorBodyCloak").orElse(null);
	}
	GeoBone back;
	@Override
	public void applyBoneTransformations(HumanoidModel<?> parent, float limbSwing) {
		var body = parent.body;
		RenderUtils.matchModelPartRot(body,back);
		back.setRotX(body.xRot+(float)Math.toRadians(-45f*limbSwing));
	}

	@Override
	public List<GeoBone> getRelevantBones() {
		var list = new ArrayList<GeoBone>();
		list.add(back);

		return list;
	}
}
