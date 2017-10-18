package org.adempiere.webui.window;

import java.util.ArrayList;
import java.util.Map;

import org.adempiere.webui.adwindow.GridView;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.panel.CustomizeGridViewPanel;
import org.adempiere.webui.theme.ThemeManager;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class CustomizeGridViewDialog extends Window {

	/**
	 * generated serial id
	 */
	private static final long serialVersionUID = -4093048147438176240L;
	
	private CustomizeGridViewPanel customizePanel;

	/**
	 *	Standard Constructor
	 * 	@param WindowNo window no
	 *  @param AD_Tab_ID tab
	 * 	@param AD_User_ID user
	 * @param columnsWidth 
	 */
	public CustomizeGridViewDialog(int windowNo, int AD_Tab_ID, int AD_User_ID, Map<Integer, String> columnsWidth,ArrayList<Integer> gridFieldIds)
	{
		setClosable(true);
		setTitle(Msg.getMsg(Env.getCtx(), "Customize"));
		initComponent(windowNo,AD_Tab_ID, AD_User_ID, columnsWidth,gridFieldIds);		
	}
	
	private void initComponent(int windowNo, int AD_Tab_ID, int AD_User_ID, Map<Integer, String> columnsWidth,ArrayList<Integer> gridFieldIds) {
		customizePanel = new CustomizeGridViewPanel(windowNo, AD_Tab_ID, AD_User_ID, columnsWidth,gridFieldIds);
		this.setStyle("position : relative;");
		if (!ThemeManager.isUseCSSForWindowSize()) {
			ZKUpdateUtil.setWindowWidthX(this, 600);
			ZKUpdateUtil.setWindowHeightX(this, 500);
		} else {
			addCallback(AFTER_PAGE_ATTACHED, t-> {
				ZKUpdateUtil.setCSSHeight(this);
				ZKUpdateUtil.setCSSWidth(this);
			});
		}
		this.setBorder("normal");
		this.setSclass("popup-dialog customize-grid-view-dialog");
		appendChild(customizePanel);
		customizePanel.createUI();
		customizePanel.query();
	}

	/**
	 * @return whether change have been successfully save to db
	 */
	public boolean isSaved() {
		return customizePanel.isSaved();
	}
	
	public void setGridPanel(GridView gridPanel){
		customizePanel.setGridPanel(gridPanel);
	}

	/**
	 * Show User customize (modal)
	 * @param WindowNo window no
	 * @param AD_Tab_ID
	 * @param columnsWidth 
	 * @param gridFieldIds list fieldId current display in gridview
	 * @param gridPanel
	 */
	public static boolean showCustomize (int WindowNo, int AD_Tab_ID, Map<Integer, String> columnsWidth,ArrayList<Integer> gridFieldIds,GridView gridPanel)
	{
		CustomizeGridViewDialog customizeWindow = new CustomizeGridViewDialog(WindowNo, AD_Tab_ID, Env.getAD_User_ID(Env.getCtx()), columnsWidth,gridFieldIds);
		customizeWindow.setGridPanel(gridPanel);
		AEnv.showWindow(customizeWindow);
		return customizeWindow.isSaved();
	}   //  showProduct    
}
