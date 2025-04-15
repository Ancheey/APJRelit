package net.ancheey.apjrelit.item;

import net.ancheey.apjrelit.APJRelitCore;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class SigilGeoCurioItem extends BasicGeoCurioItem{
	public SigilGeoCurioItem(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, "haloController",0,state -> {
			state.setAnimation(RawAnimation.begin().thenLoop("animation.apj_armor_base.halo"));
		return PlayState.CONTINUE;
		}));
	}
}
