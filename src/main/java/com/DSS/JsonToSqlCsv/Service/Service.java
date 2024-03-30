package com.DSS.JsonToSqlCsv.Service;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
/*
 * @author Pawan Bhattarai
*/

public class Service {
	public static byte[] convertJsonToSql(JSONArray jsonArray, String tableName) throws JSONException {
	    StringBuilder sqlBuilder = new StringBuilder();

	    // Iterate through JSON array elements and convert each to SQL
	    for (int i = 0; i < jsonArray.length(); i++) {
	        JSONObject jsonObject = jsonArray.getJSONObject(i);

	        // Write INSERT statement for each JSON object
	        sqlBuilder.append("INSERT INTO ").append(tableName).append(" (");

	        List<String> columnNames = new ArrayList<>();
	        extractColumnNames(jsonObject, columnNames);
	        sqlBuilder.append(String.join(", ", columnNames));

	        sqlBuilder.append(") VALUES (");

	        // Write column values
	        List<String> columnValues = new ArrayList<>();
	        extractColumnValues(jsonObject, columnValues);
	        sqlBuilder.append(String.join(", ", columnValues));

	        sqlBuilder.append(");\n");
	    }

	    // Return SQL content as byte array
	    return sqlBuilder.toString().getBytes(StandardCharsets.UTF_8);
	}


	private static void extractColumnNames(JSONObject jsonObject, List<String> columnNames) throws JSONException {
	    Iterator<String> keys = jsonObject.keys();
	    while (keys.hasNext()) {
	        String key = keys.next();
	        Object value = jsonObject.get(key);
	        if (value instanceof JSONObject) {
	            extractColumnNames((JSONObject) value, columnNames);
	        } else if (value instanceof JSONArray) {
	            JSONArray jsonArray = (JSONArray) value;
	            for (int i = 0; i < jsonArray.length(); i++) {
	                extractColumnNames(jsonArray.getJSONObject(i), columnNames);
	            }
	        } else {
	            columnNames.add(key);
	        }
	    }
	}

	private static void extractColumnValues(JSONObject jsonObject, List<String> columnValues) throws JSONException {
	    Iterator<String> keys = jsonObject.keys();
	    while (keys.hasNext()) {
	        String key = keys.next();
	        Object value = jsonObject.get(key);
	        if (value instanceof JSONObject) {
	            extractColumnValues((JSONObject) value, columnValues);
	        } else if (value instanceof JSONArray) {
	            JSONArray jsonArray = (JSONArray) value;
	            for (int i = 0; i < jsonArray.length(); i++) {
	                extractColumnValues(jsonArray.getJSONObject(i), columnValues);
	            }
	        } else {
	            columnValues.add(escapeSqlValue(value.toString()));
	        }
	    }
	}

	private static String escapeSqlValue(String value) {
	    // Escape single quotes by doubling them
	    return value.replace("'", "''");
	}

	
	 public static byte[] convertJsonToCsv(JSONArray jsonArray) throws JSONException {
	        StringBuilder csvBuilder = new StringBuilder();

	        // Extract all unique column headings
	        List<String> columnHeadings = extractColumnHeadings(jsonArray);

	        // Write column headings
	        for (int i = 0; i < columnHeadings.size(); i++) {
	            if (i > 0)
	                csvBuilder.append(",");
	            csvBuilder.append(escapeCsvValue(columnHeadings.get(i)));
	        }
	        csvBuilder.append("\n");

	        // Write data
	        for (int i = 0; i < jsonArray.length(); i++) {
	            JSONObject jsonObject = jsonArray.getJSONObject(i);
	            for (int j = 0; j < columnHeadings.size(); j++) {
	                String heading = columnHeadings.get(j);
	                if (jsonObject.has(heading)) {
	                    if (j > 0)
	                        csvBuilder.append(",");
	                    csvBuilder.append(escapeCsvValue(jsonObject.optString(heading)));
	                } else {
	                    if (j > 0)
	                        csvBuilder.append(",");
	                    csvBuilder.append("");
	                }
	            }
	            csvBuilder.append("\n");
	        }

	        // Return CSV content as byte array
	        return csvBuilder.toString().getBytes(StandardCharsets.UTF_8);
	    }

    private static List<String> extractColumnHeadings(JSONArray jsonArray) throws JSONException {
        Set<String> columnHeadingsSet = new LinkedHashSet<>();
        List<String> columnHeadings = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Iterator<String> keysIterator = jsonObject.keys();
            while (keysIterator.hasNext()) {
                String key = keysIterator.next();
                if (!columnHeadingsSet.contains(key)) {
                    columnHeadingsSet.add(key);
                    columnHeadings.add(key);
                }
            }
        }
        return columnHeadings;
    }
    private static String escapeCsvValue(String value) {
        // Check if the value contains special characters
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            // If the value contains double quotes, escape them by doubling them
            value = value.replace("\"", "\"\"");

            // If the value contains any special characters, wrap it in double quotes
            value = "\"" + value + "\"";
        }
        return value;
    }

}
