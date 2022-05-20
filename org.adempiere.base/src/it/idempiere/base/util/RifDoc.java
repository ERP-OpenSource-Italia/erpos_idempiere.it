package it.idempiere.base.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RifDoc
{
	private String numerodocumento;
	private Date datadocumento;
	private String descrizione;

	/**
	 * @param numerofattura
	 */
	public void setNumeroDocumento(String numeroDocumento) {
		this.numerodocumento = numeroDocumento;
	}

	public String getNumeroDocumento() {
		return numerodocumento;
	}

	/**
	 * @param datafattura
	 */
	public void setDataDocumento(Date dataDocumento) {
		this.datadocumento = dataDocumento;
	}

	public  Date getDataDocumento() {
		return datadocumento;
	}
	
	public String getDescrizione() {
		if(descrizione == null)
			return getNumeroDocumento() + new SimpleDateFormat("ddMMyy").format(getDataDocumento());
		else
			return descrizione;
	}
	
	public void setDescrizione(String sDescrizione) {
		this.descrizione = sDescrizione;
	}
}

