# cwvj - Java CWV2.0 Ðapp Api
This is the Java API which allows you to work with the CWV2.0 blockchain, without the additional overhead of having to write your own integration code for the platform.
## Getting started
Typically your application should depend on release versions of jsdk, Add the relevant dependency to your project:

#### Maven
```  java
<dependency>
  <groupId>io.github.cryptoworldchain</groupId>
  <artifactId>cwvj</artifactId>
  <version>2.1.3</version>
</dependency>
```
#### Gradle
```  java
compile ('io.github.cryptoworldchain:cwvj:2.1.3')
```
## Config
```  java
//the host of cwv2.0 blockchain node ，default is http://c0:8000
Config.host = "http://host:port";
```

Here is the description of public parameters of blockchain:

- `address ` the account of blockchain, and it start with "CVN" which is just an identifier of the new version blockchain, when you interact with the blockchain you can remove "CVN" by the AccountUtils.filterCVN function, however,you must remove "CVN" before you send the transaction .
- `privateKey` the private key for address, which is used to signing the transaction ,and the signature should be verified by the The public key corresponding to the private key.
- `nonce`  the nonce is used to distinguish transactions, like the transaction serial number. Once a tansaction finished, the account will automatically increase the nonce value; Usually, we recommend that the nonce value is managed locally, and the management class NonceKeeper is also provided.The process is as follows:
  - the process of using nonce：
    - Get the nonce value . 
	- Sign the transaction of current nonce and send to the blockchain.
	- Transaction finished and the result is also successful ,the nonce will increase by 1.
  - the management of local nonce：
    - Transaction finished and the result is successful：nonce increase by 1(NonceKeeper.incrNonce()).
    - Transaction send error:if nonce problem(parameter invalid, sender nonce is large than transaction nonce) ：
		refresh local nonce by query the blockchain(NonceKeeper.refreshNonce(addr)), and then you can try again.
    - Transaction send error: if business error：you can check your parameters and send the same nonce transaction.
    - Other Exception (network timeout)：if no responce from the blockchain, you need confirm current transaction is success or not , Then decide the next step.
 
- `exdata` It is the extension field, It will be displayed in hexadecimal in the transaction information. 


### Function category

