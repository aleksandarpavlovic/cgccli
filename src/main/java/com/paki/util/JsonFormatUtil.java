package com.paki.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

public class JsonFormatUtil {
    private static final int INDENT_INCREMENT = 4;

    public static String jsonObjectToPrintableString(JsonObject json) {
        return jsonObjectToPrintableString(0, json);
    }

    public static String jsonArrayToPrintableString(JsonArray json) {
        return jsonArrayToPrintableString(0, json);
    }

    private static String jsonObjectToPrintableString(int indent, JsonObject json) {
        int keyPadding = longestKeyLength(json) + 2;
        String indentation = indent > 0 ? String.format("%"+indent+"s", "") : "";
        String paddingFormat = "%-" + keyPadding + "s";
        String lineSep = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, JsonElement> entry: json.entrySet()) {
            sb.append(indentation).append(String.format(paddingFormat, entry.getKey()+":"));
            if (entry.getValue().isJsonPrimitive()) {
                sb.append(entry.getValue().getAsString()).append(lineSep);
            }
            else if (entry.getValue().isJsonArray()) {
                sb.append(lineSep);
                sb.append(jsonArrayToPrintableString(indent + INDENT_INCREMENT, entry.getValue().getAsJsonArray()));
            } else if (entry.getValue().isJsonObject()) {
                sb.append(lineSep);
                sb.append(jsonObjectToPrintableString(indent + INDENT_INCREMENT, entry.getValue().getAsJsonObject()));
            } else {
                sb.append(lineSep);
            }
        }

        return sb.toString();
    }

    private static String jsonArrayToPrintableString(int indent, JsonArray jsonArray) {
        String indentation = indent > 0 ? String.format("%"+indent+"s", "") : "";
        String lineSep = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();

        for (JsonElement jsonElement: jsonArray) {
            if (jsonElement.isJsonPrimitive()) {
                sb.append(indentation).append(jsonElement.getAsString()).append(lineSep);
            } else if (jsonElement.isJsonArray()) {
                sb.append(lineSep);
                sb.append(jsonArrayToPrintableString(indent + INDENT_INCREMENT, jsonElement.getAsJsonArray()));
            } else if (jsonElement.isJsonObject()) {
                sb.append(lineSep);
                sb.append(jsonObjectToPrintableString(indent + INDENT_INCREMENT, jsonElement.getAsJsonObject()));
            } else {
                sb.append(lineSep);
            }
        }

        return sb.toString();
    }

    private static int longestKeyLength(JsonObject json) {
        int max = 0;
        for (String key: json.keySet()) {
            max = key.length() > max ? key.length() : max;
        }
        return max;
    }
}
