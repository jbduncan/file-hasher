package uk.co.bluettduncanj.parse;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.hash.HashFunction;
import org.apache.commons.cli.*;
import uk.co.bluettduncanj.utils.Pair;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static com.google.common.hash.Hashing.md5;
import static com.google.common.hash.Hashing.sha256;
import static com.google.common.hash.Hashing.sha512;

public class ArgumentsParser {

	private static final String COMMAND_SYNTAX =
			"file-hasher[.sh|.bat] [OPTION...] ([ALGORITHM=md5, sha256, sha512] [FILE...])";
	private static final ImmutableMap<String, HashFunction> HASH_FUNCTIONS = ImmutableMap.of(
			"md5", md5(),
			"sha256", sha256(),
			"sha512", sha512()
	);

	private final String[] args;

	public static ArgumentsParser on(final String[] args) {
		return new ArgumentsParser(args);
	}

	private ArgumentsParser(final String[] args) {
		this.args = Arrays.copyOf(args, args.length);
	}

	public Optional<Pair<HashFunction, Path[]>> parse() {
		boolean bringUpHelp = false;
		if (args.length == 0) {
			bringUpHelp = true;
		}

		Options cmdOptions = new Options();

		OptionBuilder.withLongOpt("algorithm");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("ALGORITHM");
		OptionBuilder.withDescription("The hashing algorithm to use. Available algorithms: " + hashFunctionNames() + ".");
		cmdOptions.addOption(OptionBuilder.create("a"));

		OptionBuilder.withLongOpt("file");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("FILE");
		OptionBuilder.withDescription("The file to hash.");
		cmdOptions.addOption(OptionBuilder.create("f"));

		cmdOptions.addOption("h", "help", false, "Show this help menu.");

		CommandLineParser parser = new BasicParser();
		CommandLine cmd;
		try {
			cmd = parser.parse(cmdOptions, args);
		} catch (ParseException e) {
			System.err.println("Invalid arguments. Reason: " + e.getLocalizedMessage());
			return Optional.absent();
		}

		if (bringUpHelp || cmd.hasOption("h") || cmd.hasOption("help")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(COMMAND_SYNTAX, cmdOptions);
			return Optional.absent();
		}

		String[] remainingArgs = cmd.getArgs();
		String algorithm = remainingArgs[0];
		String[] fileNames = new String[]{};
		if (remainingArgs.length > 1) {
			fileNames = Arrays.copyOfRange(remainingArgs, 1, remainingArgs.length);
		}

		return Optional.of(Pair.of(
				toHashFunction(algorithm),
				toPathArray(fileNames)
		));
	}

	private HashFunction toHashFunction(String algorithm) throws IllegalArgumentException {
		HashFunction function = HASH_FUNCTIONS.get(algorithm);
		if (function == null) {
			throw new IllegalArgumentException("The algorithm specified does not exist. " +
					"Please enter one of the following: " + hashFunctionNames());
		}
		return function;
	}

	private String hashFunctionNames() {
		return Joiner.on(", ").join(HASH_FUNCTIONS.keySet());
	}

	private Path[] toPathArray(String[] pathNames) {
		Path[] paths = new Path[pathNames.length];
		for (int i = 0; i < pathNames.length; i++) {
			paths[i] = Paths.get(pathNames[i]);
		}
		return paths;
	}

}
