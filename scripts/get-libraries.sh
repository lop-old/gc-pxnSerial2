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



LIB_TEMP_PATH="$CWD/lib-temp"
NATIVE_SOURCE_PATH="$CWD/native"
JAVA_RESOURCES_PATH="$CWD/java/resources/lib"

DL_PROP_SCRIPT_PATH="$CWD/scripts/download-ftdi-prop-libraries.php"



############################################################



function doClean() {
	title "Cleaning libraries.."
	# library files
	if [[ ! -z $LIB_TEMP_PATH ]] && [[ -d "$LIB_TEMP_PATH/" ]]; then
		\rm -Rvf --preserve-root "$LIB_TEMP_PATH/" || exit 1
	fi
	# native .h files
	if [[ ! -z $NATIVE_SOURCE_PATH ]] && [[ -d "$NATIVE_SOURCE_PATH/ftdi/" ]]; then
		\rm -Rvf --preserve-root "$NATIVE_SOURCE_PATH/ftdi/" || exit 1
	fi
#	# java resources
#	if [[ ! -z $JAVA_RESOURCES_PATH ]]; then
#		if [[ -d "$JAVA_RESOURCES_PATH/ftdi-open/" ]]; then
#			\rm -Rvf --preserve-root "$JAVA_RESOURCES_PATH/ftdi-open/" || exit 1
#		fi
#		if [[ -d "$JAVA_RESOURCES_PATH/ftdi-prop/" ]]; then
#			\rm -Rvf --preserve-root "$JAVA_RESOURCES_PATH/ftdi-prop/" || exit 1
#		fi
#	fi
	echo
}



function doGetOpen() {
	title "Getting open source libftdi files.."
	# install libusb
	RPM_LIST_INSTALLED=`\rpm -qa`
	FOUND_PACKAGE=`echo $RPM_LIST_INSTALLED | grep libusb-devel`
	if [[ -z $FOUND_PACKAGE ]]; then
		sudo yum install libusb-devel || exit 1
	fi
	if [[ ! -d "$LIB_TEMP_PATH/open/" ]]; then
		\mkdir -pv "$LIB_TEMP_PATH/open/" || exit 1
	fi
	pushd "$LIB_TEMP_PATH/open/" || exit 1
		echo
		# update sources
		if [[ -d "$LIB_TEMP_PATH/open/libftdi.git/" ]]; then
			echo "Updating from remote repo.."
			pushd "$LIB_TEMP_PATH/open/libftdi.git/" || exit 1
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
		pushd "$LIB_TEMP_PATH/open/libftdi.git/" || exit 1
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
	if [[ ! -e "$JAVA_RESOURCES_PATH/linux64/" ]]; then
		\mkdir -pv "$JAVA_RESOURCES_PATH/linux64/" || exit 1
	fi
	\cp -fv \
		"$LIB_TEMP_PATH/open/libftdi.git/build/out/lib64/libftdi1.so.2.4.0" \
		"$JAVA_RESOURCES_PATH/linux64/libftdi-open-linux64.so" \
			|| exit 1
	# copy .h
	if [[ ! -e "$NATIVE_SOURCE_PATH/ftdi/open/" ]]; then
		\mkdir -pv "$NATIVE_SOURCE_PATH/ftdi/open/" || exit 1
	fi
	\cp -fv \
		"$LIB_TEMP_PATH/open/libftdi.git/src/ftdi.h" \
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
	if [[ ! -d "$LIB_TEMP_PATH/prop/" ]]; then
		\mkdir -pv "$LIB_TEMP_PATH/prop/" || exit 1
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
	CopyPropLibs  "linux64"  "linux64-<version>/release/build"  "libftd2xx.so.<version>"  "libftdi-prop-linux64.so"
	# linux32
#	CopyPropLibs  "linux32"  "linux32-<version>/release/build"  "libftd2xx.so.<version>"  "libftdi-prop-linux32.so"
#	# win64
#	CopyPropLibs  "win64"    "win64-<version>/amd64"            "*.dll"
#	# win32
#	\cp -afv \
#		"$JAVA_RESOURCES_PATH/versions/win64-version.txt" \
#		"$JAVA_RESOURCES_PATH/versions/win32-version.txt" \
#			|| exit 1
#	CopyPropLibs  "win32"    "win64-<version>/i386"             "*.dll"
	echo
	# copy .h
	if [[ ! -e "$NATIVE_SOURCE_PATH/ftdi/prop/" ]]; then
		\mkdir -pv "$NATIVE_SOURCE_PATH/ftdi/prop/" || exit 1
	fi
	\cp -avf \
		"$LIB_TEMP_PATH/prop/linux64-1.4.6/release/ftd2xx.h" \
		"$NATIVE_SOURCE_PATH/ftdi/prop/" \
			|| exit 1
	\cp -avf \
		"$LIB_TEMP_PATH/prop/linux64-1.4.6/release/WinTypes.h" \
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
	libVersion=`\cat "$JAVA_RESOURCES_PATH/versions/${osName}-version.txt"`
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
	if [[ ! -d "$JAVA_RESOURCES_PATH/$osName/" ]]; then
		\mkdir -pv "$JAVA_RESOURCES_PATH/$osName/" || exit 1
	fi
	# wild card
	if echo $filesFormat | \grep -q "*"; then
		filesPartA=`echo "$filesFormat" | \cut -f1 -d "*"`
		filesPartB=`echo "$filesFormat" | \cut -f2 -d "*"`
		\ls -lAsh "$LIB_TEMP_PATH/prop/$filesPath/$filesPartA"*"$filesPartB" || exit 1
		\cp -afv \
			"$LIB_TEMP_PATH/prop/$filesPath/$filesPartA"*"$filesPartB"             \
			"$JAVA_RESOURCES_PATH/$osName/" \
				|| exit 1
	# single file
	else
		\ls -lAsh "$LIB_TEMP_PATH/prop/$filesPath/$filesFormat" || exit 1
		# no rename
		if [[ -z $renameFile ]]; then
			\cp -afv \
				"$LIB_TEMP_PATH/prop/$filesPath/$filesFormat" \
				"$JAVA_RESOURCES_PATH/$osName/"            \
					|| exit 1
		# rename file
		else
			\cp -afv \
				"$LIB_TEMP_PATH/prop/$filesPath/$filesFormat" \
				"$JAVA_RESOURCES_PATH/$osName/$renameFile" \
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
