/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software;
 you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program;
 if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for CM_ChatEntry
 *  @author Adempiere (generated) 
 *  @version Release 3.3.0 - $Id$ */
public class X_CM_ChatEntry extends PO
{
/** Standard Constructor
@param ctx context
@param CM_ChatEntry_ID id
@param trxName transaction
*/
public X_CM_ChatEntry (Properties ctx, int CM_ChatEntry_ID, String trxName)
{
super (ctx, CM_ChatEntry_ID, trxName);
/** if (CM_ChatEntry_ID == 0)
{
setCM_ChatEntry_ID (0);
setCM_Chat_ID (0);
setCharacterData (null);
setChatEntryType (null);	// N
setConfidentialType (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_CM_ChatEntry (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** TableName=CM_ChatEntry */
public static final String Table_Name="CM_ChatEntry";

/** AD_Table_ID=877 */
public static final int Table_ID=MTable.getTable_ID(Table_Name);

protected static KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

protected BigDecimal accessLevel = BigDecimal.valueOf(7);
/** AccessLevel
@return 7 - System - Client - Org 
*/
protected int get_AccessLevel()
{
return accessLevel.intValue();
}
/** Load Meta Data
@param ctx context
@return PO Info
*/
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
/** Info
@return info
*/
public String toString()
{
StringBuffer sb = new StringBuffer ("X_CM_ChatEntry[").append(get_ID()).append("]");
return sb.toString();
}
/** Set User/Contact.
@param AD_User_ID User within the system - Internal or Business Partner Contact */
public void setAD_User_ID (int AD_User_ID)
{
if (AD_User_ID <= 0) set_Value ("AD_User_ID", null);
 else 
set_Value ("AD_User_ID", Integer.valueOf(AD_User_ID));
}
/** Get User/Contact.
@return User within the system - Internal or Business Partner Contact */
public int getAD_User_ID() 
{
Integer ii = (Integer)get_Value("AD_User_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name AD_User_ID */
public static final String COLUMNNAME_AD_User_ID = "AD_User_ID";

/** CM_ChatEntryGrandParent_ID AD_Reference_ID=399 */
public static final int CM_CHATENTRYGRANDPARENT_ID_AD_Reference_ID=399;
/** Set Chat Entry Grandparent.
@param CM_ChatEntryGrandParent_ID Link to Grand Parent (root level) */
public void setCM_ChatEntryGrandParent_ID (int CM_ChatEntryGrandParent_ID)
{
if (CM_ChatEntryGrandParent_ID <= 0) set_Value ("CM_ChatEntryGrandParent_ID", null);
 else 
set_Value ("CM_ChatEntryGrandParent_ID", Integer.valueOf(CM_ChatEntryGrandParent_ID));
}
/** Get Chat Entry Grandparent.
@return Link to Grand Parent (root level) */
public int getCM_ChatEntryGrandParent_ID() 
{
Integer ii = (Integer)get_Value("CM_ChatEntryGrandParent_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name CM_ChatEntryGrandParent_ID */
public static final String COLUMNNAME_CM_ChatEntryGrandParent_ID = "CM_ChatEntryGrandParent_ID";

/** CM_ChatEntryParent_ID AD_Reference_ID=399 */
public static final int CM_CHATENTRYPARENT_ID_AD_Reference_ID=399;
/** Set Chat Entry Parent.
@param CM_ChatEntryParent_ID Link to direct Parent */
public void setCM_ChatEntryParent_ID (int CM_ChatEntryParent_ID)
{
if (CM_ChatEntryParent_ID <= 0) set_Value ("CM_ChatEntryParent_ID", null);
 else 
set_Value ("CM_ChatEntryParent_ID", Integer.valueOf(CM_ChatEntryParent_ID));
}
/** Get Chat Entry Parent.
@return Link to direct Parent */
public int getCM_ChatEntryParent_ID() 
{
Integer ii = (Integer)get_Value("CM_ChatEntryParent_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name CM_ChatEntryParent_ID */
public static final String COLUMNNAME_CM_ChatEntryParent_ID = "CM_ChatEntryParent_ID";
/** Set Chat Entry.
@param CM_ChatEntry_ID Individual Chat / Discussion Entry */
public void setCM_ChatEntry_ID (int CM_ChatEntry_ID)
{
if (CM_ChatEntry_ID < 1) throw new IllegalArgumentException ("CM_ChatEntry_ID is mandatory.");
set_ValueNoCheck ("CM_ChatEntry_ID", Integer.valueOf(CM_ChatEntry_ID));
}
/** Get Chat Entry.
@return Individual Chat / Discussion Entry */
public int getCM_ChatEntry_ID() 
{
Integer ii = (Integer)get_Value("CM_ChatEntry_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair
*/public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), String.valueOf(getCM_ChatEntry_ID()));
}
/** Column name CM_ChatEntry_ID */
public static final String COLUMNNAME_CM_ChatEntry_ID = "CM_ChatEntry_ID";
/** Set Chat.
@param CM_Chat_ID Chat or discussion thread */
public void setCM_Chat_ID (int CM_Chat_ID)
{
if (CM_Chat_ID < 1) throw new IllegalArgumentException ("CM_Chat_ID is mandatory.");
set_ValueNoCheck ("CM_Chat_ID", Integer.valueOf(CM_Chat_ID));
}
/** Get Chat.
@return Chat or discussion thread */
public int getCM_Chat_ID() 
{
Integer ii = (Integer)get_Value("CM_Chat_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name CM_Chat_ID */
public static final String COLUMNNAME_CM_Chat_ID = "CM_Chat_ID";
/** Set Character Data.
@param CharacterData Long Character Field */
public void setCharacterData (String CharacterData)
{
if (CharacterData == null) throw new IllegalArgumentException ("CharacterData is mandatory.");
set_ValueNoCheck ("CharacterData", CharacterData);
}
/** Get Character Data.
@return Long Character Field */
public String getCharacterData() 
{
return (String)get_Value("CharacterData");
}
/** Column name CharacterData */
public static final String COLUMNNAME_CharacterData = "CharacterData";

/** ChatEntryType AD_Reference_ID=398 */
public static final int CHATENTRYTYPE_AD_Reference_ID=398;
/** Forum (threaded) = F */
public static final String CHATENTRYTYPE_ForumThreaded = "F";
/** Note (flat) = N */
public static final String CHATENTRYTYPE_NoteFlat = "N";
/** Wiki = W */
public static final String CHATENTRYTYPE_Wiki = "W";
/** Set Chat Entry Type.
@param ChatEntryType Type of Chat/Forum Entry */
public void setChatEntryType (String ChatEntryType)
{
if (ChatEntryType == null) throw new IllegalArgumentException ("ChatEntryType is mandatory");
if (ChatEntryType.equals("F") || ChatEntryType.equals("N") || ChatEntryType.equals("W"));
 else throw new IllegalArgumentException ("ChatEntryType Invalid value - " + ChatEntryType + " - Reference_ID=398 - F - N - W");
if (ChatEntryType.length() > 1)
{
log.warning("Length > 1 - truncated");
ChatEntryType = ChatEntryType.substring(0,0);
}
set_Value ("ChatEntryType", ChatEntryType);
}
/** Get Chat Entry Type.
@return Type of Chat/Forum Entry */
public String getChatEntryType() 
{
return (String)get_Value("ChatEntryType");
}
/** Column name ChatEntryType */
public static final String COLUMNNAME_ChatEntryType = "ChatEntryType";

/** ConfidentialType AD_Reference_ID=340 */
public static final int CONFIDENTIALTYPE_AD_Reference_ID=340;
/** Public Information = A */
public static final String CONFIDENTIALTYPE_PublicInformation = "A";
/** Partner Confidential = C */
public static final String CONFIDENTIALTYPE_PartnerConfidential = "C";
/** Internal = I */
public static final String CONFIDENTIALTYPE_Internal = "I";
/** Private Information = P */
public static final String CONFIDENTIALTYPE_PrivateInformation = "P";
/** Set Confidentiality.
@param ConfidentialType Type of Confidentiality */
public void setConfidentialType (String ConfidentialType)
{
if (ConfidentialType == null) throw new IllegalArgumentException ("ConfidentialType is mandatory");
if (ConfidentialType.equals("A") || ConfidentialType.equals("C") || ConfidentialType.equals("I") || ConfidentialType.equals("P"));
 else throw new IllegalArgumentException ("ConfidentialType Invalid value - " + ConfidentialType + " - Reference_ID=340 - A - C - I - P");
if (ConfidentialType.length() > 1)
{
log.warning("Length > 1 - truncated");
ConfidentialType = ConfidentialType.substring(0,0);
}
set_Value ("ConfidentialType", ConfidentialType);
}
/** Get Confidentiality.
@return Type of Confidentiality */
public String getConfidentialType() 
{
return (String)get_Value("ConfidentialType");
}
/** Column name ConfidentialType */
public static final String COLUMNNAME_ConfidentialType = "ConfidentialType";

/** ModeratorStatus AD_Reference_ID=396 */
public static final int MODERATORSTATUS_AD_Reference_ID=396;
/** Not Displayed = N */
public static final String MODERATORSTATUS_NotDisplayed = "N";
/** Published = P */
public static final String MODERATORSTATUS_Published = "P";
/** To be reviewed = R */
public static final String MODERATORSTATUS_ToBeReviewed = "R";
/** Suspicious = S */
public static final String MODERATORSTATUS_Suspicious = "S";
/** Set Moderation Status.
@param ModeratorStatus Status of Moderation */
public void setModeratorStatus (String ModeratorStatus)
{
if (ModeratorStatus == null || ModeratorStatus.equals("N") || ModeratorStatus.equals("P") || ModeratorStatus.equals("R") || ModeratorStatus.equals("S"));
 else throw new IllegalArgumentException ("ModeratorStatus Invalid value - " + ModeratorStatus + " - Reference_ID=396 - N - P - R - S");
if (ModeratorStatus != null && ModeratorStatus.length() > 1)
{
log.warning("Length > 1 - truncated");
ModeratorStatus = ModeratorStatus.substring(0,0);
}
set_Value ("ModeratorStatus", ModeratorStatus);
}
/** Get Moderation Status.
@return Status of Moderation */
public String getModeratorStatus() 
{
return (String)get_Value("ModeratorStatus");
}
/** Column name ModeratorStatus */
public static final String COLUMNNAME_ModeratorStatus = "ModeratorStatus";
/** Set Subject.
@param Subject Email Message Subject */
public void setSubject (String Subject)
{
if (Subject != null && Subject.length() > 255)
{
log.warning("Length > 255 - truncated");
Subject = Subject.substring(0,254);
}
set_Value ("Subject", Subject);
}
/** Get Subject.
@return Email Message Subject */
public String getSubject() 
{
return (String)get_Value("Subject");
}
/** Column name Subject */
public static final String COLUMNNAME_Subject = "Subject";
}
