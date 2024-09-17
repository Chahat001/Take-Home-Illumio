# Assumptions

1. Input for the flow logs will be of the ./region/year/month/day
2. Unix time End-time T.S. will determine if the log fall into the same day we are aggregating for, As it is possible to have the first flow-log file with T.S 2024-09-10T00:00:00Z to have logs from earlier day. Similarly Logs of next day will not be included in current day window
3. Software will use the flow-log end time to determine which aggregation window it falls into.
4. Software will support version 2, 3, 4,5,7 of the flow logs.
5. All tags in lookup file are converted into lowercase before storage to be treated in case insensitive way.
6. Its is assumed that first line of the csv file for the lookup table is header and will be ignored.
7. Each unique pair of port and protocol in the lookup table will only map to one single tag; however multiple unique pairs can map to same tag
8. Once the lookup file is loaded into software it cannot be changed.
9. Code cannot be ran as Jar file, because I have automated the read of individual log file from dir ./region/year/month/day directory in resource folder. It was bit cumbersome to do this in jar file  as  Jars are Zip files so you can't get an individual File out of it without unzipping it. Zip files don't exactly have directories, so it's not as simple as getting the children of a directory.
10. We are only tracking following most common protocols( ICMP, IGMP, TCP, UDP, IPV6, GRE, ESP, AH, ICMPV6, OSPF, SCTP)

# How to run & Test

1. clone the git repo and make ```Take-Home-Illumio``` your current dir
2. Make sure you have ```Apache Maven 3.6.3``` and ```java version "11.0.23" 2024-04-16 LTS``` installed.
3. Place the flow logs and lookup csv file to be analyzed in ```src/main/resoruce``` and update the params in ```src/main/org/parser/Main``` accordingly
4. run ```mvn package``` it will complile the src code and run all the test cases. **Please do not rename or move the current files in ```src/main/resoruce``` or the test cases will be failed and mvn would not compile**
5. run ```java -classpath ./target/classes org.parser.Main``` and it should print something similar to console ```CSV file created: ./Output/TagCount1726577492135.csv
   CSV file created: ./Output/SRC1726577492144.csv
   CSV file created: ./Output/DST1726577492154.csv```
