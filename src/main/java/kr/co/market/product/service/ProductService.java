package kr.co.market.product.service;

import kr.co.market.product.domain.dto.ProductDto;
import kr.co.market.product.vo.LoginVo;

import java.util.List;

public interface ProductService {
    public List<ProductDto> getSearchProduct(String searchWord);
}
