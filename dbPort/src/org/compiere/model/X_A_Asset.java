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
/** Generated Model for A_Asset
 *  @author Adempiere (generated) 
 *  @version Release 3.1.5 - $Id$ */
public class X_A_Asset extends PO
{
/** Standard Constructor
@param ctx context
@param A_Asset_ID id
@param trxName transaction
*/
public X_A_Asset (Properties ctx, int A_Asset_ID, String trxName)
{
super (ctx, A_Asset_ID, trxName);
/** if (A_Asset_ID == 0)
{
setA_Asset_Group_ID (0);
setA_Asset_ID (0);
setIsDepreciated (false);
setIsDisposed (false);
setIsFullyDepreciated (false);	// N
setIsInPosession (false);
setIsOwned (false);
setM_AttributeSetInstance_ID (0);
setName (null);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_A_Asset (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=539 */
public static final int Table_ID=MTable.getTable_ID("A_Asset");

/** TableName=A_Asset */
public static final String Table_Name="A_Asset";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"A_Asset");

protected BigDecimal accessLevel = BigDecimal.valueOf(3);
/** AccessLevel
@return 3 - Client - Org 
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
StringBuffer sb = new StringBuffer ("X_A_Asset[").append(get_ID()).append("]");
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
/** Set Asset Group.
@param A_Asset_Group_ID Group of Assets */
public void setA_Asset_Group_ID (int A_Asset_Group_ID)
{
if (A_Asset_Group_ID < 1) throw new IllegalArgumentException ("A_Asset_Group_ID is mandatory.");
set_Value ("A_Asset_Group_ID", Integer.valueOf(A_Asset_Group_ID));
}
/** Get Asset Group.
@return Group of Assets */
public int getA_Asset_Group_ID() 
{
Integer ii = (Integer)get_Value("A_Asset_Group_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name A_Asset_Group_ID */
public static final String COLUMNNAME_A_Asset_Group_ID = "A_Asset_Group_ID";
/** Set Asset.
@param A_Asset_ID Asset used internally or by customers */
public void setA_Asset_ID (int A_Asset_ID)
{
if (A_Asset_ID < 1) throw new IllegalArgumentException ("A_Asset_ID is mandatory.");
set_ValueNoCheck ("A_Asset_ID", Integer.valueOf(A_Asset_ID));
}
/** Get Asset.
@return Asset used internally or by customers */
public int getA_Asset_ID() 
{
Integer ii = (Integer)get_Value("A_Asset_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name A_Asset_ID */
public static final String COLUMNNAME_A_Asset_ID = "A_Asset_ID";
/** Set Asset Depreciation Date.
@param AssetDepreciationDate Date of last depreciation */
public void setAssetDepreciationDate (Timestamp AssetDepreciationDate)
{
set_Value ("AssetDepreciationDate", AssetDepreciationDate);
}
/** Get Asset Depreciation Date.
@return Date of last depreciation */
public Timestamp getAssetDepreciationDate() 
{
return (Timestamp)get_Value("AssetDepreciationDate");
}
/** Column name AssetDepreciationDate */
public static final String COLUMNNAME_AssetDepreciationDate = "AssetDepreciationDate";
/** Set Asset Disposal Date.
@param AssetDisposalDate Date when the asset is/was disposed */
public void setAssetDisposalDate (Timestamp AssetDisposalDate)
{
set_Value ("AssetDisposalDate", AssetDisposalDate);
}
/** Get Asset Disposal Date.
@return Date when the asset is/was disposed */
public Timestamp getAssetDisposalDate() 
{
return (Timestamp)get_Value("AssetDisposalDate");
}
/** Column name AssetDisposalDate */
public static final String COLUMNNAME_AssetDisposalDate = "AssetDisposalDate";
/** Set In Service Date.
@param AssetServiceDate Date when Asset was put into service */
public void setAssetServiceDate (Timestamp AssetServiceDate)
{
set_Value ("AssetServiceDate", AssetServiceDate);
}
/** Get In Service Date.
@return Date when Asset was put into service */
public Timestamp getAssetServiceDate() 
{
return (Timestamp)get_Value("AssetServiceDate");
}
/** Column name AssetServiceDate */
public static final String COLUMNNAME_AssetServiceDate = "AssetServiceDate";

/** C_BPartnerSR_ID AD_Reference_ID=353 */
public static final int C_BPARTNERSR_ID_AD_Reference_ID=353;
/** Set BPartner (Agent).
@param C_BPartnerSR_ID Business Partner (Agent or Sales Rep) */
public void setC_BPartnerSR_ID (int C_BPartnerSR_ID)
{
if (C_BPartnerSR_ID <= 0) set_Value ("C_BPartnerSR_ID", null);
 else 
set_Value ("C_BPartnerSR_ID", Integer.valueOf(C_BPartnerSR_ID));
}
/** Get BPartner (Agent).
@return Business Partner (Agent or Sales Rep) */
public int getC_BPartnerSR_ID() 
{
Integer ii = (Integer)get_Value("C_BPartnerSR_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name C_BPartnerSR_ID */
public static final String COLUMNNAME_C_BPartnerSR_ID = "C_BPartnerSR_ID";
/** Set Business Partner .
@param C_BPartner_ID Identifies a Business Partner */
public void setC_BPartner_ID (int C_BPartner_ID)
{
if (C_BPartner_ID <= 0) set_Value ("C_BPartner_ID", null);
 else 
set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
}
/** Get Business Partner .
@return Identifies a Business Partner */
public int getC_BPartner_ID() 
{
Integer ii = (Integer)get_Value("C_BPartner_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name C_BPartner_ID */
public static final String COLUMNNAME_C_BPartner_ID = "C_BPartner_ID";
/** Set Partner Location.
@param C_BPartner_Location_ID Identifies the (ship to) address for this Business Partner */
public void setC_BPartner_Location_ID (int C_BPartner_Location_ID)
{
if (C_BPartner_Location_ID <= 0) set_Value ("C_BPartner_Location_ID", null);
 else 
set_Value ("C_BPartner_Location_ID", Integer.valueOf(C_BPartner_Location_ID));
}
/** Get Partner Location.
@return Identifies the (ship to) address for this Business Partner */
public int getC_BPartner_Location_ID() 
{
Integer ii = (Integer)get_Value("C_BPartner_Location_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name C_BPartner_Location_ID */
public static final String COLUMNNAME_C_BPartner_Location_ID = "C_BPartner_Location_ID";
/** Set Address.
@param C_Location_ID Location or Address */
public void setC_Location_ID (int C_Location_ID)
{
if (C_Location_ID <= 0) set_Value ("C_Location_ID", null);
 else 
set_Value ("C_Location_ID", Integer.valueOf(C_Location_ID));
}
/** Get Address.
@return Location or Address */
public int getC_Location_ID() 
{
Integer ii = (Integer)get_Value("C_Location_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name C_Location_ID */
public static final String COLUMNNAME_C_Location_ID = "C_Location_ID";
/** Set Project.
@param C_Project_ID Financial Project */
public void setC_Project_ID (int C_Project_ID)
{
if (C_Project_ID <= 0) set_Value ("C_Project_ID", null);
 else 
set_Value ("C_Project_ID", Integer.valueOf(C_Project_ID));
}
/** Get Project.
@return Financial Project */
public int getC_Project_ID() 
{
Integer ii = (Integer)get_Value("C_Project_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name C_Project_ID */
public static final String COLUMNNAME_C_Project_ID = "C_Project_ID";
/** Set Description.
@param Description Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 255)
{
log.warning("Length > 255 - truncated");
Description = Description.substring(0,254);
}
set_Value ("Description", Description);
}
/** Get Description.
@return Optional short description of the record */
public String getDescription() 
{
return (String)get_Value("Description");
}
/** Column name Description */
public static final String COLUMNNAME_Description = "Description";
/** Set Guarantee Date.
@param GuaranteeDate Date when guarantee expires */
public void setGuaranteeDate (Timestamp GuaranteeDate)
{
set_Value ("GuaranteeDate", GuaranteeDate);
}
/** Get Guarantee Date.
@return Date when guarantee expires */
public Timestamp getGuaranteeDate() 
{
return (Timestamp)get_Value("GuaranteeDate");
}
/** Column name GuaranteeDate */
public static final String COLUMNNAME_GuaranteeDate = "GuaranteeDate";
/** Set Comment/Help.
@param Help Comment or Hint */
public void setHelp (String Help)
{
if (Help != null && Help.length() > 2000)
{
log.warning("Length > 2000 - truncated");
Help = Help.substring(0,1999);
}
set_Value ("Help", Help);
}
/** Get Comment/Help.
@return Comment or Hint */
public String getHelp() 
{
return (String)get_Value("Help");
}
/** Column name Help */
public static final String COLUMNNAME_Help = "Help";
/** Set Depreciate.
@param IsDepreciated The asset will be depreciated */
public void setIsDepreciated (boolean IsDepreciated)
{
set_Value ("IsDepreciated", Boolean.valueOf(IsDepreciated));
}
/** Get Depreciate.
@return The asset will be depreciated */
public boolean isDepreciated() 
{
Object oo = get_Value("IsDepreciated");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name IsDepreciated */
public static final String COLUMNNAME_IsDepreciated = "IsDepreciated";
/** Set Disposed.
@param IsDisposed The asset is disposed */
public void setIsDisposed (boolean IsDisposed)
{
set_Value ("IsDisposed", Boolean.valueOf(IsDisposed));
}
/** Get Disposed.
@return The asset is disposed */
public boolean isDisposed() 
{
Object oo = get_Value("IsDisposed");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name IsDisposed */
public static final String COLUMNNAME_IsDisposed = "IsDisposed";
/** Set Fully depreciated.
@param IsFullyDepreciated The asset is fully depreciated */
public void setIsFullyDepreciated (boolean IsFullyDepreciated)
{
set_ValueNoCheck ("IsFullyDepreciated", Boolean.valueOf(IsFullyDepreciated));
}
/** Get Fully depreciated.
@return The asset is fully depreciated */
public boolean isFullyDepreciated() 
{
Object oo = get_Value("IsFullyDepreciated");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name IsFullyDepreciated */
public static final String COLUMNNAME_IsFullyDepreciated = "IsFullyDepreciated";
/** Set In Possession.
@param IsInPosession The asset is in the possession of the organization */
public void setIsInPosession (boolean IsInPosession)
{
set_Value ("IsInPosession", Boolean.valueOf(IsInPosession));
}
/** Get In Possession.
@return The asset is in the possession of the organization */
public boolean isInPosession() 
{
Object oo = get_Value("IsInPosession");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name IsInPosession */
public static final String COLUMNNAME_IsInPosession = "IsInPosession";
/** Set Owned.
@param IsOwned The asset is owned by the organization */
public void setIsOwned (boolean IsOwned)
{
set_Value ("IsOwned", Boolean.valueOf(IsOwned));
}
/** Get Owned.
@return The asset is owned by the organization */
public boolean isOwned() 
{
Object oo = get_Value("IsOwned");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Column name IsOwned */
public static final String COLUMNNAME_IsOwned = "IsOwned";
/** Set Last Maintenance.
@param LastMaintenanceDate Last Maintenance Date */
public void setLastMaintenanceDate (Timestamp LastMaintenanceDate)
{
set_Value ("LastMaintenanceDate", LastMaintenanceDate);
}
/** Get Last Maintenance.
@return Last Maintenance Date */
public Timestamp getLastMaintenanceDate() 
{
return (Timestamp)get_Value("LastMaintenanceDate");
}
/** Column name LastMaintenanceDate */
public static final String COLUMNNAME_LastMaintenanceDate = "LastMaintenanceDate";
/** Set Last Note.
@param LastMaintenanceNote Last Maintenance Note */
public void setLastMaintenanceNote (String LastMaintenanceNote)
{
if (LastMaintenanceNote != null && LastMaintenanceNote.length() > 60)
{
log.warning("Length > 60 - truncated");
LastMaintenanceNote = LastMaintenanceNote.substring(0,59);
}
set_Value ("LastMaintenanceNote", LastMaintenanceNote);
}
/** Get Last Note.
@return Last Maintenance Note */
public String getLastMaintenanceNote() 
{
return (String)get_Value("LastMaintenanceNote");
}
/** Column name LastMaintenanceNote */
public static final String COLUMNNAME_LastMaintenanceNote = "LastMaintenanceNote";
/** Set Last Unit.
@param LastMaintenanceUnit Last Maintenance Unit */
public void setLastMaintenanceUnit (int LastMaintenanceUnit)
{
set_Value ("LastMaintenanceUnit", Integer.valueOf(LastMaintenanceUnit));
}
/** Get Last Unit.
@return Last Maintenance Unit */
public int getLastMaintenanceUnit() 
{
Integer ii = (Integer)get_Value("LastMaintenanceUnit");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name LastMaintenanceUnit */
public static final String COLUMNNAME_LastMaintenanceUnit = "LastMaintenanceUnit";
/** Set Lease Termination.
@param LeaseTerminationDate Lease Termination Date */
public void setLeaseTerminationDate (Timestamp LeaseTerminationDate)
{
set_Value ("LeaseTerminationDate", LeaseTerminationDate);
}
/** Get Lease Termination.
@return Lease Termination Date */
public Timestamp getLeaseTerminationDate() 
{
return (Timestamp)get_Value("LeaseTerminationDate");
}
/** Column name LeaseTerminationDate */
public static final String COLUMNNAME_LeaseTerminationDate = "LeaseTerminationDate";

/** Lease_BPartner_ID AD_Reference_ID=192 */
public static final int LEASE_BPARTNER_ID_AD_Reference_ID=192;
/** Set Lessor.
@param Lease_BPartner_ID The Business Partner who rents or leases */
public void setLease_BPartner_ID (int Lease_BPartner_ID)
{
if (Lease_BPartner_ID <= 0) set_Value ("Lease_BPartner_ID", null);
 else 
set_Value ("Lease_BPartner_ID", Integer.valueOf(Lease_BPartner_ID));
}
/** Get Lessor.
@return The Business Partner who rents or leases */
public int getLease_BPartner_ID() 
{
Integer ii = (Integer)get_Value("Lease_BPartner_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name Lease_BPartner_ID */
public static final String COLUMNNAME_Lease_BPartner_ID = "Lease_BPartner_ID";
/** Set Life use.
@param LifeUseUnits Units of use until the asset is not usable anymore */
public void setLifeUseUnits (int LifeUseUnits)
{
set_Value ("LifeUseUnits", Integer.valueOf(LifeUseUnits));
}
/** Get Life use.
@return Units of use until the asset is not usable anymore */
public int getLifeUseUnits() 
{
Integer ii = (Integer)get_Value("LifeUseUnits");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name LifeUseUnits */
public static final String COLUMNNAME_LifeUseUnits = "LifeUseUnits";
/** Set Location comment.
@param LocationComment Additional comments or remarks concerning the location */
public void setLocationComment (String LocationComment)
{
if (LocationComment != null && LocationComment.length() > 255)
{
log.warning("Length > 255 - truncated");
LocationComment = LocationComment.substring(0,254);
}
set_Value ("LocationComment", LocationComment);
}
/** Get Location comment.
@return Additional comments or remarks concerning the location */
public String getLocationComment() 
{
return (String)get_Value("LocationComment");
}
/** Column name LocationComment */
public static final String COLUMNNAME_LocationComment = "LocationComment";
/** Set Lot No.
@param Lot Lot number (alphanumeric) */
public void setLot (String Lot)
{
if (Lot != null && Lot.length() > 255)
{
log.warning("Length > 255 - truncated");
Lot = Lot.substring(0,254);
}
set_Value ("Lot", Lot);
}
/** Get Lot No.
@return Lot number (alphanumeric) */
public String getLot() 
{
return (String)get_Value("Lot");
}
/** Column name Lot */
public static final String COLUMNNAME_Lot = "Lot";
/** Set Attribute Set Instance.
@param M_AttributeSetInstance_ID Product Attribute Set Instance */
public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
{
if (M_AttributeSetInstance_ID < 0) throw new IllegalArgumentException ("M_AttributeSetInstance_ID is mandatory.");
set_ValueNoCheck ("M_AttributeSetInstance_ID", Integer.valueOf(M_AttributeSetInstance_ID));
}
/** Get Attribute Set Instance.
@return Product Attribute Set Instance */
public int getM_AttributeSetInstance_ID() 
{
Integer ii = (Integer)get_Value("M_AttributeSetInstance_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name M_AttributeSetInstance_ID */
public static final String COLUMNNAME_M_AttributeSetInstance_ID = "M_AttributeSetInstance_ID";
/** Set Shipment/Receipt Line.
@param M_InOutLine_ID Line on Shipment or Receipt document */
public void setM_InOutLine_ID (int M_InOutLine_ID)
{
if (M_InOutLine_ID <= 0) set_Value ("M_InOutLine_ID", null);
 else 
set_Value ("M_InOutLine_ID", Integer.valueOf(M_InOutLine_ID));
}
/** Get Shipment/Receipt Line.
@return Line on Shipment or Receipt document */
public int getM_InOutLine_ID() 
{
Integer ii = (Integer)get_Value("M_InOutLine_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name M_InOutLine_ID */
public static final String COLUMNNAME_M_InOutLine_ID = "M_InOutLine_ID";
/** Set Locator.
@param M_Locator_ID Warehouse Locator */
public void setM_Locator_ID (int M_Locator_ID)
{
if (M_Locator_ID <= 0) set_Value ("M_Locator_ID", null);
 else 
set_Value ("M_Locator_ID", Integer.valueOf(M_Locator_ID));
}
/** Get Locator.
@return Warehouse Locator */
public int getM_Locator_ID() 
{
Integer ii = (Integer)get_Value("M_Locator_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name M_Locator_ID */
public static final String COLUMNNAME_M_Locator_ID = "M_Locator_ID";
/** Set Product.
@param M_Product_ID Product, Service, Item */
public void setM_Product_ID (int M_Product_ID)
{
if (M_Product_ID <= 0) set_ValueNoCheck ("M_Product_ID", null);
 else 
set_ValueNoCheck ("M_Product_ID", Integer.valueOf(M_Product_ID));
}
/** Get Product.
@return Product, Service, Item */
public int getM_Product_ID() 
{
Integer ii = (Integer)get_Value("M_Product_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name M_Product_ID */
public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";
/** Set Name.
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
if (Name.length() > 60)
{
log.warning("Length > 60 - truncated");
Name = Name.substring(0,59);
}
set_Value ("Name", Name);
}
/** Get Name.
@return Alphanumeric identifier of the entity */
public String getName() 
{
return (String)get_Value("Name");
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair
*/public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), getName());
}
/** Column name Name */
public static final String COLUMNNAME_Name = "Name";
/** Set Next Maintenence.
@param NextMaintenenceDate Next Maintenence Date */
public void setNextMaintenenceDate (Timestamp NextMaintenenceDate)
{
set_Value ("NextMaintenenceDate", NextMaintenenceDate);
}
/** Get Next Maintenence.
@return Next Maintenence Date */
public Timestamp getNextMaintenenceDate() 
{
return (Timestamp)get_Value("NextMaintenenceDate");
}
/** Column name NextMaintenenceDate */
public static final String COLUMNNAME_NextMaintenenceDate = "NextMaintenenceDate";
/** Set Next Unit.
@param NextMaintenenceUnit Next Maintenence Unit */
public void setNextMaintenenceUnit (int NextMaintenenceUnit)
{
set_Value ("NextMaintenenceUnit", Integer.valueOf(NextMaintenenceUnit));
}
/** Get Next Unit.
@return Next Maintenence Unit */
public int getNextMaintenenceUnit() 
{
Integer ii = (Integer)get_Value("NextMaintenenceUnit");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name NextMaintenenceUnit */
public static final String COLUMNNAME_NextMaintenenceUnit = "NextMaintenenceUnit";
/** Set Quantity.
@param Qty Quantity */
public void setQty (BigDecimal Qty)
{
set_Value ("Qty", Qty);
}
/** Get Quantity.
@return Quantity */
public BigDecimal getQty() 
{
BigDecimal bd = (BigDecimal)get_Value("Qty");
if (bd == null) return Env.ZERO;
return bd;
}
/** Column name Qty */
public static final String COLUMNNAME_Qty = "Qty";
/** Set Serial No.
@param SerNo Product Serial Number  */
public void setSerNo (String SerNo)
{
if (SerNo != null && SerNo.length() > 255)
{
log.warning("Length > 255 - truncated");
SerNo = SerNo.substring(0,254);
}
set_Value ("SerNo", SerNo);
}
/** Get Serial No.
@return Product Serial Number  */
public String getSerNo() 
{
return (String)get_Value("SerNo");
}
/** Column name SerNo */
public static final String COLUMNNAME_SerNo = "SerNo";
/** Set Usable Life - Months.
@param UseLifeMonths Months of the usable life of the asset */
public void setUseLifeMonths (int UseLifeMonths)
{
set_Value ("UseLifeMonths", Integer.valueOf(UseLifeMonths));
}
/** Get Usable Life - Months.
@return Months of the usable life of the asset */
public int getUseLifeMonths() 
{
Integer ii = (Integer)get_Value("UseLifeMonths");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name UseLifeMonths */
public static final String COLUMNNAME_UseLifeMonths = "UseLifeMonths";
/** Set Usable Life - Years.
@param UseLifeYears Years of the usable life of the asset */
public void setUseLifeYears (int UseLifeYears)
{
set_Value ("UseLifeYears", Integer.valueOf(UseLifeYears));
}
/** Get Usable Life - Years.
@return Years of the usable life of the asset */
public int getUseLifeYears() 
{
Integer ii = (Integer)get_Value("UseLifeYears");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name UseLifeYears */
public static final String COLUMNNAME_UseLifeYears = "UseLifeYears";
/** Set Use units.
@param UseUnits Currently used units of the assets */
public void setUseUnits (int UseUnits)
{
set_ValueNoCheck ("UseUnits", Integer.valueOf(UseUnits));
}
/** Get Use units.
@return Currently used units of the assets */
public int getUseUnits() 
{
Integer ii = (Integer)get_Value("UseUnits");
if (ii == null) return 0;
return ii.intValue();
}
/** Column name UseUnits */
public static final String COLUMNNAME_UseUnits = "UseUnits";
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
if (Value.length() > 40)
{
log.warning("Length > 40 - truncated");
Value = Value.substring(0,39);
}
set_Value ("Value", Value);
}
/** Get Search Key.
@return Search key for the record in the format required - must be unique */
public String getValue() 
{
return (String)get_Value("Value");
}
/** Column name Value */
public static final String COLUMNNAME_Value = "Value";
/** Set Version No.
@param VersionNo Version Number */
public void setVersionNo (String VersionNo)
{
if (VersionNo != null && VersionNo.length() > 20)
{
log.warning("Length > 20 - truncated");
VersionNo = VersionNo.substring(0,19);
}
set_Value ("VersionNo", VersionNo);
}
/** Get Version No.
@return Version Number */
public String getVersionNo() 
{
return (String)get_Value("VersionNo");
}
/** Column name VersionNo */
public static final String COLUMNNAME_VersionNo = "VersionNo";
}
