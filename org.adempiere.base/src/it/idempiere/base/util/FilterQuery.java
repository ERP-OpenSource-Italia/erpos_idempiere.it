package it.idempiere.base.util;

import org.compiere.util.Env;

public class FilterQuery
{
	public static final String SPECIAL_CHAR_FUNCTION = "upper(replace(translate(?,'[.-+_,:''\"]', ' '), ' ',''))";
	public static final String REGEX = "[\\[\\.\\-\\+_,:'\"\\]]";

	protected static final String SPECIAL_LETTERS_FROM = "ÀÁÂÃÄÅĀĂĄǍǞǠǺȀȂȦȺɃƁƄÇĆĈĊČȻÐĎĐÈÉÊËĒĔĖĘĚȄȆȨɆĜĞĠĢǤǦǴĤĦȞÌÍÎÏĨĪĬĮİȈȊǏĴǰɈĶĸǨĹĻĽĿŁȽÑŃŅŇŉŊǸÒÓÔÕÖØŌŎŐǑǾȌȎȪȬȮȰŔŖŘȐȒɌßŚŜŞŠȘȿŢŤŦȚȾÙÚÛÜŨŪŬŮŰŲǓǕǗǙǛȔȖɄŴŹŻŽÝŸŶȲɎ";
	
	protected static final String SPECIAL_LETTERS_TO = "AAAAAAAAAAAAAAAAABBBCCCCCCDDDEEEEEEEEEEEEEGGGGGGGHHHIIIIIIIIIIIIJJJKKKLLLLLLNNNNNNNOOOOOOOOOOOOOOOOORRRRRRSSSSSSSTTTTTUUUUUUUUUUUUUUUUUUWZZZYYYYY";
	
	protected static final String COLUMN_REGEX_SPECIAL_LETTERS = "translate(TOREPLACE,'FROMLETTERS','TOLETTERS')";

	protected static final String REPLACE_SPECIAL_LETTERS = "TOREPLACE";
	
	protected static final String REPLACE_LETTERS_FROM = "FROMLETTERS";
	
	protected static final String REPLACE_LETTERS_TO = "TOLETTERS";
	
	public static String getFilterFunction(String function)
	{
		String filterFunction = COLUMN_REGEX_SPECIAL_LETTERS.replace(REPLACE_SPECIAL_LETTERS, function);
		filterFunction = filterFunction.replace(REPLACE_LETTERS_FROM, SPECIAL_LETTERS_FROM);
		filterFunction = filterFunction.replace(REPLACE_LETTERS_TO, SPECIAL_LETTERS_TO);
				
		return filterFunction;
	}
	
	public static String filterString(String toFilter)
	{
		toFilter = toFilter.trim().toUpperCase();
		toFilter = toFilter.replaceAll(" ","");
		toFilter = toFilter.replaceAll(FilterQuery.REGEX,"");
		
		if(STDSysConfig.isFilterSpecialLetter(Env.getAD_Client_ID(Env.getCtx()), Env.getAD_Org_ID(Env.getCtx())))
		{
			for(int i = 0; i < SPECIAL_LETTERS_FROM.length(); i++)
			{
				toFilter = toFilter.replace(SPECIAL_LETTERS_FROM.charAt(i), SPECIAL_LETTERS_TO.charAt(i));
			}
		}
		
		return toFilter;
	}
}
