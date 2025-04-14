package net.ancheey.apjrelit.item.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.armor.APJSetModel;
import net.ancheey.apjrelit.item.APJCurios;
import net.ancheey.apjrelit.item.renderer.model.CurioSetItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.texture.AnimatableTexture;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayersContainer;
import software.bernie.geckolib.util.RenderUtils;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import java.util.List;

public class CurioRenderer /*extends HumanoidModel<LivingEntity>*/ implements ICurioRenderer, GeoRenderer<CurioSetItem> {
	protected final GeoRenderLayersContainer<CurioSetItem> renderLayers = new GeoRenderLayersContainer<>(this);
	private final APJCurios curioType;
	private final APJSetModel<CurioSetItem> model;
	protected HumanoidModel<?> baseModel;
	protected float scaleWidth = 1;
	protected float scaleHeight = 1;

	protected Matrix4f entityRenderTranslations = new Matrix4f();
	protected Matrix4f modelRenderTranslations = new Matrix4f();
	private  CurioSetItem animatable;

	protected BakedGeoModel lastModel = null;
	protected Entity currentEntity = null;
	protected ItemStack currentStack = null;


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
		model.getHalo().ifPresent(k->{k.setHidden(curioType == APJCurios.Sigil); halo = k;});
		model.getRightShoulder().ifPresent(k->{k.setHidden(curioType == APJCurios.Shoulders); rightShoulder = k;});
		model.getLeftShoulder().ifPresent(k->{k.setHidden(curioType == APJCurios.Shoulders); leftShoulder = k;});
		model.getRightGlove().ifPresent(k->{k.setHidden(curioType == APJCurios.Hands); rightGlove = k;});
		model.getLeftGlove().ifPresent(k->{k.setHidden(curioType == APJCurios.Hands); leftGlove = k;});
		model.getCape().ifPresent(k->{k.setHidden(curioType == APJCurios.Back || curioType == APJCurios.StaticBack); cape = k;});
		HideAllNotImportant();
		var baked = model.getBakedModel(model.getModelResource(animatable));
		lastModel = baked;
		APJRelitCore.LOGGER.info("Head: "+(baked.getBone("armorHead").orElse(null) == null));
		APJRelitCore.LOGGER.info("lsh: "+(baked.getBone("armorLeftShoulder").orElse(null) == null));
		APJRelitCore.LOGGER.info("Halo: "+(halo == null));
		APJRelitCore.LOGGER.info("leftShoulder: "+(leftShoulder == null));
		APJRelitCore.LOGGER.info("rightShoulder: "+(rightShoulder == null));
		APJRelitCore.LOGGER.info("leftGlove: "+(leftGlove == null));
		APJRelitCore.LOGGER.info("rightGlove: "+(rightGlove == null));
		APJRelitCore.LOGGER.info("cape: "+(cape == null));
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
	private void HideAllNotImportant(){
		model.getHead().ifPresent(k->k.setHidden(true));
		model.getBody().ifPresent(k->k.setHidden(true));
		model.getLeftArm().ifPresent(k->k.setHidden(true));
		model.getRightArm().ifPresent(k->k.setHidden(true));
		model.getLeftLeg().ifPresent(k->k.setHidden(true));
		model.getRightLeg().ifPresent(k->k.setHidden(true));
		model.getRightBoot().ifPresent(k->k.setHidden(true));
		model.getLeftBoot().ifPresent(k->k.setHidden(true));
	}

