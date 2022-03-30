package com.ggf.demo.client;

import com.ggf.demo.Payment;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 映射调用的下游接口
 */
//@FeignClient(value = "cloud-payment-service",fallback = PaymentClient.MyFallback.class)
@FeignClient(value = "cloud-payment-service",fallbackFactory = PaymentClient.MyFallbackFactory.class)
public interface PaymentClient {

    @GetMapping("/payment/{id}")
    Payment  payment(@PathVariable("id")Integer id);

    /* 使用内部静态类实现--用于测试熔断降级调用返回结果 */
    @Component
    static class MyFallback implements PaymentClient{

        @Override
        public Payment payment(Integer id) {
            return new Payment(id,"熔断降级方法返回");
        }
    }
    @Component
    /** 需要获取异常信息，实现FallbackFactory接口的包装类**/
    static class MyFallbackFactory implements FallbackFactory<MyFallback>{

        @Override
        public MyFallback create(Throwable cause) {
            // 输出错误信息
            cause.printStackTrace();
            return new MyFallback();
        }
    }


}
