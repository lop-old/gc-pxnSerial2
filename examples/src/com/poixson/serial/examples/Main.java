package com.poixson.serial.examples;

import com.poixson.serial.pxnSerial;
import com.poixson.utils.StringUtils;
import com.poixson.utils.Utils;
import com.poixson.utils.xVars;


public class Main {



	public static void main(final String[] args) {
		xVars.debug(true);
		if (args.length == 0) {
			PrintHelp();
			System.exit(1);
		}
		System.out.println();
		// just to load early
		pxnSerial.LoadLibraries();
		System.out.println();

		for (int index=0; index<args.length; index++) {
			final String str =
				StringUtils.TrimFront(
					args[index],
					" ", "-", "\r", "\n"
				);
			if (Utils.isEmpty(str)) continue;
			switch (str) {

			// all examples
			case "all": {
				Utils.SleepDot("Running all tests");
				// list devices
				{
					System.out.println();
					final ExampleList example = new ExampleList();
					example.run();
				}
				// echo example
				{
					System.out.println();
					final ExampleEcho example = new ExampleEcho();
					example.run();
				}
				break;
			}

			// list devices example
			case "list": {
				Utils.SleepDot("Running list test");
				final ExampleList example = new ExampleList();
				example.run();
				break;
			}

			// echo example
			case "echo": {
				Utils.SleepDot("Running echo test");
				final ExampleEcho example = new ExampleEcho();
				if (args.length > index+1) {
					final String portName = args[++index];
					example.setPortName(portName);
					if (args.length > index+1) {
						final String baudStr = args[++index];
						example.setBaud(baudStr);
						System.out.println(
							(new StringBuilder())
								.append("Using port: ")
								.append(portName)
								.append(" baud: ")
								.append(baudStr)
								.toString()
						);
					} else {
						System.out.println("Using port: "+portName);
					}
				}
				example.run();
				break;
			}

			case "help":
				PrintHelp();
				System.exit(1);

			default:
				System.out.println("Unknown argument: "+args[index]);
				PrintHelp();
				System.exit(1);
			}
		}

		System.out.println();
		System.exit(0);
	}



	public static void PrintHelp() {
		System.out.println();
		System.out.println(" Available Examples:");
		System.out.println("   list     List available devices.");
		System.out.println("   echo [port [baud]]");
		System.out.println("            Open a port and echo the recieved data.");
		System.out.println("   all      Run all examples.");
		System.out.println();
	}



}