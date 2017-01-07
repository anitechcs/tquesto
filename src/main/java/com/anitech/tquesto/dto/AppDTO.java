package com.anitech.tquesto.dto;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.anitech.tquesto.domain.App;
import com.anitech.tquesto.util.Constants;

/**
 * App DTO
 * 
 * @author Tapas
 *
 */
public class AppDTO {

	private Long appId;
	
    @Pattern(regexp = Constants.APP_NAME_REGEX)
    @Size(min = 1, max = 20)
    private String appName;
    
    @Size(max = 100)
    private String appDescription;
    
    private byte[] appIcon;
    
    @Size(max = 200)
    private String appSecret;

    private boolean isDefault = false;
    
    private boolean enableAndroidSdk = false;
    
    private boolean enableIosSdk = false;
    
    private boolean enableJavaSdk = false;
    
    private boolean enableJsSdk = false;

    public AppDTO() {
    }
    
    public AppDTO(App app) {
    	this(app.getAppId(), app.getAppName(), app.getAppDescription(), 
    			app.getAppIcon(), app.getAppSecret(), app.isDefault(), app.isEnableAndroidSdk(), 
    			app.isEnableIosSdk(), app.isEnableJavaSdk(), app.isEnableJsSdk());
    }
    
	public AppDTO(Long appId, String appName, String appDescription, byte[] appIcon, String appSecret,
			boolean isDefault, boolean enableAndroidSdk, boolean enableIosSdk, boolean enableJavaSdk,
			boolean enableJsSdk) {
		super();
		this.appId = appId;
		this.appName = appName;
		this.appDescription = appDescription;
		this.appIcon = appIcon;
		this.appSecret = appSecret;
		this.isDefault = isDefault;
		this.enableAndroidSdk = enableAndroidSdk;
		this.enableIosSdk = enableIosSdk;
		this.enableJavaSdk = enableJavaSdk;
		this.enableJsSdk = enableJsSdk;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppDescription() {
		return appDescription;
	}

	public void setAppDescription(String appDescription) {
		this.appDescription = appDescription;
	}

	public byte[] getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(byte[] appIcon) {
		this.appIcon = appIcon;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public boolean isEnableAndroidSdk() {
		return enableAndroidSdk;
	}

	public void setEnableAndroidSdk(boolean enableAndroidSdk) {
		this.enableAndroidSdk = enableAndroidSdk;
	}

	public boolean isEnableIosSdk() {
		return enableIosSdk;
	}

	public void setEnableIosSdk(boolean enableIosSdk) {
		this.enableIosSdk = enableIosSdk;
	}

	public boolean isEnableJavaSdk() {
		return enableJavaSdk;
	}

	public void setEnableJavaSdk(boolean enableJavaSdk) {
		this.enableJavaSdk = enableJavaSdk;
	}

	public boolean isEnableJsSdk() {
		return enableJsSdk;
	}

	public void setEnableJsSdk(boolean enableJsSdk) {
		this.enableJsSdk = enableJsSdk;
	}
    
	@Override
    public String toString() {
        return "AppDTO{" +
            "appId='" + appId + '\'' +
            ", appName='" + appName + '\'' +
            ", appDescription='" + appDescription + '\'' +
            ", isDefault='" + isDefault + '\'' +
            ", enableAndroidSdk=" + enableAndroidSdk +
            ", enableIosSdk='" + enableIosSdk + '\'' +
            ", enableJavaSdk='" + enableJavaSdk + '\'' +
            ", enableJsSdk=" + enableJsSdk +
            "}";
    }
	
}
