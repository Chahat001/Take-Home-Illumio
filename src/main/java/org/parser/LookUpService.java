package org.parser;

import org.parser.AVLTree.AVLNode;
import org.parser.AVLTree.AVLTree;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class LookUpService {

    public static String CSV_DELIMITER = ",";
    public static String DEFAULT_TAG = "untagged";
    private HashMap<Integer, AVLTree> portToTag;
    private HashMap<String, AVLTree> protocolToTag;

    /**
     *
     * @param csvFileName name of the csvfile in the resource folder
     */
    public LookUpService(String csvFileName){
        this.protocolToTag = new HashMap<>();
        this.portToTag = new HashMap<>();

        ClassLoader classLoader = getClass().getClassLoader();
        try {
            InputStream inputStream = classLoader.getResourceAsStream(csvFileName);
            InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(streamReader);
            String line;
            boolean flagIgnoreFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (flagIgnoreFirstLine) {
                    flagIgnoreFirstLine = false;
                    continue;
                }
                String[] values = line.split(CSV_DELIMITER);
                Integer port = Integer.parseInt(values[0]);
                String protocol = values[1].toLowerCase();
                String tag = values[2].toLowerCase();
                AVLTree<String> portTags = portToTag.getOrDefault(port, new AVLTree<String>());
                portTags.insert(tag);
                portToTag.put(port, portTags);

                AVLTree<String> protocolTags = protocolToTag.getOrDefault(protocol, new AVLTree<String>());
                protocolTags.insert(tag);
                protocolToTag.put(protocol, protocolTags);
            }
        } catch (Exception e) { //convert any checked expception to unchecked and stop the program.
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param port
     * @param protocol
     * @return the string value if the combination of port and protocol map to something in lookup table else return "untagged"
     */
    public String returnTagForPortAndProtocol(Integer port, String protocol){
        if(portToTag.containsKey(port) && protocolToTag.containsKey(protocol)){
            AVLTree<String> smallerTagSet;
            AVLTree<String> largerTagSet;
            if(portToTag.get(port).countNodes() < protocolToTag.get(protocol).countNodes()) {
                smallerTagSet = portToTag.get(port);
                largerTagSet = protocolToTag.get(protocol);
            } else {
                smallerTagSet = protocolToTag.get(protocol);
                largerTagSet = portToTag.get(port);
            }

            for(Object tagMappedToPort : smallerTagSet){
                AVLNode<String> node = (AVLNode<String>) tagMappedToPort;
                AVLNode<String> foundTagNode = largerTagSet.search(node.val);
                if(foundTagNode != null){
                    return foundTagNode.val;
                }
            }
        }
        return "untagged";
    }
}
