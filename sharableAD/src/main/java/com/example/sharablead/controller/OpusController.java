package com.example.sharablead.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Token;
import com.example.sharablead.request.SaveOrUpdateOpusRequest;
import com.example.sharablead.service.OpusService;
import com.example.sharablead.util.TokenUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author YCL686
 * @Date 2022/10/14
 */
@Slf4j
@RestController
@RequestMapping("/opus")
@CrossOrigin
public class OpusController {
    private static final String APISTR = "opus operation";

    @Autowired
    private OpusService opusService;

    @Autowired
    private TokenUtil tokenUtil;

    /**
     * type default 0
     * 0 means order by gmtCreated desc latest
     * 1 means order by recomand to be realized
     * ......
     * @param pageSize
     * @param pageNo
     * @param orderType
     * @return
     */
    @ApiOperation(value = "pageOpusList", notes = APISTR + "pageOpusList")
    @GetMapping("/pageOpusList")
    @ApiOperationSupport(order = 1)
    public GlobalResponse pageOpusList(HttpServletRequest httpServletRequest, @RequestParam(value = "pageSize", required=true, defaultValue = "5") long pageSize, @RequestParam(value = "pageNo", required=true, defaultValue = "1") long pageNo, @RequestParam(value = "orderType", required=true, defaultValue = "0") int orderType) {
        String token = httpServletRequest.getHeader("token");
        Long userId = 0L;
        if (StringUtils.isNotBlank(token)){
            try {
                Token token1 = tokenUtil.parseToken(token);
                userId = token1.getUserId();
            }catch (Exception e){
                log.error("pageOpusList abnormal: token is carried but invalid");
            }
        }
        return opusService.pageOpusList(pageSize, pageNo, orderType, userId);
    }

    /**
     * 场景 用户首次打开发布页面或者编辑已发布作品 编辑后点击发布 调用
     * @param saveOrUpdateOpusRequest
     * @param httpServletRequest
     * @return
     */
    @ApiOperation(value = "saveOrUpdateOpus", notes = APISTR + "saveOrUpdateOpus")
    @PostMapping("/saveOrUpdateOpus")
    public GlobalResponse saveOrUpdateOpus(@RequestBody SaveOrUpdateOpusRequest saveOrUpdateOpusRequest, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("token");
        saveOrUpdateOpusRequest.setUserId(tokenUtil.parseToken(token).getUserId());
        return opusService.saveOrUpdateOpus(saveOrUpdateOpusRequest);
    }

    /**
     * 指/opus/xx页面访问 无需拦截 放开
     * @param httpServletRequest
     * @param opusId
     * @return
     */
    @ApiOperation(value = "getOpusById", notes = APISTR + "getOpusById")
    @GetMapping("/getOpusById")
    public GlobalResponse getOpusById(HttpServletRequest httpServletRequest, @RequestParam(value = "opusId", defaultValue = "0") Long opusId){
        String token = httpServletRequest.getHeader("token");
        Long userId = 0L;
        if (StringUtils.isNotBlank(token)){
            try {
                Token token1 = tokenUtil.parseToken(token);
                userId = token1.getUserId();
            }catch (Exception e){
                log.error("getOpusById abnormal: token is carried but invalid");
            }
        }
        return opusService.getOpusById(opusId, userId);
    }

    /**
     * profile页面调用作品列表 不拦截
     * @param httpServletRequest
     * @param pageSize
     * @param pageNo
     * @param userId
     * @param status
     * @return
     */

    @ApiOperation(value = "pageProfileOpusList", notes = APISTR + "pageProfileOpusList")
    @GetMapping("/pageProfileOpusList")
    public GlobalResponse pageProfileOpusList(HttpServletRequest httpServletRequest, @RequestParam(value = "pageSize", required=true, defaultValue = "5") long pageSize, @RequestParam(value = "pageNo", required=true, defaultValue = "1") long pageNo, @RequestParam(value = "userId", defaultValue = "0") Long userId, @RequestParam(value = "status", defaultValue = "0") Integer status){
        String token = httpServletRequest.getHeader("token");
        Boolean self = false;
        if (StringUtils.isNotBlank(token)){
            try {
                if (userId.equals(tokenUtil.parseToken(token).getUserId())){
                    self = true;
                }
            }catch (Exception e){
                log.info("non self operation");
            }
        }
        return opusService.pageProfileOpusList(self, status, pageNo, pageSize, userId);
    }

    @ApiOperation(value = "getOpusByIdForPublish", notes = APISTR + "getOpusByIdForPublish")
    @GetMapping("/getOpusByIdForPublish")
    public GlobalResponse getOpusByIdForPublish(HttpServletRequest httpServletRequest, @RequestParam(value = "opusId", defaultValue = "0") Long opusId){
        String token = httpServletRequest.getHeader("token");
        Long userId = tokenUtil.parseToken(token).getUserId();
        return opusService.getOpusByIdForPublish(opusId, userId);
    }


}
