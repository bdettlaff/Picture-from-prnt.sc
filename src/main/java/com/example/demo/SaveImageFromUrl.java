package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import java.util.List;


@RestController
public class SaveImageFromUrl {

    private SaveImageFromUrlService saveImageFromUrlService;

    @Autowired
    public SaveImageFromUrl(SaveImageFromUrlService saveImageFromUrlService) {
        this.saveImageFromUrlService = saveImageFromUrlService;
    }

    @RequestMapping("picture")
    public List<String> getPicture() throws IOException {
        return saveImageFromUrlService.downloadPicture();
    }

}
