package kr.co.market.auth.controller;

import kr.co.market.auth.vo.AuthVo;
import kr.co.market.common.CryptoUtil;
import kr.co.market.common.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class AuthController {

//	@Value("${SERVICE_EE}")
//	private String eeCode;

//	@Autowired
//	JandiService jandiService;

	@RequestMapping(value = "/login")
	public String login(HttpSession session) {
		log.info("Welcome your browser id = {}", session.getId());
		return "login";
	}

	@RequestMapping(value = "/loginFailed", method = RequestMethod.GET)
	public String loginFailed(HttpSession session, ModelMap model) {
		model.addAttribute("error", true);
		log.info("error login! {}", session.getId());
		return "redirect:/";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout() {
		return "redirect:/";
	}

	@RequestMapping(value = "/loginProcess", method = RequestMethod.POST)
	public String loginProcess(HttpSession session) {
		log.info("loginProcess login! {}", session.getId());
		return "redirect:/";
	}

	@RequestMapping(value = "/sessionClear", method = RequestMethod.GET)
	public String sessionClear() {
		return "redirect:/";
	}

	@RequestMapping(value = "/loginSuccess")
	public String loginSuccess(HttpServletRequest request, HttpSession session) {
		AuthVo userDetails = (AuthVo) SecurityContextHolder.getContext().getAuthentication().getDetails();
		log.info("Welcome login_success! {}, {}", session.getId(), CryptoUtil.decrypt(userDetails.getUsername()));
		SessionUtil.setAttribute("userLoginInfo", userDetails);

		userDetails.setSessionId(SessionUtil.getSessionId());
		session.setMaxInactiveInterval(60*30);
		String reqIP = "";
		if (request.getHeader("X-Forwarded-For") == null) {
			reqIP = request.getRemoteAddr();
		}else{
			reqIP = request.getHeader("X-Forwarded-For");
		}

		SessionUtil.setAttribute(session.getId(), reqIP);

//		if("PROC".equals(eeCode)){
//			jandiService.sendAlimLogin("[AR]"+CryptoUtil.decrypt(userDetails.getUsername())+" 로그인, 원격 로그인시 반드시 댓글 주세요~","","","","");
//		}

		return "redirect:/";
	}
}
