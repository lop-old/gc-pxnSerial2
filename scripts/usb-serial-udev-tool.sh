#!/bin/sh


# parse arguments
while [ $# -gt 0 ]; do
	case "$1" in
	-h|--help)
		echo
		echo "Usage: $0 [OPTION]... [DEVICE]"
		echo "Finds the Vendor and Product ID's for a serial-to-usb device."
		echo
		echo "  -i, --install    Automatically install the udev rule. (Must run as root)"
		echo "  -n, --name=XX    The abbreviated manufacturer/product name to use when generating the udev rule."
		echo "  -m, --manu=XX    Same as --name"
		echo "  -h, --help       This help message."
		echo
		exit 1
		;;
	-i|--install)
		DO_INSTALL=1
		;;
	--name=*|--manu=*)
		MANU_NAME="${1#*=}"
		;;
	-n|-m|--name|--manu)
		shift
		MANU_NAME="$1"
		;;
	*)
		if [[ ! -z $DEVICE_PATH ]]; then
			echo "Unknown arguments:  $DEVICE_PATH  $1"
			exit 1
		fi
		DEVICE_PATH="$1"
	;;
	esac
	shift
done
if [[ -z $DEVICE_PATH ]]; then
	echo "USB device path argument missing! example: /dev/ttyUSB0"
	exit 1
fi


VENDOR_ID=`udevadm info "$DEVICE_PATH" | grep "ID_VENDOR_ID="`
PRODUCT_ID=`udevadm info "$DEVICE_PATH" | grep "ID_MODEL_ID="`
VENDOR_ID=${VENDOR_ID#*=}
PRODUCT_ID=${PRODUCT_ID#*=}
if [[ -z $VENDOR_ID ]] || [[ -z $PRODUCT_ID ]]; then
	echo "Failed to find USB-Serial device!"
	exit 1
fi
if [[ ! -z $MANU_NAME ]]; then
	UDEV_RULES="SUBSYSTEM==\"tty\", ATTRS{idVendor}==\"$VENDOR_ID\", ATTRS{idProduct}==\"$PRODUCT_ID\", SYMLINK+=\"tty${MANU_NAME}%s{devpath}\"
SUBSYSTEM==\"tty\", ATTRS{idVendor}==\"$VENDOR_ID\", ATTRS{idProduct}==\"$PRODUCT_ID\", MODE=\"0666\""
fi


if [[ ! -z $DO_INSTALL ]]; then
	if [[ -z $MANU_NAME ]]; then
		echo "--name or --manu argument is required!"
		exit 1
	fi
	echo "$UDEV_RULES" >> /etc/udev/rules.d/99-usb-serial.rules || exit 1
	echo
	echo "Installed udev rule"
	echo
	exit 0
fi


echo
echo "Device: $DEVICE_PATH"
echo "Vendor ID:  $VENDOR_ID"
echo "Product ID: $PRODUCT_ID"
echo
if [[ ! -z $MANU_NAME ]]; then
	echo "Add line to: /etc/udev/rules.d/99-usb-serial.rules"
	echo
	echo "$UDEV_RULES"
	echo
fi
