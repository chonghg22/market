package kr.co.market.auth.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoginVo {

	private String id;
	private String loginId;
	private String loginPw;
	private String loginIp;
	private String loginResult;
	private String loginResultMsg;

	private String otp;
	private String passphrase;
	
}
