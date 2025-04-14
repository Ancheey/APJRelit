package net.ancheey.apjrelit.item.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.ancheey.apjrelit.armor.APJSetModel;
import net.ancheey.apjrelit.item.APJCurios;
import net.ancheey.apjrelit.item.renderer.model.CurioSetItem;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
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

public class CurioRenderer /*extends HumanoidModel<LivingEntity>*/ implements ICurioRenderer, GeoRenderer<CurioSetItem> {
	protected final GeoRenderLayersContainer<CurioSetItem> renderLayers = new GeoRenderLayersContainer<>(this);
	private final APJCurios curioType;
	private final APJSetModel<CurioSetItem> model;
	protected BakedGeoModel bakedModel;
	protected Entity currentEntity = null;
	protected ItemStack currentStack = null;
	protected CurioSetItem animatable = null;


	public static CurioRenderer forShoulders(String modelName){
		return new CurioRenderer(modelName, APJCurios.Shoulders);
	}
	public static CurioRenderer forHalo(String modelName){
		return new CurioRenderer(modelName, APJCurios.Sigil);
	}
	public static CurioRenderer forGloves(String modelName){
		return new CurioRenderer(modelName, APJCurios.Sigil);
	}
	public static CurioRenderer forCape(String modelName){
		return new CurioRenderer(modelName, APJCurios.Back);
	}
	public static CurioRenderer forStaticCape(String modelName){
		return new CurioRenderer(modelName, APJCurios.StaticBack);
	}
	protected CurioRenderer(String modelName, APJCurios curioType) {
		//super(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR));

		this.curioType = curioType;
		model = new APJSetModel<>(modelName);
		//Don't want these for curios

		addRenderLayer(new GeoRenderLayer<>(this) {
			@Override
			public GeoModel<CurioSetItem> getGeoModel() {
				return model;
			}
		});
		bakedModel = model.getBakedModel(model.getModelResource(animatable));
	}
	public void grabRelevantBones(){
		model.getHalo().ifPresent(k->halo = k);
		model.getRightShoulder().ifPresent(k->rightShoulder = k);
		model.getLeftShoulder().ifPresent(k->leftShoulder = k);
		model.getRightGlove().ifPresent(k->rightGlove = k);
		model.getLeftGlove().ifPresent(k->leftGlove = k);
		model.getCape().ifPresent(k->cape = k);
	}
	GeoBone halo;
	GeoBone leftShoulder;
	GeoBone rightShoulder;
	GeoBone rightGlove;
	GeoBone leftGlove;
	GeoBone cape;

	@Override
	public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer,
																		  int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!(renderLayerParent.getModel() instanceof HumanoidModel<?> parent)) {
			return;
		}
		if(animatable == null)
			animatable = (CurioSetItem) stack.getItem();

		//TODO: finish all renderers
		switch(curioType){
			case Shoulders -> renderShoulders(stack,slotContext,matrixStack,parent,renderTypeBuffer,light,limbSwing,limbSwingAmount,partialTicks,ageInTicks,netHeadYaw,headPitch);
			case Hands -> renderGloves(stack,slotContext,matrixStack,parent,renderTypeBuffer,light,limbSwing,limbSwingAmount,partialTicks,ageInTicks,netHeadYaw,headPitch);
			case Sigil -> renderHalo(stack,slotContext,matrixStack,parent,renderTypeBuffer,light,limbSwing,limbSwingAmount,partialTicks,ageInTicks,netHeadYaw,headPitch);
			case Back -> renderCape(stack,slotContext,matrixStack,parent,renderTypeBuffer,light,limbSwing,limbSwingAmount,partialTicks,ageInTicks,netHeadYaw,headPitch);
			case StaticBack -> renderStillCape(stack,slotContext,matrixStack,parent,renderTypeBuffer,light,limbSwing,limbSwingAmount,partialTicks,ageInTicks,netHeadYaw,headPitch);
		}
	}
	private <T extends LivingEntity, M extends EntityModel<T>> void renderShoulders(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, HumanoidModel<?> parent, MultiBufferSource renderTypeBuffer,
								 int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch){
		VertexConsumer consumer = renderTypeBuffer.getBuffer(RenderType.armorCutoutNoCull(this.getTextureLocation((CurioSetItem) stack.getItem())));

		matrixStack.pushPose();

		var bone = bakedModel.getBone("armorLeftShoulder").orElse(null);
		bone.setHidden(false);
		matrixStack.mulPose(new Quaternionf().rotateX((float)Math.toRadians(180)));
		matrixStack.translate(0,-1.5,0);
		var bone2 = bakedModel.getBone("armorRightShoulder").orElse(null);
		bone2.setHidden(false);
		//parent.leftArm.translateAndRotate(matrixStack);
		renderRecursively(matrixStack,animatable,bone,
				getRenderType(animatable, getTextureLocation(animatable),
						renderTypeBuffer,partialTicks),renderTypeBuffer,consumer,
				false,partialTicks,light, 1,1,1,1,1);
		renderRecursively(matrixStack,animatable,bone2,
				getRenderType(animatable, getTextureLocation(animatable),
						renderTypeBuffer,partialTicks),renderTypeBuffer,consumer,
				false,partialTicks,light, 1,1,1,1,1);


		matrixStack.popPose();
	}
	private <T extends LivingEntity, M extends EntityModel<T>> void renderGloves(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, HumanoidModel<?> parent, MultiBufferSource renderTypeBuffer,
																					int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch){
	}
	private <T extends LivingEntity, M extends EntityModel<T>> void renderHalo(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, HumanoidModel<?> parent, MultiBufferSource renderTypeBuffer,
																					int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch){

	}
	private <T extends LivingEntity, M extends EntityModel<T>> void renderCape(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, HumanoidModel<?> parent, MultiBufferSource renderTypeBuffer,
																					int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch){

	}
	private <T extends LivingEntity, M extends EntityModel<T>> void renderStillCape(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, HumanoidModel<?> parent, MultiBufferSource renderTypeBuffer,
																					int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch){

	}

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
	public CurioRenderer addRenderLayer(GeoRenderLayer<CurioSetItem> renderLayer) {
		this.renderLayers.addLayer(renderLayer);
		return this;
	}
}
