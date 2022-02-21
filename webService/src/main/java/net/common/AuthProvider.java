package net.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import net.config.KeysConfig;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Takanashi
 * @since 2021/11/30
 */
public class AuthProvider {
    static String issuer = "https://javapaper.net";
    static String audience = "javapaper-jwt-audience";
    static String realm = "Java Paper Project";

    static String subject = "Authentication";

    static Long expiresIn = (long) (3600 * 1000 * 24);


    static Map<String,Object> map =  new HashMap<String, Object>(){{
        put("typ","JWT");
        put("alg","HS256");
    }};


    // 签发证书
    public static Map<String, String> sign(int userId, String secretKey) {
        Map<String, String> signMap = new HashMap<>();
        signMap.put("token",
                JWT.create()
                        .withHeader(map)
                        .withIssuer(issuer)
                        .withSubject(subject)
                        .withAudience(audience)
                        .withClaim("userId", userId)
                        .withIssuedAt(new Date(System.currentTimeMillis()))
                        .withExpiresAt(new Date(System.currentTimeMillis() + expiresIn))
                        .sign(Algorithm.HMAC256(secretKey)));
        return signMap;
    }

    // AuthProvider中
    // 验证证书
    public static int validate(String token,String authkey)
    {
        Algorithm algorithm = Algorithm.HMAC256(authkey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);

        if (jwt.getIssuer().equals(AuthProvider.issuer)
                && jwt.getSubject().equals(AuthProvider.subject)
                && jwt.getAudience().contains(AuthProvider.audience)
                && jwt.getExpiresAt().after(new Date(System.currentTimeMillis()))
        ) {
            return jwt.getClaim("userId").asInt();
        } else {
            throw new JWTVerificationException("验证错误");
        }
    }
}
