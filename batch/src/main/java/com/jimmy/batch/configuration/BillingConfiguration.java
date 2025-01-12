package com.jimmy.batch.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jimmy.batch.model.Bill;
import com.jimmy.batch.model.Usage;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


@Configuration
public class BillingConfiguration {

	@Value("${usage.file.name:classpath:usageinfo.json}")
	private Resource usageResource;

//	@Bean
//	public Job job1(ItemReader<Usage> reader, ItemProcessor<Usage, Bill> itemProcessor, ItemWriter<Bill> writer) {
//		Step step = stepBuilderFactory.get("BillProcessing")
//				.<Usage, Bill>chunk(1)
//				.reader(reader)
//				.processor(itemProcessor)
//				.writer(writer)
//				.build();
//
//		return jobBuilderFactory.get("BillJob")
//				.incrementer(new RunIdIncrementer())
//				.start(step)
//				.build();
//	}

	@Bean
	public Job billJob(JobRepository jobRepository, Step billProcessing, JobCompletionNotificationListener listener) {
		return new JobBuilder("billJob", jobRepository)
				.listener(listener)
				.start(billProcessing)
				.incrementer(new RunIdIncrementer())
				.build();
	}

	@Bean
	public Step billProcessing(JobRepository jobRepository,
							   PlatformTransactionManager transactionManager,
							   ItemReader<Usage> reader,
							   ItemProcessor<Usage, Bill> billProcessor,
							   ItemWriter<Bill> writer) {
		return new StepBuilder("billProcessing", jobRepository)
				.<Usage, Bill>chunk(3, transactionManager)
				.reader(reader)
				.processor(billProcessor)
				.writer(writer)
				.build();
	}

	@Bean
	public JsonItemReader<Usage> jsonItemReader() {

		ObjectMapper objectMapper = new ObjectMapper();
		JacksonJsonObjectReader<Usage> jsonObjectReader =
				new JacksonJsonObjectReader<>(Usage.class);
		jsonObjectReader.setMapper(objectMapper);

		return new JsonItemReaderBuilder<Usage>()
				.jsonObjectReader(jsonObjectReader)
				.resource(usageResource)
				.name("UsageJsonItemReader")
				.build();
	}

	@Bean
	public JdbcBatchItemWriter<Bill> jdbcBillWriter(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Bill>()
				.beanMapped()
				.dataSource(dataSource)
				.sql("INSERT INTO BILL_STATEMENTS (id, first_name, last_name, minutes, data_usage,bill_amount) VALUES (:id, :firstName, :lastName, :minutes, :dataUsage, :billAmount)")
				.build();
	}

	@Bean
	ItemProcessor<Usage, Bill> billProcessor() {
		return new BillProcessor();
	}

}
