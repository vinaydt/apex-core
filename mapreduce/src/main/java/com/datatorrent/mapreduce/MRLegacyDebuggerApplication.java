package com.datatorrent.mapreduce;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;

import com.datatorrent.api.DAG;
import com.datatorrent.api.StreamingApplication;
import com.datatorrent.lib.io.ConsoleOutputOperator;

public class MRLegacyDebuggerApplication implements StreamingApplication {

	@Override
	public void populateDAG(DAG dag, Configuration arg1) {
		boolean allInline = true;
		
		dag.setAttribute(DAG.APPLICATION_NAME, "MRDebugger");
		
		TestInputOperator<String> inputOperator = dag.addOperator("inputOperator", new TestInputOperator<String>());
		List<String> tupleList = new ArrayList<String>();
		
		
		tupleList.add("dataanalyser-virtualbox,50030,v1,201308151431_0001");
		inputOperator.testTuples = new ArrayList<List<String>>();
		inputOperator.testTuples.add(tupleList);
//		
		MRLegacyJobStatusOperator mrJobOperator = dag.addOperator("mrJobStatusOperator", new MRLegacyJobStatusOperator());
		dag.addStream("input_mrJobStatusOperator", inputOperator.output, mrJobOperator.input);
		
		ConsoleOutputOperator consoleOperator = dag.addOperator("consoleOutputOperator", new ConsoleOutputOperator());
		dag.addStream("mrJobStatusOperator", mrJobOperator.output, consoleOperator.input).setInline(allInline);
		
		ConsoleOutputOperator mapConsoleOperator = dag.addOperator("consoleMapOutputOperator", new ConsoleOutputOperator());
		dag.addStream("mrMapStatusOperator", mrJobOperator.mapOutput, mapConsoleOperator.input).setInline(allInline);
		
		ConsoleOutputOperator reduceConsoleOperator = dag.addOperator("consoleReduceOutputOperator", new ConsoleOutputOperator());
		dag.addStream("mrReduceStatusOperator", mrJobOperator.reduceOutput, reduceConsoleOperator.input).setInline(allInline);
		
		
		// TODO Auto-generated method stub

	}

}
