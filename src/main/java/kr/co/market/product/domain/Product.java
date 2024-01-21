package kr.co.market.product.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class Product {
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
    private String popular;
    private String isDisplay;
    private LocalDateTime modDate;
    private String modUser;
    private String regUser;
    private LocalDateTime regDate;

//    @Builder
//    public Product(Long id, String title, String link, String image, String lprice, String mallName, String productId, String productType, String brand, String marker, String category1, String category2, String category3, String category4, String popular, String isDisplay, LocalDateTime modDate, String modUser, String regUser, LocalDateTime regDate) {
//        this.id = id;
//        this.title = title;
//        this.link = link;
//        this.image = image;
//        this.lprice = lprice;
//        this.mallName = mallName;
//        this.productId = productId;
//        this.productType = productType;
//        this.brand = brand;
//        this.marker = marker;
//        this.category1 = category1;
//        this.category2 = category2;
//        this.category3 = category3;
//        this.category4 = category4;
//        this.popular = popular;
//        this.isDisplay = isDisplay;
//        this.modDate = modDate;
//        this.modUser = modUser;
//        this.regUser = regUser;
//        this.regDate = regDate;
//    }
}
