package net.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

public interface IMinioService {
    public int upload(MultipartFile file,String bucketname,String filename,String contentType);
    public InputStream download(String fileName, HttpServletResponse response,String bucketname);
    public void deleteFile(String fileName,String bucketname);
    public int uploadByte(byte[] file,String bucketname,String filename);
}
