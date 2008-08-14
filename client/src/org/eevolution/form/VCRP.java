/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
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
 * Copyright (C) 2003-2007 e-Evolution,SC. All Rights Reserved.               *
 * Contributor(s): Victor Perez www.e-evolution.com                           *
 *****************************************************************************/

package org.eevolution.form;


import org.eevolution.form.crp.CRPDatasetFactory;
import org.eevolution.form.crp.CRPModel;
import org.eevolution.model.MPPMRP;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.resources.JFreeChartResources;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
//import org.jfree.ui.ApplicationFrame;
//import org.jfree.ui.RefineryUtilities;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.text.DateFormat;
import java.io.File;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.math.BigDecimal;

import org.compiere.apps.*;
import org.compiere.impexp.*;
import org.compiere.swing.*;
import org.compiere.util.*;
import org.compiere.model.*;
import org.compiere.minigrid.*;
import org.compiere.print.*;
import org.compiere.db.*;

import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.grid.ed.VDate;

import org.compiere.model.MTable;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.grid.ed.*;



public class VCRP extends CPanel
implements FormPanel, ActionListener
{	
	// begin vpj
	private static CLogger log = CLogger.getCLogger(VCRP.class);
	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;
	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		log.info( "VCRP.init");
		m_WindowNo = WindowNo;
		m_frame = frame;
		try
		{
			fillPicks();
			jbInit();
			/*dynInit();*/
			frame.getContentPane().add(northPanel, BorderLayout.NORTH);
			frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
			frame.getContentPane().add(confirmPanel, BorderLayout.SOUTH);
			frame.pack();
			//frame.m_maximize=true;
			//frame.setMaximize(true);

		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "VCRP.init", e);
		}
	}	//	init
	
	
	private CPanel northPanel = new CPanel();
	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout();
	private ConfirmPanel confirmPanel = new ConfirmPanel(true);
	private Hashtable hash = new Hashtable();
	
    private VLookup resource = null; 
    private CLabel resourceLabel =  new CLabel();
    
    private VDate dateFrom = new VDate("DateFrom", true, false, true, DisplayType.Date, "DateFrom");
    private CLabel dateFromLabel =  new CLabel();
    private int AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
    //private DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    //private JFreeChart chart;
    private ChartPanel chartPanel = new ChartPanel(createChart(new DefaultCategoryDataset(), "", null));
    protected CRPModel model;
    //private CPanel chart = new CPanel();
	
	private void jbInit() throws Exception
	{
		
		northPanel.setLayout(new java.awt.GridBagLayout());
		
        resourceLabel.setText(Msg.translate(Env.getCtx(), "S_Resource_ID"));
        
        northPanel.add(resourceLabel,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));               
        
        northPanel.add(resource ,     new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));   

        
        dateFromLabel.setText(Msg.translate(Env.getCtx(), "DateFrom"));
        
        northPanel.add(dateFromLabel,    new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));               
        
        northPanel.add(dateFrom,     new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0)); 	  
        chartPanel.setPreferredSize(new Dimension(750, 550));
		centerPanel.add(chartPanel, BorderLayout.CENTER);		 
        confirmPanel.addActionListener(this);
	}
	
	/**
	 *	Fill Picks
	 *		Column_ID from C_Order
	 *  @throws Exception if Lookups cannot be initialized
	 */
	private void fillPicks() throws Exception
	{
            
                Properties ctx = Env.getCtx();    
                //createChart(dataset);
                MLookup resourceL = MLookupFactory.get (ctx, m_WindowNo, 0, MColumn.getColumn_ID(MProduct.Table_Name,"S_Resource_ID"), DisplayType.TableDir);
                resource = new VLookup ("S_Resource_ID", false, false, true, resourceL);
                
	}	//	fillPicks
	
	public void actionPerformed (ActionEvent e)
	{
		if (e.getActionCommand().equals(ConfirmPanel.A_OK))
		{
			 
			 Timestamp date = (Timestamp) dateFrom.getValue();
			 int S_Resource_ID = ((Integer)resource.getValue()).intValue();  
			 System.out.println("ConfirmPanel.A_OK");
			 System.out.println("date" + date + " S_Resource_ID " + S_Resource_ID);
			 
			 if (date != null && S_Resource_ID != 0)
			 {
			 	System.out.println("Call createDataset(date,S_Resource_ID)");	
			 	 MResource r = MResource.get(Env.getCtx(), S_Resource_ID);
//	Ge�ndert Anfang 04.08.2005	 	
			 	 int uom_id = r.getResourceType().getC_UOM_ID();
			 	 MUOM uom = MUOM.get(Env.getCtx(),uom_id);
			 	
			 	 CategoryDataset dataset = null;
			 	 if(uom.isHour()) {
			 	 	System.out.println("\n ->is Hour<- \n");
			 	 	dataset = createDataset(date,r);
			 	 }
			 	 else {
			 	 	System.out.println("\n ->is not Hour<- \n");
			 	 	dataset = createWeightDataset(date,r);
			 	 }
//	Ge�ndert Ende 04.08.2005	
			 	 
			 	 //CategoryDataset dataset = createDataset();
			 	System.out.println("dataset.getRowCount:" +dataset.getRowCount());
			 	String title = r.getName() != null ? r.getName() : "";
			 	title = title +  " " + r.getDescription() != null ? r.getDescription() : "";
			 	JFreeChart jfreechart = createChart(dataset, title, uom);
			 	centerPanel.removeAll();
			 	chartPanel = new ChartPanel(jfreechart, false);			 	
			 	centerPanel.add(chartPanel, BorderLayout.CENTER);
			 	centerPanel.setVisible(true);			 	
			 	m_frame.pack();
				
			 }
		}
        if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL)) 
        {
             dispose();
        }
	}
	
	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}	//	dispose
	
	
	
	
	
	private JFreeChart  createChart(CategoryDataset dataset  , String title, MUOM uom) 
	{
		JFreeChart chart = ChartFactory.createBarChart3D(title," "," ",dataset,PlotOrientation.VERTICAL,true,true,false);
    
		//Hinzugef�gt Begin 05.08.2005
		if(uom == null || uom.isHour())
		{
			chart = ChartFactory.createBarChart3D
        ( title ,
          Msg.translate(Env.getCtx(), "Days"),            // X-Axis label
          Msg.translate(Env.getCtx(), "Hours"),             // Y-Axis label
          dataset,         // Dataset
		  PlotOrientation.VERTICAL, // orientation
          true,                     // include legend
          true,                     // tooltips?
          false                     // URLs?
        );
			
		
		
        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        //chart.setBackgroundPaint(Color.white);
      
		}
		//Ge�ndert 05.08.2005 Anfang
		else
		{
			chart = ChartFactory.createBarChart3D
	        ( title ,
	          Msg.translate(Env.getCtx(), "Days"),            // X-Axis label
	          Msg.translate(Env.getCtx(), "Kilo"),             // Y-Axis label
	          dataset,         // Dataset
			  PlotOrientation.VERTICAL, // orientation
	          true,                     // include legend
	          true,                     // tooltips?
	          false                     // URLs?
	        );
				        
	        //chart.setBackgroundPaint(Color.white);	 
	  
	        	        
		}

		//Ge�ndert 05.08.2005 Ende
		
		/* 
        // get a reference to the plot for further customisation...
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        
        // set up gradient paints for series...
        GradientPaint gp0 = new GradientPaint(
            0.0f, 0.0f, Color.blue, 
            0.0f, 0.0f, new Color(0, 0, 64)
        );
        GradientPaint gp1 = new GradientPaint(
            0.0f, 0.0f, Color.green, 
            0.0f, 0.0f, new Color(0, 64, 0)
        );
        GradientPaint gp2 = new GradientPaint(
            0.0f, 0.0f, Color.red, 
            0.0f, 0.0f, new Color(64, 0, 0)
        );
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );*/
        // OPTIONAL CUSTOMISATION COMPLETED.		
        
        return chart;
        
    }
	
	
