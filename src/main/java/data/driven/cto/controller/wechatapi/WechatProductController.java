package data.driven.cto.controller.wechatapi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import data.driven.cto.business.product.ProductCatgService;
import data.driven.cto.business.product.ProductPreCtoService;
import data.driven.cto.business.product.ProductService;
import data.driven.cto.business.product.ProductSupportPartsService;
import data.driven.cto.common.Constant;
import data.driven.cto.entity.parts.PartsCatgEntity;
import data.driven.cto.entity.parts.PartsEntity;
import data.driven.cto.entity.product.ProductPreCtoEntity;
import data.driven.cto.vo.product.ProductCatgVO;
import data.driven.cto.vo.product.ProductVO;
import data.driven.cto.vo.product.SupportPartsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 产品controller
 * @author hejinkai
 * @date 2018/8/18
 */
@Controller
@RequestMapping(path = "/wechatapi/product")
public class WechatProductController {

    private final Logger logger = LoggerFactory.getLogger(WechatProductController.class);

    @Autowired
    private ProductCatgService productCatgService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductPreCtoService productPreCtoService;
    @Autowired
    private ProductSupportPartsService productSupportPartsService;

    /**
     * 查找所有的产品分类
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/findProDuctCatgList")
    public JSONObject findProDuctCatgList(){
        JSONObject result = new JSONObject();
        List<ProductCatgVO> list = productCatgService.findProDuctCatgList();
        result.put("success", true);
        result.put("data", JSONArray.parseArray(JSONArray.toJSONString(list)));
        return result;
    }

    /**
     * 根据分类查询产品
     * @param catgCode
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/findProductByCatgCode")
    public JSONObject findProductByCatgCode(String catgCode){
        JSONObject result = new JSONObject();
        List<ProductVO> list = productService.findProductByCatgCode(catgCode);

        if(list != null && list.size() > 0){
            for(ProductVO product : list){
                if(product != null && product.getFilePath() != null){
                    product.setFilePath(Constant.STATIC_FILE_PATH + product.getFilePath());
                }
            }
        }

        result.put("success", true);
        result.put("data", JSONArray.parseArray(JSONArray.toJSONString(list)));
        return result;
    }

    /**
     * 根据id获取产品信息
     * @param productId
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/getProductById")
    public JSONObject getProductById(String productId){
        JSONObject result = new JSONObject();
        ProductVO product = productService.getProductById(productId);
        if(product != null && product.getFilePath() != null){
            product.setFilePath(Constant.STATIC_FILE_PATH + product.getFilePath());
        }
        result.put("success", true);
        result.put("data", JSONObject.parseObject(JSONObject.toJSONString(product)));
        return result;
    }

    /**
     * 根据产品获取预配置列表
     * @param productId
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/findProductPreCtoList")
    public JSONObject findProductPreCtoList(String productId){
        JSONObject result = new JSONObject();
        List<ProductPreCtoEntity> list = productPreCtoService.findProductPreCtoListByProductId(productId);
        result.put("success", true);
        result.put("data", JSONArray.parseArray(JSONArray.toJSONString(list)));
        return result;
    }

    /**
     * 挑选配件
     * @param productId
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/selectParts")
    public JSONObject selectParts(String productId, String preCtoId){
        JSONObject result = new JSONObject();
        List<JSONObject> resultList = new ArrayList<JSONObject>();
        List<PartsCatgEntity> partsCatgEntityList = productSupportPartsService.findPartsCatgList(productId);
        if(partsCatgEntityList != null && partsCatgEntityList.size() > 0){
            List<SupportPartsVO> supportPartsList = productSupportPartsService.findPartsByProductId(productId);
            Map<String, String> parPartsMap = null;
            if(preCtoId != null){
                List<PartsEntity> prePartsList = productPreCtoService.findPartsListByPreCtoId(preCtoId);
                parPartsMap = prePartsList.stream().collect(Collectors.toMap(o -> o.getCatgId(), o -> o.getPartsId()));
            }

            if(supportPartsList != null && supportPartsList.size() > 0){
                Map<String, List<SupportPartsVO>> supportPartsMap = supportPartsList.stream().collect(Collectors.groupingBy(o -> o.getCatgId()));

                for (PartsCatgEntity partsCatg : partsCatgEntityList){
                    JSONObject catg = new JSONObject();
                    catg.put("catg", partsCatg);
                    List<SupportPartsVO> partsVOList = supportPartsMap.get(partsCatg.getCatgId());
                    if(partsVOList != null && partsVOList.size() > 0){
                        String parPartsId = parPartsMap.get(partsCatg.getCatgId());
                        catg.put("parPartsId", parPartsId);
                    }
                    catg.put("parts", partsVOList);
                    resultList.add(catg);
                }
            }
        }
        result.put("success", true);
        result.put("data", JSONArray.parseArray(JSONArray.toJSONString(resultList)));
        return result;
    }

}
