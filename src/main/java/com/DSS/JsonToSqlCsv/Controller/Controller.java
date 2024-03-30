package com.DSS.JsonToSqlCsv.Controller;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.DSS.JsonToSqlCsv.Service.Service;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONException;
/**
 * 
 */

@CrossOrigin
@RestController
public class Controller {
    @PostMapping("/convert/toSql")
    public ResponseEntity<byte[]> convertJsonToSql(@RequestBody String jsonInput) {
        try {
            // Parse JSON input
            JSONArray jsonArray = new JSONArray(jsonInput);
            
            // Convert JSON to SQL
            // Assuming Service.convertJsonToSql returns byte[] representing SQL content
            byte[] sqlOutput = Service.convertJsonToSql(jsonArray, "test");
            
            // Set headers for SQL file
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.setContentDispositionFormData("filename", "output.sql");
            
            // Return ResponseEntity with SQL content and headers
            return ResponseEntity.ok()
                                 .headers(headers)
                                 .body(sqlOutput);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(("Error occurred: " + e.getMessage()).getBytes());
        }
    }
    @PostMapping("/convert/toCsv")
    public ResponseEntity<byte[]> convertJsonToCsv(@RequestBody String jsonInput) {
        try {
            // Parse JSON input
            JSONArray jsonArray = new JSONArray(jsonInput);

            // Convert JSON to CSV
            byte[] csvBytes = Service.convertJsonToCsv(jsonArray);

            // Set headers for CSV file
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.setContentDispositionFormData("filename", "output.csv");

            // Return ResponseEntity with CSV content and headers
            return ResponseEntity.ok()
                                 .headers(headers)
                                 .body(csvBytes);

        } catch (JSONException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(("Invalid JSON input: " + e.getMessage()).getBytes(StandardCharsets.UTF_8));
        }
    }
}