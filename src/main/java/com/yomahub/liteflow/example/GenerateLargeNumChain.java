package com.yomahub.liteflow.example;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

public class GenerateLargeNumChain {

    public static void main(String[] args) {
        String template = "<chain name=\"mainChain{}\">THEN(checkCmp, slotInitCmp, priceStepInitCmp,promotionConvertCmp, s_memberDiscountCmp,promotionChain, s_couponCmp,SWITCH(postageCondCmp).to(postageCmp, overseaPostageCmp),priceResultCmp, stepPrintCmp);</chain>\n";
        for (int i = 0; i < 10000; i++) {
            FileUtil.appendUtf8String(StrUtil.format(template, i+1), "/Users/bryan31/Desktop/flow3.el.xml.bak");

        }
    }
}
