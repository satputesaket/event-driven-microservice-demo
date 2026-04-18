package com.ezbuyshop.product.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLookUpRepository extends JpaRepository<ProductLookUpEntity, String> {

	ProductLookUpEntity findByProductIdOrTitle(String productId, String title);

}
