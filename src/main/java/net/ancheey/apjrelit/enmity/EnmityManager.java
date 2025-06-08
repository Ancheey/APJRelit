package net.ancheey.apjrelit.enmity;

import net.ancheey.apjrelit.APJAttributeRegistry;
import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.network.NetworkHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.*;

@Mod.EventBusSubscriber(modid = APJRelitCore.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EnmityManager {
	public static final HashMap<LivingEntity, EnmityList> EnmityData = new HashMap<>();


	@SubscribeEvent
	public static void onLivingHurtEvent(LivingHurtEvent e){
		if(e.getEntity() instanceof Player)
			return; //no players
		if(!(e.getSource().getEntity() instanceof  LivingEntity entity))
			return; //attacker is not a person
		if(!EnmityData.containsKey(e.getEntity())){
			EnmityData.put(e.getEntity(),new EnmityList(e.getEntity()));
		}
		var enmity = (float)(e.getAmount() * entity.getAttributeValue(APJAttributeRegistry.ENMITY_MODIFIER.get()));
		EnmityData.get(e.getEntity()).Add(entity,enmity);
		APJRelitCore.LOGGER.info("Adding "+enmity+" to "+e.getEntity().getDisplayName().getString()+" by "+entity.getDisplayName().getString());
	}
	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent e){
		EnmityData.remove(e.getEntity());
		EnmityData.forEach((k,v)->{
			v.Remove(e.getEntity());
		});
	}
	public static @Nullable EnmityList getEntityData(LivingEntity e){
		return EnmityData.get(e);
	}
	public static class EnmityList{
		private final LivingEntity enmityHost;
		private final List<EnmityObject> dataset = new ArrayList<>();
		private final Map<LivingEntity, EnmityObject> entities = new HashMap<>();
		public LivingEntity getHost(){
			return enmityHost;
		}
		public EnmityList(LivingEntity enmityHost) {
			this.enmityHost = enmityHost;
		}
		private void TryFocusTarget(LivingEntity e){
			if(enmityHost instanceof Mob mob)
				mob.setTarget(e);
		}
		private void handleServerside(Level level){
			if(level != null && !level.isClientSide) {
				var pkg = new STCEnmityDataPackage(enmityHost,this);
				for(var entity : entities.keySet()){
					if(entity instanceof ServerPlayer player)
						NetworkHandler.sendToPlayer(pkg,player);
				}
				TryFocusTarget(getTopEnmityEntity());
			}
		}
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
			handleServerside(attacker.level());
		}
		public void Set(LivingEntity attacker, float enmity){
			if(entities.containsKey(attacker))
				entities.get(attacker).enmity=enmity;
			else{
				var v = new EnmityObject();
				v.entity = attacker;
				v.enmity = enmity;
				entities.put(attacker,v);
				dataset.add(v);
			}
			handleServerside(attacker.level());
		}
		public void Remove(LivingEntity attacker){
			var i = entities.get(attacker);
			if(i == null)
				return;
			entities.remove(attacker);
			dataset.remove(i);
			handleServerside(attacker.level());
		}
		public int size(){
			return dataset.size();
		}
		public List<EnmityObject> getEnmity(){
			return dataset;
		}
		public LivingEntity getTargetAt(int index){
			index = Mth.clamp(index,0,dataset.size()-1);
			return new ArrayList<>(dataset).get(index).entity;
		}
		public float getTopEnmity(){
			if(dataset.size() == 0)
				return 0;
			return dataset.stream().max(Comparator.comparing(enmityObject -> enmityObject.enmity)).get().enmity;

		}
		public @Nullable EnmityObject getTopEnmityObject(){
			if(dataset.size() == 0)
				return null;
			return dataset.stream().max(Comparator.comparing(enmityObject -> enmityObject.enmity)).get();

		}
		public @Nullable LivingEntity getTopEnmityEntity(){
			if(dataset.size() == 0)
				return null;
			return dataset.stream().max(Comparator.comparing(enmityObject -> enmityObject.enmity)).get().entity;
		}
		public float getEnmityPercentage(LivingEntity e){
			if(!entities.containsKey(e))
				return 0f;
			var f = getTopEnmity();
			var i = entities.get(e).enmity;
			return i/f;
		}
		public Iterator<EnmityObject> getIterator(){
			return dataset.iterator();
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
