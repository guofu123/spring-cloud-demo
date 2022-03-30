package com.ggf.demo.service;

import com.ggf.demo.Payment;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import javafx.application.ConditionalFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class OrderService {




    @Bulkhead(name = "backendA", type = Bulkhead.Type.THREADPOOL)
    public CompletableFuture<Payment> order(@PathVariable("id") Integer id) throws InterruptedException {
        // 使用服务名去调用服务
        log.info("进入订单服务方法......");
        // 模拟慢调用
        Thread.sleep(10000);
        return  CompletableFuture.supplyAsync(()->new Payment(id,"线程池隔离回退......"));
    }


}
