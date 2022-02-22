# SDK接口和用例

## 准备与须知：

- Java环境下使用，java 8 +

- 引入jar包和依赖

使用配置：

- 目前只有主链domain的配置，默认为：`http://localhost:8000`。如需修改，操作如下：

``` java
//直接在Java里修改Config.host属性的值即可。
Config.host = "http://hostname:port";
```



对下面几个在接口中大量使用的参数做统一说明。接口中不再做过多解释。

- `address ` 钱包地址
- `privateKey` 钱包私钥
- `nonce`  账户nonce（交易序列号） 需要客户端本地维护，提供了nonce管理类NonceKeeper
  - 交易流程中nonce变化：
    - 查询地址nonce=n,并传入交易接口并发送交易，交易成功后，地址nonce值为n+1，
  - 本地nonce管理策略：
    - 交易成功：本地nonce值+1==》NonceKeeper.incrNonce()
    - 交易失败:nonce问题：刷新本地nonce==》NonceKeeper.refreshNonce(addr)，重新发起交易
    - 交易失败:业务逻辑问题：可自行检查发送数据问题，发起相同nonce交易
    - 异常问题(网络超时)：如果未收到链上返回结果，需要确认当前发送的nonce交易是否成功，并确认是否需要重发交易
 
- `exdata` 备注信息 

**注意：接口中有大量交易类型（包括合约方法调用）的接口，这种接口都是异步执行的。即接口返回后仅仅代表交易被接受，但不一定被执行。想获取交易执行结果必须再发起至少一次查询请求。**

> 这块儿操作以后准备封装到SDK里，业务端只需发起一次交易查询请求，轮询确认结果、异步通知都由SDK做。

下文中返回 `交易hash` 的接口一般都是这种类型。

如：

``` json
{
    //操作状态。1表示成功，其他为失败
    "retCode": 1,
    //交易hash
    "hash": "c4495c2ef3fb7881585d48d168833244e89cb615f6fa4789ac49b81c2e065cd5" 
}
```



## 接口和用例列表

     **入口类名：`com.nos.sdk.HiChain`** 