#### WalletUtils
* [getMnemonic](#WalletUtils.getMnemonic)
* [getKeyPair](#WalletUtils.getKeyPair)
* [genKeyStoreFromMnemonic](#WalletUtils.genKeyStoreFromMnemonic)
* [restoreFromKeyStore](#WalletUtils.restoreFromKeyStore)

#### NonceKeeper
* [getNonce](#NonceKeeper.getNonce)
* [incrNonce](#NonceKeeper.incrNonce)
* [refreshNonce](#NonceKeeper.refreshNonce)

#### HiChain（Chain API）
* [getUserInfo](#HiChain.getUserInfo)
* [transferTo](#HiChain.transferTo)
* [getTxInfo](#HiChain.getTxInfo)
* [rC20Create](#HiChain.rC20Create)
* [getRC20Info](#HiChain.getRC20Info)
* [getUserRC20Info](#HiChain.getRC20UserInfo)
* [rC20Transfer](#HiChain.rC20Transfer)
* [contractCreate](#HiChain.ontractCreate)
* [contractCall](#HiChain.contractCall)

#### ContractUtils
* [getFunctionBinCode](#ContractUtils.getFunctionBinCode)
* [getFunctionParamsCode](#ContractUtils.getFunctionParamsCode)

#### * ContractUtils for CWVSwap
* [getContractBinCodeMSwap](#getContractBinCodeMSwap)
* [getFunctionWithMSwap](#getFunctionWithMSwap)
* [getFunctionBinCodeWithMSwap](#getFunctionBinCodeWithMSwap)


### WalletUtils.getMnemonic

#### description
``` java
    /**
     * To generate Mnemonic Words which is to generate keyPair with function [WalletUtils.getKeyPair](#WalletUtils.getKeyPair) 
     * @return e.g.: "gas twist liar foster crunch arrow brush market author knee fit frown"
     */
    public static String getMnemonic(){
```

#### Case

``` java
    String words= WalletUtil.getMnemonic();
```

#### Return
``` java
    "gas twist liar foster crunch arrow brush market author knee fit frown"
```
### WalletUtils.getKeyPair

#### description
``` java
	/**
	 * To generate KeyPair which consists of address,privateKey,publicKey.
	 * @param mnemonic
	 * @return
	 */
	public static KeyPairs getKeyPair(String mnemonic){
```

#### Case

``` java
    String words = "gas twist liar foster crunch arrow brush market author knee fit frown";
    KeyPairs kp = WalletUtil.getKeyPair(words); 
```

#### Return
``` java
public class KeyPairs {
	String pubkey;
	String prikey;
	String address;
	String bcuid;
}
```
### WalletUtils.genKeyStoreFromMnemonic

#### description
```  java
	/**
	 * To generate KeyStore File Content ,with Mnemonic Words, which should be encrypted and written into the file :
	 * @param mnemonic
	 * @param password
	 * @return
	 */
	public static String genKeyStoreFromMnemonic(String mnemonic,String password){
```
#### Case
```  java
String words = "gas twist liar foster crunch arrow brush market author knee fit frown"; 
String ksJson = WalletUtil.genKeyStoreFromMnemonic(words,"password");

```
#### Return
```  java
"{\"ksType\":\"aes\",\"params\":{\"dklen\":256,\"c\":128,\"l\":269,\"salt\":\"27fed0c293b7a94f\"},\"pwd\":\"b68fe43f0d1a0d7aef123722670be50268e15365401c442f8806ef83b612976b\",\"cipher\":\"cbc\",\"cipherParams\":{\"iv\":\"1740afac7276ea76c23eee63f548ed43\"},\"cipherText\":\"3d80d77d9c9ddac1c44da6d8d78e6334100a60591d5338ae07ecf536b7e759692a1ac8d433ed9f0c528ac5fd9c25c38d0fb224069d835786972f6d475252f438624206b42b69b17d8d3a96ffec144ca6476520946953fc3e1c7533d0301609c72f12fd409c0d275ea8976077030212752596c26d207fba9a840aa12f9e89351be3b48d7e17ecb3eed3a8ea329b28212cf509c6b70a6f24528dc819d44b1852046382b8182804bdeb76005bf1bf8fe663fbeedfcfb93413b7bd1f103623f15ee573fade7c4dd12fc1122d41052b97598b71f66026461dbd07d66217286848eae5215d7679e9bdfd84951ca38c401af94a5ac49b51719a5dabaa3e87546e3c2d07cbd67f17afc2d4cc9497193d45aa5e74\"}"
```
### WalletUtils.restoreFromKeyStore

#### description
```  java
	/**
	 * To restore Mnemonic Words from KeyStore content with correct password.
	 * @param ksJson
	 * @param pwd
	 * @return
	 */
	public static KeyPairs restoreFromKeyStore(String ksJson,String pwd){
```  
#### Case 
```  java
	String ksJson = "{\"ksType\":\"aes\",\"params\":{\"dklen\":256,\"c\":128,\"l\":269,\"salt\":\"27fed0c293b7a94f\"},\"pwd\":\"b68fe43f0d1a0d7aef123722670be50268e15365401c442f8806ef83b612976b\",\"cipher\":\"cbc\",\"cipherParams\":{\"iv\":\"1740afac7276ea76c23eee63f548ed43\"},\"cipherText\":\"3d80d77d9c9ddac1c44da6d8d78e6334100a60591d5338ae07ecf536b7e759692a1ac8d433ed9f0c528ac5fd9c25c38d0fb224069d835786972f6d475252f438624206b42b69b17d8d3a96ffec144ca6476520946953fc3e1c7533d0301609c72f12fd409c0d275ea8976077030212752596c26d207fba9a840aa12f9e89351be3b48d7e17ecb3eed3a8ea329b28212cf509c6b70a6f24528dc819d44b1852046382b8182804bdeb76005bf1bf8fe663fbeedfcfb93413b7bd1f103623f15ee573fade7c4dd12fc1122d41052b97598b71f66026461dbd07d66217286848eae5215d7679e9bdfd84951ca38c401af94a5ac49b51719a5dabaa3e87546e3c2d07cbd67f17afc2d4cc9497193d45aa5e74\"}"
	KeyPairs kp2 = WalletUtil.restoreFromKeyStore(ksJson,"password");
        
```
#### Return
``` java
	public class KeyPairs {
		String pubkey;
		String prikey;
		String address;
		String bcuid;
	}
```


### NonceKeeper.getNonce

#### description
``` java
    /**
     * get the nonce of current address,which must be get before transaction.
     * @param addr
     * @return
     */
    public static int getNonce(String addr){
```

#### Case
``` java
int nonce = NonceKeeper.getNonce("69a0e3390ded433f3b1b292606ac2a782e6336de");
```

#### Return
``` java
6
```

### NonceKeeper.incrNonce

#### description
``` java
    /**
     * increase the nonce value of the address
     * @param addr
     */
    public static void incrNonce(String addr){
```

#### Case
``` java
NonceKeeper.incrNonce("69a0e3390ded433f3b1b292606ac2a782e6336de");
```

### NonceKeeper.refreshNonce

#### description
``` java
	/**
	 * refresh local nonce of the address from the blockchain
	 * @param address
	 * @return 
	 */
	public static void refreshNonce(String address){
```

#### Case
``` java
int nonce = NonceKeeper.refreshNonce("69a0e3390ded433f3b1b292606ac2a782e6336de");
```

#### Return
``` java
6
```

### HiChain.getUserInfo

#### description
``` java
	/**
	 * To get account info from an address:
	 * @param address
	 * @return
	 */
	public static String getUserInfo(String address){
```

#### Case

``` java
String result = HiChain.getUserInfo("69a0e3390ded433f3b1b292606ac2a782e6336de");
```

#### Return
|param|type|comment|case|
|:----|:----|:----|:----|
|retCode|string|code: 1)success -1)failed ||
|retMsg|string|message of code||
|address|string|account||
|nonce|int|transaction code||
|status|int|status of address: 0)valid -1) lock||
|storage_trie_root|string|the root hash value of storage owned by the address ||

e.g no record on the block chain :
``` shell
{
    "retCode": -1,
    "retMsg": "账户不存在",
    "nonce": 0,
    "status": 0
}
```
e.g exist record on the block chain :
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
 
### transferTo

#### description
``` java
    /**
     * send to address with CWV
     * @param fromAddr account
     * @param nonce transaction code
     * @param fromPriKey private key
     * @param exData extension data
     * @param outs recievers info {toAddr: recieve account,amount:decimal amount.To avoid floating point numbers,it is recommended to multiply by 10 is the eighteen power  }
     * @return
     */
    public static String transferTo(String fromAddr, int nonce, String fromPriKey, String exData,List<TransferInfo> outs){
```
#### Case

``` java
	String address="69a0e3390ded433f3b1b292606ac2a782e6336de";
	String priKey="1fcbcea384c0e19014b8aecf4beda883fb2e9f2fd3013ed55bc4d661ef50673b";
	//get the nonce
	int nonce = NonceKeeper.getNonce(address);
	//add reciever
	List<TransferInfo> transactionOutputList = new ArrayList<TransferInfo>(){{
		this.add(new TransferInfo("0e71f683a3e50782230ffdf4b12b234aad1d5e24","10"));
	}};
	TransactionImpl.TxResult result = HiChain.transferTo(address,nonce, priKey,"",transactionOutputList);
```
#### Return
|param|type|comment|case|
|:----|:----|:----|:----|
|retCode|string|code: 1)success -1)failed ||
|retMsg|string|message of code||
|hash|string|the hash of transaction||

    {
        "retCode": 1,
        "hash": "a4396b33f21a61f42b21b89d0c224142703b915658ecdc5ac6a3c848e96f0332d9"
    }

 > get the transaction result：by calling [Hichain.getTxInfo](#Hichain.getTxInfo)  
   
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
 
 

### HiChain.getTxInfo

#### description
``` java   
	/**
	 * To get transaction info by transaction hash
	 * @param txHash 
	 * @return
	 */
	private static TransactionImpl.TxResult getTxInfo(String txHash) {
```   
#### Case
``` java   
TransactionImpl.TxResult result = HiChain.getTxInfo("d1820b55b4a10c1f3ec3a780f021dbca4d9bb21f42a745b34b0bc3cbbcb6d0bacf");
```
#### Return
|param|type|comment|case|
|:----|:----|:----|:----|
|retCode|string|code: 1)success -1)failed ||
|retMsg|string|message of code||
|transaction|object| transaction object||
|hash|string|transaction hash||
|body|object|the transaction body object||
|nonce|int|transaction code||
|address|string|account||
|fee_hi|string|high fee||
|fee_low|string|low fee||
|inner_codetype|int|transaction type<br\>0)default<br\>1)Multi-signature<br\>2)RC20<br\>3)RC721<br\>4)cvm_contract<br\>5)JSVM_contract||
|code_data|string|code to execute||
|timestamp|long|timestamp of transaction creating||
|signature|string|Signature of address to transaction body||
|status|object|transaction status object||
|status|string|result status 0x01)success 0xff) failed||
|result|string|transaction result||
|hash|string|the hash of block||
|height|int|the height of block ||
|timestamp|int|timestamp of transaction confirming||
|node|object|the node which recieving the transaction||
|nid|string|node id||
|address|string|node address||
|accepttimestamp|int|timestamp of transaction recieving||

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
	   
### HiChain.rC20Create

#### description
``` java
	/**
	 *  publish RC20 token
	 * @param fromAddr 
	 * @param fromPriKey private key
	 * @param symbol RC20 symbol
	 * @param name RC20 name
	 * @param tos recievers info {toAddr: recieve account,amount:Hexadecimal amount.To avoid floating point numbers,it is recommended to multiply by 10 is the eighteen power  }
	 * @param exData extention data
	 * @return
	 */
	public static String rC20Create(String fromAddr,int nonce, String fromPriKey, String symbol, String name, List<TransferInfo> tos, String exData, ChainCallBack cbs, ICrypto... iCryptos) {
	```
	
#### Case
``` java
	String address="69a0e3390ded433f3b1b292606ac2a782e6336de";
	String priKey="1fcbcea384c0e19014b8aecf4beda883fb2e9f2fd3013ed55bc4d661ef50673b";
	//get nonce
	int nonce = NonceKeeper.getNonce(address);
	List<TransferInfo> tos = new ArrayList<TransferInfo>(){{
		this.add(new TransferInfo("0x3c1ea4aa4974d92e0eabd5d024772af3762720a0","0x1000000"));
	}};
	TransactionImpl.TxResult result = HiChain.rC20Create(address,nonce,priKey,"GOD","Test token",tos,"publish RC20  token test",null,LocalCrypto.getInstance());
```
#### Return
|param|type|comment|case|
|:----|:----|:----|:----|
|retCode|string|code: 1)success -1)failed ||
|retMsg|string|message of code||
|hash|string|the hash value of transaction ||

 {
     "retCode": 1,
     "hash": "25ec2a285b10d2725f2fc936299d9493a9fb2a9b18bf7eb48f8c7a53817a5f50d3"
 }
 
> get the transaction result：by calling [Hichain.getTxInfo](#Hichain.getTxInfo)  

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

### HiChain.getRC20Info

#### description
``` java
	/**
	 * get the RC20 info 
	 * @param rC20Address RC20合约account
	 * @param cbs
	 * @return
	 */
	public static String getRC20Info(String rC20Address,  ChainCallBack... cbs){
```

#### Case
``` java
	String rC20Address = "0x9dc36094489a625660dee6ebf11f51df0121ff6b";
	String result = HiChain.getRC20Info(rC20Address);
```
#### Return
|param|type|comment|case|
|:----|:----|:----|:----|
|retCode|string|code: 1)success -1)failed ||
|retMsg|string|message of code||
|token_address|string|RC20account||
|info|object|RC20 info object||
|creator|string|the creator address of RC20||
|create_time|string|the time of creating RC20||
|totalSupply|string|total amount of RC20||
|name|string|RC20 name||
|symbol|string|RC20 symbol||
|decimals|int|decimals to avoid floating point numbers||
|printable|bool|is it possible to increase||
|token_nonce|int|transaction code||

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
	
### HiChain.getUserRC20Info

#### description
``` java
    /**
     * get the RC20 info of the address
     * @param ownerAddress 
     * @param rC20Address 
     * @param cbs
     * @return
     */
    public static String getUserRC20Info(String ownerAddress,String rC20Address,  ChainCallBack... cbs){
```

#### Case
``` java
	String rC20Address = "0x082ad3805e1a374f8c91260b5fe3c3a72309f6c0";
	String ownerAddress = "0x69a0e3390ded433f3b1b292606ac2a782e6336de";
	String result = HiChain.getUserRC20Info(ownerAddress,rC20Address);
```
#### Return
|param|type|comment|case|
|:----|:----|:----|:----|
|retCode|string|code: 1)success -1)failed ||
|retMsg|string|message of code||
|token_address|string|RC20account||
|value|object|ownerAddress info if curretn account never recieve RC20, the value of the field is empty||
|balance|string|ownerAddress balance||
|totalSupply|string|total amount of RC20||
|name|string|RC20 name||
|symbol|string|RC20 symbol||
|decimals|int|decimals to avoid floating point numbers||

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
> [value.balance] is the balance of [ownerAddress=0x69a0e3390ded433f3b1b292606ac2a782e6336de]
    
### HiChain.rC20Transfer

#### description
``` java
    /**
     * transfer the RC20 to the address
     * @param fromAddr 
     * @param fromPriKey private key
     * @param rC20Address RC20account
	 * @param tos recievers info {toAddr: recieve account,amount:Hexadecimal amount.To avoid floating point numbers,it is recommended to multiply by 10 is the eighteen power  }
	 * @param exData extention data
     * @param cbs
     * @param iCryptos
     * @return
     */
    public static String rC20Transfer(String fromAddr,int nonce, String fromPriKey, String rC20Address, List<TransferInfo> tos,String exData, ChainCallBack cbs, ICrypto... iCryptos) {
```

#### Case
``` java
	String address="69a0e3390ded433f3b1b292606ac2a782e6336de";
	String priKey="1fcbcea384c0e19014b8aecf4beda883fb2e9f2fd3013ed55bc4d661ef50673b";
	//get nonce
	int nonce = NonceKeeper.getNonce(address);
	List<TransferInfo> tos = new ArrayList<TransferInfo>(){{
	 this.add(new TransferInfo("0x69a0e3390ded433f3b1b292606ac2a782e6336de","0x100000"));
	}};
	String rC20Address = "0x082ad3805e1a374f8c91260b5fe3c3a72309f6c0";
	TransactionImpl.TxResult result = HiChain.rC20Transfer(address,nonce,priKey,rC20Address,tos,"转账RC20合约测试",null,LocalCrypto.getInstance());
```
#### Return
|param|type|comment|case|
|:----|:----|:----|:----|
|retCode|string|code: 1)success -1)failed ||
|retMsg|string|message of code||
|hash|string|the hash value of transaction ||

    {
      "retCode": 1,
      "hash": "6a63347f949af8b5afbbad7c3d6578c215e0e6993241d315ffd1f3ccde2abd7d2f"
    }
  
