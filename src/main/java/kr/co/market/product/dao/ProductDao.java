package kr.co.market.product.dao;

import kr.co.market.product.vo.AuthVo;
import kr.co.market.product.vo.LoginVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface ProductDao {
    void insertProduct(LoginVo loginInfo);
}
