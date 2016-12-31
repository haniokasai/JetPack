package com.haniokasai.nukkit.JetPack;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.ProjectileHitEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerKickEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.SetEntityLinkPacket;
import cn.nukkit.network.protocol.UseItemPacket;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;



public class Main extends PluginBase implements Listener{

	static Map<Integer, Player> gun = new HashMap<Integer, Player>();
	static Map<String, Integer> rvgun = new HashMap<String, Integer>();
	static Map<String , Boolean> hook = new HashMap<String , Boolean>();
	static List<String> stop;


	public void onEnable() {

		 this.getServer().getPluginManager().registerEvents(this, this);
		getDataFolder().mkdir();
		//"dashy" => "0.4", "dashxz" => "1.3", "dashid" => "345"
		Config config = new Config(
                new File(this.getDataFolder(), "config.yml"),Config.YAML,
                new LinkedHashMap<String, Object>() {
                    {
                    	put("dashy", "0.4");
                    	put("dashxz", "1.3");
                    	put("dashid", "345");
                    	put("jumpid", "347");
                    	put("jump", "0.8");
                    	put("wireid", "369");
                    	put("wire", "3");
                    }
                });
        config.save();
        this.getServer().getLogger().info("[JetPack] Loaded");

}

	@EventHandler
	public void onmotion(PlayerInteractEvent event){
		Player player = event.getPlayer();
        int itemid = player.getInventory().getItemInHand().getId();
        String jumpid = getConfig().get("jumpid").toString();
        if(itemid == Integer.parseInt(jumpid)){
        	Vector3 MotionJ = new Vector3(0, 0, 0);
        	String jump = getConfig().get("jump").toString();
            MotionJ.y = new Float(jump);
            player.setMotion(MotionJ);
        } //watch

        String dashid = getConfig().get("dashid").toString();
        if (itemid == Integer.parseInt(dashid)) {
        	Vector3 MotionA = new Vector3(0, 0, 0);
        	String dashy = getConfig().get("dashy").toString();
            MotionA.y = new Float(dashy);
            int dirxz = player.getDirection();
            int dashxz = new Float(getConfig().get("dashxz").toString()).intValue();
            if (dirxz == 0) {
                MotionA.x = dashxz;
            }
            if (dirxz == 1) {
                MotionA.z = dashxz;
            }
            if (dirxz == 2) {
                MotionA.x = - dashxz;
            }
            if (dirxz == 3) {
                MotionA.z = - dashxz;
            }
            player.setMotion(MotionA);
        } //compass
        String wireid = getConfig().get("wireid").toString();
        if(itemid == Integer.parseInt(wireid)){
        	String wire = getConfig().get("wire").toString();
    		Vector3 motion = player.getDirectionVector().multiply(new Float(wire));
    		player.setMotion(motion);
        } //Wire gun



    }


	  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false) //DON'T FORGET THE ANNOTATION @EventHandler
		public void fishingrod(DataPacketReceiveEvent event) throws Exception {
			Player player = event.getPlayer();
			String name = player.getName();
			DataPacket pk = event.getPacket();

			UseItemPacket useItemPacket = null;
			String type ="Arrow";
			Double speed = 2.0;
			int pi;
			try{
				pi = player.getInventory().getItemInHand().getId();
			}catch (NullPointerException e){
				return;
			}
			if(pi==346){
			boolean p = false;
			try{
				useItemPacket = (UseItemPacket) pk;
				//Server.getInstance().getLogger().info(useItemPacket.toString());
				//Server.getInstance().broadcastMessage(String.valueOf(useItemPacket.face));
				if (pk instanceof UseItemPacket) {
					p = true;
				}
				}catch(Exception okok){
				}

				if(hook.containsKey(player.getName())){
					SetEntityLinkPacket setEntityLinkPk = new SetEntityLinkPacket();
					setEntityLinkPk = new SetEntityLinkPacket();
					setEntityLinkPk.rider =  rvgun.get(player.getName());
					setEntityLinkPk.riding = player.getId();
					setEntityLinkPk.type = SetEntityLinkPacket.TYPE_REMOVE;
					Server.broadcastPacket(Server.getInstance().getOnlinePlayers().values(), pk);

					setEntityLinkPk = new SetEntityLinkPacket();
					setEntityLinkPk.rider = rvgun.get(player.getName());
					setEntityLinkPk.riding = 0;
					setEntityLinkPk.type = SetEntityLinkPacket.TYPE_REMOVE;
					hook.remove(player.getName());

				}
			if(p&!hook.containsKey(player.getName())){
				CompoundTag nbt = new CompoundTag()
						.putList(new ListTag<DoubleTag>("Pos")
								.add(new DoubleTag("", player.getX()+(-Math.sin(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI))))
								.add(new DoubleTag("", player.getY()+player.getEyeHeight()-0.25))
								.add(new DoubleTag("", player.getZ()+(Math.cos(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI)))))
						.putList(new ListTag<DoubleTag>("Motion")
								.add(new DoubleTag("",-Math.sin(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI)))
								.add(new DoubleTag("",-Math.sin(player.pitch / 180 * Math.PI)))
								.add(new DoubleTag("", Math.cos(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI))))
						.putList(new ListTag<FloatTag>("Rotation")
								.add(new FloatTag("", (float) player.yaw))
								.add(new FloatTag("", (float) player.pitch)));
				Entity snowball = Entity.createEntity(type,player.chunk,nbt,player);

				snowball.setMotion(snowball.getMotion().multiply(speed));
				snowball.spawnToAll();

				SetEntityLinkPacket setEntityLinkPk = new SetEntityLinkPacket();
				setEntityLinkPk.rider =  snowball.getId();
				setEntityLinkPk.riding = player.getId();
				setEntityLinkPk.type = SetEntityLinkPacket.TYPE_RIDE;
				player.dataPacket(setEntityLinkPk);

				setEntityLinkPk = new SetEntityLinkPacket();
				setEntityLinkPk.rider =  snowball.getId();
				setEntityLinkPk.riding = player.getId();
				setEntityLinkPk.type = SetEntityLinkPacket.TYPE_RIDE;
                Server.broadcastPacket(Server.getInstance().getOnlinePlayers().values(), pk);

                setEntityLinkPk = new SetEntityLinkPacket();
                setEntityLinkPk.rider = snowball.getId();
                setEntityLinkPk.riding = 0;
                setEntityLinkPk.type = SetEntityLinkPacket.TYPE_RIDE;
                player.dataPacket(setEntityLinkPk);
				gun.put((int) snowball.getId(),player);
				rvgun.put(player.getName(),(int) snowball.getId());
				hook.put(name,true);

			}
			}
	  }

	  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false) //DON'T FORGET THE ANNOTATION @EventHandler
	    public void onProjectileHit(ProjectileHitEvent event) throws Exception{
	       Entity snowball = event.getEntity();
	        Position loc = snowball.getLocation();
	        snowball.getLevel().removeEntity(snowball);
	        if(gun.containsKey((int)snowball.getId())){
	        	Player player =gun.get((int)snowball.getId());
	        	final Vector3 vector3 = snowball.getLocation();
	        	final int x=snowball.getLocation().getFloorX();
	        	final int y =snowball.getLocation().getFloorY();
	        	final int z =snowball.getLocation().getFloorZ();
				/*Vector3 pv = vector3;
				pv.abs();
				pv.add(2);*/
	        	//Vector3 v =new Vector3(player.getFloorX(),player.getFloorY()-1,player.getFloorZ());
	        	if(player.getLevel().getBlock(vector3).getId() ==0&(player.getLevel().getBlock(new Vector3(x+1,y,z)).getId() !=0||player.getLevel().getBlock(new Vector3(x-1,y,z)).getId() !=0||player.getLevel().getBlock(new Vector3(x,y,z+1)).getId() !=0||player.getLevel().getBlock(new Vector3(x,y,z-1)).getId() !=0)){


							player.getLevel().setBlock(vector3, Block.get(20));
					player.teleport(player.getLevel().getBlock(vector3).add(0,1,0));
	        		 Server.getInstance().getScheduler().scheduleDelayedTask(new Runnable() {
				            public void run() {
				            	player.getLevel().setBlock(vector3, Block.get(0));
				            	//player.getLevel().setBlock(v, Block.get(0));
	        		 }
	        	},20*15);
	        	}
				gun.remove((int)snowball.getId());
	        	rvgun.remove(player);
				hook.remove(player.getName());
	        }
	       }

	/*@EventHandler
	public void stopmove(PlayerMoveEvent event){
		if(stop.contains(event.getPlayer().getName())){
			Location from = event.getFrom();
			Location to = event.getTo();
			if(Math.abs(from.y-to.y) >0.1 ){
				event.setCancelled(true);
			}
		}
	}*/

	@EventHandler
	public void nokick(PlayerKickEvent event){
		if(event.getReason().matches(".*"+"Attempting"+ ".*"));
			if(event.getReason().matches(".*"+"attack"+ ".*"));
				if(event.getReason().matches(".*"+"invalid"+ ".*"));
				event.setCancelled();
	}
}