package com.gf.statusflow.def;

import java.io.*;
import java.util.*;
import org.dom4j.*;
import org.dom4j.io.*;

public class DefParse
{
	private String defProcessId = null;
	private DefProcess defProcess = new DefProcess();

	public DefParse(InputStream is,String defProcessId)
	{
		try
		{
			this.defProcessId = defProcessId;
			Document doc = getDocument(is);
			if(doc != null)
			{
				Element root = doc.getRootElement();
				parseProcess(root);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public DefProcess getDefProcess()
	{
		return defProcess;
	}

	private Document getDocument(InputStream is) throws Exception
	{
		Document doc = null;
		try
		{
			SAXReader saxr = new SAXReader();
			doc = saxr.read(is);
		}
		catch(Exception e)
		{
			throw new Exception("工作流模板 "+defProcessId+" 解析错误,请检查."+e.getMessage());
		}
		return doc;
	}

	private void parseProcess(Element ele) throws Exception
	{
		try
		{
			Attribute att = ele.attribute("id");
			if(att != null)
				defProcess.setId(att.getValue());
			att = ele.attribute("name");
			if(att != null)
				defProcess.setName(att.getValue());
			att = ele.attribute("description");
			if(att != null)
				defProcess.setDescription(att.getValue());
			att = ele.attribute("uiwf");
			if(att != null)
				defProcess.setUiwf(att.getValue());
			Element extEle = ele.element("extension");
			if(extEle != null)
			{
				List extLst = extEle.elements("property");
				if(extLst != null)
				{
					for(Iterator it=extLst.iterator();it.hasNext();)
					{
						Element propEle = (Element)it.next();
						Attribute propAtt1 = propEle.attribute("key");
						Attribute propAtt2 = propEle.attribute("value");
						if(propAtt1 != null && propAtt2 != null)
						{
							defProcess.addExtMap(propAtt1.getValue(),propAtt2.getValue());
						}
					}
				}
			}
			List lst = ele.elements("status");
			if(lst != null)
			{
				for(Iterator it=lst.iterator();it.hasNext();)
				{
					Element ele2 = (Element)it.next();
					parseStatus(ele2);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception(ele+" 解析错误,请检查."+e.getMessage());
		}
	}

	private void parseStatus(Element ele) throws Exception
	{
		try
		{
			DefStatus defStatus = new DefStatus();
			Attribute att = ele.attribute("id");
			if(att != null)
				defStatus.setId(att.getValue());
			att = ele.attribute("name");
			if(att != null)
				defStatus.setName(att.getValue());
			att = ele.attribute("type");
			if(att != null)
				defStatus.setType(att.getValue());
			att = ele.attribute("isstart");
			if(att != null)
			{
				if("true".equals(att.getValue()))
					defStatus.setIsStart(true);
			}
			att = ele.attribute("workflow");
			if(att != null)
				defStatus.setWorkflow(att.getValue());
			att = ele.attribute("condition");
			if(att != null)
				defStatus.setCondition(att.getValue());
			att = ele.attribute("excluderole");
			if(att != null)
				defStatus.setExcludeRole(att.getValue());
			att = ele.attribute("batchid");
			if(att != null)
				defStatus.setBatchId(att.getValue());
			att = ele.attribute("url");
			if(att != null)
				defStatus.setUrl(att.getValue());
			
			Element ele2 = ele.element("tostatuses");
			List lst = ele2.elements("tostatus");
			if(lst != null)
			{
				for(Iterator it=lst.iterator();it.hasNext();)
				{
					Element ele3 = (Element)it.next();
					parseToStatus(defStatus,ele3);
				}
			}
			Element ele3 = ele.element("actors");
			List lst3 = ele3.elements("actor");
			if(lst3 != null)
			{
				for(Iterator it=lst3.iterator();it.hasNext();)
				{
					Element ele4 = (Element)it.next();
					parseToActor(defStatus,ele4);
				}
			}
			
			Element appEle = ele.element("appdata");
			if(appEle != null)
			{
				Element fldsEle = appEle.element("fields");
				if(fldsEle != null)
				{
					List fldLst = fldsEle.elements("field");
					for(Iterator it=fldLst.iterator();it.hasNext();)
					{
						Element fldEle = (Element)it.next();
						parseField(defStatus,fldEle);
					}
				}
				Element btnsEle = appEle.element("buttons");
				if(btnsEle != null)
				{
					List btnLst = btnsEle.elements("button");
					for(Iterator it=btnLst.iterator();it.hasNext();)
					{
						Element btnEle = (Element)it.next();
						parseButton(defStatus,btnEle);
					}
				}
			}
			
			defProcess.addStatusLst(defStatus);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception(ele+" 解析错误,请检查."+e.getMessage());
		}
	}

	private void parseToStatus(DefStatus defStatus,Element ele) throws Exception
	{
		try
		{
			DefToStatus defToStatus = new DefToStatus();
			Attribute att = ele.attribute("id");
			if(att != null)
				defToStatus.setId(att.getValue());
			att = ele.attribute("name");
			if(att != null)
				defToStatus.setName(att.getValue());
			att = ele.attribute("condition");
			if(att != null)
				defToStatus.setCondition(att.getValue());
			att = ele.attribute("invoke");
			if(att != null)
				defToStatus.setInvoke(att.getValue());
			att = ele.attribute("invokecommand");
			if(att != null)
				defToStatus.setInvokeCommand(att.getValue());
			att = ele.attribute("type");
			if(att != null)
				defToStatus.setType(att.getValue());
			defStatus.addToStatusLst(defToStatus);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception(ele+" 解析错误,请检查."+e.getMessage());
		}
	}

	private void parseToActor(DefStatus defStatus,Element ele) throws Exception
	{
		try
		{
			DefActor defActor = new DefActor();
			Attribute att = ele.attribute("id");
			if(att != null)
				defActor.setId(att.getValue());
			att = ele.attribute("type");
			if(att != null)
				defActor.setType(att.getValue());
			att = ele.attribute("loginid");
			if(att != null)
				defActor.setLoginId(att.getValue());
			att = ele.attribute("autoselect");
			if(att != null)
				defActor.setAutoSelect(att.getValue());
			att = ele.attribute("condition");
			if(att != null)
				defActor.setCondition(att.getValue());
			defStatus.addActorLst(defActor);
			List lst = ele.elements("select");
			if(lst != null)
			{
				for(Iterator it=lst.iterator();it.hasNext();)
				{
					Element ele2 = (Element)it.next();
					parseToActorSelect(defActor,ele2);
				}
			}
				
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception(ele+" 解析错误,请检查."+e.getMessage());
		}
	}
	
	private void parseToActorSelect(DefActor defActor,Element ele) throws Exception
	{
		try
		{
			DefActorSelect selActor = new DefActorSelect();
			Attribute att = ele.attribute("id");
			if(att != null)
				selActor.setId(att.getValue());
			att = ele.attribute("type");
			if(att != null)
				selActor.setType(att.getValue());
			att = ele.attribute("loginid");
			if(att != null)
				selActor.setLoginId(att.getValue());
			att = ele.attribute("switchvalue");
			if(att != null)
				selActor.setSwitchValue(att.getValue());
			defActor.addSelectLst(selActor);
				
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception(ele+" 解析错误,请检查."+e.getMessage());
		}
	}
	
	private void parseField(DefStatus defStatus,Element ele) throws Exception
	{
		try
		{
			DefField fld = new DefField();
			Attribute att = ele.attribute("id");
			if(att != null)
				fld.setId(att.getValue());
			att = ele.attribute("privilege");
			if(att != null)
				fld.setPrivilege(att.getValue());
			defStatus.addFieldLst(fld);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception(ele+" 解析错误,请检查."+e.getMessage());
		}
	}
	
	private void parseButton(DefStatus defStatus,Element ele) throws Exception
	{
		try
		{
			DefButton btn = new DefButton();
			Attribute att = ele.attribute("id");
			if(att != null)
				btn.setId(att.getValue());
			att = ele.attribute("privilege");
			if(att != null)
				btn.setPrivilege(att.getValue());
			att = ele.attribute("name");
			if(att != null)
				btn.setName(att.getValue());
			att = ele.attribute("onclick");
			if(att != null)
				btn.setOnclick(att.getValue());
			att = ele.attribute("tableid");
			if(att != null)
				btn.setTableid(att.getValue());
			att = ele.attribute("queryshow");
			if(att != null)
				btn.setQueryshow(att.getValue());
			defStatus.addButtonLst(btn);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception(ele+" 解析错误,请检查."+e.getMessage());
		}
	}
}