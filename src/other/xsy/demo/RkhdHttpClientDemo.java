package other.xsy.demo;

import java.net.URLEncoder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rkhd.platform.sdk.ScriptTrigger;
import com.rkhd.platform.sdk.exception.ScriptBusinessException;
import com.rkhd.platform.sdk.http.RkhdHttpClient;
import com.rkhd.platform.sdk.http.RkhdHttpData;
import com.rkhd.platform.sdk.http.handler.ResponseBodyHandlers;
import com.rkhd.platform.sdk.log.Logger;
import com.rkhd.platform.sdk.log.LoggerFactory;
import com.rkhd.platform.sdk.param.ScriptTriggerParam;
import com.rkhd.platform.sdk.param.ScriptTriggerResult;

public class RkhdHttpClientDemo{
	private Logger logger = LoggerFactory.getLogger();
	
	public void queryDemo() {
		try{
			RkhdHttpData data = RkhdHttpData.newBuilder()
								.callType("GET")
								.callString("/rest/data/v2/query?q=" + URLEncoder.encode("select id from account where accountName = 'test'", "utf-8"))
								.build();
			JSONObject result = RkhdHttpClient.instance().execute(data, ResponseBodyHandlers.ofJSON());
			if(result == null || !result.containsKey("code") || result.getInteger("code") != 200){
				logger.error("query failed, result is " + String.valueOf(result));
				return;
			}
			int recordsSize = result.getJSONObject("result").getInteger("count");
			if(recordsSize == 0){
				logger.info("hava no account named 'test'");
			}else{
				JSONArray records = result.getJSONObject("result").getJSONArray("records");
				//account对象name唯一,所以直接获取第一条结果
				Long id = records.getJSONObject(0).getLong("id");
				logger.info("account id is " + id);
			}
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void addDemo(){
		try{
			JSONObject account = new JSONObject();
			account.put("accountName", "test1");
			account.put("dimDepart", 222303);
			account.put("ownerId", 525646915076143L);
			account.put("entityType", "1029585");
			JSONObject record = new JSONObject();
			record.put("record", account);
			RkhdHttpData data = RkhdHttpData.newBuilder()
								.callType("POST")
								.callString("/data/v1/objects/account/create")
								.body(record.toJSONString())
								.build();
			JSONObject result = RkhdHttpClient.instance().execute(data, ResponseBodyHandlers.ofJSON());
			if(result != null && result.containsKey("id")){
				Long id = result.getLong("id");
				logger.info("create account successful, id is " + id);
			}else{
				logger.error("create account failed, result is " + result.toJSONString());
			}
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}				
	}
	
	public void updateDemo(){
		try{
			JSONObject updateData = new JSONObject();
			updateData.put("id", 684245844590598L);
			updateData.put("accountName", "newAccountName");
			RkhdHttpData data = RkhdHttpData.newBuilder()
								.callType("POST")
								.callString("/data/v1/objects/account/update")
								.body(updateData.toJSONString())
								.build();
			JSONObject result = RkhdHttpClient.instance().execute(data, ResponseBodyHandlers.ofJSON());
			if(result != null && 0 != result.getInteger("status")){
				logger.error("update account failed, result is " + result.toJSONString());
			}else{
				logger.info("update account successful");
			}
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void deleteDemo(){
		try{
			RkhdHttpData data = RkhdHttpData.newBuilder()
								.callType("POST")
								.callString("/data/v1/objects/account/delete?id=684245844590598")
								.build();
			JSONObject result = RkhdHttpClient.instance().execute(data, ResponseBodyHandlers.ofJSON());
			if(result == null || 0 != result.getInteger("status")){
				logger.error("delete account failed, result is " + result.toJSONString());
			}else{
				logger.info("delete account successful");
			}
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
