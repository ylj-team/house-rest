package house.rest.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import house.domain.PropertyDailySigned;
import house.domain.UserSubscribedProperty;
import house.domain.mapper.PropertyDailySignedMapper;
import house.domain.mapper.PropertyMapper;
import house.domain.mapper.UserSubscribedPropertyMapper;
import house.rest.RestResponse;
import house.rest.util.DateUtils;



@Controller("UserSubscribtionController")

public class UserSubscribtionController {
	static Logger logger = LoggerFactory.getLogger(UserSubscribtionController.class);

	@Autowired(required = true)
	private SqlSession sqlSession;


	public UserSubscribtionController() throws Exception {

		System.out.println("UserSubscribtionController created .");

	}

	@SuppressWarnings("restriction")
	@PostConstruct
	public void init() {
		System.out.println("UserSubscribtionController init ...");

		// DOMConfigurator.configure(HouseController.class.getResource("/conf/log4j.xml"));

	}

	@SuppressWarnings("restriction")
	@PreDestroy
	void destroy() {

	}
	
	@RequestMapping(value = "/user/subscribtion", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String handleUserSubscribtionGet(@RequestParam(value = "account", required = true) String account,
			HttpSession session) throws Exception {
		
		//String loginedAccount = (String) session.getAttribute("account");
		//session.setAttribute("account", login);
		RestResponse response=null;
		
		try{
			UserSubscribedPropertyMapper userSubscribedPropertyMapper=sqlSession.getMapper(UserSubscribedPropertyMapper.class);
			List<UserSubscribedProperty> subscribtions=userSubscribedPropertyMapper.querySubscriptionOfAccount(account);
			response=RestResponse.createSuccessResponse();
			response.body=subscribtions;
			
		}catch(Exception e){
			logger.error("",e);
			response=RestResponse.createErrorResponse();			
			response.msg=e.getMessage();
			response.body=e;
		}
		
		System.out.println(JSON.toJSONString(response, true));
		return JSON.toJSONString(response, true);
		
	}
	
	 class UserSubscribedProperty2 {
		
			public String propertyId;
			public String propertyName;
			public long subscriptTime;
			public String signedNumber;
			
			public UserSubscribedProperty2(UserSubscribedProperty userSubscribedProperty){
				this.propertyId=userSubscribedProperty.propertyId;
				this.propertyName=userSubscribedProperty.propertyName;
				this.subscriptTime=userSubscribedProperty.subscriptTime;
				this.signedNumber="0";
			}
	 }
	
	@RequestMapping(value = "/user/subscribtion_and_signed", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String handleUserSubscribtionAndSignedGet(@RequestParam(value = "account", required = true) String account,
			HttpSession session) throws Exception {
		
		//String loginedAccount = (String) session.getAttribute("account");
		//session.setAttribute("account", login);
		RestResponse response=null;
		
		try{
			UserSubscribedPropertyMapper userSubscribedPropertyMapper=sqlSession.getMapper(UserSubscribedPropertyMapper.class);
			PropertyDailySignedMapper signedMapper = sqlSession.getMapper(PropertyDailySignedMapper.class);

			List<UserSubscribedProperty> subscribtions=userSubscribedPropertyMapper.querySubscriptionOfAccount(account);
			
			
			List<UserSubscribedProperty2> subscribtion2s=new LinkedList<UserSubscribedProperty2>();
			String todayDate = DateUtils.date(System.currentTimeMillis());
			logger.info("todayDate:"+todayDate);
			for(UserSubscribedProperty subscribtion:subscribtions){
				UserSubscribedProperty2 subscribtion2=new UserSubscribedProperty2(subscribtion);
				subscribtion2.signedNumber=maxSignNumber(signedMapper.queryPropertyDailySignedByDatePropertyId(todayDate, subscribtion2.propertyId));
				subscribtion2s.add(subscribtion2);
			}
			
			response=RestResponse.createSuccessResponse();
			response.body=subscribtion2s;
			
		}catch(Exception e){
			logger.error("",e);
			response=RestResponse.createErrorResponse();			
			response.msg=e.getMessage();
			response.body=e;
		}
		
		System.out.println(JSON.toJSONString(response, true));
		return JSON.toJSONString(response, true);
		
	}

	public static String maxSignNumber(List<PropertyDailySigned> districtPropertyDailySigneds) {
		int maxSignNumber=0;
		logger.info("districtPropertyDailySigneds.size():"+districtPropertyDailySigneds.size());
		for (PropertyDailySigned districtPropertyDailySigned : districtPropertyDailySigneds) {
			int signNumber=Integer.parseInt(districtPropertyDailySigned.signedNumber);
			if(signNumber>maxSignNumber){
				maxSignNumber=signNumber;
			}
		}
		return maxSignNumber+"";
	}

	@RequestMapping(value = "/user/subscribtion", method = RequestMethod.DELETE, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String handleUserSubscribtionDelete(
			@RequestParam(value = "account", required = true) String account,
			@RequestParam(value = "propertyId", required = true) String propertyId,
			HttpSession session) throws Exception {
		
		//String loginedAccount = (String) session.getAttribute("account");
		//session.setAttribute("account", login);
		RestResponse response=null;
		try{
			UserSubscribedPropertyMapper userSubscribedPropertyMapper=sqlSession.getMapper(UserSubscribedPropertyMapper.class);	
			userSubscribedPropertyMapper.deleteSubscriptions(account, propertyId);
			response=RestResponse.createSuccessResponse();
			
		}catch(Exception e){
			logger.error("",e);
			response=RestResponse.createErrorResponse();			
			response.msg=e.getMessage();
			response.body=e;
		}
		
		System.out.println(JSON.toJSONString(response, true));
		return JSON.toJSONString(response, true);
		
	}
	
	@RequestMapping(value = "/user/subscribtion", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String handleUserSubscribtionPost(
			@RequestParam(value = "account", required = true) String account,
			@RequestParam(value = "propertyId", required = true) String propertyId,
			HttpSession session) throws Exception {
		
		//String loginedAccount = (String) session.getAttribute("account");
		//session.setAttribute("account", login);
		RestResponse response=null;
		try{
			UserSubscribedPropertyMapper userSubscribedPropertyMapper=sqlSession.getMapper(UserSubscribedPropertyMapper.class);	
			PropertyMapper propertyMapper=sqlSession.getMapper(PropertyMapper.class);	
			
			List<UserSubscribedProperty> properties=new LinkedList<UserSubscribedProperty> ();
			UserSubscribedProperty subscribed=new UserSubscribedProperty();
			subscribed.account=account;
			subscribed.propertyId=propertyId;
			subscribed.propertyName=propertyMapper.queryPropertyByPropertyId(propertyId).propertyName;
			subscribed.subscriptTime=System.currentTimeMillis();
			properties.add(subscribed);
			userSubscribedPropertyMapper.insertSubscriptions(properties);
			
			response=RestResponse.createSuccessResponse();
			
		}catch(Exception e){
			logger.error("",e);
			response=RestResponse.createErrorResponse();			
			response.msg=e.getMessage();
			response.body=e;
		}
		
		System.out.println(JSON.toJSONString(response, true));
		return JSON.toJSONString(response, true);
		
	}
}
