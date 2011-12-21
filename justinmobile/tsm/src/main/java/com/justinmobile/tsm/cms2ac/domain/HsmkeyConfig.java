package com.justinmobile.tsm.cms2ac.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.justinmobile.core.domain.AbstractEntity;
import com.justinmobile.core.domain.ResourcesFormat;

@Entity
@Table(name = "HSMKEY_CONFIG")
public class HsmkeyConfig extends AbstractEntity{

	private static final long serialVersionUID = 2111800351L;

	public static final int MODEL_SD  = 1;
	public static final int MODEL_APP = 2;
	
	/** 主键 */
	private Long id;
	/** 密钥版本 */
	private Integer version;
	/** 密钥索引 */
	private Integer index;
	/** 密钥类型
		14:读卡器通道安全协议 加密密钥
		12:读卡器通道安全协议 MAC密钥
		13:读卡器通道安全协议 密钥加密密钥
		10:读卡器通道安全协议 DAP认证密钥
		11:读卡器通道安全协议 TOKEN认证密钥
		14:空中通道安全协议 加密密钥
		12:空中通道安全协议 MAC密钥 */
	private Integer type;
	/** 厂商 */
	@ResourcesFormat(key="encryptor.vendor")
	private String vendor;

	private Integer sdModel;

	private Calendar effectiveDate;
	
	/** 密钥对应的应用类型：1-SD；2-APP */
	@ResourcesFormat(key="encryptor.model")
	private Integer model;

//	private Set<KeyProfile> keyProfiles;
//	
//	private Set<ApplicationKeyProfile> applicationKeyProfiles;
	
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = { @Parameter(name = "sequence", value = "SEQ_HSMKEY_CONFIG") })
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	@Column(name="HSMKEY_CONFIG_VERSION")
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Column(name="HSMKEY_CONFIG_INDEX")
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	@Column(name="HSMKEY_CONFIG_TYPE")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSdModel() {
		return sdModel;
	}

	public void setSdModel(Integer sdModel) {
		this.sdModel = sdModel;
	}

	public Calendar getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Calendar effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	@Column(name="HSMKEY_CONFIG_MODEL")
	public Integer getModel() {
		return model;
	}

	public void setModel(Integer model) {
		this.model = model;
	}

//	@ManyToMany
//	@JoinTable(name="KEY_PROFILE_HSMKEY", joinColumns = { @JoinColumn(name = "HSMKEY_CONFIG_ID") }, inverseJoinColumns = { @JoinColumn(name = "KEY_PROFILE_ID") })
//	@Cascade(value = {CascadeType.DELETE})
//	@LazyCollection(LazyCollectionOption.TRUE)
//	public Set<KeyProfile> getKeyProfiles() {
//		return keyProfiles;
//	}
//
//	public void setKeyProfiles(Set<KeyProfile> keyProfiles) {
//		this.keyProfiles = keyProfiles;
//	}
//
//	@ManyToMany
//	@JoinTable(name="APPLICATION_KEY_PROFILE_HSMKEY", joinColumns = { @JoinColumn(name = "HSMKEY_CONFIG_ID") }, inverseJoinColumns = { @JoinColumn(name = "KEY_PROFILE_ID") })
//	@Cascade(value = {CascadeType.DELETE})
//	@LazyCollection(LazyCollectionOption.TRUE)
//	public Set<ApplicationKeyProfile> getApplicationKeyProfiles() {
//		return applicationKeyProfiles;
//	}
//
//	public void setApplicationKeyProfiles(
//			Set<ApplicationKeyProfile> applicationKeyProfiles) {
//		this.applicationKeyProfiles = applicationKeyProfiles;
//	}

	@Override
	public String toString() {
		return "HsmkeyConfig [id=" + id + ", version=" + version + ", index="
				+ index + ", type=" + type + ", vendor=" + vendor
				+ ", effectiveDate=" + effectiveDate + ", model=" + model + "]";
	}

}