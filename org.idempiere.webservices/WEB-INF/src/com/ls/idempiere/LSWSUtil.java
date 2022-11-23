package com.ls.idempiere;

import java.util.Iterator;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LSWSUtil {

	public static LSBPDataResponse requestBPData(String country, String vatNumber) {

		String soapEndpointUrl = "http://ec.europa.eu/taxation_customs/vies/services/checkVatService/";

		return callSoapWebServiceBPData(soapEndpointUrl, country, vatNumber);
	}

	private static LSBPDataResponse callSoapWebServiceBPData(String soapEndpointUrl, String country, String vatNumber) {

		LSBPDataResponse dataResponse = null;
		SOAPConnection soapConnection = null;
		
		try {
			SOAPConnectionFactory cf = SOAPConnectionFactory.newInstance();
			soapConnection = cf.createConnection();
			// Create SOAP Connection
			String soapAction = "";
			// Send SOAP Message to SOAP Server
			SOAPMessage soapResponse = soapConnection.call(createSOAPRequestBPData(soapAction, country, vatNumber),
					soapEndpointUrl);

			dataResponse = getLSBPDataResponse(soapResponse);
			

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

	private static LSBPDataResponse getLSBPDataResponse(SOAPMessage response) throws SOAPException {

		LSBPDataResponse dataResponse = new LSBPDataResponse();

		Iterator itr = response.getSOAPBody().getChildElements();
		while (itr.hasNext()) {
			Node node = (Node) itr.next();
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element ele = (Element) node;

				NodeList nodeList = ele.getChildNodes();
				
				for(int i=0; i < nodeList.getLength()  ;i++)
				{
					Node nodeOfList = nodeList.item(i);
					Element eleOfList = (Element) nodeOfList;
					System.out.println(eleOfList.getFirstChild());
					
					switch (i) {
					case 0:
						dataResponse.country = eleOfList.getFirstChild().toString();
						break;
					case 1:
						dataResponse.vatNumber = eleOfList.getFirstChild().toString();
						break;
					case 3:
						dataResponse.isValid = eleOfList.getFirstChild().toString().equals("true");
						break;
					case 4:
						dataResponse.ragSoc = eleOfList.getFirstChild().toString();
						break;
					case 5:
						dataResponse.billAddress = eleOfList.getFirstChild().toString();
						break;
					case 6:
						dataResponse.billAddressCapCity = eleOfList.getFirstChild().toString();
						break;
					default:
						break;
					}
				}
			}
		}

		return dataResponse;
	}

	private static SOAPMessage createSOAPRequestBPData(String soapAction, String country, String vatNumber) throws Exception {
		MessageFactory mf = MessageFactory.newInstance();
		SOAPMessage soapMessage = mf.createMessage();

		createSoapEnvelopeBPData(soapMessage, country, vatNumber);

		MimeHeaders headers = soapMessage.getMimeHeaders();
		headers.addHeader("SOAPAction", soapAction);

		soapMessage.saveChanges();

		/* Print the request message, just for debugging purposes */
		System.out.println("Request SOAP Message:");
		soapMessage.writeTo(System.out);
		System.out.println("\n");

		return soapMessage;
	}

	private static void createSoapEnvelopeBPData(SOAPMessage soapMessage, String country, String vatNumber)
			throws SOAPException {
		SOAPPart soapPart = soapMessage.getSOAPPart();

		String myNamespace = "urn";
		String myNamespaceURI = "urn:ec.europa.eu:taxud:vies:services:checkVat:types";

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
		SOAPElement soapBodyElem = soapBody.addChildElement("checkVat", myNamespace);
		SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("countryCode", myNamespace);
		SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("vatNumber", myNamespace);
		soapBodyElem1.addTextNode(country);
		soapBodyElem2.addTextNode(vatNumber);
	}
}
