package com.excilys.shooflers.dashboard.server.model;

/**
 * @author Loïc Ortola on 07/06/2016.
 *         Revision represents a "change" in the dashboard system.
 *         It represents one atomic operation.
 */
public class Revision {
    public enum Action {
        ADD, UPDATE, DELETE
    }

    public enum Type {
        BUNDLE, MEDIA
    }

    private Long revision;
    private Action action;
    private Type type;
    // Target represents the target uuid
    private String target;
    // The result is used only when action = update.
    // As content is immutable, updates actually wrap a content deletion, and a new content.
    // The result represents the "resulting" uuid (the new one which was added)
    private String result;

    public Long getRevision() {
        return revision;
    }

    public Action getAction() {
        return action;
    }

    public Type getType() {
        return type;
    }

    public String getResult() {
        return result;
    }

    public String getTarget() {
        return target;
    }

    public void setRevision(Long revision) {
        this.revision = revision;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "Revision{" +
                "revision='" + revision + '\'' +
                ", action=" + action +
                ", type=" + type +
                ", target='" + target + '\'' +
                ", result='" + result + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Revision revision1 = (Revision) o;

        if (revision != null ? !revision.equals(revision1.revision) : revision1.revision != null) {
            return false;
        }
        if (action != revision1.action) {
            return false;
        }
        if (type != revision1.type) {
            return false;
        }
        if (target != null ? !target.equals(revision1.target) : revision1.target != null) {
            return false;
        }
        return result != null ? result.equals(revision1.result) : revision1.result == null;

    }

    @Override
    public int hashCode() {
        int result1 = revision != null ? revision.hashCode() : 0;
        result1 = 31 * result1 + (action != null ? action.hashCode() : 0);
        result1 = 31 * result1 + (type != null ? type.hashCode() : 0);
        result1 = 31 * result1 + (target != null ? target.hashCode() : 0);
        result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
        return result1;
    }

}