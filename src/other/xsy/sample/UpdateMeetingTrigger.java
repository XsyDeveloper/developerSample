package other.xsy.sample;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.rkhd.platform.sdk.ScriptTrigger;
import com.rkhd.platform.sdk.exception.ScriptBusinessException;
import com.rkhd.platform.sdk.http.RkhdHttpClient;
import com.rkhd.platform.sdk.http.RkhdHttpData;
import com.rkhd.platform.sdk.model.DataModel;
import com.rkhd.platform.sdk.param.ScriptTriggerParam;
import com.rkhd.platform.sdk.param.ScriptTriggerResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 
 * 修改会议室预订信息
 * @author admin
 *
 */
public class UpdateMeetingTrigger implements ScriptTrigger{

	@Override
	public ScriptTriggerResult execute(ScriptTriggerParam scriptTriggerParam)
			throws ScriptBusinessException {
		List<DataModel> list = scriptTriggerParam.getDataModelList();
		Object huiyishiName = list.get(0).getAttribute("MeetingRoom__c");
		Object beginTime = list.get(0).getAttribute("beginTime__c");
		Object end_time = list.get(0).getAttribute("endTime__c");
		Object id = list.get(0).getAttribute("id");
		
		String begin = beginTime.toString();
		String end = end_time.toString();
		String meetingManagerIds = huiyishiName + "";
		
		if(DateUtil.getBetweenDay(new Date(Long.parseLong(begin)), new Date(Long.parseLong(end))) > 0){
			throw new ScriptBusinessException("begin time and end time difference of more than 1 days");
		}
		if(!DateUtil.isBefore(new Date(Long.parseLong(begin)), new Date(Long.parseLong(end)))){
			throw new ScriptBusinessException("begin time must before end time ");
		}
		
		String firstTable = getYuding(begin,end,scriptTriggerParam.getUserId(),-1L,meetingManagerIds,0,1,id.toString());
		
		JSONObject firstTableJson = JSONObject.fromObject(firstTable);
		JSONArray firstTableJsonArray = null;
		if(firstTableJson.get("records") != null){
			firstTableJsonArray = firstTableJson.getJSONArray("records");
		} else {
			firstTableJsonArray = new JSONArray();
		}
		
		if(firstTableJsonArray.size() > 0){
			throw new ScriptBusinessException("该会议室已有会议，请选择其他时间或其他会议室！");
		}
		ScriptTriggerResult scriptTriggerResult = new ScriptTriggerResult();
		scriptTriggerResult.setDataModelList(scriptTriggerParam.getDataModelList());
		return scriptTriggerResult;
	}
	
	
	private String getYuding(String begin,String end, Long userId, Long tenantId,String meetingManagerIds,int first,int size,String id){
		try{
			
			RkhdHttpClient rkhdHttpClient = new RkhdHttpClient();
			
			RkhdHttpData rkhdHttpData = new RkhdHttpData();
			rkhdHttpData.setCallString("/data/v1/query");
			rkhdHttpData.setCall_type("POST");
			
			
			String sql = "select id,ownerId,beginTime__c,endTime__c,MeetingRoom__c from bookMeetingRoom__c where id > 0 ";
			StringBuilder sb = new StringBuilder();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			if(meetingManagerIds != null && !meetingManagerIds.equals("")){
				 sb.append(" and MeetingRoom__c in( ").append(meetingManagerIds).append(") ");
			}
			if(begin != null && !begin.equals("") && end != null && !end.equals("")){
				
				sb.append(" and ((beginTime__c <= ").append(begin).append(" and endTime__c > ").append(begin).append(")");
				sb.append(" or (beginTime__c < ").append(end).append(" and endTime__c >= ").append(end).append(")");
				sb.append(" or (beginTime__c >= ").append(begin).append(" and endTime__c <= ").append(end).append(") )");
			}
			sb.append(" and id != ").append(id);
			sb.append(" order by beginTime__c,endTime__c");
			sb.append(" limit ").append(first).append(",").append(size);
			sql = sql + sb.toString();
			System.out.println(sql);
			rkhdHttpData.putFormData("q", sql);
			
			String s = rkhdHttpClient.performRequest(rkhdHttpData);
			return s;
		}catch(Exception e){
			return e.getMessage();
		}
	}

}
