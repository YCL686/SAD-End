package com.example.sharablead.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.common.TokenException;
import com.example.sharablead.entity.Token;
import com.example.sharablead.enums.GlobalResponseEnum;
import com.example.sharablead.request.GenerateTokenRequest;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Objects;

@Component
public class TokenUtil {

@Value("${token.secret.key}")
private String secretKey;

@Autowired
private RedisUtil redisUtil;

    public String generateToken(GenerateTokenRequest token) {
        JwtClaims claims = new JwtClaims();
        claims.setSubject(token.getNickName());
        claims.setClaim("nickName",token.getNickName());
        claims.setClaim("userId", token.getUserId());
        claims.setClaim("address", token.getAddress());
        claims.setClaim("roleNames", token.getRoleNames());
        //3天有效
        claims.setExpirationTimeMinutesInTheFuture(60*24*3);

        Key key = null;
        try {
            key = new HmacKey(secretKey.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
        jws.setKey(key);
        jws.setDoKeyValidation(false); // relaxes the key length requirement
        String tokenStr = null;
        try {
            tokenStr = jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new RuntimeException(e);
        }
        redisUtil.set(token.getUserId().toString(), tokenStr, (long) (60*24*3*60));
        try {
            return jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new RuntimeException(e);
        }
    }


    public Boolean verifyToken(String token) {
        if (token == null || StringUtils.isBlank(token)) {
            throw new TokenException(GlobalResponseEnum.TOKEN_ERROR.getCode(),"token is null");
        }
        Key key = null;
        try {
            key = new HmacKey(secretKey.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("key parse error");
        }
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setAllowedClockSkewInSeconds(30)
                .setRequireSubject()
                .setVerificationKey(key)
                .setRelaxVerificationKeyValidation() // relaxes key length requirement
                .build();
        JwtClaims processedClaims = null;
        try {
            processedClaims = jwtConsumer.processToClaims(token);
        } catch (InvalidJwtException e) {
            throw new TokenException(GlobalResponseEnum.TOKEN_ERROR.getCode(),"token parse error");
        }
        String userId = processedClaims.getClaimValue("userId").toString();
        //redis不存在token缓存
        if (Objects.isNull(redisUtil.get(userId))){
            throw new TokenException(GlobalResponseEnum.TOKEN_ERROR.getCode(), "token is invalid");
        }
        //TODO auth
        return true;
    }


    public Token parseToken(String token) {
        if (token == null) {
            throw new TokenException(GlobalResponseEnum.TOKEN_ERROR.getCode(),"token is null");
        }
        Key key = null;
        try {
            key = new HmacKey(secretKey.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new TokenException(GlobalResponseEnum.TOKEN_ERROR.getCode(),"key parse error");
        }
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setAllowedClockSkewInSeconds(30)
                .setRequireSubject()
                .setVerificationKey(key)
                .setRelaxVerificationKeyValidation() // relaxes key length requirement
                .build();
        JwtClaims processedClaims = null;
        try {
            processedClaims = jwtConsumer.processToClaims(token);
        } catch (InvalidJwtException e) {
            throw new TokenException(GlobalResponseEnum.TOKEN_ERROR.getCode(),"token parse error");
        }
        String userId = processedClaims.getClaimValue("userId").toString();
        //redis不存在token缓存
        if (Objects.isNull(redisUtil.get(userId).toString())){
            throw new TokenException(GlobalResponseEnum.TOKEN_ERROR.getCode(), "token is invalid");
        }
        Token myToken = new Token();
        myToken.setAddress(processedClaims.getClaimValue("address").toString());
        myToken.setNickName(processedClaims.getClaimValue("nickName").toString());
        myToken.setUserId((Long) processedClaims.getClaimValue("userId"));
        myToken.setToken(token);
        return myToken;
    }


}
