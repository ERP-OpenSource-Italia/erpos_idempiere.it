/******************************************************************************
 * Copyright (C) 2012 Trek Global                                             *
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/

package org.compiere.apps.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.compiere.model.GridField;
import org.compiere.model.I_AD_FieldGroup;
import org.compiere.model.I_AD_UserDef_Win;
import org.compiere.model.MField;
import org.compiere.model.MRole;
import org.compiere.model.MTab;
import org.compiere.model.MUser;
import org.compiere.model.MUserDefField;
import org.compiere.model.MUserDefTab;
import org.compiere.model.PO;
import org.compiere.model.X_AD_FieldGroup;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Util;

/**
 *
 * @author Juan David Arboleda
 * @author Carlos Ruiz
 *
 */
public class TabEditor
{
	public static final String TTIP_UPDATEAONLYCHANGEDFIELDS = "UpdateOnlyChangedFieldsUserDefTab";
	public static final String TTIP_UPDATEALLFIELDS = "UpdateAllFieldsUserDefTab";

	public MTab m_tab;

	/**	Logger			*/
	public static CLogger log = CLogger.getCLogger(TabEditor.class);

	private List<MField> fields = new ArrayList<MField>();
	
	private List<GridField> gridFields = new ArrayList<GridField>();
	
	private MField m_activeField;
	
	private Map<Integer, MField> mapField = new HashMap<Integer, MField>();
	
	private Map<Integer, GridField> mapGridField = new HashMap<Integer, GridField>();
	
	// F3P: is it on window customization ?
	private MUserDefTab userDefTab= null;
	private int			windowNo;

	public List<GridField> getGridFields() {
		return gridFields;
	}

	public MTab getMTab() {
		return m_tab;
	}

	public List<MField> getMFields() {
		return fields;
	}
	
	public boolean isUserDefTab() {
		return userDefTab != null;
	}

	protected void initMFields(int windowNo, int tabid, int Table_ID) {
		
		Properties overriddenCtx = new Properties(Env.getCtx());

		// F3P: is on UserDefTab ?	
		// Properties ctx = Env.getCtx();
		if(Table_ID == MUserDefTab.Table_ID)
		{
			userDefTab = new MUserDefTab(Env.getCtx(), tabid, null);
			tabid = userDefTab.getAD_Tab_ID();
			this.windowNo = windowNo;
			
			I_AD_UserDef_Win win = userDefTab.getAD_UserDef_Win();
			
			int AD_Role_ID = win.getAD_Role_ID();
			int AD_User_ID = win.getAD_User_ID();
			
			Env.setContext(overriddenCtx, "#" + MRole.COLUMNNAME_AD_Role_ID, AD_Role_ID); // Read tab as visibile from role/user of this customization, to avoid mixing with customizations valid for current user, and apply customization valid for target role/user
			Env.setContext(overriddenCtx, "#" + MUser.COLUMNNAME_AD_User_ID, AD_User_ID);			
		}
		
		m_tab = new MTab(Env.getCtx(), tabid, null);
				
		GridField[] l_gridFields = GridField.createFields(overriddenCtx, windowNo, 0, tabid);
		for (GridField gridField : l_gridFields) {
			gridFields.add(gridField);
			mapGridField.put(gridField.getAD_Field_ID(), gridField);
			MField field = new MField(overriddenCtx, gridField.getAD_Field_ID(), null);
			
			// F3P: replace values in field from gridfield, to honor applied customizations
			
			if(userDefTab != null)
			{
				field.setSeqNo(gridField.getSeqNo());
				field.setIsDisplayed(gridField.isDisplayed());
				field.setXPosition(gridField.getXPosition());
				field.setNumLines(gridField.getNumLines());
				field.setColumnSpan(gridField.getColumnSpan());
				
				MUserDefField userDef = MUserDefField.get(overriddenCtx, field.getAD_Field_ID(), tabid, m_tab.getAD_Window_ID());
				
				if(userDef != null && userDef.getAD_FieldGroup_ID() > 0)
					field.setAD_FieldGroup_ID(userDef.getAD_FieldGroup_ID());				
			}
			
			fields.add(field);
			mapField.put(field.getAD_Field_ID(), field);
			gridField.getVO().IsReadOnly = true;
			gridField.getVO().IsMandatory = false;
			gridField.getVO().IsUpdateable = false;
			gridField.getVO().IsAlwaysUpdateable = false;
		}
		resortArrays();
	}
	
	public boolean cmd_save()
	{
		return cmd_save(false);
	}