// Added at 05.08.2005
// Begin
	public CategoryDataset createWeightDataset(Timestamp start, MResource r) {

		GregorianCalendar gc1 = new GregorianCalendar();
		gc1.setTimeInMillis(start.getTime());
		gc1.clear(Calendar.MILLISECOND);
 		gc1.clear(Calendar.SECOND);
 		gc1.clear(Calendar.MINUTE);
 		gc1.clear(Calendar.HOUR_OF_DAY);
 			  	
 		 String namecapacity = Msg.translate(Env.getCtx(), "Capacity");
 		 String nameload = Msg.translate(Env.getCtx(), "Load");
 		 String namesummary = Msg.translate(Env.getCtx(), "Summary");
 		 String namepossiblecapacity = "Possible Capacity";
 		 
 		 MResourceType t = MResourceType.get(Env.getCtx(),r.getS_ResourceType_ID());

		 DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		 
		 double currentweight = DB.getSQLValue(null, "Select SUM( (mo.qtyordered-mo.qtydelivered)*(Select mp.weight From m_product mp Where mo.m_product_id=mp.m_product_id )  )From PP_order mo Where ad_client_id=?", r.getAD_Client_ID());
		 double dailyCapacity = DB.getSQLValue(null,"Select dailycapacity From s_resource Where s_resource_id=?",r.getS_Resource_ID());
		 double utilization = DB.getSQLValue(null, "Select percentutilization From s_resource Where s_resource_id=?", r.getS_Resource_ID());

	     double summary = 0;

	     int day = 0;
	     while(day < 32) {
	    	 
 		 		day++;
 		 		
 		 		
 		 		switch(gc1.get(Calendar.DAY_OF_WEEK)) {
 		 		
					case Calendar.SUNDAY:
						
						if (t.isOnSunday()) {
							
							currentweight -= (dailyCapacity*utilization)/100;
					        summary += ((dailyCapacity*utilization)/100); 

							dataset.addValue(dailyCapacity ,namepossiblecapacity, new Integer(day));
							dataset.addValue((dailyCapacity*utilization)/100, namecapacity, new Integer(day) );
						}
						else {

							dataset.addValue(0,namepossiblecapacity, new Integer(day) );
							dataset.addValue(0, namecapacity, new Integer(day) );
						}			
						
		 		 		break;					
						
					case Calendar.MONDAY:
											
						if (t.isOnMonday()) {
							
							currentweight -= (dailyCapacity*utilization)/100;
					        summary += ((dailyCapacity*utilization)/100); 

							dataset.addValue(dailyCapacity ,namepossiblecapacity, new Integer(day));
							dataset.addValue((dailyCapacity*utilization)/100, namecapacity, new Integer(day) );
						}
						else {

							dataset.addValue(0,namepossiblecapacity, new Integer(day) );
							dataset.addValue(0, namecapacity, new Integer(day) );
						}			
						
		 		 		break;					
					
					case Calendar.TUESDAY:
								
						if (t.isOnTuesday()) {
							
							currentweight -= (dailyCapacity*utilization)/100;
					        summary += ((dailyCapacity*utilization)/100); 

							dataset.addValue(dailyCapacity ,namepossiblecapacity, new Integer(day));
							dataset.addValue((dailyCapacity*utilization)/100, namecapacity, new Integer(day) );
						}
						else {

							dataset.addValue(0,namepossiblecapacity, new Integer(day) );
							dataset.addValue(0, namecapacity, new Integer(day) );
						}			
						
		 		 		break;					

					case Calendar.WEDNESDAY:
						
						if (t.isOnWednesday()) {
							
							currentweight -= (dailyCapacity*utilization)/100;
					        summary += ((dailyCapacity*utilization)/100); 

							dataset.addValue(dailyCapacity ,namepossiblecapacity, new Integer(day));
							dataset.addValue((dailyCapacity*utilization)/100, namecapacity, new Integer(day) );
						}
						else {

							dataset.addValue(0,namepossiblecapacity, new Integer(day) );
							dataset.addValue(0, namecapacity, new Integer(day) );
						}			
						
		 		 		break;					

					case Calendar.THURSDAY:
						
						if (t.isOnThursday()) {
							
							currentweight -= (dailyCapacity*utilization)/100;
					        summary += ((dailyCapacity*utilization)/100); 

							dataset.addValue(dailyCapacity ,namepossiblecapacity, new Integer(day));
							dataset.addValue((dailyCapacity*utilization)/100, namecapacity, new Integer(day) );
						}
						else {

							dataset.addValue(0,namepossiblecapacity, new Integer(day) );
							dataset.addValue(0, namecapacity, new Integer(day) );
						}			
						
		 		 		break;					

					case Calendar.FRIDAY:
					
						if (t.isOnFriday()) {
							
							currentweight -= (dailyCapacity*utilization)/100;
					        summary += ((dailyCapacity*utilization)/100); 

							dataset.addValue(dailyCapacity ,namepossiblecapacity, new Integer(day));
							dataset.addValue((dailyCapacity*utilization)/100, namecapacity, new Integer(day) );
						}
						else {

							dataset.addValue(0,namepossiblecapacity, new Integer(day) );
							dataset.addValue(0, namecapacity, new Integer(day) );
						}			
						
		 		 		break;					

					case Calendar.SATURDAY:

						if (t.isOnSaturday()) {	
							
							currentweight -= (dailyCapacity*utilization)/100;
					        summary += ((dailyCapacity*utilization)/100); 

							dataset.addValue(dailyCapacity ,namepossiblecapacity, new Integer(day));
							dataset.addValue((dailyCapacity*utilization)/100, namecapacity, new Integer(day) );
						}
						else {

							dataset.addValue(0,namepossiblecapacity, new Integer(day) );
							dataset.addValue(0, namecapacity, new Integer(day) );
						}			
						
		 		 		break;					
				}
		
 		 		dataset.addValue(currentweight, nameload, new Integer(day));
				dataset.addValue(summary, namesummary, new Integer(day) );

				gc1.add(Calendar.DATE, 1);
 		 } 	 		 

	     return dataset;
 	}
