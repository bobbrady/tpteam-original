package edu.harvard.fas.rbrady.tpteam.tpbridge.xml;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;

import edu.harvard.fas.rbrady.tpteam.tpbridge.chart.ChartDataSet;

public class ChartDataSetXML {


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
		return (ChartDataSet[]) dataSetList.toArray(new ChartDataSet[dataSetList.size()]);
	}

}
