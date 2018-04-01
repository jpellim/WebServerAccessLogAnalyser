package com.ef;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.ef.enumerator.Duration;
import com.ef.util.DateUtil;

public class AccessLogInspect {

	private List<AccessRecord> accessRecords;

	public AccessLogInspect() {
		this.accessRecords = new ArrayList<>();
	}

	public void readFile(String fileName) {

		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

			stream.forEach(line -> {
				this.accessRecords.add(mapLineToAccessRecord(line));
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private AccessRecord mapLineToAccessRecord(String line) {

		AccessRecord accessRecord = new AccessRecord();

		String[] lines = line.split("\\|");

		LocalDateTime logDate = DateUtil.convertStringDateLogToLocalDateTime(lines[0]);
		logDate = DateUtil.converDateFormat(logDate);

		accessRecord.setDate(logDate);
		accessRecord.setIp(lines[1]);
		accessRecord.setStatus(Integer.parseInt(lines[3]));

		return accessRecord;
	}

	private void persistData(Map<AccessRecord, Long> accessRecordsMap, String startDate, String duration, Integer threshold) {

		final EntityManager em = JPAUtil.getEntityManager();
		final EntityTransaction transaction = em.getTransaction();

		try {
			transaction.begin();

			accessRecordsMap.entrySet().stream().forEachOrdered(accessRecordMap -> {
				accessRecordMap.getKey().setComment(accessRecordMap.getValue(), startDate, getEndDate(startDate, duration));
				em.persist(accessRecordMap.getKey());

			});

			transaction.commit();

		} catch (Exception ex) {
			transaction.rollback();
			ex.printStackTrace();
		} finally {
			em.close();
			JPAUtil.closeEntityManagerFactory();
		}
	}

	private LocalDateTime getEndDate(String startDate, String duration) {
		if (duration.equals(Duration.HOURLY.getDescription())) {
			return DateUtil.convertStringDateToLocalDateTime(startDate).plusHours(1).minusSeconds(1);
		} else if (duration.equals(Duration.DAILY.getDescription())) {
			return DateUtil.convertStringDateToLocalDateTime(startDate).plusDays(1).minusSeconds(1);
		}
		return null;
	}

	public Stream<AccessRecord> getAccessRecordsBetweenDates(String startDate, String duration) {
		return this.accessRecords.stream()
				.filter(record -> (DateUtil.converDateFormat(record.getDate()).isAfter(DateUtil.convertStringDateToLocalDateTime(startDate))
						&& DateUtil.converDateFormat(record.getDate()).isBefore(getEndDate(startDate, duration))));
	}

	private Map<AccessRecord, Long> countNumberOccurrences(String startDate, String duration) {

		Map<AccessRecord, Long> counts = getAccessRecordsBetweenDates(startDate, duration)
				.collect(Collectors.groupingBy(e -> e, Collectors.counting()));

		return counts;
	}

	public Map<AccessRecord, Long> getAccessRecordsVerifyingThreshold(String startDate, String duration, Integer threshold) {

		Map<AccessRecord, Long> numberOccurrencesMap = this.countNumberOccurrences(startDate, duration);

		return numberOccurrencesMap.entrySet().stream().filter(accessRecordMap -> accessRecordMap.getValue() > threshold)
				.collect(Collectors.toMap(accessRecordMap -> accessRecordMap.getKey(), e -> e.getValue()));
	}

	private void printIps(Map<AccessRecord, Long> accessRecordsMap, String startDate, String duration, Integer threshold) {
		System.out.println(
				"\nIPs that made more than " + threshold + " requests starting from " + startDate + " and with " + duration + " duration:");

		if (accessRecordsMap.isEmpty()) {
			System.out.println("No IP found with this filter.");
		} else {
			accessRecordsMap.entrySet().stream().forEachOrdered(accessRecordMap -> {
				System.out.println("IP: " + accessRecordMap.getKey().getIp() + " -> Comment: " + accessRecordMap.getKey().getComment());
			});
		}
	}

	public void start(String fileName, String startDate, String duration, Integer threshold) {
		this.readFile(fileName);

		Map<AccessRecord, Long> accessRecordsMap = getAccessRecordsVerifyingThreshold(startDate, duration, threshold);

		persistData(accessRecordsMap, startDate, duration, threshold);

		printIps(accessRecordsMap, startDate, duration, threshold);
	}
}
