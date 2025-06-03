package net.ancheey.apjrelit.enmity;

import net.ancheey.apjrelit.APJAttributeRegistry;
import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.*;

@Mod.EventBusSubscriber(modid = APJRelitCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.DEDICATED_SERVER)
public class EnmityManager {
	private static final HashMap<LivingEntity, EnmityList> EnmityData = new HashMap<>();

	@SubscribeEvent
	public static void onLivingHurtEvent(LivingHurtEvent e){
		if(e.getEntity() instanceof Player)
			return; //no players
		if(!(e.getSource().getEntity() instanceof  LivingEntity entity))
			return; //attacker is not a person
		if(!EnmityData.containsKey(e.getEntity())){
			EnmityData.put(e.getEntity(),new EnmityList());
		}
		var enmity = (float)(e.getAmount() * entity.getAttributeValue(APJAttributeRegistry.ENMITY_MODIFIER.get()));
		EnmityData.get(e.getEntity()).Add(e.getEntity(),enmity);
	}
	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent e){
		EnmityData.remove(e.getEntity());
	}
	public static @Nullable EnmityList getEntityData(LivingEntity e){
		return EnmityData.get(e);
	}
	public static class EnmityList{
		private final TreeSet<EnmityObject> dataset = new TreeSet<>();
		private final Map<LivingEntity, EnmityObject> entities = new HashMap<>();
		public void Add(LivingEntity attacker, float enmity){
			if(entities.containsKey(attacker))
				entities.get(attacker).enmity+=enmity;
			else{
				var v = new EnmityObject();
				v.entity = attacker;
				v.enmity = enmity;
				entities.put(attacker,v);
				dataset.add(v);
			}
		}
		public void Remove(LivingEntity attacker){
			var i = entities.remove(attacker);
			dataset.remove(i);
		}
		public Set<LivingEntity> getEnmityEntities(){
			return entities.keySet();
		}
		public LivingEntity getTargetAt(int index){
			index = Mth.clamp(index,0,dataset.size()-1);
			return new ArrayList<>(dataset).get(index).entity;
		}
		public float getTopEnmity(){
			if(dataset.size()>0)
				return dataset.first().enmity;
			return 0;
		}
		public @Nullable LivingEntity getTopEnmityEntity(){
			if(dataset.size() >0)
				return dataset.first().entity;
			return null;
		}
		public float getEnmityPercentage(LivingEntity e){
			if(!entities.containsKey(e))
				return 0f;
			var f = getTopEnmity();
			var i = entities.get(e).enmity;
			return i/f;
		}
	}
	public static class EnmityObject implements  Comparable<EnmityObject>{
		public LivingEntity entity;
		public float enmity;
		@Override
		public int compareTo(EnmityObject other) {
			int cmp = Float.compare(this.enmity, other.enmity);
			if (cmp == 0) {
				// Avoid equality conflicts: compare UUID or instance identity
				return Integer.compare(System.identityHashCode(this), System.identityHashCode(other));
			}
			return cmp;
		}
		public boolean equals(Object obj) {
			if (!(obj instanceof EnmityObject other)) return false;
			return this.entity == other.entity;
		}
		@Override
		public int hashCode() {
			return entity.getUUID().hashCode(); // or entity.getUUID().hashCode() if applicable
		}
	}
}
