/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 Adempiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *
 * Copyright (C) 2005 Robert Klein. robeklein@hotmail.com
 * Contributor(s): Low Heng Sin hengsin@avantz.com
 *****************************************************************************/
package org.adempiere.pipo2.handler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import javax.xml.transform.sax.TransformerHandler;

import org.adempiere.pipo2.AbstractElementHandler;
import org.adempiere.pipo2.Element;
import org.adempiere.pipo2.PIPOContext;
import org.adempiere.pipo2.PackOut;
import org.adempiere.pipo2.PackoutItem;
import org.compiere.Adempiere;
import org.compiere.model.MPackageExp;
import org.compiere.model.X_AD_Package_Exp_Detail;
import org.compiere.model.X_AD_Package_Imp_Backup;
import org.compiere.model.X_AD_Package_Imp_Detail;
import org.compiere.util.Env;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class DistFileElementHandler extends AbstractElementHandler {

	String fileDest;

	public DistFileElementHandler()
	{

	}

	public DistFileElementHandler(String fileDest)
	{
		this.fileDest=fileDest;
	}

	public void startElement(PIPOContext ctx, Element element) throws SAXException {
		String action = null;

		String releaseNumber = getStringValue(element,"ReleaseNo");
		//Check Release Number
		if(releaseNumber==null||Adempiere.MAIN_VERSION.equals(releaseNumber)||releaseNumber.equals("all")){
			String fileName = getStringValue(element, "filename");
			String sourceDirectory = getStringValue(element, "sourceDirectory");
			String targetDirectory = getStringValue(element, "targetDirectory");

			action = "New";
			InputStream inputStream;  // Stream for reading from the source file.
			OutputStream outputStream;   // Stream for writing the copy.

			String packagePath=null;
			String adempiereSourcePath=null;

			//get adempiere-all directory
			try {
				packagePath = getPackageDirectory(ctx.ctx);
				File parentDirectory = new File(packagePath);
				while (!parentDirectory.getName().equals("packages")){
					parentDirectory = parentDirectory.getParentFile();
				}
				parentDirectory = parentDirectory.getParentFile();
				adempiereSourcePath = parentDirectory.getCanonicalPath();
			} catch (IOException e1) {
				System.out.println("Can't find adempiere-all directory.");
			}


			//	Create backup directory if required
			File backupDir = new File(packagePath+File.separator+"backup"+File.separator);
			if (!backupDir.exists()){
				boolean success = (new File(packagePath+File.separator+"backup"+File.separator)).mkdirs();
				if (!success) {
					log.info("Backup directory creation failed");
				}
			}

			//Correct target directory for proper file seperator
			String fullTargetPath = adempiereSourcePath+targetDirectory;
			char slash1 = '\\';
			char slash2 = '/';
			if (File.separator.equals("/"))
				fullTargetPath = fullTargetPath.replace(slash1,slash2);
			else
				fullTargetPath = fullTargetPath.replace(slash2,slash1);

			File file = new File(fullTargetPath+fileName);
			//TODO: derive force from user parameter
			boolean force = true;
			String fileDate = null;
			//check to see if overwrites are allowed
			if (file.exists())
			{
				if (!force) {
					System.out.println(
					"Output file exists.  Use the -f option to replace it.");
					return;
				}
				//backup file to package directory
				else {
					action = "Update";
					if (log.isLoggable(Level.INFO)) log.info("Target Backup:"+fullTargetPath+fileName);
					inputStream = OpenInputfile(fullTargetPath+fileName);
					SimpleDateFormat formatter_file = new SimpleDateFormat("yyMMddHHmmssSSSSZ");
					Date today = new Date();
					fileDate = formatter_file.format(today);
					outputStream = OpenOutputfile(packagePath+File.separator+"backup"+File.separator+fileDate+"_"+fileName);
					if (log.isLoggable(Level.INFO)) log.info("Source Backup:"+packagePath+File.separator+"backup"+File.separator+fileDate+"_"+fileName);
					copyFile (inputStream, outputStream);
					log.info("Backup Complete");
				}
			}

//			Correct dist directory for proper file seperator
			String fullSourcePath=null;
			if (File.separator.equals("/"))
				fullSourcePath = sourceDirectory.replace(slash1,slash2);
			else
				fullSourcePath = sourceDirectory.replace(slash2,slash1);
			inputStream = OpenInputfile(packagePath+fullSourcePath+fileName);

//			Create Target directory if required
			File targetDir = new File(fullTargetPath);
			if (!targetDir.exists()){
				boolean success = (new File(fullTargetPath)).mkdirs();
				if (!success) {
					log.info("Target directory creation failed");
				}
			}
			outputStream = OpenOutputfile(fullTargetPath+fileName);
			//Copy File
			int success = copyFile (inputStream,outputStream);
			//Record in log
			X_AD_Package_Imp_Detail impDetail = createImportDetail(ctx, "file", fileName, 0);
			if (success != -1){
				try {
					logImportDetail (ctx, impDetail, 1, fileName, 0, action);
				} catch (SAXException e) {
					if (log.isLoggable(Level.INFO)) log.info ("setfile:"+e);
				}
			}
			else{
				try {
					logImportDetail (ctx, impDetail, 0, fileName, 0, action);
				} catch (SAXException e) {
					if (log.isLoggable(Level.INFO)) log.info ("setfile:"+e);
				}
			}
			//Record in transaction file
			X_AD_Package_Imp_Backup backup = new X_AD_Package_Imp_Backup(ctx.ctx, 0, getTrxName(ctx));
			backup.setAD_Org_ID(Env.getAD_Org_ID(ctx.ctx));
			backup.setAD_Package_Imp_Org_Dir(fullTargetPath+fileName);
			backup.setAD_Package_Imp_Bck_Dir(packagePath+File.separator+"backup"+File.separator+fileDate+"_"+fileName);
			backup.setAD_Package_Imp_ID(getPackageImpId(ctx.ctx));
			backup.saveEx();

		}
	}

	public void endElement(PIPOContext ctx, Element element) throws SAXException {
	}

	public void create(PIPOContext ctx, TransformerHandler document)
			throws SAXException {
		String FileName = Env.getContext(ctx.ctx, X_AD_Package_Exp_Detail.COLUMNNAME_FileName);
		String Source_Directory = Env.getContext(ctx.ctx, "Source_Directory");
		String Target_Directory = Env.getContext(ctx.ctx, X_AD_Package_Exp_Detail.COLUMNNAME_Target_Directory);
		String ReleaseNo = Env.getContext(ctx.ctx, X_AD_Package_Exp_Detail.COLUMNNAME_ReleaseNo);
		AttributesImpl atts = new AttributesImpl();
		addTypeName(atts, "custom");
		document.startElement("","","Dist_File",atts);
		addTextProperty(document,"filename",FileName);
		addTextProperty(document,"sourceDirectory",Source_Directory);
		addTextProperty(document,"targetDirectory",Target_Directory);
		addTextProperty(document,"ReleaseNo",ReleaseNo);
		atts.addAttribute("","","ReleaseNo","CDATA",ReleaseNo);
		document.endElement("","","Dist_File");
	}

	public void doPackout(PackOut packout, MPackageExp header, PackoutItem detail,TransformerHandler packOutDocument,TransformerHandler packageDocument,AttributesImpl atts,int recordId) throws Exception
	{
		Env.setContext(packout.getCtx().ctx, X_AD_Package_Exp_Detail.COLUMNNAME_FileName, (String)detail.getProperty("FileName"));
		Env.setContext(packout.getCtx().ctx, X_AD_Package_Exp_Detail.COLUMNNAME_ReleaseNo, (String)detail.getProperty("ReleaseNo"));
		Env.setContext(packout.getCtx().ctx, X_AD_Package_Exp_Detail.COLUMNNAME_Target_Directory, (String)detail.getProperty("TargetDirectory"));
		Env.setContext(packout.getCtx().ctx, "Source_Directory", fileDest);
		this.create(packout.getCtx(), packOutDocument);
		packout.getCtx().ctx.remove(X_AD_Package_Exp_Detail.COLUMNNAME_FileName);
		packout.getCtx().ctx.remove(X_AD_Package_Exp_Detail.COLUMNNAME_ReleaseNo);
		packout.getCtx().ctx.remove(X_AD_Package_Exp_Detail.COLUMNNAME_Target_Directory);
		packout.getCtx().ctx.remove("Source_Directory");
	}

	@Override
	public void packOut(PackOut packout, TransformerHandler packoutHandler,
			TransformerHandler docHandler,
			int recordId) throws Exception {
	}
}
