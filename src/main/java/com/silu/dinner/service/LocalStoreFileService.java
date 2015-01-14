package com.silu.dinner.service;

import com.silu.dinner.constant.StatusCode;
import com.silu.dinner.exception.ServerException;
import com.silu.dinner.file.FileInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by silu on 15-1-14.
 */
@Service
public class LocalStoreFileService implements FileService {
    private static Logger LOGGER = LoggerFactory.getLogger(LocalStoreFileService.class);
    
    @Value("${file.store.path}")
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

    private FileInfo persitFile(InputStream inputStream, String fileName,
                                String fileStorePath) throws Exception {
        FileOutputStream fou = null;
        try {
            createBucket(fileStorePath);
            int lastIndex = fileName.lastIndexOf(".") ;
            String ext =  lastIndex != -1 ? fileName.substring( lastIndex ) : fileName ;
            String randomStr = createPackage(fileStorePath); // 创建文件夹
            File obsertFile = new File(fileStorePath + randomStr + File.separator + "default"+ext);// 保存文件
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
        File packageFile = new File(fileStorePath + randomStr);
        packageFile.mkdir();
        return randomStr;
    }
}
