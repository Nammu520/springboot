package com.cn.web.controller.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cn.base.resp.CommonRespData;

@RestController
@RequestMapping("/uc")
public class UserManagerController {

    @GetMapping("/getUserInfo/")
    public  CommonRespData<String> list() {
        return CommonRespData.getInstance().success("success");
    }

}
