package org.parser;

public class Main {
    public static void main(String[] args) {

        LogAnalyzer logAnalyzer = new LogAnalyzer("us-east-1/2021/05/04/");
        logAnalyzer.AnalyzeLogs(new LookUpService("lookup_table.csv"));

        logAnalyzer.printTagCount("./Output/");
        logAnalyzer.printPortToProtocol("./Output/", LogAnalyzer.PortType.SRC);
        logAnalyzer.printPortToProtocol("./Output/", LogAnalyzer.PortType.DST);

    }
}