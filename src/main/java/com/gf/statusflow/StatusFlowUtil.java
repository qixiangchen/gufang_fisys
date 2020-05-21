package com.gf.statusflow;

import java.io.*;
import java.util.*;
import org.dom4j.*;
import org.dom4j.io.*;

public class StatusFlowUtil
{
	public static HashMap getData(String xmlData)
	{
		HashMap hp = new HashMap();
		if(xmlData == null)
			return hp;
		try
		{
			ByteArrayInputStream bais = new ByteArrayInputStream(xmlData.getBytes());
			SAXReader saxr = new SAXReader();
			Document doc = saxr.read(bais);
			Element root = doc.getRootElement();
			List eleLst = root.elements();
			if(eleLst != null)
			{
				for(Iterator it=eleLst.iterator();it.hasNext();)
				{
					Element ele = (Element)it.next();
					String qname = ele.getQualifiedName();
					String data = (String)ele.getData();
					hp.put(qname,data);
				}
			}

			bais.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return hp;
	}
	
}