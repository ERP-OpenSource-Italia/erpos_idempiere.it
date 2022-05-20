/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.plaf;

import java.util.ListResourceBundle;

/**
 *  Translation Texts for Look & Feel
 *
 *  @author     Gabriele Vivinetto - gabriele.mailing@rvmgroup.it
 *  @author     Angelo Dabala' (genied) nectosoft - translation fixes 
 *  @version    $Id: PlafRes_it.java,v 1.2 2006/07/30 00:52:24 jjanke Exp $
 */
public class PlafRes_it extends ListResourceBundle
{
	/** The data    */
	static final Object[][] contents = new String[][]
	{
	//{ "BackColType",            "Background Color Type" },
	  { "BackColType",            "Tipo Colore di Sfondo" },
	//{ "BackColType_Flat",       "Flat" },
	  { "BackColType_Flat",       "Pieno" },
	//{ "BackColType_Gradient",   "Gradient" },
	  { "BackColType_Gradient",   "Gradiente" },
	//{ "BackColType_Lines",      "Lines" },
	  { "BackColType_Lines",      "Linee" },
	//{ "BackColType_Texture",    "Texture" }, //Need to be checked. How to translate "Texture" ?
	  { "BackColType_Texture",    "Texture" },
	//
	//{ "LookAndFeelEditor",      "Look & Feel Editor" }, //Need to be checked
	  { "LookAndFeelEditor",      "Editor di Aspetto" },
	//{ "LookAndFeel",            "Look & Feel" },
	  { "LookAndFeel",            "Aspetto" },
	//{ "Theme",                  "Theme" },
	  { "Theme",                  "Tema" },
	//{ "EditAdempiereTheme",      "Edit Adempiere Theme" }, //Need to be checked
	  { "EditAdempiereTheme",      "Modifica Tema di ADempiere" },
	//{ "SetDefault",             "Default Background" },
	  { "SetDefault",             "Sfondo Predefinito" },
	//{ "SetDefaultColor",        "Background Color" },
	  { "SetDefaultColor",        "Colore di Sfondo" },
	//{ "ColorBlind",             "Color Deficiency" }, //Need to be checked
	  { "ColorBlind",             "Mancanza di Colore" },
	//{ "Example",                "Example" },
	  { "Example",                "Esempio" },
	//{ "Reset",                  "Reset" }, //Need to be checked
	  { "Reset",                  "Ripristina" },
	//{ "OK",                     "OK" },
	  { "OK",                     "OK" },
	//{ "Cancel",                 "Cancel" },
	  { "Cancel",                 "Annulla" },
	//
	//{ "AdempiereThemeEditor",    "Adempiere Theme Editor" }, //Need to be checked
	  { "AdempiereThemeEditor",    "Editor dei Temi di ADdempiere" },
	//{ "MetalColors",            "Metal Colors" },
	  { "MetalColors",            "Colori Metallici" },
	//{ "AdempiereColors",         "Adempiere Colors" },
	  { "AdempiereColors",         "Colori di ADempiere" },
	//{ "AdempiereFonts",          "Adempiere Fonts" }, //Need to be checked. Mantain the word "Font" ?
	  { "AdempiereFonts",          "Caratteri di ADempiere" },
	//{ "Primary1Info",           "Shadow, Separator" }, //Need to be checked
	  { "Primary1Info",           "Ombra, Separatore" },
	//{ "Primary1",               "Primary 1" },
	  { "Primary1",               "Primario 1" },
	//{ "Primary2Info",           "Focus Line, Selected Menu" }, //Need to be checked
	  { "Primary2Info",           "Linea Selezionata, Menu Selezionato" },
	//{ "Primary2",               "Primary 2" },
	  { "Primary2",               "Primario 2" },
	//{ "Primary3Info",           "Table Selected Row, Selected Text, ToolTip Background" }, //Need to be checked
	  { "Primary3Info",           "Riga di Tabella Selezionata, Testo Selezionato, Sfondo dei Suggerimenti " },
	//{ "Primary3",               "Primary 3" },
	  { "Primary3",               "Primario 3" },
	//{ "Secondary1Info",         "Border Lines" },
	  { "Secondary1Info",         "Linee dei Bordi" },
	//{ "Secondary1",             "Secondary 1" },
	  { "Secondary1",             "Secondario 1" },
	//{ "Secondary2Info",         "Inactive Tabs, Pressed Fields, Inactive Border + Text" }, //Need to be checked
	  { "Secondary2Info",         "Tab Inattivi, Campi Selezionati, Bordo + Testo inattivi" },
	//{ "Secondary2",             "Secondary 2" },
	  { "Secondary2",             "Secondario 2" },
	//{ "Secondary3Info",         "Background" },
	  { "Secondary3Info",         "Sfondo" },
	//{ "Secondary3",             "Secondary 3" },
	  { "Secondary3",             "Secondario 3" },
	//
	//{ "ControlFontInfo",        "Control Font" }, //Need to be checked
	  { "ControlFontInfo",        "Caratteri dei Controlli" },
	//{ "ControlFont",            "Label Font" }, //Need to be checked
	  { "ControlFont",            "Font di Etichetta" },
	//{ "SystemFontInfo",         "Tool Tip, Tree nodes" }, //Need to be checked
	  { "SystemFontInfo",         "Suggerimenti, Nodi degli Alberi" },
	//{ "SystemFont",             "System Font" }, //Need to be checked
	  { "SystemFont",             "Caratteri di Sistema" },
	//{ "UserFontInfo",           "User Entered Data" },
	  { "UserFontInfo",           "Dati Immessi dall'Utente" },
	//{ "UserFont",               "Field Font" }, //Need to be checked
	  { "UserFont",               "Caratteri dei Campi" },
//	{ "SmallFontInfo",          "Reports" },
	//{ "SmallFont",              "Small Font" }, //Need to be checked
	  { "SmallFont",              "Caratteri Piccoli" },
	//{ "WindowTitleFont",         "Title Font" }, //Need to be checked
	  { "WindowTitleFont",         "Caratteri dei Titoli" },
	//{ "MenuFont",               "Menu Font" }, //Need to be checked
	  { "MenuFont",               "Caratteri dei Menu" },
	//
	//{ "MandatoryInfo",          "Mandatory Field Background" },
	  { "MandatoryInfo",          "Sfondo Campi Obbligatori" },
	//{ "Mandatory",              "Mandatory" },
	  { "Mandatory",              "Obbligatorio" },
	//{ "ErrorInfo",              "Error Field Background" },
	  { "ErrorInfo",              "Sfondo Campi di Errore" },
	//{ "Error",                  "Error" },
	  { "Error",                  "Errore" },
	//{ "InfoInfo",               "Info Field Background" }, //Need to be checked. Is it better "Informativo" ? What with the following ?
	  { "InfoInfo",               "Sfondo Campi Informazione" },
	//{ "Info",                   "Info" },
	  { "Info",                   "Informazione" },
	//{ "WhiteInfo",              "Lines" },
	  { "WhiteInfo",              "Linee" },
	//{ "White",                  "White" },
	  { "White",                  "Bianco" },
	//{ "BlackInfo",              "Lines, Text" },
	  { "BlackInfo",              "Linee, Testo" },
	//{ "Black",                  "Black" },
	  { "Black",                  "Nero" },
	//{ "InactiveInfo",           "Inactive Field Background" },
	  { "InactiveInfo",           "Sfondo Campi Inattivo" },
	//{ "Inactive",               "Inactive" },
	  { "Inactive",               "Inattivo" },
	//{ "TextOKInfo",             "OK Text Foreground" }, //Need to be checked. How to translate Foreground ?
	  { "TextOKInfo",             "Colore Testo OK" },
	//{ "TextOK",                 "Text - OK" }, //Need to be checked
	  { "TextOK",                 "Testo - OK" },
	//{ "TextIssueInfo",          "Error Text Foreground" }, //Need to be checked
	  { "TextIssueInfo",          "Colore Testo di Errore" },
	//{ "TextIssue",              "Text - Error" },
	  { "TextIssue",              "Testo - Errore" },
	//
	//{ "FontChooser",            "Font Chooser" }, //Need to be checked
	  { "FontChooser",            "Selezionatore Carattere" },
	//{ "Fonts",                  "Fonts" }, //Need to be checked
	  { "Fonts",                  "Caratteri" },
	//{ "Plain",                  "Plain" }, //Need to be checked
	  { "Plain",                  "Normale" },
	//{ "Italic",                 "Italic" },
	  { "Italic",                 "Corsivo" },
	//{ "Bold",                   "Bold" },
	  { "Bold",                   "Grassetto" },
	//{ "BoldItalic",             "Bold & Italic" },
	  { "BoldItalic",             "Grassetto & Corsivo" },
	//{ "Name",                   "Name" },
	  { "Name",                   "Nome" },
	//{ "Size",                   "Size" },
	  { "Size",                   "Dimensione" },
	//{ "Style",                  "Style" },
	  { "Style",                  "Stile" },
	//{ "TestString",             "This is just a Test! The quick brown Fox is doing something. 12,3456.78 LetterLOne = l1 LetterOZero = O0" },
	  { "TestString",             "Questo \u00e8 solo un Test! La veloce volpe marrone st\u00e0 facendo qualcosa. 12,3456.78 LetteraLUno = l1 LetteraOZero = O0" },
	//{ "FontString",             "Font" },
	  { "FontString",             "Carattere" }, //Need to be checked
	//
	//{ "AdempiereColorEditor",    "Adempiere Color Editor" }, //Need to be checked
	  { "AdempiereColorEditor",    "Editor di Colori ADempiere" },
	//{ "AdempiereType",           "Color Type" },
	  { "AdempiereType",           "Tipo di Colore" },
	//{ "GradientUpperColor",     "Gradient Upper Color" },
	  { "GradientUpperColor",     "Colore Gradiente Superiore" },
	//{ "GradientLowerColor",     "Gradient Lower Color" },
	  { "GradientLowerColor",     "Colore Gradiente Inferiore" },
	//{ "GradientStart",          "Gradient Start" },
	  { "GradientStart",          "Inizio Gradiente" },
	//{ "GradientDistance",       "Gradient Distance" },
	  { "GradientDistance",       "Distanza Gradiente" },
	//{ "TextureURL",             "Texture URL" }, //Need to be checked. How to translate "Texture" ?
	  { "TextureURL",             "URL Texture" },
	//{ "TextureAlpha",           "Texture Alpha" }, //Need to be checked. How to translate ?
	  { "TextureAlpha",           "Texture Alpha" },
	//{ "TextureTaintColor",      "Texture Taint Color" }, //Need to be checked. How to translate ?
	  { "TextureTaintColor",      "Texture Taint Color" },
	//{ "LineColor",              "Line Color" },
	  { "LineColor",              "Colore Linea" },
	//{ "LineBackColor",          "Background Color" },
	  { "LineBackColor",          "Colore Sfondo" },
	//{ "LineWidth",              "Line Width" },
	  { "LineWidth",              "Spessore Linea" },
	//{ "LineDistance",           "Line Distance" },
	  { "LineDistance",           "Distanza Linea" },
	//{ "FlatColor",              "Flat Color" }
	  { "FlatColor",              "Colore Pieno" }
	};

	/**
	 * Get Contents
	 * @return contents
	 */
	public Object[][] getContents()
	{
		return contents;
	}
}   //  Res
