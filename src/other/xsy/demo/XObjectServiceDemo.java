package other.xsy.demo;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice.Info;

import com.rkhd.platform.sdk.ScriptTrigger;
import com.rkhd.platform.sdk.data.model.RuntimeTest1__c;
import com.rkhd.platform.sdk.exception.ApiEntityServiceException;
import com.rkhd.platform.sdk.log.Logger;
import com.rkhd.platform.sdk.log.LoggerFactory;
import com.rkhd.platform.sdk.model.BatchOperateResult;
import com.rkhd.platform.sdk.model.OperateResult;
import com.rkhd.platform.sdk.model.QueryResult;
import com.rkhd.platform.sdk.model.XObject;
import com.rkhd.platform.sdk.param.ScriptTriggerParam;
import com.rkhd.platform.sdk.param.ScriptTriggerResult;
import com.rkhd.platform.sdk.service.XObjectService;


public class XObjectServiceDemo{
	private static final Logger log = LoggerFactory.getLogger();
	
	public void addDemo(){
		//RuntimeTest1__c是我测试租户下创建的一个自定义实体，存在于modelJar中
		RuntimeTest1__c runtimeTest1__c = new RuntimeTest1__c();
		runtimeTest1__c.setName("test");
		try{
			OperateResult result = XObjectService.instance().insert(runtimeTest1__c);
			if(!result.getSuccess()){
				log.error("create failed, errorMsg is : " + result.getErrorMessage());
			}else{
				log.info("create successful, dataId is " + result.getDataId());
			}
		}catch (ApiEntityServiceException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public void addListDemo(){
		List<XObject> dataList = new ArrayList<>(); 
		for(int i = 0; i < 3; ++i){
			RuntimeTest1__c runtimeTest1__c = new RuntimeTest1__c();
			runtimeTest1__c.setName("test" + i);
		}
		try{
			BatchOperateResult result = XObjectService.instance().insert(dataList);
			if(!result.getSuccess()){
				log.error("create batch failed, errorMsg is : " + result.getErrorMessage());
			}else{
				if(result.getOperateResults() != null){
					for(OperateResult operateResult : result.getOperateResults()){
						if(operateResult.getSuccess()){
							log.info("create successful, id is " + operateResult.getDataId());
						}else{
							log.error("create failed, errorMsg is : " + operateResult.getErrorMessage());
						}
					}
				}
			}
		}catch (ApiEntityServiceException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public void queryDemo(){
		String sql = "select id, name from RuntimeTest1__c";
		try{
			QueryResult<RuntimeTest1__c> queryResult = XObjectService.instance().query(sql);
			if(!queryResult.getSuccess()){
				log.error("query failed, errorMsg is " + queryResult.getErrorMessage());
			}else{
				List<RuntimeTest1__c> records = queryResult.getRecords();
				//deal with records
			}
		}catch (ApiEntityServiceException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public void deleteDemo(){
		RuntimeTest1__c runtimeTest1__c = new RuntimeTest1__c();
		runtimeTest1__c.setId(123L);
		try{
			OperateResult operateResult = XObjectService.instance().delete(runtimeTest1__c);
			if(!operateResult.getSuccess()){
				log.error("delete failed, errorMsg is " + operateResult.getErrorMessage());
			}else{
				log.info("delete successful");
			}
		}catch (ApiEntityServiceException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public void deleteListDemo(){
		List<RuntimeTest1__c> deleteList = new ArrayList<>();
		for(int i = 0; i < 3; ++i){
			RuntimeTest1__c runtimeTest1__c = new RuntimeTest1__c();
			runtimeTest1__c.setId(1000L + i);
			deleteList.add(runtimeTest1__c);
		}
		try{
			BatchOperateResult batchOperateResult = XObjectService.instance().delete(deleteList);
			if(!batchOperateResult.getSuccess()){
				log.error("delete batch failed, errorMsg is : " + batchOperateResult.getErrorMessage());
			}else{
				if(batchOperateResult.getOperateResults() != null){
					for(int i = 0; i < batchOperateResult.getOperateResults().size(); ++i){
						OperateResult operateResult = batchOperateResult.getOperateResults().get(i);
						if(operateResult.getSuccess()){
							log.info("delete successful, id is " + deleteList.get(i).getId());
						}else{
							log.error("delete failed, errorMsg is : " + operateResult.getErrorMessage());
						}
					}
				}
			}
		}catch (ApiEntityServiceException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public void updateDemo(){
		RuntimeTest1__c runtimeTest1__c = new RuntimeTest1__c();
		runtimeTest1__c.setId(123L);
		runtimeTest1__c.setName("updateName");
		try{
			OperateResult operateResult = XObjectService.instance().update(runtimeTest1__c);
			if(!operateResult.getSuccess()){
				log.error("update failed, errorMsg is " + operateResult.getErrorMessage());
			}else{
				log.info("update successful");
			}
		}catch (ApiEntityServiceException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public void updateBatchDemo(){
		List<RuntimeTest1__c> updateList = new ArrayList<>();
		for(int i = 0; i < 3; ++i){
			RuntimeTest1__c runtimeTest1__c = new RuntimeTest1__c();
			runtimeTest1__c.setId(1000L + i);
			runtimeTest1__c.setName("UpdateName" + i);
			updateList.add(runtimeTest1__c);
		}
		try{
			BatchOperateResult batchOperateResult = XObjectService.instance().update(updateList);
			if(!batchOperateResult.getSuccess()){
				log.error("update batch failed, errorMsg is : " + batchOperateResult.getErrorMessage());
			}else{
				if(batchOperateResult.getOperateResults() != null){
					for(int i = 0; i < batchOperateResult.getOperateResults().size(); ++i){
						OperateResult operateResult = batchOperateResult.getOperateResults().get(i);
						if(operateResult.getSuccess()){
							log.info("update successful, id is " + updateList.get(i).getId());
						}else{
							log.error("update failed, errorMsg is : " + operateResult.getErrorMessage());
						}
					}
				}
			}
		}catch (ApiEntityServiceException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	

}
