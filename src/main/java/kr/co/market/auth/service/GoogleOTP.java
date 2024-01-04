package kr.co.market.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base32;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Slf4j
@Service
public class GoogleOTP {

    //OTP번호 check
    public static boolean checkCode(String userCode, String otpkey) {
        boolean result = false;

        if (userCode == null || "".equals(userCode)) {
            return result;
        }

        try {
            long otpnum = Integer.parseInt(userCode);
            long wave = new Date().getTime() / 30000;
            Base32 codec = new Base32();
            byte[] decodedKey = codec.decode(otpkey);
            int window = 1;
            for (int i = -window; i <= window; ++i) {
                long hash = verify_code(decodedKey, wave + i);
                if (hash == otpnum) result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //OPT_KEY 검증
    private static int verify_code(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = new byte[8];
        long value = t;
        for (int i = 7; i >= 0; value >>>= 8) {
            data[i] = (byte) value;
            i--;
        }

        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);
        int offset = hash[20 - 1] & 0xF;
        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            truncatedHash |= (hash[offset + i] & 0xFF);
        }
        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;

        return (int) truncatedHash;
    }
}