package org.brewchain.sdk;

import com.brewchain.sdk.model.Block;
import com.brewchain.sdk.model.TransactionImpl;
import com.google.gson.Gson;
import com.googlecode.protobuf.format.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import com.brewchain.sdk.crypto.KeyPairs;
import org.brewchain.sdk.chain.NonceKeeper;
import org.brewchain.sdk.contract.abi.Function;
import org.brewchain.sdk.model.TransferInfo;
import org.brewchain.sdk.util.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Sample {
    public static void main(String[] args) {
        //主链节点地址：运行前请根据实际环境更换
        Config.init("http://52.220.97.222:8000",168);
        //1 WalletUtil
        //1.1 WalletUtil.getMnemonic
        String words= WalletUtil.getMnemonic();
        log.info("\r 1 WalletUtil.getMnemonic ==>\n"+words);
        //1.2 WalletUtil.getKeyPair
        //**地址前缀设置 默认为CVN ,设置其他KeyPairs.ADDR_PRE="其他"，取消前缀KeyPairs.ADDR_PRE=null
        KeyPairs kp = CryptoUtil.getRandomKP();
        log.info("\r 1 WalletUtil.getKeyPair ==>\n address:{} pubKey:{} priKey:{}",kp.getAddress(),kp.getPubkey(),kp.getPrikey());
        //1.3 WalletUtil.genKeyStoreFromMnemonic
        String ksJson = WalletUtil.genKeyStoreFromMnemonic(words,"password");
        log.info("\r 1 WalletUtil.genKeyStoreFromMnemonic keyStore content ==>\n"+ksJson);
        //1.4 WalletUtil.restoreFromKeyStore
        KeyPairs kp2 = WalletUtil.restoreFromKeyStore(ksJson,"password");
        log.info("\r 1 WalletUtil.restoreFromKeyStore  ==>\naddress:{} pubKey:{} priKey:{}",kp2.getAddress(),kp2.getPubkey(),kp2.getPrikey());

        String addressA = "0xec5c1b38f0aa23bad42de39e2b5bbfd847c3db76";
        String priKey = "0x7e0d3aabb9e3b0e54a6c3ae2d10f00e7b03b9f3356db26e71e71b4b2b8cb10b0";

        final String address = AccountUtil.cvnFiler(addressA);
        //2 NonceKeeper
        //2.1 NonceKeeper.getNonce
        int nonce = NonceKeeper.getNonce(address);
        log.info("\r 2 NonceKeeper.getNonce nonce={}", nonce);
        //2.2 NonceKeeper.incrNonce
        nonce = NonceKeeper.incrNonce(address);
        log.info("\r 2 NonceKeeper.incrNonce==>\nnonce={}", nonce);
        //2.3 NonceKeeper.incrNonce
        nonce = NonceKeeper.refreshNonce(address);
        log.info("\r 2 NonceKeeper.refreshNonce==>\nnonce={}", nonce);

        Block.BlockRet blockInfo = HiChain.getLastedBlock();
        log.info("blockInfo==>",blockInfo);
        //3 HiChain
        //3.1 HiChain.getUserInfo
        String userInfo = HiChain.getUserInfo(address);
        log.info("\r 3 HiChain.getUserInfo==>\n{}",userInfo);
        //3.2 HiChain.transferTo
        List<TransferInfo> transactionOutputList = new ArrayList<TransferInfo>(){{
            this.add(new TransferInfo("3c1ea4aa4974d92e0eabd5d024772af3762720a0","1000000000000000000"));
        }};
        TransactionImpl.TxResult resultTransfer = HiChain.transferTo(address,NonceKeeper.getNonce(address), priKey,"",transactionOutputList);
        log.info("\r 3 HiChain.transferTo return==>\n{}",resultTransfer);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //3.3 HiChain.getTxInfo for HiChain.transferTo
        TransactionImpl.TxResult txResultTransfer = HiChain.getTxInfo(resultTransfer.getHash());
        log.info("\r 3 HiChain.getTxInfo return==>\n{}",new Gson().toJson(txResultTransfer.getStatus()));

        //3.4 HiChain.contractCreate
        String binCode = "6080604052600060015534801561001557600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550610191806100656000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680633fa4f2451461005c57806364a4635c14610087578063a74c2bb6146100c8575b600080fd5b34801561006857600080fd5b5061007161011f565b6040518082815260200191505060405180910390f35b34801561009357600080fd5b506100b260048036038101908080359060200190929190505050610125565b6040518082815260200191505060405180910390f35b3480156100d457600080fd5b506100dd61013c565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b60015481565b600081600154016001819055506001549050919050565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050905600a165627a7a7230582079ff7660c8107f8062425b54b0499303df4e97aa6b0c7729adee57d1eff986470029";
        TransactionImpl.TxResult resultCreate = HiChain.contractCreate(address,NonceKeeper.getNonce(address),priKey,binCode,"test create contract");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //3.3 HiChain.getTxInfo for HiChain.contractCreate
        TransactionImpl.TxResult txResultCreate = HiChain.getTxInfo(resultCreate.getHash());
        log.info("\r 3 HiChain.getTxInfo return==>\n{}",new Gson().toJson(txResultCreate.getStatus()));
        //3.5 HiChain.contractCall
        String contractAddr = "";
        if(txResultCreate.getStatus()!=null && txResultCreate.getStatus().getStatus().equals("0x01")){
            contractAddr = txResultCreate.getStatus().getResult();
        }
        String abi = "[{\"constant\": true,\"inputs\": [],\"name\": \"value\",\"outputs\": [{\"name\": \"\",\"type\": \"uint256\"}],\"payable\": false,\"stateMutability\": \"view\",\"type\": \"function\"},{\"constant\": false,\"inputs\": [{\"name\": \"inc\",\"type\": \"uint256\"}],\"name\": \"valueInc\",\"outputs\": [{\"name\": \"\",\"type\": \"uint256\"}],\"payable\": false,\"stateMutability\": \"nonpayable\",\"type\": \"function\"},{\"constant\": true,\"inputs\": [],\"name\": \"getAddr\",\"outputs\": [{\"name\": \"\",\"type\": \"address\"}],\"payable\": false,\"stateMutability\": \"view\",\"type\": \"function\"},{\"inputs\": [],\"payable\": false,\"stateMutability\": \"nonpayable\",\"type\": \"constructor\"}]";
        String functionBinCode = ContractUtil.getFunctionBinCode(abi,"valueInc",BigInteger.valueOf(1));
        TransactionImpl.TxResult resultValueInc = HiChain.contractCall(address, NonceKeeper.incrNonce(address),priKey, contractAddr, functionBinCode, "test function valueInc");
        log.info("\r 3 HiChain.contractCall valueInc return==>\n{}",resultValueInc.getStatus());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //3.3 HiChain.getTxInfo for HiChain.contractCreate
        TransactionImpl.TxResult txResultValueInc = HiChain.getTxInfo(resultValueInc.getHash());
        log.info("\r 3 HiChain.getTxInfo return==>\n{}",new Gson().toJson(txResultValueInc.getStatus()));


        //3.6 创建TOKEN ABC
        List<TransferInfo> rC20CreateOutputList = new ArrayList<TransferInfo>(){{
            this.add(new TransferInfo("0xec5c1b38f0aa23bad42de39e2b5bbfd847c3db76","10000000000000000000000000"));
        }};
        TransactionImpl.TxResult resultABC = HiChain.rC20Create(address,NonceKeeper.incrNonce(address),priKey,"ABC","ABC test", rC20CreateOutputList,"publish rc20 test");
        log.info("\r 3 HiChain.rC20Create ABC return==>\n{}", resultABC);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //3.3，查询创建METH2交易结果
        TransactionImpl.TxResult txResultABC = HiChain.getTxInfo(resultABC.getHash());
        log.info("\r 3 HiChain.getTxInfo return==>\n{}",new Gson().toJson(txResultABC.getStatus()));
        String addrABC = CryptoUtil.del0X(txResultABC.getStatus().getResult());
        //3.7，创建TOKEN DEF
        TransactionImpl.TxResult resultDEF = HiChain.rC20Create(address,NonceKeeper.incrNonce(address),priKey,"DEF","DEF test", rC20CreateOutputList,"publish rc20 test");
        log.info("\r 3 HiChain.rC20Create DEF return==>\n{}", resultDEF);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //3.3，查询创建METH2交易结果
        TransactionImpl.TxResult txResultDEF = HiChain.getTxInfo(resultCreate.getHash());
        log.info("\r 3 HiChain.getTxInfo rC20Create return==>\n{}",txResultDEF.getStatus());
        String addrDEF = CryptoUtil.del0X(txResultABC.getStatus().getResult());

        //3.6 创建TOKEN ABC
        List<TransferInfo> rC20TransferOutputList = new ArrayList<TransferInfo>(){{
            this.add(new TransferInfo("0x69a0e3390ded433f3b1b292606ac2a782e6336de","10000000000000000000000000"));
        }};
        TransactionImpl.TxResult resultRC20Transfer = HiChain.rC20Transfer(address,NonceKeeper.incrNonce(address),priKey,addrABC, rC20TransferOutputList,"transfer rc20 test");
        log.info("\r 3 HiChain.rC20Transfer ABC return==>\n{}", resultRC20Transfer);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //3.3，查询创建METH2交易结果
        TransactionImpl.TxResult txResultRC20 = HiChain.getTxInfo(resultRC20Transfer.getHash());
        log.info("\r 3 HiChain.getTxInfo rC20Transfer return==>\n{}",new Gson().toJson(txResultRC20.getStatus()));


        //4 合约Mswap
        //合约管理者
        String man = address;
        //4.2 Mswap合约编码(需要借助工具，当前存入固定文件即可)//contract_bin/MSwap.bin
        binCode = "608060405234801561001057600080fd5b50604051620024aa380380620024aa8339810180604052810190808051906020019092919080519060200190929190805182019291905050506000806000806000819055508473ffffffffffffffffffffffffffffffffffffffff168673ffffffffffffffffffffffffffffffffffffffff161415151561009057600080fd5b8351925082600181905550600091505b828210156101895783828151811015156100b657fe5b906020019060200201519050600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16141515156100fe57600080fd5b80600260008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555081806001019250506100a0565b85600460006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555084600560006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000600c81905550601e600a8190555050505050505061227980620002316000396000f300608060405260043610610112576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630dfe1681146101175780631f8d82fc1461016e578063200d2ed2146101a6578063273a5061146101d1578063277de711146101fc5780633b228b3e146102275780634cd41b33146102525780634d21a24c146102a95780635badbe4c146102dc578063704602991461030757806381b69494146103ab57806384879b4b146103d657806392dabebf146104015780639543e8ef1461042c578063983099521461046e5780639e1b0045146104c6578063d21220a714610508578063d96073cf1461055f578063e6539901146105a1578063fdff9b4d146105d9575b600080fd5b34801561012357600080fd5b5061012c61065c565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b61018c60048036038101908080359060200190929190505050610682565b604051808215151515815260200191505060405180910390f35b3480156101b257600080fd5b506101bb610a7a565b6040518082815260200191505060405180910390f35b3480156101dd57600080fd5b506101e6610a80565b6040518082815260200191505060405180910390f35b34801561020857600080fd5b50610211610a86565b6040518082815260200191505060405180910390f35b34801561023357600080fd5b5061023c610a8c565b6040518082815260200191505060405180910390f35b34801561025e57600080fd5b50610293600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610a92565b6040518082815260200191505060405180910390f35b3480156102b557600080fd5b506102be610aaa565b60405180826000191660001916815260200191505060405180910390f35b3480156102e857600080fd5b506102f1610b97565b6040518082815260200191505060405180910390f35b34801561031357600080fd5b506103366004803603810190808035600019169060200190929190505050610b9d565b604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019250505060405180910390f35b3480156103b757600080fd5b506103c0610c01565b6040518082815260200191505060405180910390f35b3480156103e257600080fd5b506103eb610c07565b6040518082815260200191505060405180910390f35b34801561040d57600080fd5b50610416610c0d565b6040518082815260200191505060405180910390f35b6104546004803603810190808035906020019092919080359060200190929190505050610c13565b604051808215151515815260200191505060405180910390f35b6104ac600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506114cd565b604051808215151515815260200191505060405180910390f35b6104ee6004803603810190808035906020019092919080359060200190929190505050611651565b604051808215151515815260200191505060405180910390f35b34801561051457600080fd5b5061051d611a87565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6105876004803603810190808035906020019092919080359060200190929190505050611aad565b604051808215151515815260200191505060405180910390f35b6105bf60048036038101908080359060200190929190505050612174565b604051808215151515815260200191505060405180910390f35b3480156105e557600080fd5b5061061a600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061218d565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600080600080600660003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205485101515156106d657600080fd5b6106eb856009546121c090919063ffffffff16565b60098190555061070760026008546121d990919063ffffffff16565b92506107436002610735856107276009548a6121d990919063ffffffff16565b61221490919063ffffffff16565b61221490919063ffffffff16565b915061076e600854610760600754856121d990919063ffffffff16565b61221490919063ffffffff16565b9050610785816007546121c090919063ffffffff16565b6007819055506107a0826008546121c090919063ffffffff16565b6008819055506107bd6008546007546121d990919063ffffffff16565b600b8190555061081585600660003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546121c090919063ffffffff16565b600660003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663a9059cbb33836040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050602060405180830381600087803b15801561091d57600080fd5b505af1158015610931573d6000803e3d6000fd5b505050506040513d602081101561094757600080fd5b8101908080519060200190929190505050151561096357600080fd5b600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663a9059cbb33846040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050602060405180830381600087803b158015610a2857600080fd5b505af1158015610a3c573d6000803e3d6000fd5b505050506040513d6020811015610a5257600080fd5b81019080805190602001909291905050501515610a6e57600080fd5b60019350505050919050565b600c5481565b60015481565b600b5481565b60095481565b60066020528060005260406000206000915090505481565b600060014303403060008081546001019190508190556040516020018084600019166000191681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166c0100000000000000000000000002815260140182815260200193505050506040516020818303038152906040526040518082805190602001908083835b602083101515610b655780518252602082019150602081019050602083039250610b40565b6001836020036101000a0380198251168184511680821785525050505050509050019150506040518091039020905090565b60005481565b60036020528060005260406000206000915090508060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060010160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905082565b600a5481565b60075481565b60085481565b6000806000806000861180610c285750600085115b1515610c3357600080fd5b600086118015610c435750600085115b15610c5157600093506114c4565b600086111561108857610c83600754610c75600854896121d990919063ffffffff16565b61221490919063ffffffff16565b9450610c9b60026008546121d990919063ffffffff16565b9250610cd783610cc96002610cbb896009546121d990919063ffffffff16565b6121d990919063ffffffff16565b61221490919063ffffffff16565b9150600082111515610ce857600080fd5b600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166323b872dd3330896040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050602060405180830381600087803b158015610de157600080fd5b505af1158015610df5573d6000803e3d6000fd5b505050506040513d6020811015610e0b57600080fd5b81019080805190602001909291905050501515610e2757600080fd5b600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166323b872dd3330886040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050602060405180830381600087803b158015610f2057600080fd5b505af1158015610f34573d6000803e3d6000fd5b505050506040513d6020811015610f4a57600080fd5b81019080805190602001909291905050501515610f6657600080fd5b610fb882600660003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205461222f90919063ffffffff16565b600660003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190555061100f6002866121d990919063ffffffff16565b90506110268160095461222f90919063ffffffff16565b6009819055506110418660075461222f90919063ffffffff16565b60078190555061105c8560085461222f90919063ffffffff16565b6008819055506110796008546007546121d990919063ffffffff16565b600b81905550600193506114c4565b60008511156114bf576110ba6008546110ac600754886121d990919063ffffffff16565b61221490919063ffffffff16565b95506110d260026008546121d990919063ffffffff16565b925061110e8361110060026110f2896009546121d990919063ffffffff16565b6121d990919063ffffffff16565b61221490919063ffffffff16565b915060008211151561111f57600080fd5b600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166323b872dd3330896040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050602060405180830381600087803b15801561121857600080fd5b505af115801561122c573d6000803e3d6000fd5b505050506040513d602081101561124257600080fd5b8101908080519060200190929190505050151561125e57600080fd5b600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166323b872dd3330886040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050602060405180830381600087803b15801561135757600080fd5b505af115801561136b573d6000803e3d6000fd5b505050506040513d602081101561138157600080fd5b8101908080519060200190929190505050151561139d57600080fd5b6113ef82600660003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205461222f90919063ffffffff16565b600660003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055506114466002866121d990919063ffffffff16565b905061145d8160095461222f90919063ffffffff16565b6009819055506114788660075461222f90919063ffffffff16565b6007819055506114938560085461222f90919063ffffffff16565b6008819055506114b06008546007546121d990919063ffffffff16565b600b81905550600193506114c4565b600093505b50505092915050565b6000600660003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054821015151561151d57600080fd5b61156f82600660003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546121c090919063ffffffff16565b600660003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190555061160482600660008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205461222f90919063ffffffff16565b600660008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055506001905092915050565b600080600c541415156116cc576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260158152602001807f696e6974706f6f6c20737461747573206572726f72000000000000000000000081525060200191505060405180910390fd5b3373ffffffffffffffffffffffffffffffffffffffff16600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161415156117ce576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260168152602001807f696e6974706f6f6c206d616e67657273206572726f720000000000000000000081525060200191505060405180910390fd5b600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166323b872dd3330866040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050602060405180830381600087803b1580156118c757600080fd5b505af11580156118db573d6000803e3d6000fd5b505050506040513d60208110156118f157600080fd5b810190808051906020019092919050505050600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166323b872dd3330856040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050602060405180830381600087803b1580156119fc57600080fd5b505af1158015611a10573d6000803e3d6000fd5b505050506040513d6020811015611a2657600080fd5b8101908080519060200190929190505050508260078190555081600881905550611a5d6008546007546121d990919063ffffffff16565b600b81905550611a776002836121d990919063ffffffff16565b6009819055506001905092915050565b600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600080600080600080600080891180611ac65750600088115b1515611ad157600080fd5b600089118015611ae15750600088115b15611aef5760009650612168565b6000891115611e3d57600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166323b872dd33308c6040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050602060405180830381600087803b158015611bf157600080fd5b505af1158015611c05573d6000803e3d6000fd5b505050506040513d6020811015611c1b57600080fd5b81019080805190602001909291905050501515611c3757600080fd5b611c60612710611c528b600a546121d990919063ffffffff16565b61221490919063ffffffff16565b9550611c75868a6121c090919063ffffffff16565b9450611cb4611ca3600754611c956008548a6121d990919063ffffffff16565b61221490919063ffffffff16565b60095461222f90919063ffffffff16565b600981905550611ccf8560075461222f90919063ffffffff16565b600781905550611cec600754600b5461221490919063ffffffff16565b9350611d03846008546121c090919063ffffffff16565b9250600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663a9059cbb33856040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050602060405180830381600087803b158015611dca57600080fd5b505af1158015611dde573d6000803e3d6000fd5b505050506040513d6020811015611df457600080fd5b81019080805190602001909291905050501515611e1057600080fd5b83600881905550611e2e6007546008546121d990919063ffffffff16565b600b8190555060019650612168565b600088111561216357600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166323b872dd33308b6040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050602060405180830381600087803b158015611f3f57600080fd5b505af1158015611f53573d6000803e3d6000fd5b505050506040513d6020811015611f6957600080fd5b81019080805190602001909291905050501515611f8557600080fd5b611fae612710611fa08a600a546121d990919063ffffffff16565b61221490919063ffffffff16565b9550611fc386896121c090919063ffffffff16565b9450611fda8660095461222f90919063ffffffff16565b600981905550611ff58560085461222f90919063ffffffff16565b600881905550612012600854600b5461221490919063ffffffff16565b9150612029826007546121c090919063ffffffff16565b9050600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663a9059cbb33836040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050602060405180830381600087803b1580156120f057600080fd5b505af1158015612104573d6000803e3d6000fd5b505050506040513d602081101561211a57600080fd5b8101908080519060200190929190505050151561213657600080fd5b816007819055506121546007546008546121d990919063ffffffff16565b600b8190555060019650612168565b600096505b50505050505092915050565b6000808211151561218457600080fd5b60019050919050565b60026020528060005260406000206000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60008282111515156121ce57fe5b818303905092915050565b60008060008414156121ee576000915061220d565b82840290508284828115156121ff57fe5b0414151561220957fe5b8091505b5092915050565b600080828481151561222257fe5b0490508091505092915050565b600080828401905083811015151561224357fe5b80915050929150505600a165627a7a7230582067181d4ec27e71b816ce83adf2701ffab5126613302b10faa2cd7ca7ae1d9fd30029";
//        0000000000000000000000000000000000000000000000000000000041414120
//        0000000000000000000000000000000000000000000000000000000042424220
//        0000000000000000000000000000000000000000000000000000000000000060
//        0000000000000000000000000000000000000000000000000000000000000001
//        000000000000000000000000fda8e7b96c92a9fd75ae77b87bf7fdb4cbaea0d5
        //4.1 ContractUtil.getContractBinCodeMSwap
        String MSwapbinCode = ContractUtil.getContractBinCodeMSwap();
        Function f = ContractUtil.getFunctionWithMSwap(MSwapUtil.FunctionEnum.construtor);
        //4.3 编译构造方法参数
        String contructorParams = ContractUtil.getFunctionParamsCode(f,new BigInteger(addrABC,16),new BigInteger(addrDEF, 16),new BigInteger[]{new BigInteger(man,16)});
        //4.4 合并合约编码及构造函数参数编码
        binCode = binCode+contructorParams;
        log.info("\r 4 HiChain.contractCreate MSwap bin_code+params_code==>\n{}",binCode);
        //4.5 发布Mswap合约
        TransactionImpl.TxResult resultMSwapCreate = HiChain.contractCreate(address,NonceKeeper.incrNonce(address),priKey,binCode,"publish MSwap contract ");
        log.info("\r 4 HiChain.contractCreate MSwap return==>\n{}",resultMSwapCreate.getStatus());

        //3.3 HiChain.getTxInfo for HiChain.contractCreate MSwap
        TransactionImpl.TxResult txResultMSwapCreate = HiChain.getTxInfo(resultMSwapCreate.getHash());
        log.info("\r 4 HiChain.getTxInfo MSwap return==>\n{}",new Gson().toJson(txResultMSwapCreate.getStatus()));
        log.info("\r 4 HiChain.contractCall [initPool] return==>\n{}",txResultCreate.getStatus().getResult());
        String contractMSwapAddr = "";
        if(txResultCreate.getStatus()!=null && txResultCreate.getStatus().getStatus().equals("0x01")){
            contractMSwapAddr = txResultCreate.getStatus().getResult();
        }


        //4.6 MSwap HiChain.contractCall initPool
        // compile function with params  ABC=10000:DEF=20000
        binCode = ContractUtil.getFunctionBinCodeWithMSwap(MSwapUtil.FunctionEnum.initPool, new BigInteger(10000+"000000000000000000"),new BigInteger(20000+"000000000000000000"));
        log.info("\r 4 ContractUtil.getFunctionBinCodeWithMSwap [initPool] ==>\n{}",binCode);
        // call initPool
        TransactionImpl.TxResult resultInitPool = HiChain.contractCall(address, NonceKeeper.incrNonce(address), priKey, contractMSwapAddr, binCode, "测试发布MSwap合约");
        log.info("\r 4 HiChain.contractCall [initPool] return==>\n{}",resultInitPool);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //3.3 HiChain.getTxInfo for HiChain.contractCall initPool
        TransactionImpl.TxResult txResultInitPool = HiChain.getTxInfo(resultInitPool.getHash());
        log.info("\r 4 HiChain.getTxInfo [initPool] return==>\n{}",new Gson().toJson(txResultInitPool.getStatus()));
        log.info("\r 4 HiChain.contractCall [initPool] result==>\n{}",txResultInitPool.getStatus().getResult());
        // [initPool] result==>
        // 0000000000000000000000000000000000000000000000000000000000000001

        //5.3 调用合约 METH2兑换MOF2 swap
        //5.4 编码合约方法  METH2=100:MOF2=0
        binCode = ContractUtil.getFunctionBinCodeWithMSwap(MSwapUtil.FunctionEnum.swap, new BigInteger(100+"000000000000000000"),BigInteger.ZERO);
        TransactionImpl.TxResult resultSwap = HiChain.contractCall(address,NonceKeeper.incrNonce(address), priKey, contractAddr, binCode, "call function swap");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //3.3 HiChain.getTxInfo for HiChain.contractCall swap
        TransactionImpl.TxResult txResultSwap = HiChain.getTxInfo(resultSwap.getHash());
        log.info("\r 4 HiChain.getTxInfo [swap] return==>\n{}",new Gson().toJson(txResultSwap.getStatus()));
        log.info("\r 4 HiChain.contractCall [swap] result==>\n{}",txResultSwap.getStatus().getResult());


        //5.3 调用合约 增加流动性 addLiquid
        //5.4 编码合约方法  METH2=100:MOF2=0
        binCode = ContractUtil.getFunctionBinCodeWithMSwap(MSwapUtil.FunctionEnum.swap, new BigInteger(100+"000000000000000000"),BigInteger.ZERO);
        TransactionImpl.TxResult resultAddLiquid = HiChain.contractCall(address,NonceKeeper.incrNonce(address), priKey, contractAddr, binCode, "call function addLiquid");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //3.3 HiChain.getTxInfo for HiChain.contractCall swap
        TransactionImpl.TxResult txResultAddLiquid = HiChain.getTxInfo(resultAddLiquid.getHash());
        log.info("\r 4 HiChain.getTxInfo [addLiquid] return==>\n{}",new Gson().toJson(txResultAddLiquid.getStatus()));
        log.info("\r 4 HiChain.contractCall [addLiquid] result==>\n{}",txResultAddLiquid.getStatus().getResult());


        //10.1 HiChain.getLastedBlock
        Block.BlockRet txResultLatestBlock = HiChain.getLastedBlock();
        log.info("\r 10 HiChain.getLastedBlock return==>\n{}",new JsonFormat().printToString(txResultLatestBlock));

        //10.2 HiChain.getBlockByHeight
        Block.BlockRet txResultBlockHeight = HiChain.getBlockByHeight(1000);
        log.info("\r 10 HiChain.getBlockByHeight return==>\n{}",new JsonFormat().printToString(txResultBlockHeight));

    }

}
