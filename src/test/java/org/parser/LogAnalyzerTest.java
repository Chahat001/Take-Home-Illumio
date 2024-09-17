package org.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LogAnalyzerTest {
    @Test
    public void testCorrectTagCount(){
        LogAnalyzer logAnalyzer = new LogAnalyzer("us-east-1/2021/05/04/");
        logAnalyzer.AnalyzeLogs(new LookUpService("lookup_table.csv"));
        Assertions.assertEquals(logAnalyzer.getTagCount("untagged"), 8);
    }

     @Test
    public void testCorrectPortToProtocolComb(){
         LogAnalyzer logAnalyzer = new LogAnalyzer("us-east-1/2021/05/04/");
         logAnalyzer.AnalyzeLogs(new LookUpService("lookup_table.csv"));
         Assertions.assertEquals(logAnalyzer.getPortProtocolCount(23, "tcp", LogAnalyzer.PortType.SRC), 1);
         Assertions.assertEquals(logAnalyzer.getPortProtocolCount(22, "tcp", LogAnalyzer.PortType.SRC), -1);
         Assertions.assertEquals(logAnalyzer.getPortProtocolCount(143, "tcp", LogAnalyzer.PortType.DST), 2);
         Assertions.assertEquals(logAnalyzer.getPortProtocolCount(143, "icmp", LogAnalyzer.PortType.DST), -1);

     }
}
