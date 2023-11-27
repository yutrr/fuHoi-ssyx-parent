package com.xie.ssyx.client.user;

import com.xie.ssyx.vo.user.LeaderAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-user")
public interface UserFeignClient {

    @GetMapping("/api/user/leader/inner/getUserAddressByUserId/{userId}")
    LeaderAddressVo getUserAddressByUserId(@PathVariable(value = "userId") Long userId);
}
