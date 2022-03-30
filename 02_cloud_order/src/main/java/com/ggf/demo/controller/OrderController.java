package com.ggf.demo.controller;

import com.ggf.demo.Payment;
import com.ggf.demo.client.PaymentClient;
import com.ggf.demo.service.OrderService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

@RequestMapping("/order")
@Controller
@Slf4j
public class OrderController {

    @Resource
    private RestTemplate restTemplate;
    /* 获取注册的列表 */
    @Resource
    private DiscoveryClient discoveryClient;
    /* 下游支付服务API的代理对象 */
    @Resource
    private PaymentClient paymentClient;
    @Resource
    private OrderService orderService;


    @GetMapping("/payment/{id}")
    /* 熔断 */
//    @CircuitBreaker(name = "backendB",fallbackMethod = "fallback")
    /* 信号量隔离的方式 */
//   @Bulkhead(name = "backendA", fallbackMethod = "fallback",type = Bulkhead.Type.SEMAPHORE)
    /* 限流的方式 */
    @RateLimiter(name = "backendA", fallbackMethod = "fallback")
    public ResponseEntity<Payment> order(@PathVariable("id") Integer id) throws InterruptedException {
//        String reqUrl = "http://localhost:9001/payment/" + id;
//        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
//        ServiceInstance instance = instances.get(0);
//        String reqUrl = "http://" + instance.getHost() + ':' + instance.getPort() + "/payment/" +id;

        // 使用服务名去调用服务
        log.info("进入订单服务方法......");

        // 模拟慢调用
        Thread.sleep(10000);

        String reqUrl = "http://cloud-payment-service/payment/" + id;
        Payment payment = restTemplate.getForObject(reqUrl, Payment.class);
        log.info("离开订单服务方法......");
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/payment/thread/{id}")
    public ResponseEntity<Payment> orderByThread(@PathVariable("id") Integer id) throws InterruptedException, ExecutionException {
      return ResponseEntity.ok(orderService.order(id).get());
    }


    /* 熔断降级返回的方法 */
    public ResponseEntity<Payment> fallback(Integer id, Throwable e){
        e.printStackTrace();
        Payment payment = new Payment();
        payment.setId(id);
        payment.setMessage("fallback.....");
        return new ResponseEntity<>(payment, HttpStatus.BAD_REQUEST);
    }

    /**
     * 使用openfeign调用下游服务
     * @param id
     * @return
     */
    @GetMapping("/feign/payment/{id}")
    public ResponseEntity<Payment> feignOrder(@PathVariable("id") Integer id){
        Payment payment = paymentClient.payment(id);
        return ResponseEntity.ok(payment);
    }


}
