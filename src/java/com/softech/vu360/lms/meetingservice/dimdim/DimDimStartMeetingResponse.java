package com.softech.vu360.lms.meetingservice.dimdim;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

public class DimDimStartMeetingResponse extends DimDimResponse{
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	private static final Logger log = Logger.getLogger(DimDimStartMeetingResponse.class);
	
	@Override
	public void parse(JSONObject jsonResponse) {
		
//		{"result":true,
//			"response":{"conference_key":"vcs1____all____faisal.ahmed____default",
//			"max_attendee_videos":"2","conferenceName":"faisal&#46;ahmed","whiteboard_enabled":"true",
//			"start_share_auto":"cob","net_profile":"3","allowJoinUrlCopy":"true","cob_app_root":"/cobr/a",
//			"account_name":"faisal.ahmed","pubAvailable":"true","user_status":"host","browser_type":"unknown",
//			"intl_tollfree":"","hands_free_on_load":"true","browserType":"unknown","upload_docs":"true",
//			"chatpodClass":"Show","dms_app_root":"/medi/a","groupName":"all","productVersion":"5.5",
//			"console_params_initial_event_poll_time":"100","conference_id":"6f5c580e-1ca7-46ee-95e2-77a75526fe74",
//			"recording_active_at_start":"false","trialFlag":"","prodCode":"null","webappName":"dimdim",
//			"enterpriseNameForResBundle":"vcs1","streaming_urls_av_rtmpt_url":"rtmpt://207.207.3.85/",
//			"room_name":"default","server_max_attendee_videos":"2","assistant_enabled":"true",
//			"dataCacheId":"7c598826-33d9-11e0-aa6e-842b2b4f9719","enterprise_name":"vcs1","organizerEmail":"null",
//			"host_return_url":"","logoImageUrl":"/content/branding/saas/vcs1/all/faisal.ahmed/default/dimdim-logo.png",
//			"joinUrl":"http://vcs1.360training.com/faisal.ahmed/","pubEnabled":"false","roomName":"default",
//			"locale_code":"en_US","tollfree":"","presenter_av":"av",
//			"mailbox_doc_upload_url":"http://dimdim1.360training.com/upload/presentation/",
//			"elapsed_time_millis":"2","presenter_annotations":"A","subscriptionFlag":"",
//			"meeting_type":"C","show_invite_links":"true","attendee_pass_key":"","locale":"en_US",
//			"ppt_enabled":"true","public_chat_enabled":"true","max_meeting_time":"600","inPopup":false,
//			"streaming_urls_dtp_rtmpt_url":"http://207.207.3.85/","console_params_top_links_settings":"true",
//			"private_chat_enabled":"true","server_address":"http://localhost:40000/","console_params_top_links_info":"true",
//			"return_url_in_setings":"http://www.dimdim.com/","email_required":"false","default_logo":"/content/branding/saas/vcs1/all/faisal.ahmed/default/dimdim-logo.png","enterpriseName":"vcs1","sessionKey":"1e0c4589-525e-474b-a605-3c0807c160e1","presenter_pass_key":"1e0c4589-525e-474b-a605-3c0807c160e1-c",
//			"toll":"1-781-555-1234","console_params_client_ajax_http_call_timeout":"60000","productWebSite":"www.dimdim.com","console_params_top_links_about":"true","max_attendee_audios":"3","server_string_id":"saas","productName":"Dimdim Web Meeting","streaming_urls_audio_rtmp_url":"rtmp://207.207.3.85:1935/",
//			"key":"vcs1____all____faisal.ahmed____default","max_participants":"50","current_server_address":"http://localhost:40000/","window_name":"console","streaming_urls_audio_rtmpt_url":"rtmpt://207.207.3.85/","res_to_start":"cob","rejoinURL":"http://vcs1.360training.com/faisal.ahmed/","standAloneCheck":"false",
//			"console_params_max_event_poll_failures":"30","console_params_max_participants":"50","recording_enabled":"true",
//			"large_video_supported":"true","pubInstall":"false","startDate":"February 8, 2011 5:16:41 PM CST","organizer_email":"null","cob_enabled":"true","organizerName":"L2ZhaXNhbC5haG1lZA==","streaming_urls_whiteboard_rtmpt_url":"rtmpt://207.207.3.85/","group_name":"all",
//			"console_params_top_links_sign_out":"true","user_mood":"normal","osType":"unknown","streaming_urls_dtp_url":"http://207.207.3.85/",
//			"webapp_name":"dimdim","pubBuildVersion2":"0","show_send_start_notification":"false","reloadConsole":"false","user_id":"0","server_max_attendee_audios":"3",
//			"streaming_urls_av_rtmp_url":"rtmp://207.207.3.85:1935/","skipAll":"false","actionId":"host","pubMajorVersion":"5","console_params_show_system_chat_messages":"true","user_name":"L2ZhaXNhbC5haG1lZA==","attendee_annotations":"A","meeting_name":"ZmFpc2FsLmFobWVk","user_role":"role.activePresenter",
//			"console_params_top_links_dialinfo":"true","serverStringId":"saas","streaming_session_key":"bfdcdd0a-cf6f-4563-9494-bebb1c7e2447",
//			"pubBuildVersion":"0","img_quality":"high","pubMinorVersion":"1","console_params_top_links_feedback":"true",
//			"console_params_show_powered_by_dimdim":"true","browser_version":"unknown","publicChat":"true","allow_host_invites":"true","lobby_enabled":"true","contentServerUrl":"http://dimdim1.360training.com/","return_url":"http://www.dimdim.com/","start_time_on_server":"1297207001072","att_pass_code":"324725",
//			"confKey":"vcs1____all____faisal.ahmed____default","console_params_regular_event_poll_time":"250","enterprise_prefix":"____","userType":"free",
//			"doc_enabled":"true","show_phone_info":"true","default_url":"http://www.dimdim.com/ads/Share_Wait-46.html",
//			"streaming_urls_whiteboard_rtmp_url":"rtmp://207.207.3.85:1935/","session_key":"1e0c4589-525e-474b-a605-3c0807c160e1","console_params_use_debug_panel":"true","productCode":"Premium","clientWindowId":"cf2f6499-a389-43f8-ba9b-b6b0c61658af",
//			"accountName":"faisal.ahmed","intl_toll":"1-781-555-1234","subject":"","console_params_max_meeting_time":"600",
//			"ui_skin":"default","default_logo_text":"RGltZGltIFdlYiBNZWV0aW5n","mod_pass_code":"324725",
//			"fullscreen_enabled":"true","feedback_email":"feedback@dimdim.com","allow_attendee_invites":"true","browserVersion":"unknown","uiSkin":"default",
//			"joinURL":"http%3A%2F%2Fvcs1.360training.com%2Ffaisal.ahmed%2F","part_list_enabled":"true",
//			"clientId":"7c598826-33d9-11e0-aa6e-842b2b4f9719","dashboardURL":"/dashboard/all/faisal.ahmed",
//			"console_params_top_panel_visible":"true","publisher_enabled":"true","streaming_urls_dtp_rtmp_url":"http://207.207.3.85/",
//			"default_about_logo":"/content/branding/saas/vcs1/all/faisal.ahmed/default/dimdim-logo-about.png"}}	
//	}
		JSONObject response = jsonResponse.getJSONObject("response");
		sessionKey = response.getString("session_key");
		roomName = response.getString("room_name");
		accountName = response.getString("accountName");
		clientId = response.getString("clientId");
	}
	private String sessionKey;
	private String accountName;
	private String  clientId;
	private String roomName;
	
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
}
