package com.ef;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {

	private static String fileName;
	private static String startDate;
	private static String duration;
	private static Integer threshold;

	public static void main(String[] args) {

		String errorMessage = validateCommandLine(args);
		if (errorMessage.isEmpty()) {
			startParser(args);
		} else {
			System.out.println(errorMessage);
		}
	}

	public static String validateCommandLine(String[] args) {
		String result = "";

		if (args.length < 2 || args.length > 4) {
			result = "Incorrect Command Line. you need to run the command line as below:\njava -cp 'parser.jar' com.ef.Parser --accesslog=/path/to/file --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100";
		} else if (commandLineArgsValue(args).get("startDate") == null) {
			result = "startDate arg not found";
		} else if (commandLineArgsValue(args).get("duration") == null) {
			result = "duration arg not found";
		} else if (commandLineArgsValue(args).get("threshold") == null) {
			result = "threshold arg not found";
		} else if (commandLineArgsValue(args).get("accesslog") == null) {
			result = "accesslog arg not found";
		}

		return result;
	}

	public static void startParser(String[] args) {

		fileName = commandLineArgsValue(args).get("accesslog");
		startDate = commandLineArgsValue(args).get("startDate");
		duration = commandLineArgsValue(args).get("duration");
		threshold = Integer.parseInt(commandLineArgsValue(args).get("threshold"));

		AccessLogInspect inspect = new AccessLogInspect();
		inspect.start(fileName, startDate, duration, threshold);
	}

	public static HashMap<String, String> commandLineArgsValue(String[] args) {
		return (HashMap<String, String>) Stream.of(args).map(arg -> arg.split("\\=")).filter(arg -> arg.length == 2)
				.collect(Collectors.toMap(arg -> arg[0], arg -> arg[1])).entrySet().stream()
				.collect(Collectors.toMap(arg -> arg.getKey().split("\\-\\-")[1], arg -> arg.getValue()));
	}
}
