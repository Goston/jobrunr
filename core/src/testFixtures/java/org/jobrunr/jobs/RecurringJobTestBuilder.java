package org.jobrunr.jobs;

import org.jobrunr.jobs.RecurringJob.CreatedBy;
import org.jobrunr.jobs.details.JobDetailsAsmGenerator;
import org.jobrunr.jobs.lambdas.IocJobLambda;
import org.jobrunr.jobs.lambdas.JobLambda;
import org.jobrunr.scheduling.Schedule;
import org.jobrunr.scheduling.cron.Cron;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import static java.util.Arrays.asList;
import static org.jobrunr.jobs.JobDetailsTestBuilder.defaultJobDetails;
import static org.jobrunr.scheduling.ScheduleExpressionType.createScheduleFromString;

public class RecurringJobTestBuilder {

    private String id;
    private String name;
    private Integer amountOfRetries;
    private JobDetails jobDetails;
    private Schedule schedule;
    private ZoneId zoneId;
    private List<String> labels;
    private CreatedBy createdBy = CreatedBy.API;
    private Instant createdAt = Instant.now();

    private RecurringJobTestBuilder() {

    }

    public static RecurringJobTestBuilder aRecurringJob() {
        return new RecurringJobTestBuilder();
    }

    public static RecurringJobTestBuilder aDefaultRecurringJob() {
        return aRecurringJob()
                .withId("anId")
                .withName("a recurring job")
                .withJobDetails(defaultJobDetails())
                .withCronExpression(Cron.daily())
                .withZoneId(ZoneId.systemDefault());
    }

    public RecurringJobTestBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public RecurringJobTestBuilder withoutId() {
        this.id = null;
        return this;
    }

    public RecurringJobTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public RecurringJobTestBuilder withAmountOfRetries(int amountOfRetries) {
        this.amountOfRetries = amountOfRetries;
        return this;
    }

    public RecurringJobTestBuilder withJobDetails(JobLambda jobLambda) {
        this.jobDetails = new JobDetailsAsmGenerator().toJobDetails(jobLambda);
        return this;
    }

    public RecurringJobTestBuilder withJobDetails(IocJobLambda jobLambda) {
        this.jobDetails = new JobDetailsAsmGenerator().toJobDetails(jobLambda);
        return this;
    }

    public RecurringJobTestBuilder withJobDetails(JobDetailsTestBuilder jobDetailsBuilder) {
        withJobDetails(jobDetailsBuilder.build());
        return this;
    }

    public RecurringJobTestBuilder withJobDetails(JobDetails jobDetails) {
        this.jobDetails = jobDetails;
        return this;
    }

    public RecurringJobTestBuilder withCronExpression(String cronExpression) {
        this.schedule = createScheduleFromString(cronExpression);
        return this;
    }

    public RecurringJobTestBuilder withZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
        return this;
    }

    public RecurringJobTestBuilder withIntervalExpression(String intervalExpression) {
        return this.withIntervalExpression(intervalExpression, Instant.now());
    }

    public RecurringJobTestBuilder withIntervalExpression(String intervalExpression, Instant createdAt) {
        this.schedule = createScheduleFromString(intervalExpression);
        this.createdAt = createdAt;
        return this;
    }

    public RecurringJobTestBuilder withScheduleExpression(String scheduleExpression) {
        return withCronExpression(scheduleExpression);
    }

    public RecurringJobTestBuilder withLabels(String... labels) {
        this.labels = asList(labels);
        return this;
    }

    public RecurringJobTestBuilder withCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public RecurringJob build() {
        final RecurringJob recurringJob = new RecurringJob(id, jobDetails, schedule, zoneId, createdBy, createdAt);
        if (amountOfRetries != null) {
            recurringJob.setAmountOfRetries(amountOfRetries);
        }
        recurringJob.setJobName(name);
        recurringJob.setLabels(labels);
        return recurringJob;
    }
}