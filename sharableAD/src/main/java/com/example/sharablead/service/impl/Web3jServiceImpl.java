package com.example.sharablead.service.impl;

import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.enums.GlobalResponseEnum;
import com.example.sharablead.service.Web3jService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class Web3jServiceImpl implements Web3jService {
    @Value("${web3.block-chain.url}")
    private String chainUrl;

    @Value("${web3.block-chain.id}")
    private Integer chainId;

    @Value("${web3.token.address}")
    private String tokenAddress;

    @Value("${web3.token.bnb-address}")
    private String bnbAddress;

    @Value("${web3.token.swap-router}")
    private String swapRouter;

    @Value("${web3.wallet.deposit-withdraw.address}")
    private String depositWithdrawAddress;

    @Value("${web3.wallet.deposit-withdraw.private-key}")
    private String depositWithdrawPrivateKey;

    @Value("${web3.wallet.on-sale.address}")
    private String onSaleWalletAddress;

    @Value("${web3.wallet.burn.address}")
    private String burnAddress;

    @Value("${web3.wallet.daily-task.address}")
    private String feedBackAddress;

    @Value("${web3.wallet.daily-task.private-key}")
    private String feedBackPrivateKey;


    Web3j depositWithdrawWeb3j = null;
    Credentials depositWithdrawCredentials = null;

    private static final String TRANSFER = "transfer";

    private static final String BALANCE_OF = "balanceOf";

    private static final String GET_AMOUNTS_IN = "getAmountsIn";

    //链上交易成功状态值
    private static final String SUCCESS_STATUS = "0x1";

    private static final String FAIL_STATUS = "0x0";

    private static final String INIT_STATUS = "0x2";

    private static final String ERROR_STRING = "-";

    private static Map<String, String> keyMap = new HashMap<>();

    @PostConstruct
    public void init() {

        keyMap.put(feedBackAddress, feedBackPrivateKey);
        keyMap.put(depositWithdrawAddress, depositWithdrawPrivateKey);
        log.info("keyMap = {}", keyMap);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(50, TimeUnit.SECONDS)//连接超时(单位:秒)
                .callTimeout(120, TimeUnit.SECONDS)//整个流程耗费的超时时间(单位:秒)--很少人使用
                .pingInterval(5, TimeUnit.SECONDS)//websocket轮训间隔(单位:秒)
                .readTimeout(60, TimeUnit.SECONDS)//读取超时(单位:秒)
                .writeTimeout(60, TimeUnit.SECONDS)//写入超时(单位:秒)
                .build();

        depositWithdrawWeb3j = Web3j.build(new HttpService(chainUrl));
        String web3ClientVersion = null;
        try {
            web3ClientVersion = depositWithdrawWeb3j.web3ClientVersion().send().getWeb3ClientVersion();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        log.info("version = " + web3ClientVersion);
        depositWithdrawCredentials = Credentials.create(depositWithdrawPrivateKey);
    }

    @Override
    public GlobalResponse withdraw(String toAddress, BigDecimal amount) {
        return transfer(depositWithdrawWeb3j, depositWithdrawCredentials, depositWithdrawAddress, toAddress, amount);
    }

    @Override
    public GlobalResponse synchronize(String fromAddress, String toAddress, BigDecimal amount) {
        if (!depositWithdrawAddress.equals(fromAddress) && !feedBackAddress.equals(fromAddress)) {
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid wallet  fromAddress");
        }

        if (!depositWithdrawAddress.equals(toAddress) && !burnAddress.equals(toAddress) && !feedBackAddress.equals(toAddress) && !onSaleWalletAddress.equals(toAddress)) {
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid wallet toAddress");
        }

        if (Objects.isNull(amount) || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid transfer amount");
        }

        Web3j synchronizeWeb3j = Web3j.build(new HttpService(chainUrl));
        String synchronizePrivateKey = keyMap.get(fromAddress);
        Credentials synchronizeCredentials = Credentials.create(synchronizePrivateKey);
        return transfer(synchronizeWeb3j, synchronizeCredentials, fromAddress, toAddress, amount);
    }

    @Override
    public GlobalResponse getBalanceOf(String fromAddress) {
        Web3j balanceOfWeb3j = Web3j.build(new HttpService(chainUrl));
        return balanceOf(balanceOfWeb3j, fromAddress);
    }

    @Override
    public GlobalResponse getSwapPrice() {
        GlobalResponse getGasPriceResponse = getGasPrice(depositWithdrawWeb3j);
        if (getGasPrice(depositWithdrawWeb3j).getCode() == GlobalResponseEnum.SUCCESS.getCode()){
            BigDecimal gasPrice = (BigDecimal) getGasPriceResponse.getData();

            log.info("gasPrice: {}", gasPrice);

            List<Type> inputParameters = new ArrayList<>();
            List<TypeReference<?>> outputParameters = new ArrayList<>();

            List<Address> list = new ArrayList<>();
            list.add(new Address(tokenAddress));
            list.add(new Address(bnbAddress));

            inputParameters.add(new Uint256(10000000));
            inputParameters.add((Type) list);

            log.info(inputParameters.toString());

            TypeReference<Uint256> typeReference = new TypeReference<>() {
            };
            outputParameters.add(typeReference);
            Function function = new Function(GET_AMOUNTS_IN, inputParameters, outputParameters);
            String data = FunctionEncoder.encode(function);
            org.web3j.protocol.core.methods.request.Transaction transaction = Transaction.createEthCallTransaction("", swapRouter, data);
            EthCall ethCall;
            BigInteger balanceValue = BigInteger.ZERO;
            try {
                ethCall = depositWithdrawWeb3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
                List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
                //balanceValue = (BigInteger) results.get(0).getValue();
                return GlobalResponse.success(results);
            } catch (IOException e) {
                log.error("balanceOf: error = {}", e.getMessage());
                return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), e.getMessage());
            }

        }
        return GlobalResponse.error(GlobalResponseEnum.UNKNOWN_ERROR.getCode(), GlobalResponseEnum.UNKNOWN_ERROR.getMessage());
    }

    //TODO need implementation and fix bug
    public GlobalResponse getGasPrice(Web3j web3j){
        try {
            EthGasPrice gasPrice = web3j.ethGasPrice().send();
            log.info(gasPrice.getGasPrice().toString());
            return GlobalResponse.success(new BigDecimal(gasPrice.getGasPrice().divide(new BigDecimal("10").pow(18).toBigInteger())).setScale(2, RoundingMode.HALF_DOWN));
        }catch (Exception e){
            log.error("getGasPrice error: {}", e.getMessage());
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), e.getMessage());
        }
    }

    public GlobalResponse transfer(Web3j web3j, Credentials credentials, String fromAddress, String toAddress, BigDecimal amount) {
        EthGetTransactionCount ethGetTransactionCount = null;
        try {
            ethGetTransactionCount = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("TRANSFER: get nonce error: {}", e.getMessage());
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "get nonce error");
        }
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        //手续费
        BigInteger gasPrice;
        EthGasPrice ethGasPrice = null;
        try {
            ethGasPrice = web3j.ethGasPrice().sendAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("TRANSFER: get gasPrice error: {}", e.getMessage());
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "get gasPrice error");
        }
        gasPrice = ethGasPrice.getGasPrice();
        BigInteger gasLimit = Convert.toWei("100000", Convert.Unit.WEI).toBigInteger();

        Function function = new Function(TRANSFER,
                Arrays.asList(new Address(toAddress), new Uint256(amount.multiply(new BigDecimal("10").pow(18)).toBigInteger())), Collections.singletonList(new TypeReference<Type>() {
        }));

        String encodedFunction = FunctionEncoder.encode(function);


        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, tokenAddress, encodedFunction);

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = null;
        try {
            ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("TRANSFER: send transaction error: {}", e.getMessage());
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "send transaction error");
        }
        String transactionHash = ethSendTransaction.getTransactionHash();
        EthGetTransactionReceipt transactionReceipt = null;
        TransactionReceipt receipt = null;
        try {
            transactionReceipt = web3j.ethGetTransactionReceipt(transactionHash).sendAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("TRANSFER: query transaction error: {}", e.getMessage());
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "query transaction error");
        }
        receipt = transactionReceipt.getResult();
        String status = Objects.isNull(receipt) ? INIT_STATUS : receipt.getStatus();
        //trade status: null 0x0 0x1 while+sleep implementation seems not well
        while (!SUCCESS_STATUS.equals(status) && !FAIL_STATUS.equals(status)) {
            try {
                Thread.sleep(2000);
                try {
                    transactionReceipt = web3j.ethGetTransactionReceipt(transactionHash).sendAsync().get();
                } catch (InterruptedException | ExecutionException e) {
                    log.error("TRANSFER: query transaction error: {}",e.getMessage());
                    return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "query transaction error");
                }
                receipt = transactionReceipt.getResult();
                status = Objects.isNull(receipt) ? INIT_STATUS : receipt.getStatus();
            } catch (InterruptedException e) {
                log.error("TRANSFER: get sleep error: {}", e.getMessage());
                return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "get sleep error");
            }
        }
        if (FAIL_STATUS.equals(status)) {
            log.error("TRANSFER: transfer error: {}", transactionHash);
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "transaction error, txHash = " + transactionHash);
        }
        return GlobalResponse.success(transactionHash);
    }

    /**
     * get the balance of the token for specific address
     * @return
     */
    public GlobalResponse balanceOf(Web3j balanceOfWeb3j, String fromAddress){
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address address = new Address(fromAddress);
        inputParameters.add(address);
        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
        };
        outputParameters.add(typeReference);
        Function function = new Function(BALANCE_OF, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        org.web3j.protocol.core.methods.request.Transaction transaction = Transaction.createEthCallTransaction(fromAddress, tokenAddress, data);

        EthCall ethCall;
        BigInteger balanceValue = BigInteger.ZERO;
        try {
            ethCall = balanceOfWeb3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            balanceValue = (BigInteger) results.get(0).getValue();
        } catch (IOException e) {
            log.error("balanceOf: error = {}", e.getMessage());
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), e.getMessage());
        }
        return GlobalResponse.success(new BigDecimal(balanceValue.divide(new BigDecimal("10").pow(18).toBigInteger())).setScale(2, RoundingMode.HALF_DOWN));

    }

}
