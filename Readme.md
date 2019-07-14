**Base64 Comparator**

This is WAES assigment to save and compare Base64 datas with Rest endpoints.

**The assignment**

- Provide 2 http endpoints that accepts JSON base64 encoded binary data on both
  endpoints
  - <"host">/v1/diff/<"ID">/left and <"host">/v1/diff/<"ID">/right
  
- The provided data needs to be diff-ed and the results shall be available on a third end point o <host">/v1/diff/<id"> 

- The results shall provide the following info in JSON format 
    
    - If equal return that 
    - If not of equal size just return that 
    - If of same size provide insight in where the diffs are, actual diffs are not needed. 
        - So mainly offsets + length in the data 
        
-  Make assumptions in the implementation explicit, choices are good but need to be communicated

**Teck Stack**

Spring boot, maven, H2 database, Tomcat.

**Run the project**

Spring boot-JAVA application:

$ mvn clean install

$ java -jar target/comparator-1.0.jar

After run the project, you can use POSTMAN for testing.