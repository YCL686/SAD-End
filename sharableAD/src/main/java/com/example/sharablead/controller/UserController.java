package com.example.sharablead.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sharablead.common.GlobalResponse;
//import com.example.sharablead.grpc.HelloClient;
import com.example.sharablead.entity.User;
import com.example.sharablead.mapper.UserMapper;
import com.example.sharablead.request.LoginRequest;
import com.example.sharablead.service.UserService;
import com.example.sharablead.util.TokenUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author inncore
 * @since 2022-09-28
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private static final String APISTR = "user operation";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenUtil tokenUtil;

//    @Autowired
//    private HelloClient helloClient;

    /**
     * 判断用户是否连接钱包即登录 需要验签
     * @return
     */
    @ApiOperation(value = "login", notes = APISTR + "login")
    @PostMapping("/login")
    @ApiOperationSupport(order = 1)
    public GlobalResponse login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }


    @ApiOperation(value = "logout", notes = APISTR + "logout")
    @GetMapping("/logout")
    @ApiOperationSupport(order = 1)
    public GlobalResponse logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        return userService.logout(token);
    }



    @ApiOperation(value = "pageUserList", notes = APISTR + "pageUserList")
    @GetMapping("/pageUserList")
    @ApiOperationSupport(order = 1)
    public GlobalResponse getConnectState() {
        Page<User> page = new Page<>(1,5);

        //queryWrapper组装查询where条件
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        userMapper.selectPage(page,queryWrapper);
        return GlobalResponse.success(page.getRecords());
    }

//    @ApiOperation(value = "grpcTest", notes = APISTR + "pageUserList")
//    @GetMapping("/grpcTest")
//    @ApiOperationSupport(order = 1)
//    public void grpcTest() {
//        helloClient.say();
//    }

    @ApiOperation(value = "getProfile", notes = APISTR + "getProfile")
    @GetMapping("getProfile")
    @ApiOperationSupport(order = 1)
    public GlobalResponse getProfile(HttpServletRequest httpServletRequest, @RequestParam(value = "userId", defaultValue = "0") Long userId){
        String token = httpServletRequest.getHeader("token");
        boolean self = false;
        try {
            if (tokenUtil.parseToken(token).getUserId().equals(userId)){
                self = true;
            }
        }catch (Exception e){
            //TODO
        }

        return userService.getProFile(self, userId);
    }

    @ApiOperation(value = "getMentionedList", notes = APISTR + "getMentionedList")
    @GetMapping("getMentionedList")
    public GlobalResponse getMentionedList(@RequestParam(value = "key", defaultValue = "0") String key){
        return userService.getMentionedList(key);
    }

    @ApiOperation(value = "getDotCount", notes = APISTR + "getDotCount")
    @GetMapping("/getDotCount")
    @ApiOperationSupport(order = 1)
    public GlobalResponse getDotCount(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        Long userId = 0L;
        try {
            userId = tokenUtil.parseToken(token).getUserId();
        }catch (Exception e){
            log.info("getDotCount: not logged");
        }
        return userService.getDotCount(userId);
    }

}
