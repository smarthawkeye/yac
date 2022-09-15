package io.github.smarthawkeye.core.common.util;

import cn.hutool.json.JSONUtil;
import com.nimbusds.jose.JWSObject;
import io.github.smarthawkeye.core.common.pojo.bo.JwtModelBO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
public class TokenUtil {

    /**
     * 创建JWT
     */
    public static String createJWT(Map<String, Object> claims, String clientId, String jwtsecret, Long time) {
        //指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Date now = new Date(System.currentTimeMillis());
        SecretKey secretKey = generalKey(jwtsecret);
        long nowMillis = System.currentTimeMillis();
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                //可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setId(clientId)
                //jwt的签发时间
                .setIssuedAt(now)
                //设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, secretKey);
        if (time >= 0) {
            long expMillis = nowMillis + time * 1000;
            Date exp = new Date(expMillis);
            //设置过期时间
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * 验证jwt
     */
    public static Claims verifyJwt(String token, String jwtsecret) {
        SecretKey key = generalKey(jwtsecret);
        Claims claims;
        try {
            //得到DefaultJwtParser
            claims = Jwts.parser()
                    //设置签名的秘钥
                    .setSigningKey(key)
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public static JwtModelBO parseJwt(String token) {
        try {
            return JSONUtil.toBean(JWSObject.parse(token).getPayload().toString(), JwtModelBO.class);
        }catch (ParseException ex){
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * 由字符串生成加密key
     */
    public static SecretKey generalKey(String jwtsecret) {
        byte[] encodedKey = Base64.decodeBase64(jwtsecret);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }
}
