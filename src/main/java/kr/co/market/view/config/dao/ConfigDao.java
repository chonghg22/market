package kr.co.market.view.config.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Mapper
public interface ConfigDao {

    Map<String, Object> getConfig();

    int configUpdate(Map<String, Object> params);

    @Transactional(readOnly=true)
    List<Map<String, Object>> getAdmin(Map<String, Object> params);

    int upsertAdmin(Map<String, Object> params);

    int deleteAdmin(Map<String, Object> params);

    void updateAdminInfo(Map<String, Object> params);
}
