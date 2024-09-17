package org.parser;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

import org.parser.AVLTree.AVLNode;
import org.parser.AVLTree.AVLTree;


public class LogAnalyzer {

    private HashMap<String, Integer> tagToCount;
    private HashMap<Integer, AVLTree<String>> srcPortToProtocolCount;
    private HashMap<Integer, AVLTree<String>> dstPortToProtocolCount;
    private HashMap<Integer, String> protocolMap;
    private String flowLogDir;

    public enum PortType {
        SRC,
        DST
    }
    public static String LOG_DELIMITER = " ";
    /**
     *
     * @param flowLogDir points to dir which contain flow logs for a day
     */
    public LogAnalyzer(String flowLogDir) {
        this.tagToCount = new HashMap<>();
        this.srcPortToProtocolCount = new HashMap<>();
        this.dstPortToProtocolCount = new HashMap<>();
        this.flowLogDir = flowLogDir;
        loadTheCommonIANAProtocols();
    }

    public void AnalyzeLogs(LookUpService lookUpService) {
        List<String> logFiles = listOfAllFilesInGivenDir();
        for(String logFile : logFiles){
            try {
                ClassLoader classLoader = getClass().getClassLoader();
                InputStream inputStream = classLoader.getResourceAsStream(this.flowLogDir + "/" + logFile);
                InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(streamReader);
                String line;

                while ((line = br.readLine()) != null) {
                    countTag(line, lookUpService);
                    countPortToProtocolCount(line, lookUpService, PortType.SRC);
                    countPortToProtocolCount(line, lookUpService, PortType.DST);

                }
            }
            catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    private void countTag(String log, LookUpService lookUpService){
        String[] logArray = log.split(LOG_DELIMITER);
        Integer dstPort = Integer.parseInt(logArray[6]);
        Integer protocolNum = Integer.parseInt(logArray[7]);
        String tag = lookUpService.returnTagForPortAndProtocol(dstPort, protocolMap.get(protocolNum).toLowerCase());
        tagToCount.put(tag, tagToCount.getOrDefault(tag, 0)+1);
    }

    private void countPortToProtocolCount(String log, LookUpService lookUpService, PortType portType){
        String[] logArray = log.split(LOG_DELIMITER);
        Integer protocolNum = Integer.parseInt(logArray[7]);
        String protocol = protocolMap.get(protocolNum).toLowerCase();
        Map<Integer, AVLTree<String>> map = null;
        Integer port = null;
        if(portType == PortType.SRC){
            port = Integer.parseInt(logArray[5]);
             map = srcPortToProtocolCount;
        } else if (portType == PortType.DST){
            port = Integer.parseInt(logArray[6]);
            map = dstPortToProtocolCount;
        }

        AVLTree<String> protocolsTree = map.getOrDefault(port, new AVLTree<String>());

        protocolsTree.insert(protocol); // idempotent making sure that protcol node always exists

        AVLNode<String> protocolNode = protocolsTree.search(protocol);
        protocolNode.count++;
        map.put(port, protocolsTree);
    }


    private List<String> listOfAllFilesInGivenDir(){
        List<String> logFiles = new ArrayList<>();
        try{
            ClassLoader classLoader = getClass().getClassLoader();
            URL url = classLoader.getResource(this.flowLogDir);
            Path path = Paths.get(url.toURI());
            Files.walk(path, 5).forEach( p -> {
                if (p.toString().endsWith(".log")){
                    logFiles.add(p.getFileName().toString());
                }
            });
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
        return logFiles;
    }

    private void loadTheCommonIANAProtocols(){
        this.protocolMap = new HashMap<>();
        // Add common protocol mappings (IANA assigned numbers)
        protocolMap.put(1, "ICMP");         // Internet Control Message Protocol
        protocolMap.put(2, "IGMP");         // Internet Group Management Protocol
        protocolMap.put(6, "TCP");          // Transmission Control Protocol
        protocolMap.put(17, "UDP");         // User Datagram Protocol
        protocolMap.put(41, "IPv6");        // IPv6 encapsulation
        protocolMap.put(47, "GRE");         // Generic Routing Encapsulation
        protocolMap.put(50, "ESP");         // Encapsulating Security Payload
        protocolMap.put(51, "AH");          // Authentication Header
        protocolMap.put(58, "ICMPv6");      // ICMP for IPv6
        protocolMap.put(89, "OSPF");        // Open Shortest Path First
        protocolMap.put(132, "SCTP");       // Stream Control Transmission Protocol

    }

    public void printTagCount(String outputDir){
        // Try-with-resources to automatically close the writer
        String csvFile = outputDir + "TagCount" + System.currentTimeMillis() + ".csv";

        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile)) ;

            // Write the header
            writeLine(writer, new String[]{"tag", "count"}, LookUpService.CSV_DELIMITER);

            // Write the data
            for (String tag : tagToCount.keySet()) {
                writeLine(writer, new String[]{tag, tagToCount.get(tag)+""}, LookUpService.CSV_DELIMITER);
            }

            System.out.println("CSV file created: " + csvFile);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void printPortToProtocol(String outputDir, PortType portType){
        // Try-with-resources to automatically close the writer
        String csvFile = outputDir + portType.toString() + System.currentTimeMillis() + ".csv";

        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile)) ;

            // Write the header
            writeLine(writer, new String[]{"Port", "Protocol", "count"}, LookUpService.CSV_DELIMITER);

            Map<Integer,AVLTree<String>> map = null;
            if(portType == PortType.SRC){
                map = srcPortToProtocolCount;
            } else if (portType == PortType.DST){
                map = dstPortToProtocolCount;
            }
            for (Integer port : map.keySet()) {
                for(Object protocolObj : map.get(port)){
                    AVLNode<String> protocolNode = (AVLNode<String>) protocolObj;
                    writeLine(writer, new String[]{port+"", protocolNode.val, protocolNode.count +""}, LookUpService.CSV_DELIMITER);
                }
            }

            System.out.println("CSV file created: " + csvFile);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    // Helper method to write a line to the CSV
    private static void writeLine(BufferedWriter writer, String[] values, String delimiter) throws IOException {
        String line = String.join(delimiter, values);
        writer.write(line);
        writer.newLine();  // Move to the next line
    }

    public int getTagCount(String tag){
        return this.tagToCount.getOrDefault(tag, -1);
    }

    public int getPortProtocolCount(Integer port, String protocol, PortType portType){
        AVLTree<String> protocolsTree = null;
        if(portType == PortType.SRC){
            protocolsTree = srcPortToProtocolCount.getOrDefault(port, null);
        } else if(portType == PortType.DST){
            protocolsTree = dstPortToProtocolCount.getOrDefault(port, null);
        }
        if(protocolsTree != null && protocolsTree.search(protocol) != null){
            return protocolsTree.search(protocol).count;
        }
        return -1;
    }

}