## 接口目录
* [生成助记词](#生成助记词)
* [生成密钥对](#生成密钥对)
* [查询地址信息](#查询地址信息)
* [查询地址nonce](#查询地址nonce)
* [查询交易信息](#查询交易信息)
* [CWV转账](#CWV转账)
* [查询交易信息](#查询交易信息)
* [合约发布](#合约发布)
* [合约调用](#合约调用)
* [RC20发行](#RC20发行)
* [查询RC20信息](#查询RC20信息)
* [查询账户RC20信息](#查询账户RC20信息)
* [RC20转账](#RC20转账)

### 生成助记词
#### 接口说明
    助记词用于生成密钥对
``` java
    /**
     * 生成助记词
     * @return e.g.: "gas twist liar foster crunch arrow brush market author knee fit frown"
     */
    public static String getMnemonic(){
```

#### 用例

``` java
    String words= WalletUtil.getMnemonic();
```

#### 返值
    
    返回字符串

### 生成密钥对
#### 接口说明
    生成地址，私钥，公钥
``` java
    /**
     * 生成助记词
     * @return e.g.: "gas twist liar foster crunch arrow brush market author knee fit frown"
     */
    public static String getMnemonic(){
```

#### 用例

``` java
    String words = "gas twist liar foster crunch arrow brush market author knee fit frown";
    KeyPairs kp = WalletUtil.getKeyPair(words); 
```

#### 返值
``` java
public class KeyPairs {
	//公钥
	String pubkey;
	//私钥
	String prikey;
	//地址
	String address;
	String bcuid;
}
```

### 查询地址信息
#### 接口说明
- 这个不是交易类型。所以直接返回结果。`User.UserInfo` 是SDK里定义的类型，需使用 `protobuf` 。

``` java
    /**
     * 查询用户信息
     * @param address
     * @param cbs
     * @return
     */
    public static String getUserInfo(String address,ChainCallBack... cbs){
```

#### 用例

``` java
String result = HiChain.getUserInfo("69a0e3390ded433f3b1b292606ac2a782e6336de");
```

#### 返值
|参数|类型|说明|示例|
|:----|:----|:----|:----|
|retCode|string|错误码 1成功 -1失败 ||
|retMsg|string|错误信息||
|address|string|地址||
|nonce|int|交易次数（发送交易需要使用该字段用于区分交易）||
|status|int|账户状态 0正常，-1异常锁定黑名单||
|storage_trie_root|string|账户storage根hash ||

e.g 链上未存在记录地址（未发生过交易的地址）
``` shell
{
    "retCode": -1,
    "retMsg": "账户不存在",
    "nonce": 0,
    "status": 0
}
```
e.g 链上已存在记录的地址
``` shell
{
    "retCode": 1,
    "address": "0x3c1ea4aa4974d92e0eabd5d024772af3762720a0",
    "nonce": 2,
    "balance": "0x17f24214d4fd319d0f07b97617d8a231baba36b0",
    "status": 0,
    "storage_trie_root": "0x004681381d2be2bc61ee96cec79ebaff8f0b53baf0438edbad1c769fb03ec906"
}
```

### 查询账户nonce
#### 接口说明
- 获取当前账户nonce,用于查询交易前传入的nonce值，使用者通过NonceKeeper获取并管理地址nonce
``` java
    /**
     * 获取地址nonce
     * @param addr 地址
     * @return
     */
    public static int getNonce(String addr){
```

#### 用例：

``` java
int nonce = NonceKeeper.getNonce("69a0e3390ded433f3b1b292606ac2a782e6336de");
```

#### 返值：
- 返回int值


### 查询交易信息
#### 接口说明
 ``` java   
    /**
     * 查询交易 用于查询链上交易结果及相关信息
     * @param txHash 交易哈希
     * @param cbs
     * @return
     */
    private static String getTxInfo(String txHash,ChainCallBack... cbs) {
 ```   
#### 用例
``` java   
String result = HiChain.getTxInfo("d1820b55b4a10c1f3ec3a780f021dbca4d9bb21f42a745b34b0bc3cbbcb6d0bacf");
```
#### 返值
|参数|类型|说明|示例|
|:----|:----|:----|:----|
|retCode|string|错误码 1成功 -1失败 ||
|retMsg|string|错误信息||
|transaction|object|交易对象||
|hash|string|交易哈希||
|body|object|交易对象||
|nonce|int|交易序号||
|address|string|地址||
|fee_hi|string|手续费高位||
|fee_low|string|手续费低位||
|inner_codetype|int|交易类型<br\>0=普通账户<br\>1=多重签名账户<br\>2=20合约账户<br\>3=721合约账户<br\>=CVM合约<br\>5=JSVM合约(可并行)||
|code_data|string|执行代码||
|timestamp|long|交易入库时间||
|signature|string|签名||
|status|object|状态信息对象||
|status|string|状态 0x01=成功 0xff失败||
|result|string|status成功时返回返回值，失败时返回错误信息||
|hash|string|块哈希||
|height|int|块高度||
|timestamp|int|确认时间||
|node|object|交易接收节点对象||
|nid|string|节点ID||
|address|string|节点地址||
|accepttimestamp|int|交易接收时间||

    {
        "retCode": 1,
        "transaction": {
            "hash": "0xd1820b55b4a10c1f3ec3a780f021dbca4d9bb21f42a745b34b0bc3cbbcb6d0bacf",
            "body": {
                "nonce": 5,
                "address": "0x3c1ea4aa4974d92e0eabd5d024772af3762720a0",
                "fee_hi": 0,
                "fee_low": 0,
                "inner_codetype": 2,
                "code_data": "0x08021a143c1ea4aa4974d92e0eabd5d024772af3762720a0220401000000520a5465737420746f6b656e5a04474f44316012",
                "timestamp": 1597286024046
            },
            "signature": "0xbe2f1ac3db2fa6baeb5013490e3c9d5230700a6699d3e7442dc0dcf36b66b5c72cae8251aeda966aa251690ca65bd98521be21e4e2970cf3c301739d2bc5cb3d816077fa4cf3f2591d5013c130c00a1b7ba56bcfb43a131d2fc6d2b614f88640ea008536a59998374045f699ad76ff824db59dd80dbb5c56218e1accfc48a20d00cfd34f215f0d81e9a85f109e57e83fc16d088a",
            "status": {
                "status": "0x01",
                "result": "0x9dc36094489a625660dee6ebf11f51df0121ff6b",
                "hash": "0x0a64fe026622858ddff8da06eea84ac1e4d5d678a9929f5156dce99e7fb8e29f",
                "height": 225489,
                "timestamp": 1597286026248
            },
            "node": {
                "nid": "V1wSx0DmriORxwznqQhmHWtJavp18",
                "address": "0x5d9cdda85093d68c28573ae9875eb32dbad6f0e0"
            },
            "accepttimestamp": 1597286024522
        }
    }
    
### CWV转账
#### 接口说明

``` java
    /**
     * 转账CWV
     * @param fromAddr 地址
     * @param nonce 交易序号
     * @param fromPriKey 私钥
     * @param exData 扩展信息
     * @param outs 接收者集合{toAddr:接收地址,amount:十进制金额,建议乘以1e18避免浮点数 }
     * @return
     */
    public static String transferTo(String fromAddr, int nonce, String fromPriKey, String exData,List<TransferInfo> outs){
```
#### 用例

``` java
        String address="69a0e3390ded433f3b1b292606ac2a782e6336de";
        String priKey="1fcbcea384c0e19014b8aecf4beda883fb2e9f2fd3013ed55bc4d661ef50673b";
        //获取nonce
        int nonce = NonceKeeper.getNonce(address);
        //增加接收者信息
        List<TransferInfo> transactionOutputList = new ArrayList<TransferInfo>(){{
            this.add(new TransferInfo("0e71f683a3e50782230ffdf4b12b234aad1d5e24","10"));
        }};
        HiChain.transferTo(address,nonce, priKey,"",transactionOutputList);
```
#### 返值
|参数|类型|说明|示例|
|:----|:----|:----|:----|
|retCode|string|错误码 1成功 -1失败 ||
|retMsg|string|错误信息||
|hash|string|交易哈希||

    {
        "retCode": 1,
        "hash": "a4396b33f21a61f42b21b89d0c224142703b915658ecdc5ac6a3c848e96f0332d9"
    }

 > 查询转账结果：需要调用【查询交易信息】接口 
   
    {
        "retCode": 1,
        "transaction": {
            "hash": "0xcda565c54f721b52d3fc683f2547e4e86ffa29012897afd616fb58ef5520ddec7c",
            "body": {
                "nonce": 5,
                "address": "0x69a0e3390ded433f3b1b292606ac2a782e6336de",
                "outputs": [
                    {
                        "address": "0x0e71f683a3e50782230ffdf4b12b234aad1d5e24",
                        "amount": "0x0a"
                    }
                ],
                "fee_hi": 0,
                "fee_low": 0,
                "inner_codetype": 0,
                "timestamp": 1597378154994
            },
            "signature": "0x840ba214fa741ed001151649cbe87088333ccf7d3a9571b22a12a5e723d9d3abe493139efe3e9a3668088bb40c662ea9cc47984c0eb5881e55df06c3e18edbfb135a29387a6a86ad91c0fc7a9882ec5743e5b54f9dee37f0b66ebce8e3c51bea9ecd9ff6b060981856bbe8432730a02c772ac6f96b364ceb2c7ed8bd5a65c1ae56fffd6bf30c796ed3c4f9c7e1dc62cd5cc6a888",
            "status": {
                "status": "0x01",
                "hash": "0xf9afa13fed85961ea42b90a064f916bd19e35fa26d1947030245e6072f33eb3c",
                "height": 321121,
                "timestamp": 1597378157739
            },
            "node": {
                "nid": "V1wSx0DmriORxwznqQhmHWtJavp18",
                "address": "0x5d9cdda85093d68c28573ae9875eb32dbad6f0e0"
            },
            "accepttimestamp": 1597378156278
        }
    }
  > 当 transaction.status.status=0x01 时转账成功 转账金额：transaction.body.outputs[0].amount=0x0a ,16进制解码为10，其他情况 transaction.status.result 为错误信息，需要16进制解码查看内容
     
    
### 合约发布
#### 接口说明

``` java
    /**
     * 合约发布
     * @param fromAddr 账户地址
     * @param fromPriKey 私钥
     * @param codeData 合约编译后的二进制码
     * @param exData 扩展信息
     * @return
     */
    public static String contractCreate(String fromAddr,int nonce,  String fromPriKey, String codeData, String exData, ChainCallBack cbs, ICrypto... iCryptos) {
```
#### 用例
- 合约代码
``` java
    pragma solidity ^0.4.22;
    
    contract TestContract{
      address owner;
      uint256 public value = 0;
    
      constructor() public{
        owner = msg.sender;
      }
      function valueInc(uint256 inc) public return (uint256){
        value = value+inc;
        return value;
      }
    
      function getAddr() public view returns(address){
        return owner;
      }
    }
``` 
- 用例代码
``` java
    String address="69a0e3390ded433f3b1b292606ac2a782e6336de";
    String priKey="1fcbcea384c0e19014b8aecf4beda883fb2e9f2fd3013ed55bc4d661ef50673b";
    //获取nonce
    int nonce = NonceKeeper.getNonce(address);
    String codeData="6080604052600060015534801561001557600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550610191806100656000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680633fa4f2451461005c57806364a4635c14610087578063a74c2bb6146100c8575b600080fd5b34801561006857600080fd5b5061007161011f565b6040518082815260200191505060405180910390f35b34801561009357600080fd5b506100b260048036038101908080359060200190929190505050610125565b6040518082815260200191505060405180910390f35b3480156100d457600080fd5b506100dd61013c565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b60015481565b600081600154016001819055506001549050919050565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050905600a165627a7a7230582079ff7660c8107f8062425b54b0499303df4e97aa6b0c7729adee57d1eff986470029";
    String result = HiChain.contractCreate(address,nonce,priKey,codeData,"发布合约测试",null,LocalCrypto.getInstance());
```
#### 返值
|参数|类型|说明|示例|
|:----|:----|:----|:----|
|retCode|string|错误码 1成功 -1失败 ||
|retMsg|string|错误信息||
|hash|string|交易哈希||

    {
        "retCode": 1,
        "hash": "8f5a949ee060e48a39637d4d6784844eba39fa5bf62c10a80688650e7055b405d9"
    }
    
 > 查询合约地址：需要调用【查询交易信息】接口 
 
    {
        "retCode": 1,
        "transaction": {
            "hash": "0x8f5a949ee060e48a39637d4d6784844eba39fa5bf62c10a80688650e7055b405d9",
            "body": {
                "nonce": 1,
                "address": "0x69a0e3390ded433f3b1b292606ac2a782e6336de",
                "fee_hi": 0,
                "fee_low": 0,
                "inner_codetype": 4,
                "code_data": "0x6080604052600060015534801561001557600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550610191806100656000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680633fa4f2451461005c57806364a4635c14610087578063a74c2bb6146100c8575b600080fd5b34801561006857600080fd5b5061007161011f565b6040518082815260200191505060405180910390f35b34801561009357600080fd5b506100b260048036038101908080359060200190929190505050610125565b6040518082815260200191505060405180910390f35b3480156100d457600080fd5b506100dd61013c565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b60015481565b600081600154016001819055506001549050919050565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050905600a165627a7a7230582079ff7660c8107f8062425b54b0499303df4e97aa6b0c7729adee57d1eff986470029",
                "timestamp": 1597371512480
            },
            "signature": "0x840ba214fa741ed001151649cbe87088333ccf7d3a9571b22a12a5e723d9d3abe493139efe3e9a3668088bb40c662ea9cc47984c0eb5881e55df06c3e18edbfb09a16942364cf211af3cfb047d424e453dc0abdbe45666ca078b50393ab561f82173e61aac5e4480208d616a13f5023bbfc551f033d74827a1c55fc263575e79c3374c7afb3220d67a26de17ac839fa6f16ec137",
            "status": {
                "status": "0x01",
                "result": "0x58406ae0885bcfd4828c9414e6e17ce910d649a1",
                "hash": "0xd5a5f1f1b545bd6ef7ada2926c395cc3e86bb2085cd520892b6f98b4bfc76df5",
                "height": 314237,
                "timestamp": 1597371514802
            },
            "node": {
                "nid": "V1wSx0DmriORxwznqQhmHWtJavp18",
                "address": "0x5d9cdda85093d68c28573ae9875eb32dbad6f0e0"
            },
            "accepttimestamp": 1597371513662
        }
    }
 > 当 transaction.status.status=0x01 时合约地址为 transaction.status.result=0x58406ae0885bcfd4828c9414e6e17ce910d649a1 ，其他情况 transaction.status.result 为错误信息，需要16进制解码查看内容
 
     
### 合约调用
#### 接口说明

``` java
    /**
     * 执行合约
     * @param fromAddr 账户
     * @param fromPriKey 私钥
     * @param contractAddress 合约地址
     * @param codeData 合约方法及参数编译后的二进制码
     * @param exData 扩展信息
     * @param cbs
     * @param iCryptos
     * @return
     */
    public static String contractCall(String fromAddr,int nonce, String fromPriKey, String contractAddress, String codeData, String exData, ChainCallBack cbs, ICrypto... iCryptos) {
```
#### 用例
- 执行合约方法及参数

    valueInc(20);
    
- 编译后代码

    64a4635c0000000000000000000000000000000000000000000000000000000000000014
    
- 用例代码
``` java
    String address="69a0e3390ded433f3b1b292606ac2a782e6336de";
    String priKey="1fcbcea384c0e19014b8aecf4beda883fb2e9f2fd3013ed55bc4d661ef50673b";
    //获取nonce
    int nonce = NonceKeeper.getNonce(address);
    String contractAddr = "0x50d71437d4fed2ce55beff935e1c67af17885c91";
    String codeData = "64a4635c0000000000000000000000000000000000000000000000000000000000000014";
    String result = HiChain.contractCall(address,nonce,priKey,contractAddr,codeData,"执行合约测试",null,LocalCrypto.getInstance());
```
#### 返值
|参数|类型|说明|示例|
|:----|:----|:----|:----|
|retCode|string|错误码 1成功 -1失败 ||
|retMsg|string|错误信息||
|hash|string|交易哈希||

    {
        "retCode": 1,
        "hash": "3f9cc4cfe5393a079b7b3075b36d4967545407d950afeb28f6b09b5fcafa7fea0c"
    }
    
 > 查询合约执行结果：需要调用【查询交易信息】接口 
 
    {
        "retCode": 1,
        "transaction": {
            "hash": "0x3f9cc4cfe5393a079b7b3075b36d4967545407d950afeb28f6b09b5fcafa7fea0c",
            "body": {
                "nonce": 2,
                "address": "0x69a0e3390ded433f3b1b292606ac2a782e6336de",
                "outputs": [
                    {
                        "address": "0x58406ae0885bcfd4828c9414e6e17ce910d649a1"
                    }
                ],
                "fee_hi": 0,
                "fee_low": 0,
                "inner_codetype": 4,
                "code_data": "0x64a4635c0000000000000000000000000000000000000000000000000000000000000014",
                "timestamp": 1597372926660
            },
            "signature": "0x840ba214fa741ed001151649cbe87088333ccf7d3a9571b22a12a5e723d9d3abe493139efe3e9a3668088bb40c662ea9cc47984c0eb5881e55df06c3e18edbfbdb87446aca047e575f9ee34c7bd4c176e1cb16d6e670d200dce8147200c474888a82e83d7f486ef0b9280c66b40a7154e886ece8462b61f514832c3cddee745862094d351ea9b20257e435582ebbe0b9d3a25634",
            "status": {
                "status": "0x01",
                "result": "0x0000000000000000000000000000000000000000000000000000000000000014",
                "hash": "0xc251e3f835ce178e215d3ea30846cc7ca726584930cc3bc7861ad7b27d4ec673",
                "height": 315703,
                "timestamp": 1597372929440
            },
            "node": {
                "nid": "V1wSx0DmriORxwznqQhmHWtJavp18",
                "address": "0x5d9cdda85093d68c28573ae9875eb32dbad6f0e0"
            },
            "accepttimestamp": 1597372927928
        }
    }
 > 当transaction.status.status=0x01时执行结果为 transaction.status.result=0x0000000000000000000000000000000000000000000000000000000000000014 解码后也就是20，其他情况 transaction.status.result为错误信息，需要16进制解码查看内容
 
### RC20发行
#### 接口说明
 
``` java
/**
 *  发行RC20
 * @param fromAddr 账户
 * @param fromPriKey 私钥
 * @param symbol RC20标志
 * @param name RC20名称
 * @param tos RC20接收账户信息 {toAddr:接收地址,amount:十六进制金额,建议乘以1e18避免浮点数 }
 * @param exData 扩展信息
 * @param cbs
 * @param iCryptos
 * @return
 */
public static String rC20Create(String fromAddr,int nonce, String fromPriKey, String symbol, String name, List<TransferInfo> tos, String exData, ChainCallBack cbs, ICrypto... iCryptos) {
```
#### 用例
``` java
String address="69a0e3390ded433f3b1b292606ac2a782e6336de";
String priKey="1fcbcea384c0e19014b8aecf4beda883fb2e9f2fd3013ed55bc4d661ef50673b";
//获取nonce
int nonce = NonceKeeper.getNonce(address);
List<TransferInfo> tos = new ArrayList<TransferInfo>(){{
    this.add(new TransferInfo("0x3c1ea4aa4974d92e0eabd5d024772af3762720a0","0x1000000"));
}};
String result = HiChain.rC20Create(address,nonce,priKey,"GOD","Test token",tos,"发布RC20合约测试",null,LocalCrypto.getInstance());
```
#### 返值
|参数|类型|说明|示例|
|:----|:----|:----|:----|
|retCode|string|错误码 1成功 -1失败 ||
|retMsg|string|错误信息||
|hash|string|交易哈希||

 {
     "retCode": 1,
     "hash": "25ec2a285b10d2725f2fc936299d9493a9fb2a9b18bf7eb48f8c7a53817a5f50d3"
 }
 
> 查询发布RC20结果：需要调用【查询交易信息】接口 

{
    "retCode": 1,
    "transaction": {
        "hash": "0x25ec2a285b10d2725f2fc936299d9493a9fb2a9b18bf7eb48f8c7a53817a5f50d3",
        "body": {
            "nonce": 6,
            "address": "0x69a0e3390ded433f3b1b292606ac2a782e6336de",
            "fee_hi": 0,
            "fee_low": 0,
            "inner_codetype": 2,
            "code_data": "0x08021a143c1ea4aa4974d92e0eabd5d024772af3762720a0220110520a5465737420746f6b656e5a03474f446012",
            "timestamp": 1597387054474
        },
        "signature": "0x840ba214fa741ed001151649cbe87088333ccf7d3a9571b22a12a5e723d9d3abe493139efe3e9a3668088bb40c662ea9cc47984c0eb5881e55df06c3e18edbfba435ffd5f74399e1669349cab043b133a26d5e78283d8ca6bc3d55a647635a48a1a9dd727d14c484254ba76d183025b05c2b9b74b2bf262bd4e17acf2108b88eca7b3b062a7285f4d6b0b0a7ff22845b8af3892e",
        "status": {
            "status": "0x01",
            "result": "0xcdb2508f3bfee75c37258b97b6d56a57d9d8d86a",
            "height": 330325,
            "timestamp": 1597387057714
        },
        "node": {
            "nid": "V1wSx0DmriORxwznqQhmHWtJavp18",
            "address": "0x5d9cdda85093d68c28573ae9875eb32dbad6f0e0"
        },
        "accepttimestamp": 1597387055760
    }
}
> 当transaction.status.status=0x01 成功,名称为GOD的RC20地址为 transaction.status.result=0xcdb2508f3bfee75c37258b97b6d56a57d9d8d86a 解码后也就是20，其他情况 transaction.status.result为错误信息，需要16进制解码查看内容

### 查询RC20信息
#### 接口说明

``` java
/**
 * 查询RC20信息
 * @param rC20Address RC20合约地址
 * @param cbs
 * @return
 */
public static String getRC20Info(String rC20Address,  ChainCallBack... cbs){
```
#### 用例
``` java
String rC20Address = "0x9dc36094489a625660dee6ebf11f51df0121ff6b";
String result = HiChain.getRC20Info(rC20Address);
```
#### 返值
|参数|类型|说明|示例|
|:----|:----|:----|:----|
|retCode|string|错误码 1成功 -1失败 ||
|retMsg|string|错误信息||
|token_address|string|RC20地址||
|info|object|RC20信息对象||
|creator|string|创建者||
|create_time|string|创建时间||
|totalSupply|string|当前发行总量||
|name|string|RC20 名称||
|symbol|string|RC20 标志||
|decimals|int|精度 避免浮点数||
|printable|bool|可增发||
|token_nonce|int|交易次数||

    {
        "token_address": "0xcdb2508f3bfee75c37258b97b6d56a57d9d8d86a",
        "info": {
            "creator": "0x69a0e3390ded433f3b1b292606ac2a782e6336de",
            "create_time": 1597387057714,
            "totalSupply": "0x10",
            "name": "Test token",
            "symbol": "GOD",
            "decimals": 18,
            "printable": true
        },
        "token_nonce": 0,
        "ret_code": 1
    }
### 查询账户RC20信息
#### 接口说明

``` java
    /**
     * 查询账户RC20信息
     * @param ownerAddress 账户
     * @param rC20Address RC20合约地址
     * @param cbs
     * @return
     */
    public static String getUserRC20Info(String ownerAddress,String rC20Address,  ChainCallBack... cbs){
```
#### 用例
``` java
String rC20Address = "0x082ad3805e1a374f8c91260b5fe3c3a72309f6c0";
String ownerAddress = "0x69a0e3390ded433f3b1b292606ac2a782e6336de";
String result = HiChain.getUserRC20Info(ownerAddress,rC20Address);
```
#### 返值
|参数|类型|说明|示例|
|:----|:----|:----|:----|
|retCode|string|错误码 1成功 -1失败 ||
|retMsg|string|错误信息||
|token_address|string|RC20地址||
|value|object|ownerAddress信息 当前地址没有转入过当前RC20，本字段为空||
|balance|string|ownerAddress余额||
|totalSupply|string|当前发行总量||
|name|string|RC20 名称||
|symbol|string|RC20 标志||
|decimals|int|精度 避免浮点数||

    {
        "token_address": "0x082ad3805e1a374f8c91260b5fe3c3a72309f6c0",
        "value": {
            "balance": "0x100000"
        },
        "totalSupply": "0x100000",
        "name": "Test token",
        "symbol": "GOD1",
        "decimals": 18,
        "ret_code": 1
    }
> value.balance 为当前RC20用户 ownerAddress=0x69a0e3390ded433f3b1b292606ac2a782e6336de 的余额
    
### RC20转账
#### 接口说明

``` java
    /**
     * 转账RC20
     * @param fromAddr 账户
     * @param fromPriKey 私钥
     * @param rC20Address RC20地址
     * @param tos RC20接收账户信息 {toAddr:接收地址,amount:十六进制金额,建议乘以1e18避免浮点数 }
     * @param exData 扩展信息
     * @param cbs
     * @param iCryptos
     * @return
     */
    public static String rC20Transfer(String fromAddr,int nonce, String fromPriKey, String rC20Address, List<TransferInfo> tos,String exData, ChainCallBack cbs, ICrypto... iCryptos) {
```
#### 用例
``` java
 String address="69a0e3390ded433f3b1b292606ac2a782e6336de";
 String priKey="1fcbcea384c0e19014b8aecf4beda883fb2e9f2fd3013ed55bc4d661ef50673b";
 //获取nonce
 int nonce = NonceKeeper.getNonce(address);
 List<TransferInfo> tos = new ArrayList<TransferInfo>(){{
     this.add(new TransferInfo("0x69a0e3390ded433f3b1b292606ac2a782e6336de","0x100000"));
 }};
 String rC20Address = "0x082ad3805e1a374f8c91260b5fe3c3a72309f6c0";
 String result = HiChain.rC20Transfer(address,nonce,priKey,rC20Address,tos,"转账RC20合约测试",null,LocalCrypto.getInstance());
```
#### 返值
|参数|类型|说明|示例|
|:----|:----|:----|:----|
|retCode|string|错误码 1成功 -1失败 ||
|retMsg|string|错误信息||
|hash|string|交易哈希||

    {
      "retCode": 1,
      "hash": "6a63347f949af8b5afbbad7c3d6578c215e0e6993241d315ffd1f3ccde2abd7d2f"
    }
  
> 查询发布RC20结果：需要调用【查询交易信息】接口 

    {
        "retCode": 1,
        "transaction": {
            "hash": "0x6a63347f949af8b5afbbad7c3d6578c215e0e6993241d315ffd1f3ccde2abd7d2f",
            "body": {
                "nonce": 8,
                "address": "0x69a0e3390ded433f3b1b292606ac2a782e6336de",
                "outputs": [
                    {
                        "address": "0x082ad3805e1a374f8c91260b5fe3c3a72309f6c0"
                    }
                ],
                "fee_hi": 0,
                "fee_low": 0,
                "inner_codetype": 2,
                "code_data": "0x08031a1469a0e3390ded433f3b1b292606ac2a782e6336de22031000006012",
                "timestamp": 1597393749097
            },
            "signature": "0x840ba214fa741ed001151649cbe87088333ccf7d3a9571b22a12a5e723d9d3abe493139efe3e9a3668088bb40c662ea9cc47984c0eb5881e55df06c3e18edbfbb05c5d5750f578ff7fa51bfc216e3b1805de5e338b08e026504fb45a0d80bcf8b2910573b7056d5548430c7e0c5bab278e50400eb941286d14165b7faa4d0632929ee737ee060107e379fd09133b6c610fb81103",
            "status": {
                "status": "0x01",
                "hash": "0x7ae0e3ebbf5cb014ce48db7887bb5ac5759c594f0d0d091f9cbb3898673e0ecd",
                "height": 337251,
                "timestamp": 1597393751870
            },
            "node": {
                "nid": "V1wSx0DmriORxwznqQhmHWtJavp18",
                "address": "0x5d9cdda85093d68c28573ae9875eb32dbad6f0e0"
            },
            "accepttimestamp": 1597393750399
        }
    }
    
> 当transaction.status.status=0x01 成功，其他情况 transaction.status.result为错误信息，需要16进制解码查看内容

 