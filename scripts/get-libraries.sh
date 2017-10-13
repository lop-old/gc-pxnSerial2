#!/bin/bash
clear
echo
source "/usr/bin/shellscripts/common.sh"
export CWD=`pwd`
if [[ "$CWD/" == *"/scripts/" ]]; then
	\pushd "$CWD/../" >/dev/null >/dev/null || exit 1
		export CWD=`pwd`
	\popd >/dev/null
fi
############################################################



LIBRARIES_PATH="$CWD/libraries"
NATIVE_SOURCE_PATH="$CWD/native"
JAVA_RESOURCES_PATH="$CWD/java/resources"

LIB_VERSIONS_PATH="$JAVA_RESOURCES_PATH/lib/versions"

DL_PROP_SCRIPT_PATH="$CWD/scripts/download-ftdi-prop-libraries.php"



############################################################



function doClean() {
	title "Cleaning libraries.."
	# library files
	if [[ ! -z $LIBRARIES_PATH ]] && [[ -d "$LIBRARIES_PATH/" ]]; then
		\rm -Rvf --preserve-root "$LIBRARIES_PATH/" || exit 1
	fi
	# native .h files
	if [[ ! -z $NATIVE_SOURCE_PATH ]] && [[ -d "$NATIVE_SOURCE_PATH/ftdi/" ]]; then
		\rm -Rvf --preserve-root "$NATIVE_SOURCE_PATH/ftdi/" || exit 1
	fi
	# java resources
	if [[ ! -z $JAVA_RESOURCES_PATH ]] && [[ -d "$JAVA_RESOURCES_PATH/lib/" ]]; then
		\rm -Rvf --preserve-root "$JAVA_RESOURCES_PATH/lib/" || exit 1
	fi
	echo
}



function doGetOpen() {
	title "Getting open source libftdi files.."
	if [[ ! -d "$LIBRARIES_PATH/" ]]; then
		\mkdir -pv "$LIBRARIES_PATH/" || exit 1
	fi
	pushd "$LIBRARIES_PATH/" || exit 1
		echo
		# update sources
		if [[ -d "$LIBRARIES_PATH/libftdi.git/" ]]; then
			echo "Updating from remote repo.."
			pushd "$LIBRARIES_PATH/libftdi.git/" || exit 1
				\git pull || exit 1
			popd
		# get sources
		else
			echo "Cloning from remote repo.."
			\git clone "git://developer.intra2net.com/libftdi" \
				libftdi.git || exit 1
		fi
		echo
		# build libftdi
		pushd "$LIBRARIES_PATH/libftdi.git/" || exit 1
			echo "Building libftdi.."
			if [[ -e "build/" ]]; then
				echo "Removing existing build/ directory.."
				\rm -Rvf --preserve-root build/ || exit 1
				echo
			fi
			mkdir -pv "build/" || exit 1
			title "Building libftdi.."
			pushd "build/" || exit 1
				\cmake -DCMAKE_INSTALL_PREFIX=`pwd`/out/ ../ || exit 1
				\make         || exit 1
				\make install || exit 1
			popd
			echo
			title "Successfully build libftdi!"
		popd
	popd
	title "Copy open source libftdi files.."
	# copy libs
	if [[ ! -e "$JAVA_RESOURCES_PATH/lib/linux64/" ]]; then
		\mkdir -pv "$JAVA_RESOURCES_PATH/lib/linux64/" || exit 1
	fi
	\cp -fv \
		"$LIBRARIES_PATH/libftdi.git/build/out/lib64/libftdi1.so.2.4.0" \
		"$JAVA_RESOURCES_PATH/lib/linux64/libftdi-linux64.so" \
			|| exit 1
	# copy .h
	if [[ ! -e "$NATIVE_SOURCE_PATH/ftdi/open/" ]]; then
		\mkdir -pv "$NATIVE_SOURCE_PATH/ftdi/open/" || exit 1
	fi
	\cp -fv \
		"$LIBRARIES_PATH/libftdi.git/src/ftdi.h" \
		"$NATIVE_SOURCE_PATH/ftdi/open/"         \
			|| exit 1
	\pushd "$NATIVE_SOURCE_PATH/ftdi/open/" >/dev/null || exit 1
		\dos2unix ftdi.h || exit 1
	\popd >/dev/null
	echo
	title "Finished building open source libftdi!"
}



