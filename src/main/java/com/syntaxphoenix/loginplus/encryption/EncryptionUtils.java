package com.syntaxphoenix.loginplus.encryption;

import java.security.MessageDigest;

import com.syntaxphoenix.loginplus.config.MainConfig;

import at.favre.lib.crypto.bcrypt.BCrypt;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;

public class EncryptionUtils {
	
	public static String hashPassword(String password, EncryptionType type) {
		if (type == EncryptionType.SHA256) {
			return sha256(password);
		} 
		
		if (type == EncryptionType.SHA512) {
			return sha512(password);
		} 
		
		if (type == EncryptionType.MD_5) {
			return md5(password);
		} 
		
		if (type == EncryptionType.ARGON_2) {
			Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2id);
			return argon2.hash(MainConfig.argon2Cores, MainConfig.argon2Memory * 1024, MainConfig.argon2Parallelism, password);
		}
		
		if (type == EncryptionType.BCRYPT) {
			return BCrypt.withDefaults().hashToString(MainConfig.bcryptRounds, password.toCharArray());
		}
		return null;
	}
	
	public static boolean verifyPassword(String password, EncryptionType type, String hash) {
		if (type == EncryptionType.SHA256 || type == EncryptionType.SHA512 || type == EncryptionType.MD_5) {
			return hashPassword(password, type).equalsIgnoreCase(hash);
		} 
		if (type == EncryptionType.ARGON_2) {
			Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2id);
			return argon2.verify(hash, password);
		} 
		if (type == EncryptionType.BCRYPT) {
			BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hash);
			return result.verified;
		}
		return false;
	}

	public static String sha512(String base) {
	    try{
	        MessageDigest digest = MessageDigest.getInstance("SHA-512");
	        byte[] hash = digest.digest(base.getBytes("UTF-8"));
	        StringBuffer hexString = new StringBuffer();

	        for (int i = 0; i < hash.length; i++) {
	            String hex = Integer.toHexString(0xff & hash[i]);
	            if(hex.length() == 1) hexString.append('0');
	            hexString.append(hex);
	        }

	        return hexString.toString();
	    } catch(Exception ex){
	       throw new RuntimeException(ex);
	    }
	}
	
	public static String md5(String base) {
	    try{
	        MessageDigest digest = MessageDigest.getInstance("MD5");
	        byte[] hash = digest.digest(base.getBytes("UTF-8"));
	        StringBuffer hexString = new StringBuffer();

	        for (int i = 0; i < hash.length; i++) {
	            String hex = Integer.toHexString(0xff & hash[i]);
	            if(hex.length() == 1) hexString.append('0');
	            hexString.append(hex);
	        }

	        return hexString.toString();
	    } catch(Exception ex){
	       throw new RuntimeException(ex);
	    }
	}

	public static String sha256(String base) {
	    try{
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hash = digest.digest(base.getBytes("UTF-8"));
	        StringBuffer hexString = new StringBuffer();

	        for (int i = 0; i < hash.length; i++) {
	            String hex = Integer.toHexString(0xff & hash[i]);
	            if(hex.length() == 1) hexString.append('0');
	            hexString.append(hex);
	        }

	        return hexString.toString();
	    } catch(Exception ex){
	       throw new RuntimeException(ex);
	    }
	}
	
}
