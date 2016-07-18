package uk.co.bluettduncanj.app;

import com.google.common.base.Optional;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.HashingInputStream;
import com.google.common.io.ByteStreams;
import uk.co.bluettduncanj.parse.ArgumentsParser;
import uk.co.bluettduncanj.utils.Pair;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Application {

	public static void main(final String[] args) {

		ArgumentsParser parser = ArgumentsParser.on(args);
		Optional<Pair<HashFunction, Path[]>> returnedArgs = parser.parse();

		if (returnedArgs.isPresent()) {
			HashFunction function = returnedArgs.get().getFirst();
			Path[] paths = returnedArgs.get().getSecond();

			for (Path path : paths) {
				try (HashingInputStream stream =
						new HashingInputStream(function, Files.newInputStream(path, StandardOpenOption.READ))) {

					System.out.println(hash + " *" + stream.hash());

				} catch (FileNotFoundException e) {
					System.err.println("File " + path + " could not be found. Reason: " + e.getMessage());
					return;

				} catch (IOException e) {
					System.err.println("Unexpected I/O error. Reason: " + e.getMessage());
					return;
				}
			}
		}
	}

}
