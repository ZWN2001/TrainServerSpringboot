package com.zwn.trainserverspringboot.util;

import com.zwn.trainserverspringboot.command.bean.UserDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class JwtUtils {
	private static final String CLAIM_KEY_USER_NAME = "user_name";
	
	private static final String CLAIM_KEY_AUTHORITIES = "scope";
	
	private final Map<Long, String> tokenMap = new ConcurrentHashMap<>(32);

    private final String secret = "111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
        "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
        "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
        "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
        "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";

    private final Long access_token_expiration = 86400L;
//    jwt.header=Authorization
//    jwt.secret=mySecret
//    jwt.expiration=
//    jwt.tokenHead=Bearer
	
	private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
	
	
	public UserDetail getUserFromToken(String token) {
        UserDetail userDetail;
        try {
            final Claims claims = getClaimsFromToken(token);
            long userId = getUserIdFromToken(token);
            String username = claims.getSubject();
            String role = claims.get(CLAIM_KEY_AUTHORITIES).toString();
            userDetail = new UserDetail(userId, username, "",role);
        } catch (Exception e) {
            userDetail = null;
        }
        return userDetail;
    }
	
    public long getUserIdFromToken(String token) {
        long userId;
        try {
            final Claims claims = getClaimsFromToken(token);
            userId = Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            userId = 0;
        }
        return userId;
    }

    public String getUsernameFromToken(String token) {
        String userName;
        try {
            final Claims claims = getClaimsFromToken(token);
            userName = String.valueOf(claims.get(CLAIM_KEY_USER_NAME));
        } catch (Exception e) {
            userName = "";
        }
        return userName;
    }
    
    public void putToken(long userId, String token) {
        tokenMap.put(userId, token);
    }

    public void deleteToken(long userId) {
        tokenMap.remove(userId);
    }

    public boolean containToken(long userId, String token) {
        return String.valueOf(userId).length() != 0 && tokenMap.containsKey(userId) && tokenMap.get(userId).equals(token);
    }
    
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            refreshedToken = generateAccessToken(Long.parseLong(claims.getSubject()), claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }
    
    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = claims.getIssuedAt();
        } catch (Exception e) {
            created = null;
        }
        return created;
    }
    
    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }
    
    public Boolean validateToken(String token, UserDetails userDetails) {
        UserDetail userDetail = (UserDetail) userDetails;
        final long userId = getUserIdFromToken(token);
        final String username = getUsernameFromToken(token);
//        final Date created = getCreatedDateFromToken(token);
        return (userId == userDetail.getUserId()
                && username.equals(userDetail.getUsername())
                && !isTokenExpired(token)
//                && !isCreatedBeforeLastPasswordReset(created, userDetail.getLastPasswordResetDate())
        );
    }
    

    
    public String generateAccessToken(UserDetail userDetail) {
        Map<String, Object> claims = generateClaims(userDetail);
        claims.put(CLAIM_KEY_AUTHORITIES, authoritiesToArray(userDetail.getAuthorities()).get(0));
        return generateAccessToken(userDetail.getUserId(), claims);
    }
    
    private Map<String, Object> generateClaims(UserDetail userDetail) {
        Map<String, Object> claims = new HashMap<>(16);
        claims.put(CLAIM_KEY_USER_NAME, userDetail.getUsername());
        return claims;
    }
    
    private List<String> authoritiesToArray(Collection<? extends GrantedAuthority> authorities) {
        List<String> list = new ArrayList<>();
        for (GrantedAuthority ga : authorities) {
            list.add(ga.getAuthority());
        }
        return list;
    }
    
    private String generateAccessToken(long subject, Map<String, Object> claims) {
        return generateToken(subject, claims, access_token_expiration);
    }
    
    private String generateToken(long subject, Map<String, Object> claims, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(subject))
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate(expiration))
                .compressWith(CompressionCodecs.DEFLATE)
                .signWith(SIGNATURE_ALGORITHM, secret)
                .compact();
    }
    
    private Date generateExpirationDate(long expiration) {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }
    
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }
    
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

}
