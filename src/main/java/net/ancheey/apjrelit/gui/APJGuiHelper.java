package net.ancheey.apjrelit.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;

@OnlyIn(Dist.CLIENT)
public class APJGuiHelper {
	public static final ResourceLocation ICONS = ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"textures/gui/icons.png");
	public static void renderMajorUnitFrame(ForgeGui gui, GuiGraphics guiGraphics, float animationTick, int x,int y, LivingEntity unit, boolean mirrored){
		if(!gui.shouldDrawSurvivalElements())
			return;
		if(mirrored){
			renderMirroredMajorUnitFrame(guiGraphics,animationTick,x,y,unit);
		}
		else{
			renderMajorUnitFrame(guiGraphics,animationTick,x,y,unit);
		}
	}
	private static void renderMajorUnitFrame(GuiGraphics guiGraphics, float animationTick, int x,int y, LivingEntity unit){
		var mc = Minecraft.getInstance();
		guiGraphics.blit(ICONS,x,y,0,0,128,32);
		APJGuiHelper.renderLivingEntityPortrait(guiGraphics,x+14,y+4,25,25,unit,1,animationTick, true);
		renderMajorUnitFrameHealth(guiGraphics,x+43,y+11,unit);
		if(unit instanceof Player player){
			var level = ""+player.experienceLevel;
			var lvw = mc.font.width(level);
			guiGraphics.drawString(mc.font,level,(x+7)-(lvw/2),(y+22),0xFF55FF11);
			renderMajorUnitFrameMana(guiGraphics,x+43,y+21,player);
		}
	}
	private static void renderMirroredMajorUnitFrame(GuiGraphics guiGraphics, float animationTick, int x,int y, LivingEntity unit){
		var mc = Minecraft.getInstance();
		guiGraphics.blit(ICONS, x, y, 128, 0, 128, 32);
		APJGuiHelper.renderLivingEntityPortrait(guiGraphics,x+89,y+4,25,25,unit,1,animationTick,false);
		renderMajorUnitFrameHealth(guiGraphics,x+7,y+11,unit);
		if(unit instanceof Player player){
			var level = ""+player.experienceLevel;
			var lvw = mc.font.width(level);
			guiGraphics.drawString(mc.font,level,(x+121)-(lvw/2),(y+22),0xFF55FF11);
			renderMajorUnitFrameMana(guiGraphics,x+7,y+21,player);
		}
	}
	public static void renderMajorUnitFrame(ForgeGui gui, GuiGraphics guiGraphics, float animationTick, int x,int y, LivingEntity unit){
		renderMajorUnitFrame(gui,guiGraphics,animationTick,x,y,unit,false);
	}
	private static void renderMajorUnitFrameHealth(GuiGraphics guiGraphics, int x, int y, LivingEntity e){
		var mc = Minecraft.getInstance();
		var max = Math.ceil(e.getMaxHealth());
		var current = Math.ceil(e.getHealth());
		var hpPercent = current/max;

		guiGraphics.setColor(0.2f,1f,0.1f,1f);
		guiGraphics.blit(ICONS,x,y,0,32,(int)(78*hpPercent),9);
		guiGraphics.setColor(1f,1f,1f,1f);

		var absorb = e.getAbsorptionAmount();
		var absorbPercent = Math.min(1,absorb/max);
		guiGraphics.setColor(0.9f,0.9f,1f,1f);
		if(absorb > max-current){
			guiGraphics.blit(ICONS,x+ (int)(78-(78*absorbPercent)),y,(int)(78-78*absorbPercent),32,(int)(Math.ceil(78 * absorbPercent)),9);
		}
		else if(absorb > 0){
			guiGraphics.blit(ICONS,x+(int)(78*hpPercent),y,(int)(78*hpPercent),32,(int)Math.ceil(78*absorbPercent),9);
		}
		guiGraphics.setColor(1f,1f,1f,1f);
		var text = (absorb>0?(int)absorb+ "+":"")+ ((int)current)+"/"+((int)max);
		guiGraphics.drawString(mc.font, text,x+78-mc.font.width(text),y+1,(hpPercent)<=0.2?0xA1FF0000:0xA1FFFFFF,false);
	}
	private static void renderMajorUnitFrameMana(GuiGraphics guiGraphics, int x, int y, Player e){
		var mc = Minecraft.getInstance();
		var max = Math.ceil(e.getAttributeValue(AttributeRegistry.MAX_MANA.get()));
		var current = Math.ceil(ClientMagicData.getPlayerMana());
		var manaPercent = current/max;
		var text = ((int)current)+"/"+((int)max);
		guiGraphics.setColor(0.2f,0.3f,1f,1f);
		guiGraphics.blit(ICONS,x,y,0,32,(int)(78*manaPercent),9);
		guiGraphics.setColor(1f,1f,1f,1f);
		guiGraphics.drawString(mc.font, text,x+78-mc.font.width(text),y+1,(manaPercent)<0.2?0xA1FF0000:0xA1FFFFFF,false);
	}

	//minor frames
	public static void renderMinorUnitFrame(ForgeGui gui, GuiGraphics guiGraphics, float animationTick, int x,int y, LivingEntity unit){
		if(!gui.shouldDrawSurvivalElements())
			return;
		var mc = Minecraft.getInstance();
		guiGraphics.blit(ICONS,x,y,0,41,64,15);
		renderMinorUnitFrameHealth(guiGraphics,x+12,y+2,unit);
		var isLowHP =  (unit.getHealth() / unit.getMaxHealth() < 0.2f);
		if(unit instanceof Player player) {
			renderPlayerStaticPortrait(guiGraphics, x + 2, y + 3, animationTick, player,isLowHP);
			renderMinorUnitFrameMana(guiGraphics,x+12,y+11,player);
			var text = player.getDisplayName().getString();
			text = text.length() > 9? text.substring(0,8)+"..":text;
			guiGraphics.drawString(mc.font, text,x+13,y+3,isLowHP?0xA1FF0000:0xA1FFFFFF,false);
		}
		else {
			guiGraphics.blit(ICONS, x + 2, y + 4, 0, 56, 8, 8);
			var text = unit.getDisplayName().getString();
			text = text.length() > 9? text.substring(0,8)+"..":text;
			guiGraphics.drawString(mc.font, text,x+13,y+3,isLowHP?0xA1FF0000:0xA1FFFFFF,false);
		}

	}
	private static void renderMinorUnitFrameHealth(GuiGraphics guiGraphics, int x, int y, LivingEntity e){
		var max = Math.ceil(e.getMaxHealth());
		var current = Math.ceil(e.getHealth());
		var hpPercent = current/max;

		guiGraphics.setColor(0.2f,1f,0.1f,1f);
		guiGraphics.blit(ICONS,x,y,0,32,(int)(50*hpPercent),8);
		guiGraphics.setColor(1f,1f,1f,1f);

		var absorb = e.getAbsorptionAmount();
		var absorbPercent = Math.min(1,absorb/max);
		guiGraphics.setColor(0.9f,0.9f,1f,1f);
		if(absorb > max-current){
			guiGraphics.blit(ICONS,x+ (int)(50-(50*absorbPercent)),y,(int)(50-50*absorbPercent),32,(int)(Math.ceil(50 * absorbPercent)),8);
		}
		else if(absorb > 0){
			guiGraphics.blit(ICONS,x+(int)(50*hpPercent),y,(int)(50*hpPercent),32,(int)Math.ceil(50*absorbPercent),8);
		}
		guiGraphics.setColor(1f,1f,1f,1f);
	}
	private static void renderMinorUnitFrameMana(GuiGraphics guiGraphics, int x, int y, Player e){
		var mc = Minecraft.getInstance();
		var max = Math.ceil(e.getAttributeValue(AttributeRegistry.MAX_MANA.get()));
		var current = Math.ceil(ClientMagicData.getPlayerMana());
		var manaPercent = current/max;
		guiGraphics.setColor(0.2f,0.3f,1f,1f);
		guiGraphics.blit(ICONS,x,y,0,32,(int)(50*manaPercent),2);
		guiGraphics.setColor(1f,1f,1f,1f);
	}
	//portraits
	public static void renderLivingEntityPortrait(GuiGraphics gui, int x, int y, int width, int height, LivingEntity entity, float scale,float animationTick, boolean faceRight){
		var mc = Minecraft.getInstance();
		int scaledWidth = mc.getWindow().getGuiScaledWidth();
		int scaledHeight = mc.getWindow().getGuiScaledHeight();

		int screenWidth = mc.getWindow().getScreenWidth();
		int screenHeight = mc.getWindow().getScreenHeight();

		float scaleX = (float) screenWidth / (float) scaledWidth;
		float scaleY = (float) screenHeight / (float) scaledHeight;
		int scissorX = Math.round(x * scaleX);
		int scissorY = Math.round((scaledHeight - y - height) * scaleY);
		int scissorW = Math.round(width * scaleX);
		int scissorH = Math.round(height * scaleY);
		float mouseX = 45f*(!faceRight?1:-1) + (float)(-3f*Math.cos(animationTick* Math.PI*2));
		float mouseY = (float)(2*Math.sin(animationTick* Math.PI*2))-0.2f;
		var entityHeightScale = entity.getBbHeight();
		RenderSystem.enableScissor(scissorX, scissorY, scissorW, scissorH);
		InventoryScreen.renderEntityInInventoryFollowsMouse(gui,x+(width/2),y+(int)(15+22*entityHeightScale),(int)(25*scale),mouseX,mouseY,entity);
		RenderSystem.disableScissor();
	}
	public static void renderPlayerStaticPortrait(GuiGraphics gui, int x, int y, float animationTick, Player player, boolean isLowHealth){
		ResourceLocation skin = Minecraft.getInstance().getSkinManager()
				.getInsecureSkinLocation(player.getGameProfile());
		if(player.isDeadOrDying()){
			gui.setColor(0.3f,0.3f,0.3f,1f);
		}
		else if(isLowHealth){
			gui.setColor(0.3f+(float) (0.6f * Math.sin(animationTick * Math.PI)),0.3f,0.3f,1f);
		}
		gui.blit(skin, x, y, 8, 8, 8, 8, 64, 64);
		gui.blit(skin, x, y, 40, 8, 8, 8, 64, 64);
		gui.setColor(1f,1f,1f,1f);
	}
}
