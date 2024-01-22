package kr.co.market.product.dao;

import kr.co.market.product.domain.Product;
import kr.co.market.product.domain.dto.ProductDto;
import kr.co.market.product.vo.AuthVo;
import kr.co.market.product.vo.LoginVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface ProductDao {
    int upsertProduct(ProductDto product);
}