// End 	
	
	public CategoryDataset createDataset(Timestamp start ,MResource r)
 	{
		 //System.out.println("Create new data set");
		 GregorianCalendar gc1 = new GregorianCalendar();
		 gc1.setTimeInMillis(start.getTime());
		 gc1.clear(Calendar.MILLISECOND);
 		 gc1.clear(Calendar.SECOND);
 		 gc1.clear(Calendar.MINUTE);
 		 gc1.clear(Calendar.HOUR_OF_DAY);
 			  	
 		 Timestamp date = start;
 		 String namecapacity = Msg.translate(Env.getCtx(), "Capacity");
 		 System.out.println("\n Namecapacity :"+namecapacity);
 		 String nameload = Msg.translate(Env.getCtx(), "Load");
 		 System.out.println("\n Nameload :"+nameload);
 		 String namesummary = Msg.translate(Env.getCtx(), "Summary");
 		 System.out.println("\n Namesummary :"+namesummary);
		 MResourceType t = MResourceType.get(Env.getCtx(),r.getS_ResourceType_ID());
		 System.out.println("\n Resourcetype "+t);
		 int days = 1;
	     long hours = 0;
	     
		 if (t.isTimeSlot())                			
 			hours = MPPMRP.getHoursAvailable(t.getTimeSlotStart(),t.getTimeSlotEnd());                		
 		 else 
 		 	hours  = 24;
		 
		 DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		 
		 //		Long Hours = new Long(hours); 			 		 			 
		 int C_UOM_ID = DB.getSQLValue(null,"SELECT C_UOM_ID FROM M_Product WHERE S_Resource_ID = ? " , r.getS_Resource_ID());
		 MUOM uom = MUOM.get(Env.getCtx(),C_UOM_ID);
		 System.out.println("\n uom1 "+uom+"\n");
		 //System.out.println("um.isHour()"+ uom.isHour() );
	     if (!uom.isHour())
 	     {	
	     	System.out.println("\n uom2 "+uom+"\n");
 			return dataset;		 		 			 
 		 }			 		
	     System.out.println("\n Dataset "+dataset+"\n");
	     long summary = 0;
	     
 		 while(days < 32)
 		 {	
 		 		//System.out.println("Day Number" + days);
 		 		String day = new String(new Integer (date.getDate()).toString()); 
                                System.out.println("r.getS_Resource_ID()" + r.getS_Resource_ID());
                                System.out.println("Date:"  +  date);
 		 		long HoursLoad = getLoad(r,date).longValue();
 		 		Long Hours = new Long(hours); 
 		 		System.out.println("Summary "+ summary);
                                System.out.println("Hours Load "+ HoursLoad);
 		 		
 		 		switch(gc1.get(Calendar.DAY_OF_WEEK))
				{
					case Calendar.SUNDAY:
						days ++; 
						if (t.isOnSunday())
						{	//System.out.println("si Sunday");			 		 										 		 			 		
							 //Msg.translate(Env.getCtx(), "OnSunday");
							dataset.addValue(hours, namecapacity, day );
							dataset.addValue(HoursLoad ,nameload, day );
					        dataset.addValue(summary, namesummary, day );
					        summary = summary + Hours.intValue() - (HoursLoad); //+ (Hours.intValue() - ((seconds / 3600)));
					        gc1.add(Calendar.DATE, 1);
			 		 		date = new Timestamp(gc1.getTimeInMillis());
			 		 		break;
						}
						else
						{	//System.out.println("no Sunday");									 		 			 	
							//String day = Msg.translate(Env.getCtx(), "OnSunday") ;
							dataset.addValue(0, namecapacity, day );
							dataset.addValue(HoursLoad , nameload, day);
							dataset.addValue(summary, namesummary, day );
							summary = summary - (HoursLoad); 
							gc1.add(Calendar.DATE, 1);
			 		 		date = new Timestamp(gc1.getTimeInMillis());
			 		 		break;
						}					
					case Calendar.MONDAY:
						days ++; 
						if (t.isOnMonday())
						{		//System.out.println("si Monday");			 		 										 		 			 		
								//String day = Msg.translate(Env.getCtx(), "OnMonday") ;
								dataset.addValue(hours, namecapacity, day );
								dataset.addValue(HoursLoad , nameload, day );
						        dataset.addValue(summary, namesummary, day);
						        summary = summary + Hours.intValue() - (HoursLoad ); 
						        gc1.add(Calendar.DATE, 1);
				 		 		date = new Timestamp(gc1.getTimeInMillis());
				 		 		break;
						}
						else
						{
								//System.out.println("no Monday");		 		 			 		
								//String day = Msg.translate(Env.getCtx(), "OnMonday")  ;
								dataset.addValue(0, namecapacity, day );
								dataset.addValue(HoursLoad, nameload, day );
								dataset.addValue(summary, namesummary, day );
								summary = summary  - (HoursLoad); 
								gc1.add(Calendar.DATE, 1);
				 		 		date = new Timestamp(gc1.getTimeInMillis());
				 		 		break;
						}
					case Calendar.TUESDAY:
						days ++; 
						if (t.isOnTuesday())
						{	//System.out.println("si TuesDay");			 		 										 		 			 		
							//String day = Msg.translate(Env.getCtx(), "OnTuesday");							
							dataset.addValue(hours, namecapacity, day );				
							dataset.addValue(HoursLoad, nameload, day );
					        dataset.addValue(summary, namesummary, day );
					        summary = summary + Hours.intValue() - (HoursLoad); 
					        gc1.add(Calendar.DATE, 1);
			 		 		date = new Timestamp(gc1.getTimeInMillis());
			 		 		break;
						}
						else
						{
							//System.out.println("no TuesDay");			 		 			 		
							//String day = Msg.translate(Env.getCtx(), "OnTuesday");
							dataset.addValue(0, namecapacity, day );
							dataset.addValue(HoursLoad, nameload, day);
							dataset.addValue(summary, namesummary, day);
							summary = summary - (HoursLoad); 
							gc1.add(Calendar.DATE, 1);
			 		 		date = new Timestamp(gc1.getTimeInMillis());
			 		 		break;
						}
					case Calendar.WEDNESDAY:
						days ++; 
						if (t.isOnWednesday())
						{				 		 										 		 			 		
							//String day = Msg.translate(Env.getCtx(), "OnWednesday");
							dataset.addValue(hours, namecapacity, day);			
							dataset.addValue(HoursLoad, nameload, day);
					        dataset.addValue(summary, namesummary, day);
					        summary = summary + Hours.intValue() - (HoursLoad); 
					        gc1.add(Calendar.DATE, 1);
			 		 		date = new Timestamp(gc1.getTimeInMillis());
			 		 		break;
						}
						else
						{
									 		 			 		
							//String day = Msg.translate(Env.getCtx(), "OnWednesday");
							dataset.addValue(0, namecapacity, day);
							dataset.addValue(HoursLoad, nameload, day);
							dataset.addValue(summary, namesummary, day);
							summary = summary - (HoursLoad); 
							gc1.add(Calendar.DATE, 1);
			 		 		date = new Timestamp(gc1.getTimeInMillis());
			 		 		break;
						}
					case Calendar.THURSDAY:
						days ++; 
						if (t.isOnThursday())
						{				 		 										 		 			 		
							//String day = Msg.translate(Env.getCtx(), "OnThursday");							
							dataset.addValue(hours, namecapacity, day);				
							dataset.addValue(HoursLoad, nameload, day);
					        dataset.addValue(summary, namesummary, day);
					        summary = summary + Hours.intValue() - (HoursLoad); 
					        gc1.add(Calendar.DATE, 1);
			 		 		date = new Timestamp(gc1.getTimeInMillis());
			 		 		break;
						}
						else
						{
									 		 			 		
							//String day = Msg.translate(Env.getCtx(), "OnThursday");
							dataset.addValue(0, namecapacity, day);
							dataset.addValue(HoursLoad, nameload, day);
							dataset.addValue(summary, namesummary, day);
							summary = summary  - (HoursLoad); 
							gc1.add(Calendar.DATE, 1);
			 		 		date = new Timestamp(gc1.getTimeInMillis());
			 		 		break;
						}						
					case Calendar.FRIDAY:
						days ++; 
						if (t.isOnFriday())
						{				 		 										 		 			 		
							//String day = Msg.translate(Env.getCtx(), "OnFriday");
							dataset.addValue(hours, namecapacity, day);
							dataset.addValue(HoursLoad, nameload, day);												       
					        dataset.addValue(summary, namesummary, day);
					        summary = summary + Hours.intValue() - (HoursLoad); 
					        gc1.add(Calendar.DATE, 1);
			 		 		date = new Timestamp(gc1.getTimeInMillis());
			 		 		break;
						}
						else
						{
									 		 			 		
							//String day = Msg.translate(Env.getCtx(), "OnFriday");
							dataset.addValue(0, namecapacity, day);
							dataset.addValue(HoursLoad, nameload, day);
							dataset.addValue(summary, namesummary, day);
							summary = summary - (HoursLoad); 
							gc1.add(Calendar.DATE, 1);
			 		 		date = new Timestamp(gc1.getTimeInMillis());
			 		 		break;
						}
					case Calendar.SATURDAY:
						days ++; 
						if (t.isOnSaturday())	
						{				 		 										 		 			 		
							//String day = Msg.translate(Env.getCtx(), "OnSaturday");
							dataset.addValue(hours, namecapacity, day);
							dataset.addValue(HoursLoad,nameload, day);
					        dataset.addValue(summary,namesummary, day);
					        summary = summary + Hours.intValue() - (HoursLoad); 
					        gc1.add(Calendar.DATE, 1);
			 		 		date = new Timestamp(gc1.getTimeInMillis());
			 		 		break;
						}
						else
						{
							//String day = Msg.translate(Env.getCtx(), "OnSaturday");
							dataset.addValue(0, namecapacity, day);
							dataset.addValue(HoursLoad, nameload, day);
							dataset.addValue(summary, namesummary, day);
							summary = summary - (HoursLoad); 
							gc1.add(Calendar.DATE, 1);
			 		 		date = new Timestamp(gc1.getTimeInMillis());
			 		 		break;
						}				
				}
 		 		 		 	
 		 } 	 		 
 		return dataset;
 	}
	
	private BigDecimal getLoad(MResource r, Timestamp start)
 	{
		model = CRPDatasetFactory.get(start, start, r);
		return model.calculateLoad(start, r, null);
		
 	}
	
	


}
