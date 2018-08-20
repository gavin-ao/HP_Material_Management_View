package data.driven.cto.business.product;

import data.driven.cto.entity.parts.PartsEntity;
import data.driven.cto.entity.product.ProductPreCtoEntity;

import java.util.List;

/**
 * 产品预配置表
 * @author hejinkai
 * @date 2018/8/18
 */
public interface ProductPreCtoService {

    /**
     * 根据产品id获取产品预配置的产品对象
     * @param productId
     * @return
     */
    public List<ProductPreCtoEntity> findProductPreCtoListByProductId(String productId);

    /**
     * 根据预配置产品id获取该预配置下的所有配件
     * @param preCtoId
     * @return
     */
    public List<PartsEntity> findPartsListByPreCtoId(String preCtoId);


}
