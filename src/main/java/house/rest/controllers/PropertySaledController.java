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
import org.apache.ibatis.session.SqlSessionFactory;
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
import house.domain.mapper.PropertyDailySignedMapper;
import house.rest.RestResponse;
import house.rest.util.DateUtils;

@Controller("PropertySaledController")

public class PropertySaledController {

	static Logger logger = LoggerFactory.getLogger(PropertySaledController.class);

//	TimeZone zone = null;
	//SimpleDateFormat ISO_time_format = null;

	List<String> sidsOfHZ;
	List<String> sidsOfXS;
	List<String> sidsOfYH;

	@Autowired(required = true)
	private SqlSessionFactory sqlSessionFactory;

	@Autowired(required = true)
	private SqlSession sqlSession;

	public PropertySaledController() throws Exception {

		System.out.println("PropertySaledController created .");

		//zone = TimeZone.getTimeZone("GMT+8"); // 时区

		//ISO_time_format = new SimpleDateFormat("yyyy-MM-dd");
	//	ISO_time_format.setTimeZone(zone);

		sidsOfHZ = new LinkedList<String>();
		sidsOfHZ.add("33");
		sidsOfHZ.add("330102");
		sidsOfHZ.add("330103");
		sidsOfHZ.add("330104");
		sidsOfHZ.add("330105");
		sidsOfHZ.add("330103");
		sidsOfHZ.add("330108");
		sidsOfHZ.add("330110");
		sidsOfHZ.add("330186");
		sidsOfHZ.add("330231");

		sidsOfXS = new LinkedList<String>();
		sidsOfXS.add("330181");

		sidsOfYH = new LinkedList<String>();
		sidsOfYH.add("330184");
	}

	@SuppressWarnings("restriction")
	@PostConstruct
	public void init() {
		System.out.println("DailySaleController init ...");

		// DOMConfigurator.configure(HouseController.class.getResource("/conf/log4j.xml"));

	}

	@SuppressWarnings("restriction")
	@PreDestroy
	void destroy() {

	}

	public static List<PropertyDailySigned> maxSignNumber(List<PropertyDailySigned> districtPropertyDailySigneds) {
		Map<String, PropertyDailySigned> map = new HashMap<String, PropertyDailySigned>();

		// List<PropertyDailySigned> districtPropertyDailySigneds=
		// mapper.queryPropertyDailySignedByDatePropertyTypeCode(signedDate,
		// propertyTypeCode);
		for (PropertyDailySigned districtPropertyDailySigned : districtPropertyDailySigneds) {
			PropertyDailySigned exist = map.get(districtPropertyDailySigned.propertyId);
			if (exist == null) {
				map.put(districtPropertyDailySigned.propertyId, districtPropertyDailySigned);
			} else {
				if (Integer.parseInt(districtPropertyDailySigned.signedNumber) > Integer.parseInt(exist.signedNumber)) {
					map.put(districtPropertyDailySigned.propertyId, districtPropertyDailySigned);
				}
			}
		}
		LinkedList<PropertyDailySigned> max = new LinkedList<PropertyDailySigned>();
		max.addAll(map.values());
		return max;
	}
	@RequestMapping(value = "/property/saled", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String handleDayPropertySaled(@RequestParam(value = "date", required = false) String date,HttpSession session) throws Exception {

		RestResponse response = null;
		try {
			if (date == null) {
				date = DateUtils.date(System.currentTimeMillis());
			}

			String cityName = "全部";

			PropertyDailySignedMapper mapper = sqlSession.getMapper(PropertyDailySignedMapper.class);

			List<PropertyDailySigned> propertyDailySigneds =mapper.queryPropertyDailySignedByDate(date);
		//	mapper.queryPropertyDailySignedByDatePropertyTypeCodes(signedDate, propertyTypeCodes)
		//	mapper.queryPropertyDailySignedByDateDistrict(signedDate, district);
		//  mapper.queryPropertyDailySignedByDatePropertyTypeCode(signedDate, propertyTypeCode);
			
			List<PropertyDailySigned> maxPropertyDailySigneds = maxSignNumber(propertyDailySigneds);
			
			response = RestResponse.createSuccessResponse();
			response.body=maxPropertyDailySigneds;
			
		} catch (Exception e) {
			logger.error("",e);
			response=RestResponse.createErrorResponse();
			response.msg=e.getMessage();
			response.body=e;
		}
		
		System.out.println(JSON.toJSONString(response, true));
		return JSON.toJSONString(response, true);

	}
	@RequestMapping(value = "/property/saled/district", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String handleDayPropertySaledDistrict(@RequestParam(value = "date", required = false) String date, @RequestParam(value = "district", required = true) String district,
			HttpSession session) throws Exception {

		RestResponse response = null;
		try {
			if (date == null) {
				date = DateUtils.date(System.currentTimeMillis());
			}

			String cityName = "全部";

			PropertyDailySignedMapper mapper = sqlSession.getMapper(PropertyDailySignedMapper.class);

			List<PropertyDailySigned> propertyDailySigneds =mapper.queryPropertyDailySignedByDateDistrict(date, district);
		//	mapper.queryPropertyDailySignedByDateDistrict(signedDate, district);
			//mapper.queryPropertyDailySignedByDatePropertyTypeCode(signedDate, propertyTypeCode);
			
			List<PropertyDailySigned> maxPropertyDailySigneds = maxSignNumber(propertyDailySigneds);
			
			response = RestResponse.createSuccessResponse();
			response.body=maxPropertyDailySigneds;
			
		} catch (Exception e) {
			logger.error("",e);
			response=RestResponse.createErrorResponse();
			response.msg=e.getMessage();
			response.body=e;
		}
		
		System.out.println(JSON.toJSONString(response, true));
		return JSON.toJSONString(response, true);

	}
	
	
}
