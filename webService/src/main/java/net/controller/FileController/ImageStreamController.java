package net.controller.FileController;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.JWTVerificationException;
import net.common.AssembleResponseMsg;
import net.common.AuthProvider;
import net.config.ImageAuthConfig;
import net.model.ResponseBody;
import net.service.IMinioService;
import net.util.MinioImageUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//用于处理图片流的上传下载请求
@RestController
@CrossOrigin
public class ImageStreamController {

    @Autowired
    IMinioService minioService;

    @Autowired
    MinioImageUtil imageUtil;

    @Autowired
    private ImageAuthConfig imageAuthConfig;

    //上传头像
    @RequestMapping(value = "/imageStream/setAvatar",produces = "application/json;charset=utf-8",method = RequestMethod.POST)//
    public ResponseBody uploadAvatar(@RequestParam("file") MultipartFile file, @RequestParam("imageToken") String imgtoken){//,
        /*
        解析imgtoken
         */
        //String imgtoken="";
        Map map = JSONObject.parseObject(imgtoken);
        if(!validate(map.get("token").toString())){
            return new AssembleResponseMsg().failure(200,"error","头像更新失败");
        }
        String fileName = "avatar/";
        String nextName = map.get("message").toString();
        fileName+=nextName+".png";
        int result = minioService.upload(file,imageUtil.getBucketname(),fileName,file.getContentType());
        if(result==0){
            fileName = getImageLocation()+fileName;
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("imagehref",fileName);
            return new AssembleResponseMsg().success(resultMap);
        }

        return new AssembleResponseMsg().failure(200,"error","头像更新失败");
    }

    //上传图片
    @RequestMapping(value = "/imageStream/setImage",produces = "application/json;charset=utf-8")//
    public ResponseBody uploadImage(@RequestParam("file") MultipartFile file,@RequestParam("imageToken") String imgtoken){
        /*
        解析imgtoken
         */

        String originalFileName = file.getOriginalFilename();
        String[] ff = originalFileName.split("\\.");
        String fileName = ff[0]+UUID.randomUUID().toString();
        int result = minioService.upload(file,imageUtil.getBucketname(),fileName+"."+ff[1],file.getContentType());
        if(result==0){
            Map<String,Object> resultMap = new HashMap<>();
            fileName = getUrlHead()+getImageLocation()+fileName+"/"+ff[1];
            resultMap.put("imagehref",fileName);
            return new AssembleResponseMsg().success(resultMap);
        }
        return new AssembleResponseMsg().failure(200,"error","图片上传失败");
    }

    //获得图片
    @RequestMapping(value = "/imageStream/getImage/{imagehref}/{suffix}")
    public void getImage(@PathVariable("imagehref") String imagehref,@PathVariable("suffix") String suffix, HttpServletResponse response){
        String filename = getName(imagehref,suffix);
        InputStream is = minioService.download(filename,response,imageUtil.getBucketname());
        try(OutputStream os = response.getOutputStream();){
            /*
            byte [] buffer = new byte[10240]; // 图片文件流缓存池
            while(is.read(buffer) != -1){
                os.write(buffer);
            }
            os.flush();

             */
            IOUtils.copy(is, response.getOutputStream());
            is.close();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    private String getName(String imghref,String suffix){
        imghref = imghref.replace('.','/');
        return imghref+'.'+suffix;
    }

    private boolean validate(String token){
        int id;
        try{
            id = AuthProvider.validate(token,imageAuthConfig.getAuthKey());
        }
        catch (Exception e){
            return false;
        }
        if(id == 123456)return true;
        return false;
    }

    private String getImageLocation(){
        return "/imageStream/getImage/";
    }
    private String getUrlHead(){
        return "http://127.0.0.1:8000/";
    }

}
