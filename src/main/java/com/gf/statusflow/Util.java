package com.gf.statusflow;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;


public class Util {
	private static Logger log = LoggerFactory.getLogger(Util.class);
	private static Properties wfProp = null;
	private static ApplicationContext ctx = null;
	
	public static void setCtx(ApplicationContext actx)
	{
		ctx = actx;
	}
	
	public static Object getBean(Class clz)
	{
		return ctx.getBean(clz);
	}
	
	public static String fmtStr(Object obj)
	{
		if(obj == null)
			return "";
		return obj.toString();
	}
	
	public static String getString(javax.servlet.http.HttpServletRequest req,String name)
	{
		String value = fmtStr(req.getParameter(name));	
		return value;
	}
	
	public static Integer getInteger(javax.servlet.http.HttpServletRequest req,String name)
	{
		String value = fmtStr(req.getParameter(name));
		try
		{
			return Integer.parseInt(value);
		}
		catch(Exception e)
		{
			return new Integer(0);
		}
	}
	
	public static IUser getLoginUser()
	{
		Subject subject = SecurityUtils.getSubject();
		IUser user = (IUser)subject.getSession().getAttribute("loginuser");
		return user;
	}
	
	public static String getMD5(String pwd)
	{   
        byte[] source = pwd.getBytes();   
        String s = null;      
        char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};       
        try     
        {      
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");      
            md.update( source );      
            byte tmp[] = md.digest(); 
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
            }       
            s = new String(str);
        }catch( Exception e ) {      
           e.printStackTrace();      
        }      
        return s;      
    }
	
   public static Properties getLoginProp(String loginId)
   {
		StringTokenizer st = new StringTokenizer(loginId,":");
		String flag = "";
		if(st.hasMoreElements())
			flag = st.nextToken();
		String loginId2 = "";
		if(st.hasMoreElements())
			loginId2 = st.nextToken();
		Properties prop = new Properties();
		if(flag.endsWith("test"))
		{
			flag = flag.substring(0, flag.length()-4);
			prop.setProperty("testmode","test");
		}
		else
			prop.setProperty("testmode","no");
		prop.setProperty("flag", flag);
		prop.setProperty("loginid", loginId2);
		return prop;
	}

   public static Properties getRoleProp(String roleId)
   {
		StringTokenizer st = new StringTokenizer(roleId,":");
		String flag = "";
		if(st.hasMoreElements())
			flag = st.nextToken();
		String name = "";
		if(st.hasMoreElements())
			name = st.nextToken();
		Properties prop = new Properties();
		prop.setProperty("flag", flag);
		prop.setProperty("name", name);
		return prop;
	}
	   
	public static String getGuFangHome()
	{
		try
		{
			String gfpath = System.getProperty("GUFANG_HOME");
			if(gfpath != null && !"".equals(gfpath))
				return gfpath;
			
			ClassLoader cl = Util.class.getClassLoader();
			java.net.URL url = cl.getResource("com/gf/statusflow/Util.class");
			String path = url.getPath();
			String packagePath = "com/gf/statusflow/Util.class";
			int pos = path.indexOf(packagePath);
			String path2 = path.substring(0,pos)+"GUFANG_HOME/";
			return path2;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getLastWorkflow(String workflow)
	{
		try
		{
			if(wfProp == null)
			{
				String home = getGuFangHome();
				String fileName = home + "/workflow/wfconf.properties";
				FileInputStream fis = new FileInputStream(fileName);
				wfProp = new Properties();
				wfProp.load(fis);
				fis.close();
			}
		}
		catch(Exception e)
		{
			wfProp = new Properties();
			log.error("getLastWorkflow", e);
		}
		String wf = wfProp.getProperty(workflow);
		if("".equals(Util.fmtStr(wf)))
			return workflow;
		else
			return wf;
	}
}