> get the transaction result：by calling [Hichain.getTxInfo](#Hichain.getTxInfo)  

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

    
### HiChain.contractCreate

#### description

``` java
    /**
     * 合约发布
     * @param fromAddr 账户account
     * @param fromPriKey private key
     * @param codeData 合约编译后的二进制码
     * @param exData extention data
     * @return
     */
    public static TransactionImpl.TxResult contractCreate(String fromAddr,int nonce,  String fromPriKey, String codeData, String exData, ChainCallBack cbs, ICrypto... iCryptos) {
```
#### Case
- Contract code 
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
- Case code
``` java
    String address="69a0e3390ded433f3b1b292606ac2a782e6336de";
    String priKey="1fcbcea384c0e19014b8aecf4beda883fb2e9f2fd3013ed55bc4d661ef50673b";
    //get nonce
    int nonce = NonceKeeper.getNonce(address);
    String codeData="6080604052600060015534801561001557600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550610191806100656000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680633fa4f2451461005c57806364a4635c14610087578063a74c2bb6146100c8575b600080fd5b34801561006857600080fd5b5061007161011f565b6040518082815260200191505060405180910390f35b34801561009357600080fd5b506100b260048036038101908080359060200190929190505050610125565b6040518082815260200191505060405180910390f35b3480156100d457600080fd5b506100dd61013c565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b60015481565b600081600154016001819055506001549050919050565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050905600a165627a7a7230582079ff7660c8107f8062425b54b0499303df4e97aa6b0c7729adee57d1eff986470029";
    TransactionImpl.TxResult result = HiChain.contractCreate(address,nonce,priKey,codeData,"contract create test",null,LocalCrypto.getInstance());
```
#### Return
|param|type|comment|case|
|:----|:----|:----|:----|
|retCode|string|code: 1)success -1)failed ||
|retMsg|string|message of code||
|hash|string|the hash value of transaction ||

    {
        "retCode": 1,
        "hash": "8f5a949ee060e48a39637d4d6784844eba39fa5bf62c10a80688650e7055b405d9"
    }
    
 > get the transaction result：by calling [Hichain.getTxInfo](#Hichain.getTxInfo)  
 
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

     
### HiChain.contractCall

#### description

``` java
    /**
     * call contract function
     * @param fromAddr 
     * @param fromPriKey private key
     * @param contractAddress 合约account
     * @param codeData the encode of contract function with or without params 
     * @param exData extention data
     * @return
     */
    public static TransactionImpl.TxResult contractCall(String fromAddr,int nonce, String fromPriKey, String contractAddress, String codeData, String exData) {
```
#### Case
- function and params
```
    valueInc(20);
```  
- compiled code
```
    64a4635c0000000000000000000000000000000000000000000000000000000000000014
```    
- Case code
``` java
    String address="69a0e3390ded433f3b1b292606ac2a782e6336de";
    String priKey="1fcbcea384c0e19014b8aecf4beda883fb2e9f2fd3013ed55bc4d661ef50673b";
    //get nonce
    int nonce = NonceKeeper.getNonce(address);
    String contractAddr = "0x50d71437d4fed2ce55beff935e1c67af17885c91";
    String codeData = "64a4635c0000000000000000000000000000000000000000000000000000000000000014";
    TransactionImpl.TxResult result = HiChain.contractCall(address,nonce,priKey,contractAddr,codeData,"contract execute test",null,LocalCrypto.getInstance());
```
#### Return
|param|type|comment|case|
|:----|:----|:----|:----|
|retCode|string|code: 1)success -1)failed ||
|retMsg|string|message of code||
|hash|string|the hash value of transaction ||

    {
        "retCode": 1,
        "hash": "3f9cc4cfe5393a079b7b3075b36d4967545407d950afeb28f6b09b5fcafa7fea0c"
    }
    
 > get the transaction result：by calling [Hichain.getTxInfo](#Hichain.getTxInfo)  
 
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

 
