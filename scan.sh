#!/bin/bash
# Minimal Wi-Fi/BLE sweep for Linux hosts.
# Usage: ./scan.sh [iface] [outdir]
set -e
IFACE=${1:-wlan0}
OUT_DIR=${2:-logs}
mkdir -p "$OUT_DIR"
TS=$(date -u +"%Y%m%d-%H%M%S")

# Wi-Fi scan
if command -v iw >/dev/null; then
  iw dev "$IFACE" scan > "$OUT_DIR/wifi-$TS.log" 2>&1 || true
fi

# BLE scan (10s)
if command -v hcitool >/dev/null; then
  timeout 10s hcitool lescan --duplicates > "$OUT_DIR/ble-$TS.log" 2>&1 || true
fi

echo "Logs stored in $OUT_DIR"