	public boolean cmd_save(boolean saveAllCustom) {
		
		if(userDefTab != null) // Save for AD_UserDefTab
		{
			Properties overriddenCtx = new Properties(Env.getCtx());
			
			I_AD_UserDef_Win win = userDefTab.getAD_UserDef_Win();
			
			int AD_Role_ID = win.getAD_Role_ID();
			int AD_User_ID = win.getAD_User_ID();
			
			Env.setContext(overriddenCtx, "#" + MRole.COLUMNNAME_AD_Role_ID, AD_Role_ID); // Read tab as visibile from role/user of this customization, to avoid mixing with customizations valid for current user, and apply customization valid for target role/user
			Env.setContext(overriddenCtx, "#" + MUser.COLUMNNAME_AD_User_ID, AD_User_ID);		
			
			// Re-read fields to be able to save only changes
			GridField[] gridFields = GridField.createFields(overriddenCtx, windowNo, 0, userDefTab.getAD_Tab_ID());

			HashMap<Integer, GridField> mapIdToField = new HashMap<>();
			for(GridField gf:gridFields)
			{
				mapIdToField.put(gf.getAD_Field_ID(), gf);
			}
			
			for(MField field: fields)
			{
				boolean		changed = false;
				MUserDefField userDef = userDefTab.getUserDefField(field.getAD_Field_ID());
				GridField	gf = mapIdToField.get(field.getAD_Field_ID());
				
				if(userDef == null)
				{
					userDef = new MUserDefField(overriddenCtx, 0, null);
					userDef.setAD_UserDef_Tab_ID(userDefTab.getAD_UserDef_Tab_ID());
					userDef.setAD_Field_ID(field.getAD_Field_ID());
				}
				
				// Check field (new value) versus computed value (gridField)
				
				if(saveAllCustom || gf.getSeqNo() != field.getSeqNo())
				{
					userDef.setSeqNo(field.getSeqNo());
					changed = true;
				}
				
				if(saveAllCustom || gf.isDisplayed() != field.isDisplayed())
				{
					userDef.setIsDisplayed(Util.asString(field.isDisplayed()));
					changed = true;
				}
				
				if(saveAllCustom || gf.getXPosition() != field.getXPosition())
				{
					userDef.setXPosition(field.getXPosition());
					changed = true;
				}
				
				if(saveAllCustom || gf.getNumLines() != field.getNumLines())
				{
					userDef.setNumLines(field.getNumLines());
					changed = true;
				}
				
				if(saveAllCustom || gf.getColumnSpan() != field.getColumnSpan())
				{
					userDef.setColumnSpan(field.getColumnSpan());
					changed = true;
				}
				
				String fieldGroupName = "";
				
				if(field.getAD_FieldGroup_ID() > 0)
				{
					I_AD_FieldGroup fieldGroup = field.getAD_FieldGroup();
					
					if(Env.isBaseLanguage(overriddenCtx, MTab.Table_Name))
						fieldGroupName = fieldGroup.getName();
					else
						fieldGroupName = ((PO)fieldGroup).get_Translation(X_AD_FieldGroup.COLUMNNAME_Name, Env.getAD_Language(overriddenCtx)); 
				}
				
				if(saveAllCustom || gf.getFieldGroup().equals(fieldGroupName) == false)
				{
					userDef.setAD_FieldGroup_ID(field.getAD_FieldGroup_ID());
					changed = true;
				}				
				
				if(changed || saveAllCustom)
				{
					userDef.setIsViewEnable(true);
					userDef.saveEx();
				}
			}
			
		}
		else // Standard save
		{
			for (MField field : fields) {
				if (field.isActive())
					field.saveEx();
			}			
		}
		
		return true;
	}

	protected MField getMField(int fieldid) {
		return 	mapField.get(fieldid);
	}

	protected GridField getGridField(MField field) {
		return  mapGridField.get( field.getAD_Field_ID());
	}

	public MField getActiveMField() {
		return m_activeField;
	}

	protected void setActiveMField(MField field) {
		m_activeField = field;
	}

	protected void resortArrays() {
		Collections.sort(fields, new Comparator<MField>() {
			@Override
			public int compare(MField field1, MField field2) {
				String compare1 = String.format("%s%10d%s",
						(field1.isDisplayed() ? "0" : "1"), 
						(field1.isDisplayed() ? field1.getSeqNo() : 0),
						field1.getName());
				String compare2 = String.format("%s%10d%s",
						(field2.isDisplayed() ? "0" : "1"), 
						(field2.isDisplayed() ? field2.getSeqNo() : 0),
						field2.getName());
				return compare1.compareTo(compare2);
			}
		});

		int seq = 10;
		for (MField field : fields) {
			if (field.isDisplayed()) {
				field.setSeqNo(seq);
				seq = seq + 10;
			} else {
				field.setSeqNo(0);
			}
		}

		Collections.sort(gridFields, new Comparator<GridField>() {
			@Override
			public int compare(GridField f1, GridField f2) {
				MField field1 = getMField(f1.getAD_Field_ID());
				MField field2 = getMField(f2.getAD_Field_ID());
				String compare1 = String.format("%s%10d%s",
						(field1.isDisplayed() ? "0" : "1"), 
						(field1.isDisplayed() ? field1.getSeqNo() : 0),
						field1.getName());
				String compare2 = String.format("%s%10d%s",
						(field2.isDisplayed() ? "0" : "1"), 
						(field2.isDisplayed() ? field2.getSeqNo() : 0),
						field2.getName());
				return compare1.compareTo(compare2);
			}
		});
		
	}

}   //  TabEditor
