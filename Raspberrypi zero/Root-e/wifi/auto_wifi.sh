#!/bin/bash
echo "##########################"
echo "Root e SmartFarm wifi auto config"
echo "##########################"

echo "wifi ssid: $1"
echo "wifi passwd: $2"

echo "network={" | sudo tee -a /etc/wpa_supplicant/wpa_supplicant.conf
echo "	ssid=\"$1\"" | sudo tee -a /etc/wpa_supplicant/wpa_supplicant.conf
echo "	psk=\"$2\"" | sudo tee -a /etc/wpa_supplicant/wpa_supplicant.conf
echo "}" | sudo tee -a /etc/wpa_supplicant/wpa_supplicant.conf

sudo wpa_cli -i wlan0 reconfigure
