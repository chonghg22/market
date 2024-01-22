package kr.co.market.product.service.impl;

import kr.co.market.common.NaverSearchApi;
import kr.co.market.product.dao.ProductDao;
import kr.co.market.product.domain.dto.ProductDto;
import kr.co.market.product.domain.type.ProductDataConvert;
import kr.co.market.product.service.ProductService;
import kr.co.market.common.CryptoUtil;
import kr.co.market.product.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Value("${naver.clientId}")
    private String clientId;

    //애플리케이션 클라이언트 시크릿
    @Value("${naver.clientSecret}")
    private String clientSecret;

    @Override
    public List<ProductDto> getSearchProduct(String searchWord) {
        List<ProductDto> productDtoList = ProductDataConvert.productApiConvertedData(clientId,clientSecret,searchWord);
        for(ProductDto productDto : productDtoList){
            productDao.upsertProduct(productDto);
        }
        return productDtoList;
    }


}

