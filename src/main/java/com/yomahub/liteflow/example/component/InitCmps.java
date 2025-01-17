package com.yomahub.liteflow.example.component;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.annotation.LiteflowMethod;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.LiteFlowMethodEnum;
import com.yomahub.liteflow.example.bean.PriceCalcReqVO;
import com.yomahub.liteflow.example.bean.PriceStepVO;
import com.yomahub.liteflow.example.bean.ProductPackVO;
import com.yomahub.liteflow.example.enums.PriceTypeEnum;
import com.yomahub.liteflow.example.slot.PriceContext;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

@LiteflowComponent
public class InitCmps {

    /**
     * 检查参数
     *
     * @param bindCmp
     */
    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS, nodeId = "checkCmp")
    public void processCheckCmp(NodeComponent bindCmp){
        //拿到请求参数
        PriceCalcReqVO req = bindCmp.getSlot().getRequestData();
        //参数验证完成
    }

    /**
     * 初始化上下文
     *
     * @param bindCmp
     */
    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS, nodeId = "slotInitCmp")
    public void processSlotInitCmp(NodeComponent bindCmp){
        //把主要参数冗余到slot里
        PriceCalcReqVO req = bindCmp.getRequestData();
        // 出参
        PriceContext context = bindCmp.getContextBean(PriceContext.class);
        context.setOrderNo(req.getOrderNo());
        context.setOversea(req.isOversea());
        context.setMemberCode(req.getMemberCode());
        context.setOrderChannel(req.getOrderChannel());
        context.setProductPackList(req.getProductPackList());
        context.setCouponId(req.getCouponId());
    }

    @LiteflowMethod(value = LiteFlowMethodEnum.IS_ACCESS, nodeId = "slotInitCmp")
    public boolean accessSlotInitCmp(NodeComponent bindCmp){
        PriceCalcReqVO req = bindCmp.getSlot().getRequestData();
        if(req != null){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 记录价格变更的日志
     *
     * @param bindCmp
     */
    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS, nodeId = "priceStepInitCmp")
    public void processPriceStepInitCmp(NodeComponent bindCmp){
        PriceContext context = bindCmp.getContextBean(PriceContext.class);

        //初始化价格步骤
        List<ProductPackVO> packList = context.getProductPackList();
        BigDecimal totalOriginalPrice = new BigDecimal(0);
        for(ProductPackVO packItem : packList) {
            // 结算总价格 = 商品单价 * 数量
            totalOriginalPrice = totalOriginalPrice.add(packItem.getSalePrice().multiply(new BigDecimal(packItem.getCount())));
        }
        context.addPriceStep(new PriceStepVO(PriceTypeEnum.ORIGINAL,
                null,
                null,
                totalOriginalPrice,
                totalOriginalPrice,
                PriceTypeEnum.ORIGINAL.getName()));
        context.setOriginalOrderPrice(totalOriginalPrice);
    }

    @LiteflowMethod(value = LiteFlowMethodEnum.IS_ACCESS, nodeId = "priceStepInitCmp")
    public boolean accessPriceStepInitCmp(NodeComponent bindCmp){
        PriceContext context = bindCmp.getContextBean(PriceContext.class);
        if(CollectionUtils.isNotEmpty(context.getProductPackList())){
            return true;
        }else{
            return false;
        }
    }
}
