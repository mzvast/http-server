package com.silu.dinner.file;

public class FileInfo {
	public String getBucketName() {
		return bucketName;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	private String url;//文件存储的url
	private String accessKey;//访问秘钥
	private String storeService;//存储服务器  格式为   upaiyun   amazons3    
	private String bucketName;
	private String region;
    private String ext;

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public String getStoreService() {
		return storeService;
	}
	public void setStoreService(String storeService) {
		this.storeService = storeService;
	}
	public FileInfo() {
		
	}
	public FileInfo(String url, String accessKey, String storeService) {
		super();
		this.url = url;
		this.accessKey = accessKey;
		this.storeService = storeService;
	}
	
}
