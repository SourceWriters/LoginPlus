package com.syntaxphoenix.loginplus.premium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.bukkit.entity.Player;

public class PremiumCheck {

	// TODO: Make proper implementation
	public static boolean isPremium(Player p) {
	    String player = p.getName();
	    System.out.println(player);
	    String value = "false";
	    URL URLBase = URLConnection.getConnection("https://minecraft.net/haspaid.jsp?user=" + player);
	    try {
	    	java.net.URLConnection con = URLBase.openConnection();
	      	BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

	      	value = br.readLine();
	    } catch (MalformedURLException e) {
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	    try {
	    	URLConnection.setConnection(URLBase);
	      	if (value.equalsIgnoreCase("true")) {
		        return true;
	      	}
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    return false;
	}
}
