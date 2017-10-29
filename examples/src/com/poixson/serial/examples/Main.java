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
		// just to load early, not necessary
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
				// list devices example
				RunExample(
					"list",
					new ExampleList()
				);
				// echo example
				RunExample(
					"echo",
					args,
					ExampleEcho.class
				);
				break;
			}

			// list devices example
			case "list": {
				RunExample(
					"list",
					new ExampleList()
				);
				break;
			}

			// echo example
			case "echo": {
				RunExample(
					"echo",
					args,
					ExampleEcho.class
				);
				break;
			}

			// display help
			case "help":
				PrintHelp();
				System.exit(1);

			// unknown argument
			default:
				System.out.println("Unknown argument: "+args[index]);
				PrintHelp();
				System.exit(1);
			}
		}

		System.out.println();
		System.exit(0);
	}



	public static void RunExample(final String title, final String[] args,
			final Class<? extends AbstractExample> exampleClss) {
		final AbstractExample example;
		try {
			example = exampleClss.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return;
		}
		if (args.length > 1) {
			example.setPortName(args[1]);
			if (args.length > 2) {
				example.setBaud(args[2]);
			}
		}
		RunExample(
			title,
			(Runnable) example
		);
	}
	public static void RunExample(final String title,
			final Runnable example) {
		System.out.println();
		Utils.SleepDot(
			(new StringBuilder())
				.append(" [ Running ")
				.append(title)
				.append(" example.. ] ")
				.toString()
		);
		System.out.println();
		example.run();
		System.out.println();
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
