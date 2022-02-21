package net.service.impl;

import com.sun.javaws.exceptions.InvalidArgumentException;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.PutObjectOptions;
import net.service.IMinioService;
import net.util.MinioImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
public class MinioServiceImpl implements IMinioService {
    @Autowired
    MinioClient minioClient;

    @Autowired
    private MinioImageUtil minioProperties;

    //文件上传
    @Override
    public int upload(MultipartFile file,String bucketname,String filename,String contentType) {
        int state=0;
        if (file == null || file.getSize() == 0 || file.isEmpty()) {
            throw new RuntimeException("上传文件为空，请重新上传");
        }
        try {
            final InputStream is = file.getInputStream();
            // 获取文件名
            assert filename != null;
            PutObjectOptions putOption =  new PutObjectOptions(is.available(), -1);
            putOption.setContentType(contentType);
            minioClient.putObject(bucketname, filename, is, putOption);
        }
        catch (Exception e){
            e.printStackTrace();
            state = 1;
        }
        return state;
    }
    //文件上传
    @Override
    public int uploadByte(byte[] file,String bucketname,String filename) {
        int state=0;
        if (file == null || file.length == 0 ) {
            throw new RuntimeException("上传文件为空，请重新上传");
        }
        try {
            InputStream ffile = new ByteArrayInputStream(file);
            final InputStream is = ffile;
            // 获取文件名
            assert filename != null;
            PutObjectOptions putOption =  new PutObjectOptions(is.available(), -1);
            minioClient.putObject(bucketname, filename, is, putOption);
        }
        catch (Exception e){
            e.printStackTrace();
            state = 1;
        }
        return state;
    }
    //文件下载
    @Override
    public InputStream download(String fileName, HttpServletResponse response,String bucketname) {
        InputStream inputStream = null;
        // 根据文件名拿到minio中的文件对象
        try {
            ObjectStat object = minioClient.statObject(bucketname, fileName);
        // 设置响应头类型
            response.setContentType(object.contentType());
            inputStream = minioClient.getObject(bucketname, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    //删除文件
    @Override
    public void deleteFile(String fileName,String bucketname){
        try {
            minioClient.removeObject(bucketname,fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
