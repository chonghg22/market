package kr.co.market.auth.dao;

import kr.co.market.auth.vo.AuthVo;
import kr.co.market.auth.vo.LoginVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Mapper
public interface AuthDao {

    AuthVo selectCpMemberInfo(LoginVo loginInfo);

    String selectGetPhone(String encrypt);

    void insertCallOPTSender(Map<String, Object> loginMap);

    String selectCheckId(@Param("id") int id);

    int insertLoginLog(LoginVo loginInfo);

    void updateLoginLog(LoginVo loginInfo);

    void updateLastLogIn(LoginVo loginInfo);

    void deleteAdminRole(LoginVo loginInfo);
}
