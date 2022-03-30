package com.ggf.demop.controller;

import com.ggf.demo.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/payment")
@Controller
public class PaymentController {


    @Value("${server.port}")
    private String serverPort;

    /**
     * 模拟支付请求
     * @param id 订单id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Payment> payment(@PathVariable("id")Integer id){
        Payment payment = new Payment(id, "支付成功！ 服务端口为:"+serverPort);
        return ResponseEntity.ok(payment);
    }

}
