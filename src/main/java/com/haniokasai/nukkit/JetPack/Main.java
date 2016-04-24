package com.haniokasai.nukkit.JetPack;

import java.io.File;
import java.util.LinkedHashMap;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;



public class Main extends PluginBase implements Listener{



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

}