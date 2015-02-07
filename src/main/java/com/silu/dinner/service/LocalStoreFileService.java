package com.silu.dinner.service;

import com.silu.dinner.constant.StatusCode;
import com.silu.dinner.exception.ServerException;
import com.silu.dinner.file.FileInfo;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.UUID;

/**
 * Created by silu on 15-1-14.
 */
@Service
public class LocalStoreFileService implements FileService {
    private static Logger LOGGER = LoggerFactory.getLogger(LocalStoreFileService.class);

    @Value("#{configproperties['file.store.path']}")
    private String fileDir;
    @Override
    public String storeFile(InputStream inputStream, String fileName, long size) throws ServerException {
        LOGGER.debug("fileStorePath:"+fileDir);
        if(fileDir == null){
            fileDir = "/home/silu/data";
        }
        FileInfo fileInfo;
        try {
            fileInfo = persitFile(inputStream,fileName,fileDir);
        } catch (Exception e) {
            LOGGER.warn("fails to store file.",e);
            throw new ServerException(StatusCode.INNER_ERROR,"fails to store file");
        }

        return fileInfo.getAccessKey();
    }

    @Override
    public void getfile(HttpServletResponse res, String fileId, Long range) throws ServerException {
        FileInputStream input = null;
        ServletOutputStream os = null;
        String filePath = getFilePackage(fileDir,fileId) + File.separator+ fileId;
        
        LOGGER.debug("get file. filePath:{}",filePath);
        File file = new File(filePath);
        if(file != null){
            try {
                readFile(res, file, input, os, range);
            } catch (Exception e) {
                LOGGER.warn("fails to read file.file:{}",fileId,e);
                new ServerException(StatusCode.FILE_FAIL_TO_FIIND,"fails to find the file.");
            }
        }
    }

    private void readFile(HttpServletResponse res, File obsertFile, FileInputStream input, ServletOutputStream os, Long range) throws Exception {
        long len = obsertFile.length() - range;
        os = res.getOutputStream();

        res.setHeader("Content-Disposition", "attachment; filename="
                + obsertFile.getName());
        res.setContentType("application/octet-stream; charset=UTF-8");
        res.setHeader("Content-Length", String.valueOf(len));

        transferTo(obsertFile, range, len, Channels.newChannel(os));
    }

    private void transferTo(File file, long position, long count,
                            WritableByteChannel target) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fis.getChannel().transferTo(position, count, target);
        } catch (IOException e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(fis);
        }
    }
    
    private FileInfo persitFile(InputStream inputStream, String fileName,
                                String fileStorePath) throws Exception {
        FileOutputStream fou = null;
        try {
            createBucket(fileStorePath);
            int lastIndex = fileName.lastIndexOf(".") ;
            String ext =  lastIndex != -1 ? fileName.substring( lastIndex ) : fileName ;
            String randomStr = createPackage(fileStorePath); // 创建文件夹
            File obsertFile = new File(getFilePackage(fileStorePath,randomStr) + File.separator+ randomStr);// 保存文件
            if(!obsertFile.exists()){
                obsertFile.createNewFile();
            }

            fou = new FileOutputStream(obsertFile);
            byte[] temp = new byte[4096];
            while (true) {
                int read = inputStream.read(temp);
                if (read == -1) {
                    break;
                } else {
                    fou.write(temp, 0, read);
                    fou.flush();
                }
            }
            FileInfo info = new FileInfo();
            info.setAccessKey(randomStr);
            info.setBucketName(fileStorePath);
            info.setStoreService("localfile");
            info.setExt(ext);

            return info;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (fou != null) {
                fou.close();
            }
        }
    }

    private void createBucket(String fileStorePath) {
        File path = new File(fileStorePath);
        if (!path.exists()) {
            path.mkdirs();
        }
    }

    private String createPackage(String fileStorePath) {
        String randomStr = UUID.randomUUID().toString();
        //暂时支持yi级目录 应该可以满足短时期内文件数目的需求
        File packageFile = new File(getFilePackage(fileStorePath,randomStr));
        if(!packageFile.exists()){
            packageFile.mkdir();
        }
        return randomStr;
    }
    
    private String getFilePackage(String fileStorePath,String fileRandomNum){
        return fileStorePath + File.separator+ fileRandomNum.substring(0,4);
    }
}
