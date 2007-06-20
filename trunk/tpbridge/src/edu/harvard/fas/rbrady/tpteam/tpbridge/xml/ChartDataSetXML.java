/********************************************************************
 * 
 * File		:	ChartDataSetXML.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	XML Serialization utility methods for ChartDataSet
 * 				ojbects
 *  
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbridge.xml;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import edu.harvard.fas.rbrady.tpteam.tpbridge.chart.ChartDataSet;

/*******************************************************************************
 * File : ChartDataSetXML.java
 * 
 * Description : XML Serialization utility methods for ChartDataSet ojbects
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class ChartDataSetXML {

	/**
	 * Serializes a single ChartDataSet object into an XML String
	 * 
	 * @param chartDataSet
	 *            the ChartDataSet object to be serialized
	 * @return the XML String of the serialization
	 */
	public static String getXML(ChartDataSet chartDataSet) {
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			BeanWriter bWriter = new BeanWriter(baos);
			bWriter.setWriteEmptyElements(false);
			bWriter.enablePrettyPrint();
			bWriter.writeXmlDeclaration("<?xml version='1.0' ?>");
			bWriter.write("chartDataSet", chartDataSet);
			bWriter.flush();
			bWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baos.toString();
	}

	/**
	 * Reconstitutes a ChartDataSet object from its XML serialization String
	 * 
	 * @param dataSetXML
	 *            the String XML serializaton
	 * @return the reconstituted ChartDataSet object
	 */
	public static ChartDataSet getDataSetFromXML(String dataSetXML) {
		ChartDataSet dataSet = null;
		try {
			BeanReader reader = new BeanReader();
			reader.registerBeanClass("chartDataSet", ChartDataSet.class);
			StringReader xmlReader = new StringReader(dataSetXML);
			dataSet = (ChartDataSet) reader.parse(xmlReader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataSet;
	}

	/**
	 * Serializes a List of ChartDataSet objects into an XML String
	 * 
	 * @param dataSetList
	 *            the List of ChartDataSet objects to be serialized
	 * @return the XML String of the serialization
	 */
	public static String getListXML(List<ChartDataSet> dataSetList) {

		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			BeanWriter bWriter = new BeanWriter(baos);
			bWriter.setWriteEmptyElements(false);
			bWriter.enablePrettyPrint();
			bWriter.writeXmlDeclaration("<?xml version='1.0' ?>");
			bWriter.writeXmlDeclaration("<chartDataSets>");
			for (ChartDataSet dataSet : dataSetList)
				bWriter.write("chartDataSet", dataSet);
			bWriter.writeXmlDeclaration("</chartDataSets>");
			bWriter.flush();
			bWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baos.toString();

	}

	/**
	 * Reconstitutes a List of ChartDataSet objects from its 
	 * XML serialization String
	 * 
	 * @param dataSetListXML the XML serialization String
	 * @return An array of reconstituted ChartDataSet objects
	 */
	@SuppressWarnings("unchecked")
	public static ChartDataSet[] getDataSetsFromXML(String dataSetListXML) {
		ArrayList<ChartDataSet> dataSetList = null;
		try {
			BeanReader reader = new BeanReader();
			reader.registerBeanClass("chartDataSets", ArrayList.class);
			reader.registerBeanClass("chartDataSet", ChartDataSet.class);
			StringReader xmlReader = new StringReader(dataSetListXML);
			dataSetList = (ArrayList<ChartDataSet>) reader.parse(xmlReader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (ChartDataSet[]) dataSetList
				.toArray(new ChartDataSet[dataSetList.size()]);
	}
}
