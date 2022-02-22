package org.brewchain.sdk.model;

import lombok.Getter;

/**转账信息*/
@Getter
public class TransferInfo {

    private String toAddr;//接收地址
    private String amount;//转账主币数量

    public TransferInfo(String toAddr, String amount) {
        this.toAddr = toAddr;
        this.amount = amount;
    }
}