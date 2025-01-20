package com.fdt.portal.controller;

import com.fdt.portal.service.StoreService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Resource
    private StoreService storeService;

    @RequestMapping("add")
    public Long addStore(){
        return null;
    }
}
