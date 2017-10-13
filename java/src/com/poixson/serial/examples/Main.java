package com.poixson.serial.examples;

import com.poixson.utils.ErrorMode;
import com.poixson.utils.NativeAutoLoader;
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
		LoadLibraries();
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

		System.exit(0);
	}



	private static void LoadLibraries() {
		final NativeAutoLoader loader =
			NativeAutoLoader.get()
				.setErrorMode(ErrorMode.EXCEPTION)
				.setClassRef(Main.class)
				.addDefaultSearchPaths()
				.setResourcesPath("lib/linux64/")
				.setLocalLibPath("lib/")
				.enableExtract()
				.enableReplace();
		// load libftd2xx.so (prop)
		{
			final boolean result =
				loader.Load("libftd2xx.so");
			if (!result) {
				System.out.println("Failed to load ftd2xx library!");
				return;
			}
		}
		System.out.println();
		// load libftdi-linux64.so (open)
		{
			final boolean result =
				loader.Load("libftdi-linux64.so");
			if (!result) {
				System.out.println("Failed to load libftdi library!");
				return;
			}
		}
		System.out.println();
		// load pxnserial.so
		{
			final boolean result =
				loader.Load("pxnserial-linux64.so");
			if (!result) {
				System.out.println("Failed to load pxnSerial library!");
				return;
			}
		}
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