function doGetProp() {
	title "Getting official ftd2xx files.."
	if [[ ! -d "$LIBRARIES_PATH/prop/" ]]; then
		\mkdir -pv "$LIBRARIES_PATH/prop/" || exit 1
	fi
	# download and extract files
	pushd "$CWD/" || exit 1
		php "$DL_PROP_SCRIPT_PATH" || exit 1
	popd
	echo
	title "Copy official ftd2xx files.."
	# copy libs
	#         <osName>   <libraries path>                             <libraries file format>   <optional rename format>
	# linux64
	CopyPropLibs  "linux64"  "linux64-<version>/release/build"  "libftd2xx.so.<version>"  "libftd2xx.so"
	# linux32
	CopyPropLibs  "linux32"  "linux32-<version>/release/build"  "libftd2xx.so.<version>"  "libftd2xx.so"
	# win64
	CopyPropLibs  "win64"    "win64-<version>/amd64"            "*.dll"
	# win32
	\cp -afv \
		"$LIB_VERSIONS_PATH/prop/win64-version.txt" \
		"$LIB_VERSIONS_PATH/prop/win32-version.txt" \
			|| exit 1
	CopyPropLibs  "win32"    "win64-<version>/i386"             "*.dll"
	echo
	# copy .h
	if [[ ! -e "$NATIVE_SOURCE_PATH/ftdi/prop/" ]]; then
		\mkdir -pv "$NATIVE_SOURCE_PATH/ftdi/prop/" || exit 1
	fi
	\cp -avf \
		"$LIBRARIES_PATH/prop/linux64-1.4.6/release/ftd2xx.h" \
		"$NATIVE_SOURCE_PATH/ftdi/prop/" \
			|| exit 1
	\cp -avf \
		"$LIBRARIES_PATH/prop/linux64-1.4.6/release/WinTypes.h" \
		"$NATIVE_SOURCE_PATH/ftdi/prop/" \
			|| exit 1
	echo
	\pushd "$NATIVE_SOURCE_PATH/ftdi/prop/" >/dev/null || exit 1
		\dos2unix ftd2xx.h   || exit 1
		\dos2unix WinTypes.h || exit 1
	\popd >/dev/null
	echo
	title "Finished getting official ftd2xx!"
}
function CopyPropLibs() {
	osName="$1"
	# read lib version number
	if [ -z $LIB_VERSIONS_PATH ]; then
		echo "LIB_VERSIONS_PATH not set!"
		exit 1
	fi
	libVersion=`\cat $LIB_VERSIONS_PATH/prop/${osName}-version.txt`
	if [ -z $libVersion ]; then
		echo "Failed to get lib version for os: $osName"
		exit 1
	fi
	# prepare paths
	filesPath="${2/<version>/$libVersion}"
	filesFormat="${3/<version>/$libVersion}"
	renameFile="$4"
	echo
	echo " libs: $filesPath"
	if [[ ! -d "$JAVA_RESOURCES_PATH/lib/$osName/" ]]; then
		\mkdir -pv "$JAVA_RESOURCES_PATH/lib/$osName/" || exit 1
	fi
	# wild card
	if echo $filesFormat | \grep -q "*"; then
		filesPartA=`echo "$filesFormat" | \cut -f1 -d "*"`
		filesPartB=`echo "$filesFormat" | \cut -f2 -d "*"`
		\ls -lAsh "$LIBRARIES_PATH/prop/$filesPath/$filesPartA"*"$filesPartB" || exit 1
		\cp -afv \
			"$LIBRARIES_PATH/prop/$filesPath/$filesPartA"*"$filesPartB"             \
			"$JAVA_RESOURCES_PATH/lib/$osName/" \
				|| exit 1
	# single file
	else
		\ls -lAsh "$LIBRARIES_PATH/prop/$filesPath/$filesFormat" || exit 1
		# no rename
		if [[ -z $renameFile ]]; then
			\cp -afv \
				"$LIBRARIES_PATH/prop/$filesPath/$filesFormat" \
				"$JAVA_RESOURCES_PATH/lib/$osName/"            \
					|| exit 1
		# rename file
		else
			\cp -afv \
				"$LIBRARIES_PATH/prop/$filesPath/$filesFormat" \
				"$JAVA_RESOURCES_PATH/lib/$osName/$renameFile" \
					|| exit 1
		fi
	fi
}



# parse arguments
while [[ $# -ge 1 ]]; do
	case "$1" in
		clean|clear|-c|--clean)
			DO_CLEAN=1
		;;
		all|-a|--all)
			DO_CLEAN=1
			DO_GET_OPEN=1
			DO_GET_PROP=1
		;;
		open|-o|--open)
			DO_GET_OPEN=1
		;;
		prop|official|-p|--prop)
			DO_GET_PROP=1
		;;
		*)
			echo -ne "\n\nUnknown argument: $1\n\n"
			exit 1
		;;
	esac
	shift
done

if [[ ! -z $DO_CLEAN ]]; then
	doClean
fi
if [[ ! -z $DO_GET_OPEN ]]; then
	doGetOpen
fi
if [[ ! -z $DO_GET_PROP ]]; then
	doGetProp
fi

echo
exit 0
