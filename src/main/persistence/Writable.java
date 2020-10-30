package persistence;

import org.json.JSONObject;

// anything in the Password Manager app that is capable of being written to JSON will implement Writable
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
