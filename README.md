# Vantage Scanner (Phantom Scout scaffolding)
Offline-first Wi-Fi/BLE scanning toolkit paving the way for CSI-based through-wall pose estimation.

## Build & Install (Android)
1. **Prepare environment**
   - Android Studio or command line SDK with API 34 and JDK17.
   - Clone repo; ensure Gradle wrapper is executable: `chmod +x gradlew`.
2. **Build debug APK**
   ```bash
   ./gradlew assembleDebug
   ```
   Output: `app/build/outputs/apk/debug/app-debug.apk`.
3. **Install on device**
   - Enable *Developer options* and *USB debugging* on the phone.
   - Plug in and run:
     ```bash
     adb install -r app/build/outputs/apk/debug/app-debug.apk
     ```
4. **First run**
   - Grant runtime permissions when prompted: `ACCESS_FINE_LOCATION`, `NEARBY_WIFI_DEVICES`, and Bluetooth.
   - Current build shows placeholder UI; scanners and logging arrive next.

Keep Gradle caches (`~/.gradle`) and downloaded SDK packages on your dev box for offline rebuilds.

## Roadmap
### MVP (v0.1)
- [ ] Foreground ScanningService + notification
- [ ] Runtime permission flow
- [ ] Wi-Fi scanner (12–15s) + list UI
- [ ] BLE scanner (low-power) + list UI
- [ ] Risk dial (green/amber/red) with top 3 reasons
- [ ] “My Phone” panel (Wi-Fi/BLE/NFC/UWB/Airplane settings)
- [ ] CSV/JSON logging to `/Documents/PhantomScout` + Share

### Signature Guardian (v0.2)
- [ ] SelfTelemetry (probeRate, hotspot, BLE duty, RAT)
- [ ] Profiles (Base/Patrol/Near-OBJ)
- [ ] Rules engine + Leaks UI + Fix actions
- [ ] AAR report exporter
