package kr.co.market.provider;

import kr.co.market.auth.service.AuthService;
import kr.co.market.auth.vo.AuthVo;
import kr.co.market.auth.vo.LoginVo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * API Basic Authentication
 *
 */
@Component
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	private AuthService authService;

	@Autowired
	@Qualifier("sessionRegistry")
	private SessionRegistry sessionRegistry;

	@Autowired(required = false)
	private HttpServletRequest request;

	@SneakyThrows
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		String ip = req.getHeader("X-FORWARDED-FOR");
		if (ip == null)
			ip = req.getRemoteAddr();

		LoginVo loginInfo = new LoginVo();
		loginInfo.setLoginId((String) authentication.getPrincipal());
		loginInfo.setLoginPw((String) authentication.getCredentials());
		loginInfo.setLoginIp(ip);
		loginInfo.setOtp(request.getParameter("otpSecret"));
		loginInfo.setPassphrase(request.getParameter("pw"));

		log.info("login id = {}, ip = {}", loginInfo.getLoginId(), loginInfo.getLoginIp());

		Map<String, Object> authMap = authService.getLogin(loginInfo);

		AuthVo authVo;
		UsernamePasswordAuthenticationToken auth;
		if( authMap == null ){
			log.error(">>>> authMap is null");
			throw new BadCredentialsException("관리자 계정을 확인해주세요.");
		}  else {
			String resultCode = (String) authMap.get("resultCode");
			if( "0000".equals(resultCode) ) {
				authVo = (AuthVo) authMap.get("authVo");
				if(!"ADMIN ".equals(authVo.getRole())){
					log.error("접근 권한이 없습니다. {}", authVo.getRole());
					throw new BadCredentialsException("접근 권한이 없습니다. \n관리자에게 문의해 주세요.");
				}

				//세션 information 에서 세션 정보 조회
				List<Object> principals = sessionRegistry.getAllPrincipals();
				for (Object principal: principals) {
					List<SessionInformation> se = sessionRegistry.getAllSessions(principal, false);
					if(se.size() > 0 && principal.toString().indexOf((String) authentication.getPrincipal()) > -1){
						log.error("reduplication session");
						SessionInformation sessionInformation = sessionRegistry.getSessionInformation(se.get(0).getSessionId());
						sessionRegistry.removeSessionInformation(sessionInformation.getSessionId());

						authService.deleteAdminRole(loginInfo);
						sessionInformation.expireNow();
					}
				}

				auth = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), authVo.getAuthorities());
				auth.setDetails(authVo);
			} else {
				log.error(">>>> Login failed. resultCode:{}", resultCode);
				throw new BadCredentialsException("아이디 또는 비밀번호를 확인 하세요.");
			}
		}

		if(auth == null) {
			log.error(">>>> auth is null");
			throw new BadCredentialsException("시스템 내부 오류가 발생하였습니다. \n관리자에게 문의해 주세요.");
		}

		return auth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
