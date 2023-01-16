package it.itsar.progettocorso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Objects;

public class Storia implements Serializable {
    private String id;
    private String title;
    private String summary;
    private String content;

    public Storia(JSONObject storiaJson) {
        try {
            this.id = storiaJson.getString("id");
            this.title = storiaJson.getString("title");
            this.summary = storiaJson.getString("summary");
            this.content = storiaJson.getString("content");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Storia storia = (Storia) o;
        return id.equals(storia.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",this.getId());
            jsonObject.put("title",this.getTitle());
            jsonObject.put("summary",this.getSummary());
            jsonObject.put("content",this.getContent());
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Storia(String id, String title, String summary, String content) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.content = content;
    }

    public Storia() {
    }
}
