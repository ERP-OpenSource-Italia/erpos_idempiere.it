package com.ls.idempiere;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.axis.soap.MessageFactoryImpl;
import org.apache.axis.soap.SOAPConnectionFactoryImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LSSOAPWSUtil {

	public static String submitOrder(String soapEndpointUrl,String data) {
		
		soapEndpointUrl = "https://195.242.186.3/MotoBiznesWS/WSMotoKlientTD.asmx";

		return callSoapWebServiceSubmitOrder(soapEndpointUrl, data);
	}
	
	public static List<String> getInvoices(String soapEndpointUrl,String fiks,String nr_kontrahenta_motonet,String data,boolean isLines) {
		
		soapEndpointUrl = "https://195.242.186.3/MotoBiznesWS/WSMotoKlient.asmx";

		return callSoapWebServiceGetInvoices(soapEndpointUrl, data,fiks,nr_kontrahenta_motonet,isLines);
	}
	
	private static List<String> callSoapWebServiceGetInvoices(String soapEndpointUrl, String data,String fiks, String nr_kontrahenta_motonet, boolean isLines) {

		List<String> dataResponse = null;
		SOAPConnection soapConnection = null;
		
		try {
			SOAPConnectionFactory cf = new SOAPConnectionFactoryImpl();
			soapConnection = cf.createConnection();
			// Create SOAP Connection
			// Send SOAP Message to SOAP Server
			SOAPMessage soapResponse = soapConnection.call(createSOAPGetInvoices(data,fiks,nr_kontrahenta_motonet,isLines),
					soapEndpointUrl);

			dataResponse = getInvoicesResponse(soapResponse,isLines);
			

		} catch (Exception e) {
			System.err.println(
					"\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				soapConnection.close();
			} 
			catch (SOAPException e) 
			{
				e.printStackTrace();
			}
		}

		return dataResponse;
	}

	private static String callSoapWebServiceSubmitOrder(String soapEndpointUrl, String data) {

		String dataResponse = null;
		SOAPConnection soapConnection = null;
		
		try {
			SOAPConnectionFactory cf = new SOAPConnectionFactoryImpl();
			soapConnection = cf.createConnection();
			// Create SOAP Connection
			// Send SOAP Message to SOAP Server
			SOAPMessage soapResponse = soapConnection.call(createSOAPSubmitOrderData(data),
					soapEndpointUrl);

			dataResponse = getSubmitOrderError(soapResponse);
			

		} catch (Exception e) {
			System.err.println(
					"\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				soapConnection.close();
			} 
			catch (SOAPException e) 
			{
				e.printStackTrace();
			}
		}

		return dataResponse;
	}

	private static String getSubmitOrderError(SOAPMessage response) throws SOAPException {

		String errorResponse =null;

		Iterator itr = response.getSOAPBody().getChildElements();
		while (itr.hasNext()) {
			Node node = (Node) itr.next();
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element ele = (Element) node;

				NodeList nodeList = ele.getChildNodes();
				
				for(int i=0; i < nodeList.getLength();)
				{
					Node nodeOfList = nodeList.item(i);
					Element eleOfList = (Element) nodeOfList;
					System.out.println(eleOfList.getFirstChild());
					
					errorResponse = eleOfList.getFirstChild().toString();
					break;
				}
			}
		}

		return errorResponse;
	}
	
	private static List<String> getInvoicesResponse(SOAPMessage response,boolean isLines) throws SOAPException {

		List<String> listResponse = new ArrayList<String>();

		Iterator itr = response.getSOAPBody().getChildElements();
		while (itr.hasNext()) {
			Node node = (Node) itr.next();
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element ele = (Element) node;

				NodeList nodeList = ele.getChildNodes();
				
				for(int i=0; i < nodeList.getLength();i++)
				{
					Node nodeOfList = nodeList.item(i);
					Element eleOfList = (Element) nodeOfList;
					
					if(eleOfList.getFirstChild() != null && isLines == false)
					{
							listResponse.add(eleOfList.getFirstChild().toString()
									.replace("<ns1:string xmlns:ns1=\"http://moto-profil.pl/\">", "").replace("</ns1:string>", ""));
					}
					else if(eleOfList.getFirstChild() != null)
					{
						NodeList listNodeLines = eleOfList.getChildNodes();
						for(int a=0; a < listNodeLines.getLength();a++)
						{
							Node nodeOfListLine = listNodeLines.item(a);
							
							listResponse.add(nodeOfListLine.toString()
									.replace("<ns1:string xmlns:ns1=\"http://moto-profil.pl/\">", "").replace("</ns1:string>", ""));
						}
					}
				}
			}
		}

		return listResponse;
	}
	
	

	private static SOAPMessage createSOAPGetInvoices(String data,String fiks, String nr_kontrahenta_motonet, boolean isLines) throws Exception {
		MessageFactory mf = new MessageFactoryImpl();
		SOAPMessage soapMessage = mf.createMessage();

		createSoapEnvelopeGetInvoiceData(soapMessage,data,fiks,nr_kontrahenta_motonet,isLines);

		MimeHeaders headers = soapMessage.getMimeHeaders();
		String soapAction = isLines ? "http://moto-profil.pl/ZwrocPozycjeFaktury" : "http://moto-profil.pl/ZwrocNumeryFaktur";
		headers.addHeader("SOAPAction", soapAction);

		soapMessage.saveChanges();

		/* Print the request message, just for debugging purposes */
		System.out.println("Request SOAP Message:");
		soapMessage.writeTo(System.out);
		System.out.println("\n");

		return soapMessage;
	}
	
	private static SOAPMessage createSOAPSubmitOrderData(String data) throws Exception {
		MessageFactory mf = new MessageFactoryImpl();
		SOAPMessage soapMessage = mf.createMessage();

		createSoapEnvelopeSubmitOrderData(soapMessage,data);

		MimeHeaders headers = soapMessage.getMimeHeaders();
		String soapAction = "http://moto-profil.pl/SubmitOrder";
		headers.addHeader("SOAPAction", soapAction);

		soapMessage.saveChanges();

		/* Print the request message, just for debugging purposes */
		System.out.println("Request SOAP Message:");
		soapMessage.writeTo(System.out);
		System.out.println("\n");

		return soapMessage;
	}
	
	
	private static void createSoapEnvelopeGetInvoiceData(SOAPMessage soapMessage, String data,String fiks, String nr_kontrahenta_motonet, boolean isLines )
			throws SOAPException {
		SOAPPart soapPart = soapMessage.getSOAPPart();

		String myNamespace = "moto";
		//String myNamespaceURI = "urn:ec.europa.eu:taxud:vies:services:checkVat:types";
		String myNamespaceURI = "http://moto-profil.pl/";

		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);

		/*
		 * Constructed SOAP Request Message: <SOAP-ENV:Envelope
		 * xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"
		 * xmlns:myNamespace="http://www.webserviceX.NET"> <SOAP-ENV:Header/>
		 * <SOAP-ENV:Body> <myNamespace:GetInfoByCity> <myNamespace:USCity>New
		 * York</myNamespace:USCity> </myNamespace:GetInfoByCity> </SOAP-ENV:Body>
		 * </SOAP-ENV:Envelope>
		 */

		// SOAP Body
		SOAPBody soapBody = envelope.getBody();
		SOAPElement sub_data = soapBody.addChildElement(isLines ? "ZwrocPozycjeFaktury":"ZwrocNumeryFaktur", myNamespace);
		
		SOAPElement soap_fiks = sub_data.addChildElement("fiks", myNamespace);
		soap_fiks.addTextNode(fiks);
		
		SOAPElement soap_nr_kontrahenta_motonet = sub_data.addChildElement("nr_kontrahenta_motonet", myNamespace);
		soap_nr_kontrahenta_motonet.addTextNode(nr_kontrahenta_motonet);
		
		SOAPElement specify_data = sub_data.addChildElement(isLines ? "numer_ksiegowy":"data", myNamespace);
		specify_data.addTextNode(data);
		
	}
	
	
	private static void createSoapEnvelopeSubmitOrderData(SOAPMessage soapMessage, String data)
			throws SOAPException {
		SOAPPart soapPart = soapMessage.getSOAPPart();

		String myNamespace = "moto";
		//String myNamespaceURI = "urn:ec.europa.eu:taxud:vies:services:checkVat:types";
		String myNamespaceURI = "http://moto-profil.pl/";

		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);

		/*
		 * Constructed SOAP Request Message: <SOAP-ENV:Envelope
		 * xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"
		 * xmlns:myNamespace="http://www.webserviceX.NET"> <SOAP-ENV:Header/>
		 * <SOAP-ENV:Body> <myNamespace:GetInfoByCity> <myNamespace:USCity>New
		 * York</myNamespace:USCity> </myNamespace:GetInfoByCity> </SOAP-ENV:Body>
		 * </SOAP-ENV:Envelope>
		 */

		// SOAP Body
		SOAPBody soapBody = envelope.getBody();
		SOAPElement soapSubmitOrder = soapBody.addChildElement("SubmitOrder", myNamespace);
		
		SOAPElement soapBodyParam = soapSubmitOrder.addChildElement("param", myNamespace);
		soapBodyParam.addTextNode(data);
		
	}
}
