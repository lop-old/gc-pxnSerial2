<?php
//http://www.wch.cn/download/CH341SER_LINUX_ZIP.html



$page_url   = 'http://www.ftdichip.com/Drivers/D2XX.htm';
$url_prefix = 'http://www.ftdichip.com/Drivers/';

$tempDir      = "<cwd>/lib-temp/prop";
$versionsPath = "<cwd>/java/resources/lib/versions";

$download32 = FALSE;



// ############################################################



// prepare paths
$cwd = \getcwd();
if (empty($cwd) || !\is_dir("$cwd/")) {
	echo "\n ** Missing cwd: $cwd ** \n\n";
	exit(1);
}
$parts = \explode('/', $cwd);
if (end($parts) == 'scripts') {
	$cwd = \realpath("$cwd/..");
}
unset($parts);
if (empty($cwd) || !\is_dir("$cwd/")) {
	echo "\n ** Error preparing cwd: $cwd ** \n\n";
	exit(1);
}
$tempDir      = \str_replace('<cwd>', $cwd, $tempDir     );
$versionsPath = \str_replace('<cwd>', $cwd, $versionsPath);
if (empty($tempDir)) {
	echo "\n ** Missing tempDir! ** \n\n";
	exit(1);
}
if (empty($versionsPath)) {
	echo "\n ** Missing versionsPath! ** \n\n";
	exit(1);
}
// prepare temp dir
if (!\is_dir("$tempDir/")) {
	$result = \mkdir(
		"$tempDir/",
		0775,
		TRUE
	);
	if (!$result) {
		echo "\n ** Failed to create temp dir: $tempDir ** \n\n";
		exit(1);
	}
}



// get page html
$html = \file_get_contents($page_url);
if ($html === FALSE) {
	echo "Failed to download url: $page_url\n";
	exit(1);
}
$html = \str_replace(
	[ "\r", "\t" ],
	[ "\n", ' '  ],
	$html
);



$funcTrimLeft = function($text) {
	if (empty($text)) return $text;
	while (TRUE) {
		$first = \substr($text, 0, 1);
		switch ($first) {
		case ' ':
		case '"':
		case "'":
		case "\t":
		case "\r":
		case "\n":
			$text = \substr($text, 1);
			continue;
		default:
			return $text;
		}
	}
};
$funcTrimRight = function($text) {
	if (empty($text)) return $text;
	while (TRUE) {
		$last = \substr($text, -1, 1);
		switch ($last) {
		case ' ':
		case '"':
		case "'":
		case "\t":
		case "\r":
		case "\n":
			$text = \substr($text, 0, -1);
			continue;
		default:
			return $text;
		}
	}
};
$funcTrim = function($text) {
	global $funcTrimLeft, $funcTrimRight;
	$text = $funcTrimLeft( $text);
	$text = $funcTrimRight($text);
	return $text;
};


$funcCheckExplodeArray = function(&$array, $line, $expected=2) {
	if ($array === NULL) {
		echo "\n ** Failed on line $line ** \n\n";
		exit(1);
	}
	if ($expected !== NULL) {
		if (\count($array) != $expected) {
			echo "\n ** Failed to parse on line $line expected $expected ** \n\n";
			exit(1);
		}
	}
};


$funcParseURL = function(&$html) {
	global $funcCheckExplodeArray, $funcTrim;
	global $url_prefix;
	$array = \explode('<a ',   $html,     2); $funcCheckExplodeArray($array, __LINE__);
	$array = \explode('href=', $array[1], 2); $funcCheckExplodeArray($array, __LINE__);
	$array = \explode('>',     $array[1], 2); $funcCheckExplodeArray($array, __LINE__);
	$result = $funcTrim($array[0]);
	$html = $array[1];
	if (\strpos($result, ' ') !== FALSE) {
		$array = \explode(' ', $result);
		$result = $funcTrim($array[0]);
	}
	unset($array);
	return $url_prefix.$result;
};
$funcParseVersion = function(&$html) {
	global $funcCheckExplodeArray, $funcTrim;
	$array = \explode('<', $html, 2); $funcCheckExplodeArray($array, __LINE__);
	$result = $array[0];
	$html = $array[1];
	if (\strpos($result, ' ') !== FALSE) {
		$array = \explode(' ', $result, 2); $funcCheckExplodeArray($array, __LINE__);
		$result = $array[0];
	}
	unset($array);
	return $funcTrim($result);
};


