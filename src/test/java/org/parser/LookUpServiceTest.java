package org.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.parser.LookUpService;

public class LookUpServiceTest {

    @Test
    public void testFileForExistingTags(){
        LookUpService lookUpService = new LookUpService("lookup_table.csv");
        Assertions.assertEquals(lookUpService.returnTagForPortAndProtocol(25, "tcp"), "sv_p1");
        Assertions.assertEquals(lookUpService.returnTagForPortAndProtocol(143, "tcp"), "email");

    }

    @Test
    public void testFileForMultiplePortAndProtocolCombToSameTag(){
        LookUpService lookUpService = new LookUpService("lookup_table.csv");
        Assertions.assertEquals(lookUpService.returnTagForPortAndProtocol(25, "tcp"), "sv_p1");
        Assertions.assertEquals(lookUpService.returnTagForPortAndProtocol(23, "tcp"), "sv_p1");
    }

    @Test
    public void testNonExistingPortAndProtocolComb(){
        LookUpService lookUpService = new LookUpService("lookup_table.csv");
        Assertions.assertEquals(lookUpService.returnTagForPortAndProtocol(27, "icmp"), "untagged");
    }

    @Test
    public void testInvalidFile(){
        Assertions.assertThrowsExactly(RuntimeException.class, () -> new LookUpService("LookUpTable_NonExisitng.csv"));
        Assertions.assertThrowsExactly(RuntimeException.class, () -> new LookUpService("LookUpTable1.csv"));
    }
}