	public void prepForRender(@Nullable Entity entity, ItemStack stack, @Nullable HumanoidModel<?> baseModel){
		if (entity == null || baseModel == null)
			return;
		this.baseModel = baseModel;
		this.currentEntity = entity;
		this.currentStack = stack;
		this.animatable = (CurioSetItem) stack.getItem();
	}
	@Override
	public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer,
																		  int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!(renderLayerParent.getModel() instanceof HumanoidModel<?> parent)) {
			return;
		}
		grabRelevantBones();
		prepForRender(slotContext.entity(), stack, (HumanoidModel<?>) renderLayerParent.getModel());


		/*model.getLeftShoulder().ifPresent(k->
				renderRecursively(matrixStack,animatable,k,
						getRenderType(animatable, getTextureLocation(animatable),
								renderTypeBuffer,partialTicks),renderTypeBuffer,consumer,
						false,partialTicks,light, 1,1,1,1,1)
				);*/
		//renderRecursively(matrixStack,animatable,leftShoulder,getRenderType(animatable, getTextureLocation(animatable),renderTypeBuffer,partialTicks),renderTypeBuffer,consumer,false,partialTicks,light, 1,1,1,1,1);
		//renderRecursively(leftShoulder, matrixStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
		//this.renderToBuffer(matrixStack, renderTypeBuffer, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		//this.renderToBuffer(matrixStack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		switch(curioType){
			case Shoulders -> renderShoulders(stack,slotContext,matrixStack,renderLayerParent,renderTypeBuffer,light,limbSwing,limbSwingAmount,partialTicks,ageInTicks,netHeadYaw,headPitch);
			case Hands -> renderGloves(stack,slotContext,matrixStack,renderLayerParent,renderTypeBuffer,light,limbSwing,limbSwingAmount,partialTicks,ageInTicks,netHeadYaw,headPitch);
			case Sigil -> renderHalo(stack,slotContext,matrixStack,renderLayerParent,renderTypeBuffer,light,limbSwing,limbSwingAmount,partialTicks,ageInTicks,netHeadYaw,headPitch);
			case Back -> renderCape(stack,slotContext,matrixStack,renderLayerParent,renderTypeBuffer,light,limbSwing,limbSwingAmount,partialTicks,ageInTicks,netHeadYaw,headPitch);
			case StaticBack -> renderStillCape(stack,slotContext,matrixStack,renderLayerParent,renderTypeBuffer,light,limbSwing,limbSwingAmount,partialTicks,ageInTicks,netHeadYaw,headPitch);
		}
	}
	private <T extends LivingEntity, M extends EntityModel<T>> void renderShoulders(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer,
								 int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch){
		VertexConsumer consumer = renderTypeBuffer.getBuffer(RenderType.armorCutoutNoCull(this.getTextureLocation((CurioSetItem) stack.getItem())));

		matrixStack.pushPose();

		var bone = lastModel.getBone("armorLeftShoulder").orElse(null);
		matrixStack.mulPose(new Quaternionf().rotateX((float)Math.toRadians(180)));
		matrixStack.translate(0,-1.5,0);
		var bone2 = lastModel.getBone("armorRightShoulder").orElse(null);
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
	private <T extends LivingEntity, M extends EntityModel<T>> void renderGloves(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer,
																					int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch){
	}
	private <T extends LivingEntity, M extends EntityModel<T>> void renderHalo(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer,
																					int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch){

	}
	private <T extends LivingEntity, M extends EntityModel<T>> void renderCape(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer,
																					int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch){

	}
	private <T extends LivingEntity, M extends EntityModel<T>> void renderStillCape(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer,
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
	/*public CurioRenderer withScale(float scale) {
		return withScale(scale, scale);
	}
	public CurioRenderer withScale(float scaleWidth, float scaleHeight) {
		this.scaleWidth = scaleWidth;
		this.scaleHeight = scaleHeight;

		return this;
	}

	public void renderToBuffer(PoseStack pPoseStack,MultiBufferSource pBufferSource ,VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha){
		Minecraft mc = Minecraft.getInstance();

		float partialTick = mc.getFrameTime();
		RenderType renderType = getRenderType(this.animatable, getTextureLocation(this.animatable), pBufferSource, partialTick);
		pBuffer = ItemRenderer.getArmorFoilBuffer(pBufferSource, renderType, false, this.currentStack.hasFoil());

		defaultRender(pPoseStack, this.animatable, pBufferSource, null, pBuffer,
				0, partialTick, pPackedLight);
	}

	@Override
	public void renderRecursively(PoseStack poseStack, CurioSetItem animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		if (bone.isTrackingMatrices()) {
			Matrix4f poseState = new Matrix4f(poseStack.last().pose());

			bone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
			bone.setLocalSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.entityRenderTranslations));
		}

		GeoRenderer.super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}*/
}
