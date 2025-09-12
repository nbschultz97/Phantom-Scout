#!/usr/bin/env python3
"""Convert `iw` scan dumps into CSV with optional OUI vendor mapping."""
import csv
import re
import sys
from pathlib import Path

OUI_FILE = Path(__file__).with_name('oui.csv')


def load_oui(path: Path = OUI_FILE) -> dict:
    table = {}
    if path.exists():
        with open(path, newline='') as f:
            for row in csv.reader(f):
                if len(row) >= 2:
                    table[row[0].upper()[:8]] = row[1]
    return table


def parse_log(in_path: Path, out_path: Path, oui: dict) -> None:
    bssid_re = re.compile(r"BSS ([0-9a-f:]{17})")
    ssid_re = re.compile(r"SSID: (.*)")
    signal_re = re.compile(r"signal: (-?\d+) dBm")
    chan_re = re.compile(r"DS Parameter set: channel (\d+)")
    records = []
    current = {}
    for line in in_path.read_text().splitlines():
        if m := bssid_re.search(line):
            if current:
                records.append(current)
                current = {}
            current['BSSID'] = m.group(1)
        elif m := ssid_re.search(line):
            current['SSID'] = m.group(1).strip()
        elif m := signal_re.search(line):
            current['RSSI'] = m.group(1)
        elif m := chan_re.search(line):
            current['Channel'] = m.group(1)
    if current:
        records.append(current)
    with open(out_path, 'w', newline='') as f:
        writer = csv.DictWriter(f, fieldnames=['BSSID', 'SSID', 'Channel', 'RSSI', 'Vendor'])
        writer.writeheader()
        for r in records:
            prefix = r.get('BSSID', '').upper()[:8]
            r['Vendor'] = oui.get(prefix, '')
            writer.writerow(r)


if __name__ == '__main__':
    if len(sys.argv) < 3:
        print('Usage: parse_wifi_log.py <wifi.log> <out.csv>')
        sys.exit(1)
    oui = load_oui()
    parse_log(Path(sys.argv[1]), Path(sys.argv[2]), oui)
