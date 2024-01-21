package kr.co.market.product.controller;

import kr.co.market.product.domain.Product;
import kr.co.market.product.domain.dto.ProductDto;
import kr.co.market.product.service.ProductService;
import kr.co.market.common.NaverSearchApi;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@PropertySource("classpath:code.properties")
public class ProductController {

	@Autowired
	private ProductService productService;

	@RequestMapping(value = "/searchProductPage")
	public String getSearchProductPage() {
		return "searchProduct";
	}


	@RequestMapping(value = "/searchProduct")
	@ResponseBody
	public Map<String,Object> getSearchProduct(@RequestParam(defaultValue="") String searchWord) {

		Map<String, Object> itemList = new HashMap<>();
		List<ProductDto> productList = productService.getSearchProduct(searchWord);
		itemList.put("productList",productList);
		return itemList;
	}

}
