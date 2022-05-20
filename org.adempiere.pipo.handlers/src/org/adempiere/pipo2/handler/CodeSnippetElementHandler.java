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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import javax.xml.transform.sax.TransformerHandler;


import org.compiere.Adempiere;
import org.compiere.model.X_AD_Package_Imp_Backup;
import org.compiere.model.X_AD_Package_Imp_Detail;

import org.compiere.util.Env;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import org.adempiere.pipo2.AbstractElementHandler;
import org.adempiere.pipo2.CodeSnippetElementParameters;
import org.adempiere.pipo2.Element;
import org.adempiere.pipo2.PIPOContext;
import org.adempiere.pipo2.PackOut;
import org.adempiere.pipo2.PackoutItem;

public class CodeSnippetElementHandler extends AbstractElementHandler {

	public void startElement(PIPOContext ctx, Element element) throws SAXException {
		String action = null;
		action = "Update";
		String releaseNumber = getStringValue(element, "ReleaseNo");
		//Check Release Number
		if(Adempiere.MAIN_VERSION.equals(releaseNumber)||releaseNumber.equals("all")){
			String sourceName = getStringValue(element, "filename");
			String targetDirectory = getStringValue(element, "filedir");
			String oldCode = getStringValue(element, "oldcode");
			String newCode = getStringValue(element, "newcode");

			InputStream source;  // Stream for reading from the source file.
			OutputStream copy;   // Stream for writing the copy.

			String packagePath=null;
			String sourcePath=null;

			//get adempiere-all directory
			try {
				packagePath = getPackageDirectory(ctx.ctx);
				File parentDirectory = new File(packagePath);
				while (!parentDirectory.getName().equals("packages")){
					parentDirectory = parentDirectory.getParentFile();
				}
				parentDirectory = parentDirectory.getParentFile();
				sourcePath = parentDirectory.getCanonicalPath();
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
			String fullDirectory = sourcePath+targetDirectory;
			String targetDirectoryModified=null;
			String fileDate = null;
			char slash1 = '\\';
			char slash2 = '/';
			if (File.separator.equals("/"))
				targetDirectoryModified = fullDirectory.replace(slash1,slash2);
			else
				targetDirectoryModified = fullDirectory.replace(slash2,slash1);

			File file = new File(targetDirectoryModified+sourceName);
			if (log.isLoggable(Level.INFO)) log.info(targetDirectoryModified+sourceName);
			//TODO: derived force from user parameter
			boolean force = true;
			//	check to see if overwrites are allowed
			if (file.exists()) {
				if (!force) {
					System.out.println("Output file exists.  Use the -f option to replace it.");
					return;
				}
				//backup file to package directory
				else {
					action = "Update";
					if (log.isLoggable(Level.INFO)) log.info("Target Backup:"+targetDirectoryModified+sourceName);
					source = OpenInputfile(targetDirectoryModified+sourceName);
					SimpleDateFormat formatter_file = new SimpleDateFormat("yyMMddHHmmssSSSSZ");
					Date today = new Date();
					fileDate = formatter_file.format(today);
					copy = OpenOutputfile(packagePath+File.separator+"backup"+File.separator+fileDate+"_"+sourceName);
					if (log.isLoggable(Level.INFO)) log.info("Source Backup:"+packagePath+File.separator+"backup"+File.separator+fileDate+"_"+sourceName);
					copyFile (source,copy);
					log.info("Backup Complete");
				}
			}

			int success = readReplace(targetDirectoryModified+sourceName, oldCode, newCode);

			X_AD_Package_Imp_Detail impDetail = createImportDetail(ctx, "codesnipit", sourceName, 0);
			//	Record in log
			if (success != -1){
				try {
					logImportDetail (ctx, impDetail, 1, sourceName, 0, action);
				} catch (SAXException e) {
					if (log.isLoggable(Level.INFO)) log.info ("setfile:"+e);
				}
			}
			else{
				try {
					logImportDetail (ctx, impDetail, 0, sourceName, 0, action);
				} catch (SAXException e) {
					if (log.isLoggable(Level.INFO)) log.info ("setfile:"+e);
				}
			}

			//Record in transaction file
			X_AD_Package_Imp_Backup backup = new X_AD_Package_Imp_Backup(ctx.ctx, 0, getTrxName(ctx));
			backup.setAD_Org_ID(Env.getAD_Org_ID(ctx.ctx));
			backup.setAD_Package_Imp_ID(getPackageImpId(ctx.ctx));
			backup.setAD_Package_Imp_Org_Dir(targetDirectoryModified+sourceName );
			backup.setAD_Package_Imp_Bck_Dir(packagePath+File.separator+"backup"+File.separator+fileDate+"_"+sourceName);
			backup.saveEx();

		}
	}


    /**
     *	Find and replace code
     *
     *      @param file name
     *  	@param old string
     *  	@param new string
     *
     */
    public static int readReplace(String fname, String oldPattern, String replPattern){
    	String line;
    	StringBuilder sb = new StringBuilder();

    	try {

    		FileInputStream fis = new FileInputStream(fname);
    		BufferedReader reader=new BufferedReader ( new InputStreamReader(fis));
    		while((line = reader.readLine()) != null) {
    			line = line.replaceAll(oldPattern, replPattern);
    			System.err.println(line);
    			sb.append(line+"\n");
    		}
    		reader.close();
    		BufferedWriter out=new BufferedWriter ( new FileWriter(fname));
    		out.write(sb.toString());
    		out.close();
    	}
    	catch (Throwable e) {
    	            System.err.println("error replacing codesnipit "+e);
    	            return -1;
    	}
    	return 0;
    }


	public void endElement(PIPOContext ctx, Element element)
			throws SAXException {
	}


	public void create(PIPOContext pipoContext, TransformerHandler document)
			throws SAXException {
		String FileDir = Env.getContext(pipoContext.ctx, CodeSnippetElementParameters.DESTINATION_DIRECTORY);
		String FileName = Env.getContext(pipoContext.ctx, CodeSnippetElementParameters.DESTINATION_FILE_NAME);
		String OldCode = Env.getContext(pipoContext.ctx, CodeSnippetElementParameters.AD_Package_Code_Old);
		String NewCode = Env.getContext(pipoContext.ctx, CodeSnippetElementParameters.AD_Package_Code_New);
		String ReleaseNo = Env.getContext(pipoContext.ctx, CodeSnippetElementParameters.RELEASE_NO);
		AttributesImpl atts = new AttributesImpl();
		addTypeName(atts, "custom");
		createSnipitBinding(atts, FileDir, FileName, OldCode, NewCode, ReleaseNo);
		document.startElement("","","Code_Snipit",atts);
		document.endElement("","","Code_Snipit");
	}

	private AttributesImpl createSnipitBinding( AttributesImpl atts, String FileDir, String FileName, String OldCode, String NewCode, String ReleaseNo)
	{
		atts.clear();
		atts.addAttribute("","","filedir","CDATA",FileDir);
		atts.addAttribute("","","filename","CDATA",FileName);
		String preOldCode = OldCode.toString();
		String preNewCode = NewCode.toString();
		String modOldCode = preOldCode.replaceAll("\\$","\\\\\\$").replaceAll("\\.","\\\\.")
		.replaceAll("\\^","\\\\^").replaceAll("\\(","\\\\(").replaceAll("\\)","\\\\)")
		.replaceAll("\\[","\\\\[").replaceAll("\\/","\\\\/").replaceAll("\\+","\\\\+")
		.replaceAll("\\*","\\\\*").replaceAll("\\|","\\\\|");
		String modNewCode = preNewCode.replaceAll("\\$","\\\\\\$").replaceAll("\\.","\\\\.")
		.replaceAll("\\^","\\\\^").replaceAll("\\(","\\\\(").replaceAll("\\)","\\\\)")
		.replaceAll("\\[","\\\\[").replaceAll("\\/","\\\\/").replaceAll("\\+","\\\\+")
		.replaceAll("\\*","\\\\*").replaceAll("\\|","\\\\|");
		atts.addAttribute("","","oldcode","CDATA",modOldCode);
		atts.addAttribute("","","newcode","CDATA",modNewCode);
		atts.addAttribute("","","ReleaseNo","CDATA",ReleaseNo);
		return atts;
	}

	public void packOut(PackOut packout, TransformerHandler packoutHandler, TransformerHandler docHandler,int recordId) throws Exception
	{
		PackoutItem detail = packout.getCurrentPackoutItem();
		Env.setContext(packout.getCtx().ctx, CodeSnippetElementParameters.DESTINATION_DIRECTORY, (String)detail.getProperty(CodeSnippetElementParameters.DESTINATION_DIRECTORY));
		Env.setContext(packout.getCtx().ctx, CodeSnippetElementParameters.DESTINATION_FILE_NAME, (String)detail.getProperty(CodeSnippetElementParameters.DESTINATION_FILE_NAME));
		Env.setContext(packout.getCtx().ctx, CodeSnippetElementParameters.AD_Package_Code_Old, (String)detail.getProperty(CodeSnippetElementParameters.AD_Package_Code_Old));
		Env.setContext(packout.getCtx().ctx, CodeSnippetElementParameters.AD_Package_Code_New, (String)detail.getProperty(CodeSnippetElementParameters.AD_Package_Code_New));
		Env.setContext(packout.getCtx().ctx, CodeSnippetElementParameters.RELEASE_NO, (String)detail.getProperty(CodeSnippetElementParameters.RELEASE_NO));
		this.create(packout.getCtx(), packoutHandler);
		packout.getCtx().ctx.remove(CodeSnippetElementParameters.DESTINATION_DIRECTORY);
		packout.getCtx().ctx.remove(CodeSnippetElementParameters.DESTINATION_FILE_NAME);
		packout.getCtx().ctx.remove(CodeSnippetElementParameters.AD_Package_Code_Old);
		packout.getCtx().ctx.remove(CodeSnippetElementParameters.AD_Package_Code_New);
		packout.getCtx().ctx.remove(CodeSnippetElementParameters.RELEASE_NO);
	}
}
