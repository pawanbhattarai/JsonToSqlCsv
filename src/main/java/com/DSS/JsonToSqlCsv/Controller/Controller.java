/**
 * 
 */
package com.DSS.JsonToSqlCsv.Controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.DSS.JsonToSqlCsv.Service.Service;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 */
@RestController
public class Controller {
	@PostMapping("/convert/toSql")
    public ResponseEntity<String> convertJsonToSql(@RequestBody String jsonInput) {
		  String sqlOutput=null;
		try {
            // Parse JSON input
            JSONArray jsonArray = new JSONArray(jsonInput);
            // Convert JSON to SQL
            for (int n = 0; n < jsonArray.length(); n++) {
                JSONObject jsonObject = jsonArray.getJSONObject(n);
             sqlOutput = Service.convertJsonToSql(jsonObject,"test");
            }
            return ResponseEntity.ok(sqlOutput);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }
	
	@PostMapping("/convert/toCsv")
    public ResponseEntity<String> convertJsonToCsv(@RequestBody String jsonInput) {
        try {
            // Parse JSON input
            JSONArray jsonArray = new JSONArray(jsonInput);
           // Convert JSON to CSV
            String csvOutput = Service.convertJsonToCsv(jsonArray);
            return ResponseEntity.ok(csvOutput);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }
}
