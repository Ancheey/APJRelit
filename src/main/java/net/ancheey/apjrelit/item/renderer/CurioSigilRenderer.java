package net.ancheey.apjrelit.item.renderer;
import net.minecraft.client.model.HumanoidModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import software.bernie.geckolib.util.RenderUtils;

import java.util.ArrayList;
import java.util.List;

public class CurioSigilRenderer extends GeoCurioRenderer {
	protected CurioSigilRenderer(String modelName) {
		super(modelName);
		halo = bakedModel.getBone("armorHeadHalo").orElse(null);
		addRenderLayer(new AutoGlowingGeoLayer<>(this));
	}
	GeoBone halo;
	@Override
	public void applyBoneTransformations(HumanoidModel<?> parent, float limbSwing) {
		var head = parent.head;
		RenderUtils.matchModelPartRot(head,halo);
	}
	@Override
	public List<GeoBone> getRelevantBones() {
		var list = new ArrayList<GeoBone>();
		list.add(halo);

		return list;
	}
}
