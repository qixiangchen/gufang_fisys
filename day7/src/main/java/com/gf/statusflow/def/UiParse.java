package com.gf.statusflow.def;

import java.io.*;
import java.util.*;
import org.dom4j.*;
import org.dom4j.io.*;

public class UiParse
{
	private String defProcessId = null;
	private UiProcess uiProcess = new UiProcess();

	public UiParse(InputStream is,String defProcessId)
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

	public UiProcess getUiProcess()
	{
		return uiProcess;
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
			throw new Exception("工作流UI模板 "+defProcessId+" 解析错误,请检查."+e.getMessage());
		}
		return doc;
	}

	private void parseProcess(Element ele) throws Exception
	{
		try
		{
			Attribute att = ele.attribute("id");
			if(att != null)
				uiProcess.setId(att.getValue());
			att = ele.attribute("name");
			if(att != null)
				uiProcess.setName(att.getValue());
			att = ele.attribute("description");
			if(att != null)
				uiProcess.setDescription(att.getValue());
			List nodeLst = ele.elements("node");
			List<UiNode> uiNodeList = new ArrayList<UiNode>();
			for(Iterator it=nodeLst.iterator();it.hasNext();)
			{
				UiNode uiNode = new UiNode();
				Element nodeEle = (Element)it.next();
				Attribute idAtt = nodeEle.attribute("id");
				if(idAtt != null)
					uiNode.setId(idAtt.getValue());
				Attribute nameAtt = nodeEle.attribute("name");
				if(nameAtt != null)
					uiNode.setName(nameAtt.getValue());
				Attribute xAtt = nodeEle.attribute("x");
				if(xAtt != null)
					uiNode.setX(xAtt.getValue());
				Attribute yAtt = nodeEle.attribute("y");
				if(yAtt != null)
					uiNode.setY(yAtt.getValue());
				Attribute nameXAtt = nodeEle.attribute("namex");
				if(nameXAtt != null)
					uiNode.setNameX(nameXAtt.getValue());
				Attribute nameYAtt = nodeEle.attribute("namey");
				if(nameYAtt != null)
					uiNode.setNameY(nameYAtt.getValue());
				Attribute textXAtt = nodeEle.attribute("textx");
				if(textXAtt != null)
					uiNode.setTextX(textXAtt.getValue());
				Attribute textYAtt = nodeEle.attribute("texty");
				if(textYAtt != null)
					uiNode.setTextY(textYAtt.getValue());
				Attribute userNameAtt = nodeEle.attribute("username");
				if(userNameAtt != null)
					uiNode.setUserName(userNameAtt.getValue());
				List<String> toList = new ArrayList<String>();
				List toNodeLst = nodeEle.elements("tonode");
				for(Iterator it2=toNodeLst.iterator();it2.hasNext();)
				{
					Element toNode = (Element)it2.next();
					Attribute idAtt2 = toNode.attribute("id");
					if(idAtt2 != null)
						toList.add(idAtt2.getValue());
				}
				uiNode.setToNodeList(toList);
				uiNodeList.add(uiNode);
			}
			uiProcess.setNodeList(uiNodeList);

		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception(ele+" 解析错误,请检查."+e.getMessage());
		}
	}
}