package kr.co.market.product.domain.dto;

import kr.co.market.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ProductDto {

    private Long id;
    private String title;
    private String link;
    private String image;
    private String lprice;
    private String mallName;
    private String productId;
    private String productType;
    private String brand;
    private String marker;
    private String category1;
    private String category2;
    private String category3;
    private String category4;
    private LocalDateTime regDate;
    private String popular;
    private String isDisplay;
    private LocalDateTime modDate;
    private String modUser;
    private String regUser;

    public static ProductDto of(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .link(product.getLink())
                .image(product.getImage())
                .lprice(product.getLprice())
                .mallName(product.getMallName())
                .productId(product.getProductId())
                .productType(product.getProductType())
                .brand(product.getBrand())
                .marker(product.getMarker())
                .category1(product.getCategory1())
                .category2(product.getCategory2())
                .category3(product.getCategory3())
                .category4(product.getCategory4())
                .regDate(product.getRegDate())
                .popular(product.getPopular())
                .isDisplay(product.getIsDisplay())
                .modDate(product.getModDate())
                .modUser(product.getModUser())
                .regUser(product.getRegUser())
                .build();

    }
}
