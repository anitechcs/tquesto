package com.anitech.tquesto.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.anitech.tquesto.util.Constants;

/**
 * App domain model
 * 
 * @author Tapas
 *
 */
@Entity
@Table(name="tq_app", schema="tquesto")
public class App extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq-gen")
    @SequenceGenerator(name="seq-gen",sequenceName="APP_ID_SEQ", initialValue=10000000, allocationSize=1)
    @Column(name = "app_id", length = 20)
    private Long appId;

    @NotNull
    @Pattern(regexp = Constants.APP_NAME_REGEX)
    @Size(min = 1, max = 20)
    @Column(name = "app_name", length = 20, unique = true, nullable = false)
    private String appName;
    
    @Size(max = 100)
    @Column(name = "app_description", length = 100)
    private String appDescription;
    
    @Column(name = "app_icon")
    private byte[] appIcon;
    
    @Size(max = 200)
    @Column(name = "app_secret", length = 200)
    private String appSecret;

    @NotNull
    @Column(name="is_default", nullable = false)
    private boolean isDefault = false;
    
    @NotNull
    @Column(name="enable_android_sdk", nullable = false)
    private boolean enableAndroidSdk = false;
    
    @NotNull
    @Column(name="enable_ios_sdk", nullable = false)
    private boolean enableIosSdk = false;
    
    @NotNull
    @Column(name="enable_java_sdk", nullable = false)
    private boolean enableJavaSdk = false;
    
    @NotNull
    @Column(name="enable_js_sdk", nullable = false)
    private boolean enableJsSdk = false;

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        App app = (App) o;

        if (!appName.equals(app.appName)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return appName.hashCode();
    }

    @Override
    public String toString() {
        return "App{" +
            "appName='" + appName + '\'' +
            ", appDescription='" + appDescription + '\'' +
            ", isDefault='" + isDefault + '\'' +
            ", enableAndroidSdk='" + enableAndroidSdk + '\'' +
            ", enableIosSdk='" + enableIosSdk + '\'' +
            ", enableJavaSdk='" + enableJavaSdk + '\'' +
            ", enableJsSdk='" + enableJsSdk + '\'' +
            "}";
    }
    
}
