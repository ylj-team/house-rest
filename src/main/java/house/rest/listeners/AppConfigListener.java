package house.rest.listeners;

import java.io.IOException;
import java.io.InputStream;
import java.security.Security;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.xml.DOMConfigurator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class AppConfigListener implements ServletContextListener{

	static Logger logger=LoggerFactory.getLogger(AppConfigListener.class);

	static	public SqlSessionFactory  sqlSessionFactory;
	
	public void contextInitialized(ServletContextEvent sce) {
		
		System.out.println("AppConfigListener contextInitialized init ...");
	/*
		System.out.println("log4j init ...");
		DOMConfigurator.configure(AppConfigListener.class.getResource("/conf/log4j.xml"));
		
	    String resource = "mybatis/mybatis-config.xml";
		try {
			InputStream inputStream = Resources.getResourceAsStream(resource);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);    	
			System.out.println("mybatis init ...");
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // 创建SqlSessionFcatory
         
		
	*/
	}
	
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("AppConfigListener contextDestroyed init ...");

	}	
}
