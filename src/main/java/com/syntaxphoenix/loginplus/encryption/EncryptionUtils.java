package com.syntaxphoenix.loginplus.encryption;

import java.security.MessageDigest;

import at.favre.lib.crypto.bcrypt.BCrypt;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;

public class EncryptionUtils {
	
	private Argon2 argon2;
	private int argon2Cores;
	private int argon2Memory;
	private int argon2Parallelism;
	private int bcryptRounds;
	
	public EncryptionUtils(int argon2Cores, int argon2Memory, int argon2Parallelism, int bcryptRounds) {
		this.argon2 = Argon2Factory.create(Argon2Types.ARGON2id);
		
		this.argon2Cores = argon2Cores;
		this.argon2Memory = argon2Memory;
		this.argon2Parallelism = argon2Parallelism;
		this.bcryptRounds = bcryptRounds;
	}
	
	public String hashPassword(String password, EncryptionType type) {
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
			return this.argon2.hash(argon2Cores, argon2Memory * 1024, argon2Parallelism, password);
		}
		
		if (type == EncryptionType.BCRYPT) {
			return BCrypt.withDefaults().hashToString(bcryptRounds, password.toCharArray());
		}
		return null;
	}
	
	public boolean verifyPassword(String password, EncryptionType type, String hash) {
		if (type == EncryptionType.SHA256 || type == EncryptionType.SHA512 || type == EncryptionType.MD_5) {
			return hashPassword(password, type).equalsIgnoreCase(hash);
		} 
		if (type == EncryptionType.ARGON_2) {
			return this.argon2.verify(hash, password);
		} 
		if (type == EncryptionType.BCRYPT) {
			BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hash);
			return result.verified;
		}
		return false;
	}

	private String sha512(String base) {
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
	
	private String md5(String base) {
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

	private String sha256(String base) {
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
