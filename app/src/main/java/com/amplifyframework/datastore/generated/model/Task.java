package com.amplifyframework.datastore.generated.model;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.BelongsTo;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;
import com.amplifyframework.core.model.temporal.Temporal;

import java.util.Objects;
import java.util.UUID;

/** This is an auto generated class representing the Task type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Tasks", authRules = {
        @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "byTeam", fields = {"teamId","name"})
public final class Task implements Model {
    public static final QueryField ID = field("Task", "id");
    public static final QueryField NAME = field("Task", "name");
    public static final QueryField BODY = field("Task", "body");
    public static final QueryField STATE = field("Task", "state");
    public static final QueryField TEAM_TASK = field("Task", "teamId");
    private final @ModelField(targetType="ID", isRequired = true) String id;
    private final @ModelField(targetType="String", isRequired = true) String name;
    private final @ModelField(targetType="String") String body;
    private final @ModelField(targetType="TaskState") TaskState state;
    private final @ModelField(targetType="Team") @BelongsTo(targetName = "teamId", type = Team.class) Team teamTask;
    private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
    private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
    /** @deprecated This API is internal to Amplify and should not be used. */
    @Deprecated
    public String resolveIdentifier() {
        return id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBody() {
        return body;
    }

    public TaskState getState() {
        return state;
    }

    public Team getTeamTask() {
        return teamTask;
    }

    public Temporal.DateTime getCreatedAt() {
        return createdAt;
    }

    public Temporal.DateTime getUpdatedAt() {
        return updatedAt;
    }

    private Task(String id, String name, String body, TaskState state, Team teamTask) {
        this.id = id;
        this.name = name;
        this.body = body;
        this.state = state;
        this.teamTask = teamTask;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if(obj == null || getClass() != obj.getClass()) {
            return false;
        } else {
            Task task = (Task) obj;
            return ObjectsCompat.equals(getId(), task.getId()) &&
                    ObjectsCompat.equals(getName(), task.getName()) &&
                    ObjectsCompat.equals(getBody(), task.getBody()) &&
                    ObjectsCompat.equals(getState(), task.getState()) &&
                    ObjectsCompat.equals(getTeamTask(), task.getTeamTask()) &&
                    ObjectsCompat.equals(getCreatedAt(), task.getCreatedAt()) &&
                    ObjectsCompat.equals(getUpdatedAt(), task.getUpdatedAt());
        }
    }

    @Override
    public int hashCode() {
        return new StringBuilder()
                .append(getId())
                .append(getName())
                .append(getBody())
                .append(getState())
                .append(getTeamTask())
                .append(getCreatedAt())
                .append(getUpdatedAt())
                .toString()
                .hashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Task {")
                .append("id=" + String.valueOf(getId()) + ", ")
                .append("name=" + String.valueOf(getName()) + ", ")
                .append("body=" + String.valueOf(getBody()) + ", ")
                .append("state=" + String.valueOf(getState()) + ", ")
                .append("teamTask=" + String.valueOf(getTeamTask()) + ", ")
                .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
                .append("updatedAt=" + String.valueOf(getUpdatedAt()))
                .append("}")
                .toString();
    }

    public static NameStep builder() {
        return new Builder();
    }

    /**
     * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
     * This is a convenience method to return an instance of the object with only its ID populated
     * to be used in the context of a parameter in a delete mutation or referencing a foreign key
     * in a relationship.
     * @param id the id of the existing item this instance will represent
     * @return an instance of this model with only ID populated
     */
    public static Task justId(String id) {
        return new Task(
                id,
                null,
                null,
                null,
                null
        );
    }

    public CopyOfBuilder copyOfBuilder() {
        return new CopyOfBuilder(id,
                name,
                body,
                state,
                teamTask);
    }
    public interface NameStep {
        BuildStep name(String name);
    }


    public interface BuildStep {
        Task build();
        BuildStep id(String id);
        BuildStep body(String body);
        BuildStep state(TaskState state);
        BuildStep teamTask(Team teamTask);
    }


    public static class Builder implements NameStep, BuildStep {
        private String id;
        private String name;
        private String body;
        private TaskState state;
        private Team teamTask;
        public Builder() {

        }

        private Builder(String id, String name, String body, TaskState state, Team teamTask) {
            this.id = id;
            this.name = name;
            this.body = body;
            this.state = state;
            this.teamTask = teamTask;
        }

        @Override
        public Task build() {
            String id = this.id != null ? this.id : UUID.randomUUID().toString();

            return new Task(
                    id,
                    name,
                    body,
                    state,
                    teamTask);
        }

        @Override
        public BuildStep name(String name) {
            Objects.requireNonNull(name);
            this.name = name;
            return this;
        }

        @Override
        public BuildStep body(String body) {
            this.body = body;
            return this;
        }

        @Override
        public BuildStep state(TaskState state) {
            this.state = state;
            return this;
        }

        @Override
        public BuildStep teamTask(Team teamTask) {
            this.teamTask = teamTask;
            return this;
        }

        /**
         * @param id id
         * @return Current Builder instance, for fluent method chaining
         */
        public BuildStep id(String id) {
            this.id = id;
            return this;
        }
    }


    public final class CopyOfBuilder extends Builder {
        private CopyOfBuilder(String id, String name, String body, TaskState state, Team teamTask) {
            super(id, name, body, state, teamTask);
            Objects.requireNonNull(name);
        }

        @Override
        public CopyOfBuilder name(String name) {
            return (CopyOfBuilder) super.name(name);
        }

        @Override
        public CopyOfBuilder body(String body) {
            return (CopyOfBuilder) super.body(body);
        }

        @Override
        public CopyOfBuilder state(TaskState state) {
            return (CopyOfBuilder) super.state(state);
        }

        @Override
        public CopyOfBuilder teamTask(Team teamTask) {
            return (CopyOfBuilder) super.teamTask(teamTask);
        }
    }
}