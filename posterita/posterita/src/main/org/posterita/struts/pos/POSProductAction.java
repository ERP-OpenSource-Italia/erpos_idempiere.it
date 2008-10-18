/**
 *  Product: Posterita Web-Based POS and Adempiere Plugin
 *  Copyright (C) 2007  Posterita Ltd
 *  This file is part of POSterita
 *  
 *  POSterita is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * Created on May 22, 2006
 */


package org.posterita.struts.pos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.compiere.model.MLocator;
import org.compiere.model.MProduct;
import org.compiere.model.MProductPrice;
import org.compiere.model.MStorage;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Trx;
import org.posterita.Constants;
import org.posterita.beans.ProductBean;
import org.posterita.beans.ProductDetailsBean;
import org.posterita.beans.ProductSalesInfoBean;
import org.posterita.beans.ProductSalesSummaryBean;
import org.posterita.businesslogic.POSGoodsManager;
import org.posterita.businesslogic.POSProductManager;
import org.posterita.businesslogic.ProductCart;
import org.posterita.businesslogic.administration.PriceListManager;
import org.posterita.businesslogic.administration.ProductManager;
import org.posterita.businesslogic.administration.WarehouseManager;
import org.posterita.businesslogic.performanceanalysis.CSVReportManager;
import org.posterita.businesslogic.performanceanalysis.ReportManager;
import org.posterita.core.RandomStringGenerator;
import org.posterita.core.TmkJSPEnv;
import org.posterita.core.TrxPrefix;
import org.posterita.exceptions.ApplicationException;
import org.posterita.exceptions.BarcodeAlreadyExistsException;
import org.posterita.exceptions.CannotInactivateProductException;
import org.posterita.exceptions.InvalidBarcodeException;
import org.posterita.exceptions.OperationException;
import org.posterita.exceptions.ProductAlreadyExistException;
import org.posterita.exceptions.ProductNotFoundException;
import org.posterita.form.POSProductForm;
import org.posterita.lib.UdiConstants;
import org.posterita.struts.core.DefaultForm;



