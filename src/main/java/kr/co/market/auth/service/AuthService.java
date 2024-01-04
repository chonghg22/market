package kr.co.market.auth.service;

import kr.co.market.auth.dao.AuthDao;
import kr.co.market.auth.vo.AuthVo;
import kr.co.market.auth.vo.LoginVo;
import kr.co.market.common.CryptoUtil;
import kr.co.market.view.config.dao.ConfigDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private AuthDao authDao;

    @Autowired
    ConfigDao configDao;

    @Autowired
    GoogleOTP googleOTP;

//    @Value("${SERVICE_EE}")
    private String eeCode;

//    @Value("${SERVICE_SECRET}")
    private String secretStr;

//    @Value("${SERIVCE_OPT}")
    private String otp;

    private static final String ALGORITHM = "HmacSHA1";
    private static byte[] secretKey = null;

    public Map<String, Object> getLogin(LoginVo loginInfo) {
        Map<String, Object> rtMap = new HashMap<>();
        if (loginInfo.getLoginId() == null || loginInfo.getLoginId() == "") return rtMap;

        int logId = authDao.insertLoginLog(loginInfo);
        //아이디확인
        AuthVo authVo = authDao.selectCpMemberInfo(loginInfo);

        if (authVo == null || logId < 1) {
            loginInfo.setLoginResult("FAIL");
            loginInfo.setLoginResultMsg("잘못된 아이디");
            authDao.updateLoginLog(loginInfo);
            rtMap.put("resultCode", "9999");
            rtMap.put("account", null);
            return rtMap;
        }

        //패스워드 확인
        String dbPasswd = String.valueOf(authVo.getPasswd());
        String dbSalt = String.valueOf(authVo.getSalt());
        String passwd = loginInfo.getLoginPw();
        passwd = CryptoUtil.getPBKDF2(passwd, dbSalt).toUpperCase();
        if (!passwd.equals(dbPasswd.toUpperCase())) {
            loginInfo.setLoginResult("FAIL");
            loginInfo.setLoginResultMsg("잘못된 비밀번호");
            authDao.updateLoginLog(loginInfo);
            rtMap.put("resultCode", "9999");
            rtMap.put("account", null);
            return rtMap;
        }

        if("PROC".equals(eeCode)){
            if (!checkOtpPw(loginInfo)) {
                rtMap.put("resultCode", "9999");
                rtMap.put("account", null);
                return rtMap;
            }

            //ip 확인
            String accessIp = String.valueOf(authVo.getAccessIp());
            if (!(accessIp.equals(loginInfo.getLoginIp()) || "112.169.75.145".equals(loginInfo.getLoginIp()))) {
                loginInfo.setLoginResult("FAIL");
                loginInfo.setLoginResultMsg("다른 IP[" + loginInfo.getLoginIp() + "]");
                authDao.updateLoginLog(loginInfo);
                rtMap.put("resultCode", "9999");
                rtMap.put("account", null);
                return rtMap;
            }
        }

        log.info(authVo.getUsername() + " login in success");
        loginInfo.setLoginResult("SUCCESS");
        loginInfo.setLoginResultMsg("성공!");
        authDao.updateLoginLog(loginInfo);
        authDao.updateLastLogIn(loginInfo);
        rtMap.put("resultCode", "0000");
        rtMap.put("authVo", authVo);

        return rtMap;
    }

    public boolean checkOtpPw(LoginVo loginInfo) {
        boolean rtnValue = false;

        Map<String, Object> getConfig = configDao.getConfig();
        boolean equalPW = String.valueOf(getConfig.get("countersign")).equals(loginInfo.getPassphrase());
        boolean equalOTP = googleOTP.checkCode(loginInfo.getOtp(), otp);

        if (!equalPW) {
            loginInfo.setLoginResult("FAIL");
            loginInfo.setLoginResultMsg("잘못된 암구호");
            authDao.updateLoginLog(loginInfo);
            return rtnValue;
        }

        if (equalOTP) {
            rtnValue = true;
        } else {
            loginInfo.setLoginResult("FAIL");
            loginInfo.setLoginResultMsg("잘못된 OTP");
            authDao.updateLoginLog(loginInfo);
            return rtnValue;
        }

        return rtnValue;
    }


    public String getPhone(String id) {
        String enc = authDao.selectGetPhone(CryptoUtil.encrypt(id));
        return CryptoUtil.decrypt(enc);
    }

    public String getPinTxt() throws Exception {
        secretKey = secretStr.getBytes();
        return create();
    }

    private static long create(long time) throws Exception {
        byte[] data = new byte[8];

        long value = time;
        for (int i = 0; i < data.length; ++i) {
            data[i] = (byte) value;
        }

        Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(new SecretKeySpec(secretKey, ALGORITHM));
        byte[] hash = mac.doFinal(data);
        int offset = hash[20 - 1] & 0xF;

        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            truncatedHash |= hash[offset + i] & 0xFF;
        }

        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;

        return truncatedHash;
    }

    public static String create() throws Exception {
        return String.format("%06d", create(new Date().getTime()));
    }

    public void callOTPSender(Map<String, Object> loginMap) {
        authDao.insertCallOPTSender(loginMap);
    }

    public String checkId(int id) {
        return authDao.selectCheckId(id);
    }

    public void deleteAdminRole(LoginVo loginInfo) { authDao.deleteAdminRole(loginInfo); }
}
