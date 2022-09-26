package com.web.community.controller;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.web.community.dto.FileDTO;
import com.web.community.exception.CustomizeErrorCode;
import com.web.community.exception.CustomizeException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class FileController {
    private static final String SECRET_ID = "AKIDdEfttfFbkiSyIZAg1CC1gGfvxkInALK8";
    private static final String SECRET_KEY = "oSv8IH1jMDtOgnjeSFtKzRineOwFKAlC";
    private static final String BUCKETNAME = "theresa-code-1314110966";
    private static final String REGIONID = "ap-guangzhou";

    @PostMapping(value = "/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request){
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        if (file == null){
            throw new CustomizeException(CustomizeErrorCode.FILE_EMPTY);
        }
        String url = "";
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(SECRET_ID, SECRET_KEY);
        // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig中包含了设置region, https(默认http), 超时, 代理等set方法, 使用可参见源码或者接口文档FAQ中说明
        ClientConfig clientConfig = new ClientConfig(new Region(REGIONID));
        // 3 生成cos客户端
        COSClient cosClient = new COSClient(cred, clientConfig);
        //这里修改一下文件名字
        String oldFileName = file.getOriginalFilename();
        String eName = oldFileName.substring(oldFileName.lastIndexOf("."));
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = formatter.format(date);
        //新名字
        String newFileName = time + eName;
        // 简单文件上传, 最大支持 5 GB, 适用于小文件上传
        // 大文件上传请参照 API 文档高级 API 上传
        File localFile = null;
        try {
            localFile = File.createTempFile("temp", null);
            file.transferTo(localFile);
            // 指定要上传到 COS 上的路径
            String KEY = "images/" + newFileName;
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKETNAME, KEY, localFile);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            // putobjectResult会返回文件的etag
            URL objectUrl = cosClient.getObjectUrl(BUCKETNAME, KEY);//线上地址URL
            url = objectUrl.toString();
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(1);
            fileDTO.setUrl(url);
            System.out.println(url);
            return fileDTO;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭客户端(关闭后台线程)
            cosClient.shutdown();
        }
        return null;
    }
}
