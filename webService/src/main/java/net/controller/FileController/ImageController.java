package net.controller.FileController;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.sun.imageio.plugins.common.ImageUtil;
import net.common.AssembleResponseMsg;
import net.common.AuthProvider;
import net.config.ImageAuthConfig;
import net.config.KeysConfig;
import net.service.ILoginService;
import net.service.IMinioService;
import net.service.IRegisterService;
import net.service.ISearchService;
import net.service.impl.SearchServiceImpl;
import net.util.MinioImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.model.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//用于处理与数据库读写有关的图片预上传、预下载请求，本次项目仅处理图片预上传请求

@RestController
@CrossOrigin
public class ImageController {
    @Autowired
    private ImageAuthConfig imageAuthConfig;

    @Autowired
    private IRegisterService userService;

    @Autowired
    private KeysConfig keysConfig;

    //上传头像（已测试）
    @RequestMapping(value = "/image/uploadAvatar",produces = "application/json;charset=utf-8")
    public ResponseBody uploadAvatar(@RequestBody String str){
        Map map =  JSONObject.parseObject(str);
        String token =  map.get("token").toString();
        int id;
        try{
            id = AuthProvider.validate(token,keysConfig.getAuthKey());
        }catch (JWTVerificationException e)
        {
            return new AssembleResponseMsg().failure(401,"Unauthorized","证书验证错误");
        }
        /*
        imagehref包含的内容：{
            图像类型：image or avatar
            类型信息：如果是avatar，则包含用户id
            授权信息：
        }
        url:imageStream/setImage
         */
        Map<String,Object> searchMap = new HashMap<String,Object>();
        searchMap.put("id",id);
        searchMap.put("avatar",getImageLocation()+"avatar."+id+"/png");
        int result = userService.updateUserAvatar(searchMap);
        if(result >0){
            Map<String,String> imghrefMap = sign();
            imghrefMap.put("type","avatar");
            imghrefMap.put("message",Integer.toString(id));
            String imagehref = JSONObject.toJSON(imghrefMap).toString();
            Map<String,Object> resultMap = new HashMap<String,Object>();
            resultMap.put("url","/imageStream/setAvatar");
            resultMap.put("imageToken",imagehref);
            return new AssembleResponseMsg().success(resultMap);
        }
        else{
            return new AssembleResponseMsg().failure(200,"error","头像更新失败");
        }
    }

    //上传图片（已测试）
    @RequestMapping(value = "/image/uploadImage",produces = "application/json;charset=utf-8")
    public ResponseBody uploadImage(@RequestBody String str){
        Map map =  JSONObject.parseObject(str);
        String token =  map.get("token").toString();
        int id;
        try{
            id = AuthProvider.validate(token,keysConfig.getAuthKey());
        }catch (JWTVerificationException e)
        {
            return new AssembleResponseMsg().failure(401,"Unauthorized","证书验证错误");
        }
        /*
        imagehref包含的内容：{
            图像类型：image or avatar
            类型信息：如果是图像，则不包含信息
            授权信息：123456
        }
        url:imageStream/setImage
         */
        Map<String,String> imghrefMap = sign();
        imghrefMap.put("type","iamge");
        imghrefMap.put("message","null");
        String imagehref = JSONObject.toJSON(imghrefMap).toString();
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("url","/imageStream/setImage");
        resultMap.put("imageToken",imagehref);
        return new AssembleResponseMsg().success(resultMap);
    }

    private Map<String, String> sign(){
        return AuthProvider.sign(123456, imageAuthConfig.getAuthKey());
    }

    private String getImageLocation(){
        return "/imageStream/getImage/";
    }

}
