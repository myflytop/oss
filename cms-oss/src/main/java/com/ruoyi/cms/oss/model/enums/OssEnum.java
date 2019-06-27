package com.ruoyi.cms.oss.model.enums;

public enum OssEnum {
	STORAGE(0,"storage"),
	SM(1,"sm"),
	IMAGE(2,"image"),	
	REDUCE(3,"reduce"),
	DOUCMENT(4,"document"),
	MEDIA(5,"media"),
	OTHER(6,"other"),
	BACKUP(7,"backup"),
	LOG(8,"log"),
	;
	OssEnum(int key,String value){
		this.key=key;
		this.value=value;
	};
	private int key;
	
	private String value;

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


}
