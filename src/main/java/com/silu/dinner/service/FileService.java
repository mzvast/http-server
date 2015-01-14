package com.silu.dinner.service;

import com.silu.dinner.exception.ServerException;

import java.io.InputStream;

/**
 * Created by silu on 15-1-14.
 */
public interface FileService {
    String storeFile(InputStream inputStream,String fileName,long size) throws ServerException;
}