$info = [];


// windows
$array = \explode('>Windows*</td>', $html, 2); $funcCheckExplodeArray($array, __LINE__);
$html = $array[1]; unset($array);
// win32
//$info['win32'] = [
//	'url'     => $funcParseURL(    $html),
//	'version' => $funcParseVersion($html)
//];
// win64
$info['win64'] = [
	'url'     => $funcParseURL(    $html),
	'version' => $funcParseVersion($html)
];

// linux
$array = \explode('>Linux</td>', $html, 2); $funcCheckExplodeArray($array, __LINE__);
$html = $array[1]; unset($array);
// linux 32
$info['linux32'] = [
	'url'     => $funcParseURL(    $html),
	'version' => $funcParseVersion($html)
];
// linux 64
$info['linux64'] = [
	'url'     => $funcParseURL(    $html),
	'version' => $funcParseVersion($html)
];

// arm
$info['arm5-soft-float'] = [
	'url'     => $funcParseURL(    $html),
	'version' => $funcParseVersion($html)
];
$info['arm5-soft-float-uclibc'] = [
	'url'     => $funcParseURL(    $html),
	'version' => $funcParseVersion($html)
];
$info['arm6-hard-float'] = [
	'url'     => $funcParseURL(    $html),
	'version' => $funcParseVersion($html)
];
$info['arm7-hard-float'] = [
	'url'     => $funcParseURL(    $html),
	'version' => $funcParseVersion($html)
];
$info['arm8-hard-float'] = [
	'url'     => $funcParseURL(    $html),
	'version' => $funcParseVersion($html)
];

// mac
$array = \explode('>Mac OS X<br>', $html, 2); $funcCheckExplodeArray($array, __LINE__);
$html = $array[1]; unset($array);
$info['mac'] = [
	'url'     => $funcParseURL(    $html),
	'version' => $funcParseVersion($html)
];

// android
$array = \explode('>Android (Java D2XX)</td>', $html, 2); $funcCheckExplodeArray($array, __LINE__);
$html = $array[1]; unset($array);
$info['android'] = [
	'url'     => $funcParseURL($html),
	'version' => ''
];
$array = \explode('<br>',  $html,     2); $funcCheckExplodeArray($array, __LINE__);
$array = \explode('</td>', $array[1], 2); $funcCheckExplodeArray($array, __LINE__);
$info['android']['version'] = $funcTrim( $array[0] );
$html = $array[1]; unset($array);


// don't download unused for now
if (!$download32) {
	unset($info['win64']);
	unset($info['linux32']);
	unset($info['arm5-soft-float']);
	unset($info['arm5-soft-float-uclibc']);
	unset($info['arm6-hard-float']);
	unset($info['arm7-hard-float']);
	unset($info['arm8-hard-float']);
	unset($info['mac']);
	unset($info['android']);
}


