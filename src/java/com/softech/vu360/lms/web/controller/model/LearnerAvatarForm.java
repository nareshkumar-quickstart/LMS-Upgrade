/**
 * 
 */
package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;


/**
 * @author sana.majeed
 *
 */
public class LearnerAvatarForm implements ILMSBaseInterface {
	private static final long serialVersionUID = 1L;	
	private MultipartFile avatar = null;	
	private List<String> defaultAvatarList = new ArrayList<String>();
	private String avatarURL = null;
	
	
	/**
	 * @param avatar the avatar to set
	 */
	public void setAvatar(MultipartFile avatar) {
		this.avatar = avatar;
	}
	/**
	 * @return the avatar
	 */
	public MultipartFile getAvatar() {
		return avatar;
	}
	/**
	 * @return the defaultAvatarList
	 */
	public List<String> getDefaultAvatarList() {
		return defaultAvatarList;
	}
	/**
	 * @param defaultAvatarList the defaultAvatarList to set
	 */
	public void setDefaultAvatarList(List<String> defaultAvatarList) {
		this.defaultAvatarList = defaultAvatarList;
	}
	/**
	 * @param avatarURL the avatarURL to set
	 */
	public void setAvatarURL(String avatarURL) {
		this.avatarURL = avatarURL;
	}
	/**
	 * @return the avatarURL
	 */
	public String getAvatarURL() {
		return avatarURL;
	}

}