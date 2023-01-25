package tg.jwt;

import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTHelper {

	public static String generateToken(String username) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(new FileInputStream("d:/Secret.jks"), "root123".toCharArray());
		Key key = keyStore.getKey("xxxx", "root123".toCharArray());
		JwtBuilder builder = Jwts.builder();
		builder.setIssuedAt(new Date());
		builder.setIssuer("Nilesh");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 15);
		builder.setExpiration(cal.getTime());
		builder.setSubject(username);
		Map<String, String> claims = new HashMap();
		claims.put("role", "Admin");
		claims.put("name", "John Doe");
		claims.put("address", "Pune");
		claims.put("dept", "HR");
		claims.put("auth", "Manager");
		claims.put("location", "IN");
		claims.put("accountid", "298849844");
		claims.put("dob", "25-12-1992");
		builder.setClaims(claims);
		builder.setSubject(username);
		builder.signWith(key, SignatureAlgorithm.RS512);
		String result = builder.compact();
		return result;

	}

	public static boolean isValid(String token) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(new FileInputStream("d:/Secret.jks"), "root123".toCharArray());
		Key key = keyStore.getKey("xxxx", "root123".toCharArray());
		JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();
		parser.parseClaimsJws(token);
		return true;
	}

	public static String getUsernameFromToken(String token) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(new FileInputStream("d:/Secret.jks"), "root123".toCharArray());
		Key key = keyStore.getKey("xxxx", "root123".toCharArray());
		JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();
		Jws<Claims> claims = parser.parseClaimsJws(token);
		return claims.getBody().getSubject();
	}

	public static void main(String[] args) throws Exception {
		String token = generateToken("xxx");
		System.out.println(token);
		if (isValid(token)) {
			System.out.println("Token iS Valid for User " + getUsernameFromToken(token));
		}

	}
}