// download files
if (!\is_dir("$versionsPath/")) {
	$result = \mkdir(
		"$versionsPath/",
		0775,
		TRUE
	);
	if (!$result) {
		echo "\n ** Failed to create {$tempDir}/versions/ directory! ** \n\n";
		exit(1);
	}
}
foreach ($info as $v1 => $v2) {
	$version = $v2['version'];
	$url     = $v2['url'];
	$result = \file_put_contents(
		"{$versionsPath}/{$v1}-version.txt",
		$version
	);
	if ($result === FALSE) {
		echo "\n ** Failed to create file: {$v1}-version.txt ** \n";
		exit(1);
	}
	$ext = '';
	$pos = \strrpos($url, '.');
	if ($pos !== FALSE) {
		$ext = \substr($url, $pos);
	}
	$filePath = "{$tempDir}/{$v1}-{$version}{$ext}";
	$info[$v1]['filepath'] = $filePath;
	echo "\n";
	echo "Downloading $v1..\n";
	echo "Version: $version\n";
	echo "    URL: $url\n";
	echo "To File: $filePath\n";
	if (\is_file($filePath)) {
		echo "File already exists, skipping..\n";
		continue;
	}
	$handleIn = \fopen($url, 'rb');
	if ($handleIn === FALSE) {
		echo "\n ** Failed to download file on line ".__LINE__." url: $url ** \n\n";
		exit(1);
	}
	$handleOut = \fopen($filePath, 'wb');
	if ($handleOut === FALSE) {
		echo "\n ** Failed to write downloaded file on line ".__LINE__." path: $filePath ** \n\n";
		exit(1);
	}
	$dotSize = 1024 * 102.4; // 100KB
	$chunkSize = 1024 * 8;   // 8KB
	$downloadedSize = 0;
	$lastDotSize    = 0;
	while(!\feof($handleIn)) {
		\fwrite(
			$handleOut,
			\fread(
				$handleIn,
				$chunkSize
			),
			$chunkSize
		);
		$downloadedSize += $chunkSize;
		if ($downloadedSize - $lastDotSize > $dotSize) {
			echo ' .';
			$lastDotSize = $downloadedSize;
		}
	}
	\fclose($handleOut);
	\fclose($handleIn);
	echo "\n";
	$fileSize = \filesize($filePath);
	$sizeStr = '';
	if ($fileSize > 1024 * 1024 * 1024) {
		$size = \round( $fileSize / (1024.0 * 1024.0 * 102.4) ) / 10.0;
		$sizeStr = "{$size}GB";
	} else
	if ($fileSize > 1024 * 1024) {
		$size = \round( $fileSize / (1024.0 * 102.4) ) / 10.0;
		$sizeStr = "{$size}MB";
	} else
	if ($fileSize > 1024) {
		$size = \round( $fileSize / 102.4 ) / 10.0;
		$sizeStr = "{$size}KB";
	} else {
		$sizeStr = "{$fileSize}B";
	}
	echo "Downloaded $sizeStr\n";
	echo "\n";
}
echo "\n\n";


// extract files
echo "Extracting files..\n\n";
foreach ($info as $v1 => $v2) {
	$filePath = $v2['filepath'];
	$pos = \strrpos($filePath, '/');
	if ($pos === FALSE) {
		echo "\n ** Failed to detect file name for path: $filePath ** \n\n";
		exit(1);
	}
	$fileName = \substr($filePath, $pos + 1);
	$pos = \strrpos($fileName, '.');
	if ($pos === FALSE) {
		echo "\n ** Failed to detect name and ext for file: $fileName ** \n\n";
		exit(1);
	}
	$name = \substr($fileName, 0, $pos);
	$ext = \substr($fileName, $pos + 1);
	$extractPath = "{$tempDir}/$name";
	if (\is_dir($extractPath)) continue;
	switch ($ext) {
	case 'zip':
		echo "\n";
		$zip = new ZipArchive();
		$result = $zip->open($filePath);
		if ($result !== TRUE) {
			echo "\n ** Failed to open zip file: $filePath ** \n\n";
			exit(1);
		}
		echo "Extracting $fileName ..";
		$result = \mkdir("$extractPath/", 0775);
		if (!$result) {
			echo "\n ** Failed to create $extractPath/ directory! ** \n\n";
			exit(1);
		}
		$zip->extractTo("$extractPath/");
		$zip->close();
		echo "\n";
		break;
	case 'tgz':
		echo "\n";
		$phar = new PharData($filePath);
		echo "Extracting $fileName ..";
		$result = \mkdir("$extractPath/", 0775);
		$result = $phar->extractTo("$extractPath/");
		if (!$result) {
			echo "\n ** Failed to extract: $filePath ** \n\n";
			exit(1);
		}
		echo "\n";
		break;
	default:
	}
}
echo "\n\n";


echo " Finished! \n\n";
