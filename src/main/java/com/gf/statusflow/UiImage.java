package com.gf.statusflow;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gf.statusflow.def.DefWorkItem;
import com.gf.statusflow.def.UiNode;
import com.gf.statusflow.def.UiParse;
import com.gf.statusflow.def.UiProcess;

@Component
public class UiImage {
	@Autowired
	private IOrgModel orgMng = null;
	
	public boolean isExist(List<DefWorkItem> dwiList,String nodeId)
	{
		for(DefWorkItem dwi:dwiList)
		{
			if(dwi.getStatusId().equals(nodeId))
				return true;
		}
		return false;
	}
	
	public byte[] genImage(String processId,List<DefWorkItem> dwiList,int width,int height)
	{
		byte[] data = null;
		try
		{
			String file = Util.getGuFangHome()+"workflowui/"+processId+".xml";
			FileInputStream fis = new FileInputStream(file);
			UiParse uiParse = new UiParse(fis,processId);
			UiProcess uiProc = uiParse.getUiProcess();
			
			BufferedImage image = new BufferedImage(width,height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = image.getGraphics();
			g.setColor(new Color(255, 255, 255));
			g.fillRect(0, 0, width, height);
			List<UiNode> nodeList = uiProc.getNodeList();
			for(UiNode node:nodeList)
			{
				String x = node.getX();
				Integer iX = 0;
				try
				{
					iX = Integer.parseInt(x);
				}
				catch(Exception ig)
				{
					iX = 0;
				}
				String y = node.getY();
				Integer iY = 0;
				try
				{
					iY = Integer.parseInt(y);
				}
				catch(Exception ig)
				{
					iY = 0;
				}
				String nameX = node.getNameX();
				Integer iNameX = 0;
				try
				{
					iNameX = Integer.parseInt(nameX);
				}
				catch(Exception ig)
				{
					iNameX = 0;
				}
				String nameY = node.getNameY();
				Integer iNameY = 0;
				try
				{
					iNameY = Integer.parseInt(nameY);
				}
				catch(Exception ig)
				{
					iNameY = 0;
				}
				String textX = node.getTextX();
				Integer iTextX = 0;
				try
				{
					iTextX = Integer.parseInt(textX);
				}
				catch(Exception ig)
				{
					iTextX = 0;
				}
				String textY = node.getTextY();
				Integer iTextY = 0;
				try
				{
					iTextY = Integer.parseInt(textY);
				}
				catch(Exception ig)
				{
					iTextY = 0;
				}
				String name = node.getName();
				int cnt = passedTime(dwiList,node.getId());
				String txt = getText(dwiList,node.getId());		
				if(cnt>0)
				{
					g.setColor(new Color(255, 0, 0));
					g.fillOval(iX, iY, 10, 10);
				}
				else
				{
					g.setColor(new Color(0, 255, 0));
					g.drawOval(iX, iY, 10, 10);
				}
				g.setColor(new Color(0, 0, 0));
				g.drawString(name, iNameX,iNameY);
				if(!"".equals(txt))
				{
					g.setColor(new Color(255, 0, 0));
					g.drawString(txt, iTextX,iTextY);
				}
				else
				{
					g.setColor(new Color(0, 255, 0));
					g.drawString(Util.fmtStr(node.getUserName()), iTextX,iTextY);
				}
				g.setColor(new Color(0, 0, 0));
				List<String> toNodeList = node.getToNodeList();					
				for(String toNode:toNodeList)
				{
					for(UiNode node2:nodeList)
					{
						if(node2.getId().equals(toNode))
						{
							//if(isExist(dwiList,node.getId()) && isExist(dwiList,toNode))
							//	g.setColor(new Color(255, 0, 0));
							//else
							//	g.setColor(new Color(0, 0, 0));
							drawLine(g,iX,iY,node2);
						}				
					}
				}
			}
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(image, "png", os);
	
			data = os.toByteArray();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return data;
	}
	
	private void drawLine(Graphics g,Integer iX,Integer iY,UiNode node)
	{
		System.out.println("node.name="+node.getName());
		String x2 = node.getX();
		Integer iX2 = 0;
		try
		{
			iX2 = Integer.parseInt(x2);
		}
		catch(Exception ig)
		{
			iX2 = 0;
		}
		String y2 = node.getY();
		Integer iY2 = 0;
		try
		{
			iY2 = Integer.parseInt(y2);
		}
		catch(Exception ig)
		{
			iY2 = 0;
		}
		if(iX2.intValue()>iX.intValue())
		{
			g.drawLine(iX+10, iY+5, iX2, iY2+5);

			Integer arrowX1 = iX+10;
			Integer arrowY1 = iY+5;
			Integer arrayX2 = iX2;
			Integer arrayY2 = iY2+5;
			g.setColor(new Color(0, 0, 255));
			drawArrow(g,arrowX1,arrowY1,arrayX2,arrayY2);
			g.setColor(new Color(0, 0, 0));
		}
		if(iX2.intValue()==iX.intValue())
		{
			if(iY2.intValue()>iY.intValue())
			{
				g.drawLine(iX+5, iY+10, iX2+5, iY2);

				Integer arrowX1 = iX+5;
				Integer arrowY1 = iY+10;
				Integer arrayX2 = iX2+5;
				Integer arrayY2 = iY2;
				g.setColor(new Color(0, 0, 255));
				drawArrow(g,arrowX1,arrowY1,arrayX2,arrayY2);
				g.setColor(new Color(0, 0, 0));
			}
			else if(iY.intValue()==iY2.intValue())
			{}
			else
			{
				g.drawLine(iX+5, iY+10, iX2+5, iY2+10);
				Integer arrowX1 = iX+5;
				Integer arrowY1 = iY+10;
				Integer arrayX2 = iX2+5;
				Integer arrayY2 = iY2+10;
				g.setColor(new Color(0, 0, 255));
				drawArrow(g,arrowX1,arrowY1,arrayX2,arrayY2);
				g.setColor(new Color(0, 0, 0));
			}
		}
		if(iX2.intValue()<iX.intValue())
		{
			g.drawLine(iX, iY+5, iX2+10, iY2+5);

			Integer arrowX1 = iX;
			Integer arrowY1 = iY+5;
			Integer arrayX2 = iX2+10;
			Integer arrayY2 = iY2+5;
			g.setColor(new Color(0, 0, 255));
			drawArrow(g,arrowX1,arrowY1,arrayX2,arrayY2);
			g.setColor(new Color(0, 0, 0));
		}
	}
	
	private void drawArrow(Graphics g,Integer iX,Integer iY,Integer iX2,Integer iY2)
	{
		//垂直线
		if(iX.intValue() == iX2.intValue())
		{
			//终端在上侧 iX2 
			if(iY2.intValue()>iY.intValue())
			{
				g.drawLine(iX2, iY2, iX2-2, iY2-5);
				g.drawLine(iX2, iY2, iX2+2, iY2-5);
			}
			//终端在下侧
			if(iY2.intValue()<iY.intValue())
			{
				g.drawLine(iX2, iY2, iX2-2, iY2+5);
				g.drawLine(iX2, iY2, iX2+2, iY2+5);
			}
		}
		//水平线
		else if(iY.intValue() == iY2.intValue())
		{
			//终点在右侧
			if(iX2.intValue()>iX.intValue())
			{
				g.drawLine(iX2, iY2, iX2-5, iY2-2);
				g.drawLine(iX2, iY2, iX2-5, iY2+2);
			}
			//终点在左侧
			if(iX2.intValue()<iX.intValue())
			{
				g.drawLine(iX2, iY2, iX2+5, iY2-2);
				g.drawLine(iX2, iY2, iX2+5, iY2+2);
			}
		}
		//斜线,需要计算斜率
		else
		{
			double k=-(iY2.doubleValue() - iY.doubleValue())/(iX2.doubleValue()-iX.doubleValue());
			double darc = Math.atan(k);
			if(k<0)
				darc = darc + Math.PI;
			double darc1 = Math.tan(Math.PI-(darc - Math.PI/6));
			double darc2 = Math.tan(Math.PI-(darc + Math.PI/6));
			//y = kx + b
			double b1 = iY2 - iX2.intValue()*darc1;
			double b2 = iY2 - iX2.intValue()*darc2;
			//终点在右侧
			if(iX2.intValue()>iX.intValue())
			{				
				double dY1 = (iX2 - 4)*darc1 + b1;
				g.drawLine(iX2, iY2, (iX2 - 4), (int)dY1);
				double dY3 = (iX2 - 4)*darc2 + b2;
				g.drawLine(iX2, iY2, (iX2 - 4), (int)dY3);
			}
			else
			{
				double dY1 = (iX2 + 4)*darc1 + b1;
				g.drawLine(iX2, iY2, (iX2 + 4), (int)dY1);
				double dY3 = (iX2 - 4)*darc2 + b2;
				g.drawLine(iX2, iY2, (iX2 + 4), (int)dY3);
			}
		}
	}
	
	private int passedTime(List<DefWorkItem> dwiList,String nodeId)
	{
		int cnt = 0;
		for(DefWorkItem dwi:dwiList)
		{
			if(dwi.getStatusId().equals(nodeId))
				cnt++;
		}
		return cnt;
	}
	
	private String getText(List<DefWorkItem> dwiList,String nodeId)
	{
		String rtn = "";
		String finishTime = "";
		for(DefWorkItem dwi:dwiList)
		{
			if(dwi == null)
				continue;
			if(dwi.getStatusId().equals(nodeId) && !"pass".equals(dwi.getFlag()))
			{
				String userId = Util.fmtStr(dwi.getUserId());
				IUser user = orgMng.getUserById(userId);
				String userName = "";
				if(user != null)
					userName =user.getName();
				finishTime = Util.fmtStr(dwi.getFinishTime());
				if("".equals(finishTime))
					finishTime = "待审批......";
				if(rtn.indexOf(Util.fmtStr(userName))<0)
					rtn = rtn + Util.fmtStr(userName)+";";
				if("Start".equals(dwi.getStatusId()))
					break;
			}
		}
		if(!"".equals(finishTime))
			rtn = rtn + "["+finishTime+"]";
		return rtn;
	}
}