public class POSProductAction extends POSDispatchAction
{
    public static final String CREATE_OR_UPDATE_PRODUCT = "createOrUpdateProduct";
    public ActionForward createOrUpdateProduct(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws ApplicationException, OperationException
    {
    	 ActionForward fwd=init(mapping,form,request,response);
         if (fwd!=null)
         {
             return fwd;
         }
         
         Properties ctx=TmkJSPEnv.getCtx(request);
         POSProductForm pf = (POSProductForm)form;
         ProductBean bean = (ProductBean)pf.getBean();
         ProductBean productBean = new ProductBean();
         
         Trx trx = Trx.get(TrxPrefix.getPrefix(),true);
      
         try
         {
        	 trx.start();            
        	 POSProductManager.createOrUpdateProduct(ctx,bean,trx.getTrxName());
        	 productBean = POSProductManager.viewPOSProduct(ctx, bean.getProductId(), trx.getTrxName());
        	 
        	 postGlobalMessage("message.product.saved", request);
        	 trx.commit();
         }
         catch(ProductAlreadyExistException e1)
         {
        	 postGlobalError("error.product.already.exists",request);
        	 trx.rollback();
        	 return mapping.getInputForward();
         }
         catch(BarcodeAlreadyExistsException e2)
         {
        	 postGlobalError("error.barcode.already.exists",request);
        	 trx.rollback();
        	 return mapping.getInputForward();
         }
         catch(NumberFormatException e3)
         {
        	 postGlobalError("error.numberformatexception.price",request);
        	 trx.rollback();
        	 trx.close();
        	 return mapping.getInputForward();
         }
         catch(OperationException e)
         {
        	 postGlobalError("error.process", e.getMessage(), request);
        	 trx.rollback();
        	 trx.close();
        	 return mapping.getInputForward();
         }
         finally
         {
        	 trx.close();
         }
         ArrayList<ProductBean> list = (ArrayList<ProductBean>) request.getSession().getAttribute(Constants.VIEW_POS_PRODUCTS);
         if (list == null)
         {
        	 list = new ArrayList<ProductBean>();
         }
         list.add(productBean);
         ArrayList<ProductBean> productPriceLists = PriceListManager.getProductPriceLists(ctx, bean.getProductId(), bean.getOrgId(), null);
         request.getSession().setAttribute(Constants.PRODUCT_PRICE_LISTS, productPriceLists);
         
         pf.populate(productBean);
    	return mapping.findForward(CREATE_OR_UPDATE_PRODUCT);
    }
    
    public static final String VIEW_PRODUCT = "viewProduct";
    public ActionForward viewProduct(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws ApplicationException, OperationException
    {
        ActionForward fwd=init(mapping,form,request,response);
        if (fwd!=null)
        {
        	return fwd;
        }
        Properties ctx=TmkJSPEnv.getCtx(request);
        POSProductForm pf = (POSProductForm)form;
        Integer productId = Integer.valueOf(request.getParameter("productId"));
        String next =request.getParameter("isNext");
        
        Boolean isNext= null;
        if ("true".equals(next))
        {
        	isNext = true;
        }
        else if ("false".equals(next))
        {
        	isNext = false;
        }
        
        ArrayList<ProductBean> list = (ArrayList<ProductBean>) request.getSession().getAttribute(Constants.VIEW_POS_PRODUCTS); 
        ProductBean bean = null;
                
        bean = POSProductManager.getProductBean(ctx, list, productId, isNext);        
        pf.populate(bean);
        
        ArrayList<ProductBean> productPriceLists = PriceListManager.getProductPriceLists(ctx, productId, bean.getOrgId(), null);
        ArrayList<KeyNamePair> uomList = POSProductManager.getUoms(ctx, null);
        ArrayList taxList = POSGoodsManager.getAllTaxCategory(ctx);   
        
        request.getSession().setAttribute(Constants.UOM_LIST, uomList);
        request.getSession().setAttribute(Constants.EXISTING_BAR_CODE,bean.getBarCode());
        request.getSession().setAttribute(Constants.TAX_CATEGORY_ID,taxList);
        request.getSession().setAttribute(Constants.PRODUCT_PRICE_LISTS, productPriceLists);
        
        return mapping.findForward(VIEW_PRODUCT);
    }
      
    
    /*public static final String UPDATE_PRODUCT_DETAILS="updateProductDetails";
    public ActionForward updateProductDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws ApplicationException, OperationException
    {
        ActionForward fwd=init(mapping,form,request,response);
        if(fwd!=null)
            return fwd;
        Properties ctx=TmkJSPEnv.getCtx(request);
        DefaultForm df = (DefaultForm)form;
        ProductBean bean = (ProductBean)df.getBean();
        bean.setFile(df.getFile());
        String existingBarCode = (String)request.getSession().getAttribute(Constants.EXISTING_BAR_CODE);
        
        Trx trx = Trx.get(TrxPrefix.getPrefix(),true);
        
        try
        {
        	trx.start();
            POSProductManager.editProduct(ctx,bean,existingBarCode,trx.getTrxName());
            trx.commit();
        }
        catch(BarcodeAlreadyExistsException e1)
        {
            
            trx.rollback();
            postGlobalError("error.barcode.already.exists",request);
            return mapping.getInputForward();
        }
        catch(CannotInactivateProductException e2)
        {
            trx.rollback();
            postGlobalError("error.product.cannot.inactivate", e2.getMessage(), request);
            return mapping.getInputForward();
           
        }
        catch(InvalidContentTypeException e3)
        {
        	trx.rollback();
            postGlobalError("error.invalid.content", e3.getMessage(), request);
            return mapping.getInputForward();
        }
        finally
        {
            trx.close();
        }
        ProductBean productBean= POSProductManager.viewPOSProduct(ctx,bean.getProductId().intValue(), null);
        
        ArrayList<ProductBean> list = new ArrayList<ProductBean>();
        list.add(productBean);
        
        refreshProductList(request, list);
        
        request.getSession().setAttribute(Constants.PRODUCT_DETAILS,productBean);
        return mapping.findForward(UPDATE_PRODUCT_DETAILS);
    }*/
        
    public static final String VIEW_ALL_POS_PRODUCTS="viewAllPOSProducts";
    public static final String LIST_PRICE_LISTS = "listPriceLists";
    public ActionForward viewAllPOSProducts(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws ApplicationException, OperationException
    {
        ActionForward fwd=init(mapping,form,request,response);
        if(fwd!=null)
            return fwd;
        Properties ctx=TmkJSPEnv.getCtx(request);
        DefaultForm df = (DefaultForm)form;
        ProductBean bean = (ProductBean) df.getBean();
        ArrayList<ProductBean> list=null;
        
        String fromDeletePriceOnPriceList = request.getParameter("isFromDeletePriceOnPriceList");
        Integer priceListId;
        Integer priceListVersionId;
       
        //Check if user just clicked on the delete price on priceList submenu
        if(fromDeletePriceOnPriceList != null && fromDeletePriceOnPriceList.equals("true"))
        {
        	priceListId = Integer.valueOf(request.getParameter("priceListId"));
        	priceListVersionId = PriceListManager.getPriceListVersionID(ctx, priceListId, null);
        	
        	list = new ArrayList<ProductBean>();	 
        	
        	list = POSProductManager.getProductBeans(ctx, priceListVersionId, null);
    		
    		if(list.isEmpty())
    		{
    			postGlobalError("error.no.product.found.on.pricelist", request);
    			request.setAttribute(Constants.IS_FROM_DELETE_PRODUCT_PRICE, "true");
    			return mapping.findForward(LIST_PRICE_LISTS);
    		}
    		else
    		{
    			request.getSession().setAttribute(Constants.PRICE_LIST_VERSION_ID, priceListVersionId);
    			request.setAttribute(Constants.IS_FROM_DELETE_PRODUCT_PRICE, "true");
    		}
        }
        else
        {
	        try
	        {
	            list=new ArrayList<ProductBean>();
	            list=POSProductManager.viewAllProducts(ctx,bean.getProductName(),bean.getBarCode(), bean.getDescription());
	            
	        }
	        catch(ProductNotFoundException e1)
	        {
	            postGlobalError("error.product.not.defined.client",request);
	            return mapping.getInputForward();
	            
	        }
        }
        
        //for export
        ArrayList<Object[]> exportProductData = null;
        try
        {
            exportProductData = POSProductManager.getExportData(ctx, list);
        }
        catch (IOException e)
        {
            postGlobalError("error.process",request);
            return mapping.getInputForward();
        }
        String csvReport = CSVReportManager.generateCSVReport(ctx,exportProductData);
        String csvURI = ReportManager.getReportURI(csvReport,request); 
        
        request.getSession().setAttribute(Constants.CSV_FILE,csvURI);
        request.getSession().setAttribute(Constants.VIEW_POS_PRODUCTS,list); 
        return mapping.findForward(VIEW_ALL_POS_PRODUCTS);
    }
    
    
    public static final String VIEW_POS_PRODUCT_DETAIL="viewPOSProductDetails";
    public static final String ERROR_VIEW_PRODUCT_DETAIL = "errorViewProductDetails";
    
    public ActionForward viewPOSProductDetailInfo(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws ApplicationException, OperationException
    {
    	  ActionForward fwd=init(mapping,form,request,response);
          if(fwd!=null)
              return fwd;
          Properties ctx=TmkJSPEnv.getCtx(request);
          DefaultForm df = (DefaultForm)form;
          ProductDetailsBean bean = (ProductDetailsBean) df.getBean();
          
          try
          {
        	  int productId = bean.getProductId().intValue();
        	  ProductDetailsBean prodDetailsBean = ProductManager.getProductDetailInfo(ctx, productId, null);
        	  
        	  request.setAttribute(Constants.PRODUCT_DETAIL_INFO, prodDetailsBean);
        	  
        	  return mapping.findForward(VIEW_POS_PRODUCT_DETAIL);
          }
          catch(Exception ex)
          {
        	  return mapping.findForward(ERROR_VIEW_PRODUCT_DETAIL);
          }
    }
    
    public ActionForward validateProductName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException, OperationException, IOException
    {
    	DefaultForm df = (DefaultForm) form;
    	String productName = df.getProductName();
    	Properties ctx=TmkJSPEnv.getCtx(request);
    	 
    	boolean isNameInvalid = (ProductManager.getSimilarProduct(ctx, productName, null) != 0); 
    	 
    	response.setContentType("text/plain");
    	PrintWriter out = response.getWriter();    	
    	out.print(isNameInvalid);
    	out.flush();
    	out.close();    	
    	
    	return null;
    }
    
    public ActionForward validateProductBarcode(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException, OperationException, IOException
    {
    	DefaultForm df = (DefaultForm) form;
    	String barcode = df.getBarCode();
    	Properties ctx=TmkJSPEnv.getCtx(request);
    	 
    	boolean isBarcodeValid = ProductManager.isBarCodePresent(ctx, barcode, null);
    	
    	response.setContentType("text/plain");
    	PrintWriter out = response.getWriter();
    	out.print(isBarcodeValid);
    	out.flush();
    	out.close();
    	
    	return null;
    }
    
    public static final String SEARCH_POS_PRODUCTS = "searchPOSProducts" ;
    
    public ActionForward searchPOSProducts (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException, OperationException, IOException
    {
    	viewAllPOSProducts(mapping, form, request, response);
    	
    	return mapping.findForward(SEARCH_POS_PRODUCTS);
    }
        
    
    public ActionForward addAllProducts (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException, OperationException, IOException
    {
    	Properties ctx=TmkJSPEnv.getCtx(request);
        HttpSession session = request.getSession();
        
        ProductCart cart = (ProductCart) session.getAttribute(Constants.PRODUCT_CART);
        ArrayList<ProductBean> productList = (ArrayList<ProductBean>) session.getAttribute(Constants.VIEW_POS_PRODUCTS); 
        
        if(productList == null)
        {
        	return mapping.findForward(VIEW_ALL_POS_PRODUCTS);
        }
        
        if(cart == null)
        {
        	cart = new ProductCart(ctx);
        }
        
        for(ProductBean bean:productList)
        {
        	Integer productId = bean.getProductId();
        	
        	if(productId == null) continue;
        	if(cart.hasProduct(productId.intValue())) continue;
        	
        	cart.addProduct(productId.intValue());
        }
        
        session.setAttribute(Constants.PRODUCT_CART,cart);
        
        return mapping.findForward(VIEW_ALL_POS_PRODUCTS);
        
    }
    
    public ActionForward addToCart (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException, OperationException, IOException
    {
    	Properties ctx=TmkJSPEnv.getCtx(request);
        DefaultForm df = (DefaultForm)form;
        ProductBean bean = (ProductBean) df.getBean();
        
        Integer productId = bean.getProductId();
        HttpSession session = request.getSession();
        
        ProductCart cart = (ProductCart) session.getAttribute(Constants.PRODUCT_CART);
        
        if( cart == null )
        {
        	cart = new ProductCart( ctx );
        }
        
        cart.addProduct( productId.intValue() );        
        session.setAttribute(Constants.PRODUCT_CART, cart);
        
        String script = "productAdded("+ productId +");setCartCounter("+ cart.getNoOfProducts() +")";
        
        PrintWriter writer = response.getWriter();        
        writer.print( script );
        writer.flush();
        writer.close();
    	
    	return null;
    }
    
    public ActionForward removeFromCart (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException, OperationException, IOException
    {
    	Properties ctx=TmkJSPEnv.getCtx(request);
        DefaultForm df = (DefaultForm)form;
        ProductBean bean = (ProductBean) df.getBean();
        
        Integer productId = bean.getProductId();
        HttpSession session = request.getSession();
        
        ProductCart cart = (ProductCart) session.getAttribute(Constants.PRODUCT_CART);
        
        if( cart == null )
        {
        	cart = new ProductCart( ctx );
        }
        
        cart.removeProduct( productId.intValue() );        
        session.setAttribute(Constants.PRODUCT_CART, cart);
        
        String script = "productRemoved("+ productId +");setCartCounter("+ cart.getNoOfProducts() +")";
        
        PrintWriter writer = response.getWriter();        
        writer.print( script );
        writer.flush();
        writer.close();
    	
    	return null;
    }
    
    public ActionForward clearCart (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException, OperationException, IOException
    {      
        HttpSession session = request.getSession();           
        session.removeAttribute(Constants.PRODUCT_CART);
        request.getSession().removeAttribute(Constants.VIEW_POS_PRODUCTS);
                
        String script = "clearAll();";        
        PrintWriter writer = response.getWriter();        
        writer.print( script );
        writer.flush();
        writer.close();
    	
    	return null;
    }     
    
    public static final String VIEW_CART = "viewCart";
    public ActionForward viewCart(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws ApplicationException, OperationException
    {
        ActionForward fwd=init(mapping,form,request,response);
        if(fwd!=null)
            return fwd;
        
        HttpSession session = request.getSession();  
        ProductCart cart = (ProductCart) session.getAttribute(Constants.PRODUCT_CART);
        
        ArrayList<ProductDetailsBean> productList = null;
        
        if(cart != null)
        {
        	productList = cart.getProducts();
            session.setAttribute(Constants.PRODUCT_DETAILS, productList);
        }
        
        return mapping.findForward(VIEW_CART);
    }
    
    public static final String REMOVE = "remove";
    public ActionForward remove(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws ApplicationException, OperationException
    {
        ActionForward fwd=init(mapping,form,request,response);
        if(fwd!=null)
            return fwd;
        
        HttpSession session = request.getSession();  
        ProductCart cart = (ProductCart) session.getAttribute(Constants.PRODUCT_CART);
        
        DefaultForm df = (DefaultForm) form;
        ProductBean bean = (ProductBean) df.getBean();        
        Integer productId = bean.getProductId();
        
        if(cart != null)
        {
        	cart.removeProduct( productId.intValue() );
        	session.setAttribute(Constants.PRODUCT_CART, cart);
        }
        
        return mapping.findForward(REMOVE);
    }
    
    //----------------------------------------------------------------------------------------------------------------------------------------------------
    
    public static final String EXPORT_CSV = "exportCSV";
    public ActionForward exportCSV(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws ApplicationException, OperationException, IOException
    {
        ActionForward fwd=init(mapping,form,request,response);
        if(fwd!=null)
            return fwd;
        
        Properties ctx=TmkJSPEnv.getCtx(request);
        HttpSession session = request.getSession();  
        ProductCart cart = (ProductCart) session.getAttribute(Constants.PRODUCT_CART);
          
        if(cart == null)
        {
        	String msg = "Cannot print barcode. Reason: cart is empty!";
        	throw new OperationException( msg );
        }
        
        String reportName = POSProductManager.getProductCartAsCSV( ctx, cart );
        String reportURI = ReportManager.getReportURI( reportName, request );        	
        response.sendRedirect(reportURI);        
        
        //return mapping.findForward(EXPORT_CSV);
        return null;
    }
    
    
    public static final String PRINT_BARCODE = "printBarcode";
    public ActionForward printBarcode(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws ApplicationException, OperationException, IOException
    {
        ActionForward fwd=init(mapping,form,request,response);
        if(fwd!=null)
            return fwd;
        
        Properties ctx = TmkJSPEnv.getCtx(request);
        HttpSession session = request.getSession();  
        ProductCart cart = (ProductCart) session.getAttribute(Constants.PRODUCT_CART);
        
        if(cart == null)
        {
        	String msg = "Cannot print barcode. Reason: cart is empty!";
        	throw new OperationException( msg );
        }
        
        String printData = POSProductManager.getPrintBarcodeData( ctx, cart, null ); 
        //send it to client
        
        String filename 	= RandomStringGenerator.randomstring() + ".txt"; 
        String filepath 		= ReportManager.getReportPath( filename );
        
        FileOutputStream fos = new FileOutputStream( new File( filepath ) );
        fos.write( printData.getBytes() );
        fos.flush();
        fos.close();
        
        String fileURL = ReportManager.getReportURI( filename, request );
        response.sendRedirect( fileURL );  
        
        return null;
    }
    
//    public static final String GENERATE_PROD_PDF ="generateproductpdf";
//	public ActionForward generateproductpdf(ActionMapping mapping, ActionForm form,HttpServletRequest request,HttpServletResponse response) throws ApplicationException, OperationException
//	{
//		ActionForward fwd = init(mapping,form,request,response);
//		if (fwd!=null)
//			return fwd;
//		
//		Properties ctx = TmkJSPEnv.getCtx(request);
//		
//		String reportName=POSProductManager.productcatalogue(ctx);
//	
//		ReportManager.writeReport(reportName, response);
//		
//		
//		return null;
//	}
//    

 public static final String VIEW_PRODUCT_SALES_DETAILS = "viewProductSalesDetails";
    public ActionForward viewProductSalesDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws ApplicationException, OperationException, IOException
    {
        ActionForward fwd=init(mapping,form,request,response);
        if(fwd!=null)
            return fwd;
        
        Properties ctx = TmkJSPEnv.getCtx(request);
        HttpSession session = request.getSession();  
        
        DefaultForm df = (DefaultForm) form;
        ProductBean bean = (ProductBean) df.getBean();        
        Integer productId = bean.getProductId();
        
        ProductSalesSummaryBean summaryBean = POSProductManager.getProductSalesInfoSummary( ctx, productId.intValue(), null );
        ArrayList<ProductSalesInfoBean> salesDetails = POSProductManager.getProductSalesInfoDetails( ctx, productId.intValue(), null );
        
        ProductSalesSummaryBean[] bucket = POSProductManager.getSalesBucket(ctx, productId.intValue(), null);
        
        
        session.setAttribute( Constants.PRODUCT_SALES_SUMMARY, summaryBean );
        session.setAttribute( Constants.PRODUCT_SALES_DETAILS, salesDetails );
        session.setAttribute( Constants.PRODUCT_SALES_BUCKET, bucket );
        
        return mapping.findForward( VIEW_PRODUCT_SALES_DETAILS );
    }
    
    
    public static final String UPDATE_BULK_PRODUCT_DETAILS="updateBulkProductDetails";
    public ActionForward updateBulkProductDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws ApplicationException, OperationException
    {
        ActionForward fwd=init(mapping,form,request,response);
        if(fwd!=null)
            return fwd;
        Properties ctx=TmkJSPEnv.getCtx(request);
        DefaultForm df = (DefaultForm)form;
        df.validate(mapping,request);
        
        ProductBean bean = (ProductBean)df.getBean();
        bean.setFile(df.getFile());
        
        ProductCart cart = (ProductCart) request.getSession().getAttribute(Constants.PRODUCT_CART);
        
        Trx trx = Trx.get(TrxPrefix.getPrefix(),true);
        Integer[] productIds = cart.getProductIDs();
        
        try
        {
        	trx.start();
            POSProductManager.editBulkProduct(ctx,productIds,bean,trx.getTrxName());
            trx.commit();
        }
        catch(BarcodeAlreadyExistsException e1)
        {
            
            trx.rollback();
            postGlobalError("error.barcode.already.exists",request);
            return mapping.getInputForward();
        }
        catch(CannotInactivateProductException e2)
        {
            trx.rollback();
            postGlobalError("error.product.cannot.inactivate", e2.getMessage(), request);
            return mapping.getInputForward();
           
        }
        finally
        {
            trx.close();
        }
        
                
        ArrayList<ProductBean> list = new ArrayList<ProductBean>();
        
        for(int product_id : productIds)
        {
        	ProductBean productBean = POSProductManager.viewPOSProduct(ctx, product_id, null);
        	list.add(productBean);
        }        
        refreshProductList(request, list);
        
        return mapping.findForward(UPDATE_BULK_PRODUCT_DETAILS);
    }
    
    
    public static final String INIT_UPDATE_BULK_PRODUCT_DETAILS = "initUpdateBulkProductDetails"; 
    public ActionForward initUpdateBulkProductDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws OperationException
    {
    	Properties ctx=TmkJSPEnv.getCtx(request);
    	ArrayList taxList = POSGoodsManager.getAllTaxCategory(ctx); 
    	
    	request.setAttribute(Constants.TAX_CATEGORY_ID,taxList);    	
    	return mapping.findForward(INIT_UPDATE_BULK_PRODUCT_DETAILS);
    }
    
    /*public static final String VIEW_PRODUCT = "viewProduct"; 
    public ActionForward viewProduct(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws OperationException, ApplicationException
    {    	
    	viewProductForUpdate(mapping,form,request,response);
    	
    	return mapping.findForward(VIEW_PRODUCT);
    }*/
    
    public static final String ACTIVATE_PRODUCT = "activateProduct"; 
    public ActionForward activateProduct(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws OperationException, ApplicationException
    {    	
    	ActionForward fwd=init(mapping,form,request,response);
        if(fwd!=null)
            return fwd;
        Properties ctx=TmkJSPEnv.getCtx(request);
        DefaultForm df = (DefaultForm)form;
        df.validate(mapping,request);
        
        ProductBean bean = (ProductBean)df.getBean();
        Integer productId = bean.getProductId();
        
        ProductManager.activateProducts(ctx, new Integer[]{productId},null);
        
        ProductBean productBean= POSProductManager.viewPOSProduct(ctx,bean.getProductId().intValue(), null);        
        ArrayList<ProductBean> list = new ArrayList<ProductBean>();
        list.add(productBean);
        
        // Refresh product list
        refreshProductList(request, list);
    	
    	return mapping.findForward(ACTIVATE_PRODUCT);
    }
    
    public static final String DEACTIVATE_PRODUCT = "deactivateProduct"; 
    public ActionForward deactivateProduct(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws OperationException, ApplicationException
    {    	
    	ActionForward fwd=init(mapping,form,request,response);
        if(fwd!=null)
            return fwd;
        Properties ctx=TmkJSPEnv.getCtx(request);
        DefaultForm df = (DefaultForm)form;
        df.validate(mapping,request);
        
        ProductBean bean = (ProductBean)df.getBean();
        Integer productId = bean.getProductId();
      
        try
        {
            ProductManager.inactivateProducts(ctx, new Integer[]{productId},null);  
        }
        catch(CannotInactivateProductException e2)
        {
            postGlobalError("error.product.cannot.inactivate", e2.getMessage(), request);
            return mapping.getInputForward();
        }        
        
        ProductBean productBean= POSProductManager.viewPOSProduct(ctx,bean.getProductId().intValue(), null);        
        ArrayList<ProductBean> list = new ArrayList<ProductBean>();
        list.add(productBean);
        
        // Refresh product list
        refreshProductList(request, list);   
        
    	return mapping.findForward(DEACTIVATE_PRODUCT);
    }
    
    public static final String PRICE_CHECK = "priceCheck";
    public ActionForward priceCheck(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws OperationException, ApplicationException
    {    	
    	ActionForward fwd=init(mapping,form,request,response);
    	
        if(fwd!=null)
        {
            return fwd;
        }
        
        Properties ctx=TmkJSPEnv.getCtx(request);
        DefaultForm df = (DefaultForm)form;        
        String barcode = df.getBarCode();
        
        ProductBean productBean = null;
        try 
        {
			productBean = POSProductManager.getProduct(ctx, barcode, null);
			request.setAttribute(Constants.PRODUCT_DETAILS, productBean);
		} 
        catch (InvalidBarcodeException e) 
		{
			postGlobalError("error.invalid.barcode", "barcode", request);
			return mapping.getInputForward();
		}
        catch (ProductNotFoundException e) 
		{
        	postGlobalError("error.notfound", "Product", request);
        	return mapping.getInputForward();
		}
        
        return mapping.findForward(PRICE_CHECK);
    }
    
    
    /**
     * This method is called to update product list present in session
     * @param request
     * @param list
     */
    private void refreshProductList(HttpServletRequest request, ArrayList<ProductBean> productList) 
    {
    	ArrayList<ProductBean> list = (ArrayList<ProductBean>) request.getSession().getAttribute(Constants.VIEW_POS_PRODUCTS);
        if(list == null)
        {
        	return;
        }
    	
    	if(!list.isEmpty())
        {
    		for(ProductBean bean : productList)
    		{
    			int index = list.indexOf(bean);
            	if(index != -1)
            	{
            		list.set(index, bean);
            	}
    		}
    		
    		request.getSession().setAttribute(Constants.VIEW_POS_PRODUCTS, list);        	
        }       
		
	}
    
    
    public static final String GET_PRODUCT_DETAILS = "getProductDetails";
    public ActionForward getProductDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws OperationException, ApplicationException, IOException
    {    	
    	ActionForward fwd=init(mapping,form,request,response);
    	
        if(fwd!=null)
        {
            return fwd;
        }
        
        Properties ctx=TmkJSPEnv.getCtx(request);
        
        String productIdAsStr = request.getParameter("productId");
        String priceListIdAsStr = request.getParameter("priceListId");
        int productId = Integer.parseInt(productIdAsStr);
        int priceListId = Integer.parseInt(priceListIdAsStr);
        
        Env.setContext(ctx, UdiConstants.PRICELIST_CTX_PARAM, priceListId); 	
        String currSymbol = PriceListManager.getCurrency(ctx, priceListId);
        
        ProductBean details = POSProductManager.viewPOSProduct(ctx, productId, priceListId, null);
        
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("name: \"" + details.getProductName() + "\"");
        sb.append(", id: \"" + details.getProductId() + "\"");
        sb.append(", description: \"" + details.getDescription() + "\"");
        sb.append(", barcode: '" + details.getBarCode() + "'");
        sb.append(", taxCategory: '" + details.getTaxCategoryName() + "'");
        sb.append(", productCategory: '" + details.getProductCategoryName() + "'");
        sb.append(", priceStd: '" + details.getPriceStandard() + "'");
        sb.append(", priceList: '" + details.getPriceList() + "'");
        sb.append(", priceLimit: '" + details.getPriceLimit() + "'");
        sb.append(", priceListID: '" + details.getPriceListId() + "'");
        sb.append(", taxRate: '" + details.getTaxRate() + "'");
        sb.append(", isTaxIncluded: '" + details.getIsTaxIncluded() + "'");
        sb.append(", currSymbol: '" + currSymbol + "'");
        sb.append(", stockQty: '" + details.getQtyOnHand() + "'");
        sb.append(", unitsPerPack: '" + details.getUnitsPerPack() + "'");
        sb.append("}");
        
        PrintWriter writer = response.getWriter();        
        writer.print( sb.toString() );
        writer.flush();
        writer.close();
        
        return null;
    }
    
    public static final String GET_PRODUCT_DETAILS_FOR_INVENTORY = "getProductDetailsForInventory";
    public ActionForward getProductDetailsForInventory(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws OperationException, ApplicationException, IOException
    {       
        ActionForward fwd=init(mapping,form,request,response);
        
        if(fwd!=null)
        {
            return fwd;
        }
        
        Properties ctx=TmkJSPEnv.getCtx(request);
        
        String productIdAsStr = request.getParameter("productId");
        String priceListIdAsStr = request.getParameter("priceListId");
        int productId = Integer.parseInt(productIdAsStr);
        int priceListId = Integer.parseInt(priceListIdAsStr);
        
        Env.setContext(ctx, UdiConstants.PRICELIST_CTX_PARAM, priceListId);     
        String currSymbol = PriceListManager.getCurrency(ctx, priceListId);
        
        ProductBean details = POSProductManager.viewPOSProductForInventory(ctx, productId, priceListId, null);
        
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("name: '" + details.getProductName() + "'");
        sb.append(", id: '" + details.getProductId() + "'");
        sb.append(", description: '" + details.getDescription() + "'");
        sb.append(", barcode: '" + details.getBarCode() + "'");
        sb.append(", taxCategory: '" + details.getTaxCategoryName() + "'");
        sb.append(", productCategory: '" + details.getProductCategoryName() + "'");
        sb.append(", priceStd: '" + details.getPriceStandard() + "'");
        sb.append(", priceList: '" + details.getPriceList() + "'");
        sb.append(", priceLimit: '" + details.getPriceLimit() + "'");
        sb.append(", priceListID: '" + details.getPriceListId() + "'");
        sb.append(", taxRate: '" + details.getTaxRate() + "'");
        sb.append(", currSymbol: '" + currSymbol + "'");
        sb.append(", stockQty: '" + details.getQtyOnHand() + "'");
        sb.append("}");
        
        PrintWriter writer = response.getWriter();        
        writer.print( sb.toString() );
        writer.flush();
        writer.close();
        
        return null;
    }
    
    public static final String POS_SUB_MENU_ITEMS = "POSSubMenuItems";
    public ActionForward deleteProductPricesOnPriceList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws OperationException, ApplicationException
    {
    	ActionForward fwd=init(mapping,form,request,response);
    	
        if(fwd!=null)
        {
            return fwd;
        }
        
        ArrayList<ProductBean> list = new ArrayList<ProductBean>();
        list = (ArrayList<ProductBean>)request.getSession().getAttribute(Constants.VIEW_POS_PRODUCTS);
                
        Iterator<ProductBean> itr = list.iterator();
        Integer productId;
        Integer priceListVersionId = (Integer)request.getSession().getAttribute(Constants.PRICE_LIST_VERSION_ID);
        Properties ctx = TmkJSPEnv.getCtx(request);
               
        while(itr.hasNext())
        {
        	ProductBean productBean =  itr.next();
        	if (productBean != null)
        	{
        		productId = productBean.getProductId();
        	        	        	
	        	if(productId != null && productId != 0 && priceListVersionId != null)
	        	{
	        		MProductPrice productPrice = MProductPrice.get(ctx, priceListVersionId, productId, null);
	        		productPrice.delete(true);
	        	}
        	}
        }
        
        return mapping.findForward(POS_SUB_MENU_ITEMS);
    }
    
    public static String GET_CREATE_PRODUCT_FORM = "getCreateProductForm";
    public ActionForward getCreateProductForm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException, OperationException
    {
    	viewProduct(mapping, form, request, response);
    	
    	return mapping.findForward(GET_CREATE_PRODUCT_FORM);
    }
    
    public static final String VIEW_PRODUCT_SALES_SUMMARY = "viewProductSalesSummary";
    public ActionForward viewProductSalesSummary(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws ApplicationException, OperationException, IOException
    {
        ActionForward fwd=init(mapping,form,request,response);
        if(fwd!=null)
            return fwd;
        
        Properties ctx = TmkJSPEnv.getCtx(request);
        HttpSession session = request.getSession(); 
        
        String productIdStr = request.getParameter("productId");
        
        if(productIdStr != null && !productIdStr.equals(""))
        {
            int productIdInt = Integer.parseInt(productIdStr);
            StringBuffer sb = new StringBuffer();
                     
            ProductSalesSummaryBean[] summaryBeans = POSProductManager.getSalesBucket( ctx, productIdInt, null );
            
            int warehouseId = WarehouseManager.getDefaultWarehouse(ctx).getM_Warehouse_ID();
            MLocator locator = MLocator.get(ctx, warehouseId, "", "0", "0", "0");
            BigDecimal qtyAvailable = MStorage.getQtyAvailable(warehouseId, locator.get_ID(), productIdInt, 0, null);
            
            MProduct product = new MProduct(ctx, productIdInt, null);
                       
            sb.append("{");
            sb.append("name:'" + product.getName() + "'");
            sb.append(", id:'" + product.getM_Product_ID() + "'");
            sb.append(", qtyOnHand:'" + qtyAvailable + "'");
            
            for(int i = 0; i < summaryBeans.length; i++) 
            {
                if(i != 2 || i != 3) // skip sales figures for 2 and 3 months
                {
                    sb.append(", qtySold" + i +":'" + summaryBeans[i].getQtySold() + "'");
                    sb.append(", totalAmt" + i + ":'" + summaryBeans[i].getTotalAmount() + "'");
                }
            }
            
            sb.append("}");
            
            PrintWriter writer = response.getWriter();        
            writer.print( sb.toString() );
            writer.flush();
            writer.close();
            
        }
        return null;
    }
}
