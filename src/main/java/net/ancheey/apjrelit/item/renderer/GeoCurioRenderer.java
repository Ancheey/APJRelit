package net.ancheey.apjrelit.item.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.ancheey.apjrelit.armor.APJSetModel;
import net.ancheey.apjrelit.item.CurioSetItem;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.texture.AnimatableTexture;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayersContainer;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import java.util.List;

/**
 * CurioSetItem is just <T extends GeoItem & CurioItem>
 */
public abstract class GeoCurioRenderer implements ICurioRenderer, GeoRenderer<CurioSetItem> {
	protected final GeoRenderLayersContainer<CurioSetItem> renderLayers = new GeoRenderLayersContainer<>(this);
	private final APJSetModel<CurioSetItem> model;
	protected BakedGeoModel bakedModel;
	protected Entity currentEntity = null;
	protected ItemStack currentStack = null;
	protected CurioSetItem animatable = null;


	public static GeoCurioRenderer forShoulders(String modelName){
		return new CurioShoulderRenderer(modelName);
	}
	public static GeoCurioRenderer forSigil(String modelName){
		return new CurioSigilRenderer(modelName);
	}
	public static GeoCurioRenderer forGloves(String modelName){
		return new CurioHandsRenderer(modelName);
	}
	public static GeoCurioRenderer forCape(String modelName){
		return new CurioBackRenderer(modelName);
	}
	public static GeoCurioRenderer forStaticCape(String modelName){
		return new CurioStaticBackRenderer(modelName);
	}
	protected GeoCurioRenderer(String modelName) {
		//APJSetModel is a "class APJSetModel<T extends GeoItem> extends GeoModel<T>"
		//Passing the string as a model name just interpolates it and searches for resources. You can swap it for whatever you want;
		model = new APJSetModel<>(modelName);

		//Any further actions you should do using the bakedModel.
		addRenderLayer(new GeoRenderLayer<>(this) {
			@Override
			public GeoModel<CurioSetItem> getGeoModel() {
				return model;
			}
		});
		bakedModel = model.getBakedModel(model.getModelResource(animatable));
	}

	/**
	 *  DO NOT OVERRIDE ANY FURTHER. USE renderCurio INSTEAD.
	 */
	@Override
	public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer,
																		  int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!(renderLayerParent.getModel() instanceof HumanoidModel<?> parent)) {
			return;
		}
		if(animatable == null)
			animatable = (CurioSetItem) stack.getItem();

		var relevatBones = getRelevantBones();
		if(relevatBones != null)
			for(var bone : relevatBones)
				if(bone != null)
					bone.setHidden(false);

		VertexConsumer consumer = renderTypeBuffer.getBuffer(RenderType.armorCutoutNoCull(this.getTextureLocation((CurioSetItem) stack.getItem())));
		matrixStack.pushPose();
		matrixStack.translate(0,24/16f,0);
		matrixStack.scale(-1,-1,1);
		applyBoneTransformations(parent);
		defaultRender(matrixStack,animatable,renderTypeBuffer, getRenderType(animatable, getTextureLocation(animatable),
				renderTypeBuffer,partialTicks),consumer,netHeadYaw,partialTicks,light);
		matrixStack.popPose();
	}

	/**
	 * Use RenderUtils.matchModelPartRot to rotate your desired bones
	 * @param parent parent humanoid model - copy from it
	 */
	public abstract  void applyBoneTransformations(HumanoidModel<?> parent);
	public abstract  List<GeoBone> getRelevantBones();
	@Override
	public GeoModel<CurioSetItem> getGeoModel() {
		return this.model;
	}

	@Override
	public CurioSetItem getAnimatable() {
		return this.animatable;
	}
	@Override
	public void fireCompileRenderLayersEvent() {}

	@Override
	public boolean firePreRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
		return true;
	}

	@Override
	public void firePostRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {

	}

	@Override
	public void updateAnimatedTextureFrame(CurioSetItem animatable) {
		if (this.currentEntity != null)
			AnimatableTexture.setAndUpdate(getTextureLocation(animatable));
	}
	@Override
	public long getInstanceId(CurioSetItem animatable) {
		return -GeoItem.getId(this.currentStack);
	}
	@Override
	public RenderType getRenderType(CurioSetItem animatable, ResourceLocation texture, @org.jetbrains.annotations.Nullable MultiBufferSource bufferSource, float partialTick) {
		return RenderType.armorCutoutNoCull(texture);
	}
	@Override
	public List<GeoRenderLayer<CurioSetItem>> getRenderLayers() {
		return this.renderLayers.getRenderLayers();
	}
	public GeoCurioRenderer addRenderLayer(GeoRenderLayer<CurioSetItem> renderLayer) {
		this.renderLayers.addLayer(renderLayer);
		return this;
	}
	@Override
	public void actuallyRender(PoseStack poseStack, CurioSetItem animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource,
								VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay,
								float red, float green, float blue, float alpha) {
		updateAnimatedTextureFrame(animatable);

		var relevatBones = getRelevantBones();
		if(relevatBones != null)
			for(var bone : relevatBones) {
			renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight,
					packedOverlay, red, green, blue, alpha);
		}
	}
}
