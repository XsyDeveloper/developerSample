package other.xsy.demo;

import com.alibaba.fastjson.JSONObject;
import com.rkhd.platform.sdk.ScriptTrigger;
import com.rkhd.platform.sdk.exception.ScriptBusinessException;
import com.rkhd.platform.sdk.exception.XsyHttpException;
import com.rkhd.platform.sdk.http.CommonData;
import com.rkhd.platform.sdk.http.CommonHttpClient;
import com.rkhd.platform.sdk.http.CommonResponse;
import com.rkhd.platform.sdk.http.handler.ResponseBodyHandlers;
import com.rkhd.platform.sdk.log.Logger;
import com.rkhd.platform.sdk.log.LoggerFactory;
import com.rkhd.platform.sdk.param.ScriptTriggerParam;
import com.rkhd.platform.sdk.param.ScriptTriggerResult;

//CommonHttpClient使用方法与RkhdHttpClinet类似，可用用来访问非销售易的api接口，这里以创建一个account为例，其余功能可以参考RkhdHttpClinetDemo中的代码
public class CommonHttpClientDemo{
	private static final Logger log = LoggerFactory.getLogger();
	String token = "Bearer 112233";
	
	
	public void addDemo() throws XsyHttpException{
		JSONObject account = new JSONObject();
		account.put("accountName", "test121");
		account.put("dimDepart", 222303);
		account.put("ownerId", 525646915076143L);
		account.put("entityType", "1029585");
		JSONObject record = new JSONObject();
		record.put("record", account);
		CommonData data = CommonData.newBuilder()
						.callType("POST")
						.callString("https://api.xiaoshouyi.com/data/v1/objects/account/create")
						.header("Authorization", token)
						.header("Content-Type", "application/json")
						.body(record.toJSONString())
						.build();
		CommonResponse<JSONObject> result = CommonHttpClient.instance().execute(data, ResponseBodyHandlers.ofJSON());
		if(result.getCode() != 200){
			log.error("error code is " + result.getCode());
		}
		JSONObject json = result.getData();
		if(json != null && json.containsKey("id")){
			log.info("create successful, id is " + json.getLong("id"));
		}else{
			log.error("create failed, result is " + json.toJSONString());
		}
	}



	@Override
	public ScriptTriggerResult execute(ScriptTriggerParam arg0) throws ScriptBusinessException {
		// TODO Auto-generated method stub
		try{
			addDemo();
		}catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(), e);
		}
		return new ScriptTriggerResult(arg0.getDataModelList());
	}

}
