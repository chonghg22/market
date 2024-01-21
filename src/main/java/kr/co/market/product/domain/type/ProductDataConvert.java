package kr.co.market.product.domain.type;

import kr.co.market.common.NaverSearchApi;
import kr.co.market.product.domain.Product;
import kr.co.market.product.domain.dto.ProductDto;
import lombok.Getter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ProductDataConvert {

    public static List<ProductDto> productApiConvertedData(String clientId, String clientSecret,String searchWord){
        System.out.println("clientId = " + clientId);
        System.out.println("clientSecret = " + clientSecret);
        System.out.println("searchWord = " + searchWord);
        String Naverapi = NaverSearchApi.naverAPICall(clientId,clientSecret,searchWord);
        System.out.println("Naverapi = " + Naverapi);
        List<ProductDto> ProductDtoList = new ArrayList<>();
        Product product = new Product();
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(Naverapi);
            JSONArray itemsArray = (JSONArray) jsonObject.get("items");

            for (Object item : itemsArray) {
                JSONObject itemObject = (JSONObject) item;
                System.out.println("itemObject = " + itemObject);
                System.out.println("itemObject.get(\"title\") = " + itemObject.get("title"));
                product.setTitle(itemObject.get("title").toString());
                product.setLink((String)itemObject.get("link"));
                product.setImage((String)itemObject.get("image"));
                product.setLprice((String)itemObject.get("lprice"));
                product.setMallName((String)itemObject.get("mallName"));
                product.setProductId((String)itemObject.get("productId"));
                product.setProductType((String)itemObject.get("productType"));
                product.setBrand((String)itemObject.get("brand"));
                product.setMarker((String)itemObject.get("maker"));
                product.setCategory1((String)itemObject.get("category1"));
                product.setCategory2((String)itemObject.get("category2"));
                product.setCategory3((String)itemObject.get("category3"));
                product.setCategory4((String)itemObject.get("category4"));

                ProductDtoList.add(ProductDto.of(product));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ProductDtoList;
    }
}
